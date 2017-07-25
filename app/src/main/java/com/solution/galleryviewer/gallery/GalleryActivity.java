package com.solution.galleryviewer.gallery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.solution.galleryviewer.BaseActivity;
import com.solution.galleryviewer.R;
import com.solution.galleryviewer.extras.Constants;
import com.solution.galleryviewer.fullscreen.FullScreenActivity;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryActivity extends BaseActivity implements
        AdapterView.OnItemClickListener, GalleryContract.View {

    @BindView(R.id.grid_images)
    GridView grid;

    @BindView(R.id.progress)
    ProgressBar progress;

    @BindView(R.id.text)
    TextView text;

    private GalleryAdapter adapter;
    private List<String> paths;
    private GalleryPresenter presenter;
    private int imageSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        progress.setVisibility(View.GONE);
        presenter = new GalleryPresenter(this);
        Display display = ((WindowManager)
                getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageSize = display.getWidth() / Constants.COUNT_IMAGES_LANDSCAPE;
        } else {
            imageSize = display.getWidth() / Constants.COUNT_IMAGES_PORTRAIT;
        }
        setupViews();
        checkPermission();
    }

    @Override
    protected String getToolbarTitle() {
        return getResources().getString(R.string.gallery_viewer);
    }

    @Override
    protected boolean isHasBackButton() {
        return false;
    }

    protected void onDestroy() {
        super.onDestroy();
        final GridView grid = this.grid;
        final int count = grid.getChildCount();
        ImageView v;
        for (int i = 0; i < count; i++) {
            v = (ImageView) grid.getChildAt(i);
            (v.getDrawable()).setCallback(null);
        }
    }

    void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.CODE_SELECT_FROM_GALLERY);
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
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadImages();
                } else {
                    grid.setVisibility(View.GONE);
                    text.setVisibility(View.VISIBLE);
                    text.setText(R.string.no_permission);
                }
                break;
        }
    }

    private void setupViews() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            grid.setNumColumns(Constants.COUNT_IMAGES_LANDSCAPE);
        } else {
            grid.setNumColumns(Constants.COUNT_IMAGES_PORTRAIT);
        }
        grid.setClipToPadding(false);
        grid.setOnItemClickListener(this);
        adapter = new GalleryAdapter(getApplicationContext(), imageSize);
        grid.setAdapter(adapter);
    }

    private void loadImages() {
        final Object data = getLastNonConfigurationInstance();
        if (data == null) {
            presenter.loadImages(imageSize);
        } else {
            paths = (List<String>) data;
            if (paths.size() == 0) {
                presenter.loadImages(imageSize);
            }
            showImages(paths, imageSize);
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        final GridView grid = this.grid;
        final int count = grid.getChildCount();
        final List<Bitmap> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            final ImageView v = (ImageView) grid.getChildAt(i);
            list.add(((GlideBitmapDrawable) v.getDrawable()).getBitmap());
        }
        return list;
    }

    @Override
    public void showImages(List<String> uris, int imageSize) {
        this.paths = uris;
        if (uris.size() == 0) {
            grid.setVisibility(View.GONE);
            text.setVisibility(View.VISIBLE);
            text.setText(R.string.no_images);
        } else {
            text.setVisibility(View.GONE);
            for (String path : uris) {
                adapter.addPhoto(path);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showProgress(boolean isShow) {
        progress.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Intent intent = new Intent(this, FullScreenActivity.class);
        intent.putExtra(Constants.POSITION, position);
        startActivity(intent);
    }

}