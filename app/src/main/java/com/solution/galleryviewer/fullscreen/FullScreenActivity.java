package com.solution.galleryviewer.fullscreen;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.ImageView;
import com.solution.galleryviewer.R;
import com.solution.galleryviewer.extras.Constants;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FullScreenActivity extends Activity implements FullScreenContract.View, ViewPager.OnPageChangeListener {

    @BindView(R.id.pager)
    ViewPager pager;

    private int position;
    private List<Bitmap> photos;
    private FullScreenAdapter adapter;
    private FullScreenPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        position = intent.getIntExtra(Constants.POSITION, 0);

        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter = new FullScreenPresenter(this);
        checkPermission();
    }

    protected void onDestroy() {
        super.onDestroy();
        final ViewPager pager = this.pager;
        final int count = pager.getChildCount();
        ImageView v;
        for (int i = 0; i < count; i++) {
            v = (ImageView) pager.getChildAt(i);
            ( v.getDrawable()).setCallback(null);
        }
    }

    void checkPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
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
            photos = (List<Bitmap>) data;
            if (photos.size() == 0) {
                presenter.loadImages();
            }
            showImages(photos);
        }
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
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
    public void showImages(List<Bitmap> bitmaps) {
        photos = bitmaps;
        adapter.update(bitmaps);
        adapter.notifyDataSetChanged();
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
