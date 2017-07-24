package com.solution.galleryviewer.fullscreen;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.solution.galleryviewer.BaseActivity;
import com.solution.galleryviewer.R;
import com.solution.galleryviewer.extras.Constants;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FullScreenActivity extends BaseActivity
        implements FullScreenContract.View, ViewPager.OnPageChangeListener {

    @BindView(R.id.pager)
    ViewPager pager;

    private int position;
    private List<String> photos;
    private FullScreenAdapter adapter;
    private FullScreenPresenter presenter;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.POSITION, position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        ButterKnife.bind(this);

        if (savedInstanceState == null){
            position = getIntent().getIntExtra(Constants.POSITION, 0);
        } else {
            position = savedInstanceState.getInt(Constants.POSITION);
        }
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    @Override
    protected String getToolbarTitle() {
        return getResources().getString(R.string.fullscreen_viewer);
    }

    @Override
    protected boolean isHasBackButton() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter = new FullScreenPresenter(this);
        setupViews();
        checkPermission();
    }

    protected void onDestroy() {
        super.onDestroy();
        final ViewPager pager = this.pager;
        final int count = pager.getChildCount();
        ImageView v;
        for (int i = 0; i < count; i++) {
            v = (ImageView) pager.getChildAt(i);
            if ( v.getDrawable() != null){
                ( v.getDrawable()).setCallback(null);
            }
        }
    }

    void checkPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.CODE_SELECT_FROM_GALLERY);
            }
        } else {
            loadImages();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.CODE_SELECT_FROM_GALLERY:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadImages();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fullscreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                String path = photos.get(position);
                Toast.makeText(this, String.format(getString(R.string.image_deleted), path), Toast.LENGTH_SHORT).show();
                presenter.delete(path);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViews() {
        adapter = new FullScreenAdapter(this);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);
    }

    private void loadImages() {
        final Object data = getLastNonConfigurationInstance();
        if (data == null) {
            presenter.loadImages();
        } else {
            photos = (List<String>) data;
            if (photos.size() == 0) {
                presenter.loadImages();
            }
            showImages(photos);
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        final ViewPager grid = pager;
        final int count = grid.getChildCount();
        final List<Bitmap> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            final ImageView v = (ImageView) grid.getChildAt(i);
            list.add(((BitmapDrawable) v.getDrawable()).getBitmap());
        }
        return list;
    }

    @Override
    public void showImages(List<String> bitmaps) {
        photos = bitmaps;
        adapter.update(bitmaps);
        adapter.notifyDataSetChanged();
        pager.setCurrentItem(position);
    }

    @Override
    public void updateImages(List<String> bitmaps) {
        photos = bitmaps;
        pager.setAdapter(null);
        adapter.update(bitmaps);
        adapter.notifyDataSetChanged();
        pager.setAdapter(adapter);
        pager.invalidate();
        pager.setCurrentItem(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
