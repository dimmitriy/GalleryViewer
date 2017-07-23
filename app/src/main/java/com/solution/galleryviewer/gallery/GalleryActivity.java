package com.solution.galleryviewer.gallery;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.solution.galleryviewer.R;
import com.solution.galleryviewer.extras.Constants;
import com.solution.galleryviewer.fullscreen.FullScreenActivity;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryActivity extends Activity implements
        AdapterView.OnItemClickListener, GalleryContract.View {

    @BindView(R.id.gridImages)
    GridView gridImages;

    private ImagesAdapter imagesAdapter;
    private Display display;
    private List<Bitmap> photos;
    private GalleryPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter = new GalleryPresenter(this);
        checkPermission();
    }

    protected void onDestroy() {
        super.onDestroy();
        final GridView grid = gridImages;
        final int count = grid.getChildCount();
        ImageView v;
        for (int i = 0; i < count; i++) {
            v = (ImageView) grid.getChildAt(i);
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
        gridImages.setNumColumns(Constants.COUNT_IMAGES);
        gridImages.setClipToPadding(false);
        gridImages.setOnItemClickListener(this);
        imagesAdapter = new ImagesAdapter(getApplicationContext());
        gridImages.setAdapter(imagesAdapter);
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
        final GridView grid = gridImages;
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
        for (Bitmap bitmap : bitmaps) {
            imagesAdapter.addPhoto(bitmap);
            imagesAdapter.notifyDataSetChanged();
        }
    }

    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Intent intent = new Intent(this, FullScreenActivity.class);
        intent.putExtra(Constants.POSITION, position);
        startActivity(intent);
    }

}