package com.solution.galleryviewer.fullscreen;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

    @BindView(R.id.text)
    TextView text;

    private int position;
    private List<String> paths;
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
                } else {
                    pager.setVisibility(View.GONE);
                    text.setVisibility(View.VISIBLE);
                    text.setText(R.string.no_permission);
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
                if (paths.size() == 0){
                    Toast.makeText(this, getString(R.string.no_image_delete), Toast.LENGTH_SHORT).show();
                    pager.setVisibility(View.GONE);
                    text.setVisibility(View.VISIBLE);
                    text.setText(R.string.no_images);
                } else {
                    String path;
                    if (paths.size() == 1){
                        path = paths.get(0);
                    } else {
                        path = paths.get(position);
                    }
                    Toast.makeText(this, String.format(getString(R.string.image_deleted), path), Toast.LENGTH_SHORT).show();
                    presenter.delete(path);
                }
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
            paths = (List<String>) data;
            if (paths.size() == 0) {
                presenter.loadImages();
            }
            showImages(paths);
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        final ViewPager grid = pager;
        final int count = grid.getChildCount();
        final List<TransitionDrawable> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            final ImageView v = (ImageView) grid.getChildAt(i);
            list.add(((TransitionDrawable) v.getDrawable()));
        }
        return list;
    }

    @Override
    public void showImages(List<String> paths) {

        if (paths.size() == 0){
            onBackPressed();
        } else {
            pager.setVisibility(View.VISIBLE);
            text.setVisibility(View.GONE);
            this.paths = paths;
            adapter.update(this.paths);
            adapter.notifyDataSetChanged();
            pager.setCurrentItem(position);
        }
    }

    @Override
    public void updateImages(List<String> bitmaps) {
        paths = bitmaps;
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
