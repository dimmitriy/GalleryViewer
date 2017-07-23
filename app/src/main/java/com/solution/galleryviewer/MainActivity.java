package com.solution.galleryviewer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.solution.galleryviewer.adapter.ImagesAdapter;
import com.solution.galleryviewer.async.LoadImagesAsyncTask;
import com.solution.galleryviewer.async.OnLoadedImageListener;
import com.solution.galleryviewer.extras.Constants;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity implements
        AdapterView.OnItemClickListener, OnLoadedImageListener {

    @BindView(R.id.gridImages)
    GridView gridImages;

    private ImagesAdapter imagesAdapter;
    private Display display;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        setupViews();
        setProgressBarIndeterminateVisibility(true);
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
            new LoadImagesAsyncTask(this, this).execute();
        } else {
            final Bitmap[] photos = (Bitmap[]) data;
            if (photos.length == 0) {
                new LoadImagesAsyncTask(this, this).execute();
            }
            for (Bitmap photo : photos) {
                addImage(photo);
            }
        }
    }

    private void addImage(Bitmap ... bitmaps) {
        for (Bitmap bitmap : bitmaps) {
            imagesAdapter.addPhoto(bitmap);
            imagesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        final GridView grid = gridImages;
        final int count = grid.getChildCount();
        final Bitmap[] list = new Bitmap[count];

        for (int i = 0; i < count; i++) {
            final ImageView v = (ImageView) grid.getChildAt(i);
            list[i] = ((BitmapDrawable) v.getDrawable()).getBitmap();
        }
        return list;
    }

    @Override
    public void onLoadedImage(Bitmap ... images) {
        for (Bitmap image : images) {
            imagesAdapter.addPhoto(image);
            imagesAdapter.notifyDataSetChanged();
        }
    }

    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        int columnIndex;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);
        if (cursor != null) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToPosition(position);
            String imagePath = cursor.getString(columnIndex);

            FileInputStream is = null;
            BufferedInputStream bis = null;
            try {
                is = new FileInputStream(new File(imagePath));
                bis = new BufferedInputStream(is);
                Bitmap bitmap = BitmapFactory.decodeStream(bis);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, parent.getWidth(), parent.getHeight(), true);
                bitmap.recycle();
                //Display bitmap (scaledBitmap)
            }
            catch (Exception e) {
                //Try to recover
            }
            finally {
                try {
                    if (bis != null) {
                        bis.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                    cursor.close();
                } catch (Exception e) {
                }
            }
        }
    }

}