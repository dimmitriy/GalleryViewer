package com.solution.galleryviewer.async;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import com.solution.galleryviewer.extras.Constants;

import java.io.IOException;

public class LoadImagesAsyncTask extends AsyncTask<Object, Bitmap, Object> {

    private final OnLoadedImageListener listener;
    private final Activity activity;

    public LoadImagesAsyncTask(Activity activity, OnLoadedImageListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    protected Object doInBackground(Object... params) {
        Bitmap bitmap;
        Bitmap newBitmap;
        Uri uri;

        String[] projection = {MediaStore.Images.Thumbnails._ID};
        Cursor cursor = activity.managedQuery( MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
        int size = cursor.getCount();
        if (size != 0) {
            int imageID;
            for (int i = 0; i < size; i++) {
                cursor.moveToPosition(i);
                imageID = cursor.getInt(columnIndex);
                uri = Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + imageID);
                try {
                    bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri));
                    if (bitmap != null) {
                        newBitmap = Bitmap.createScaledBitmap(bitmap, Constants.IMAGE_SIZE, Constants.IMAGE_SIZE, true);
                        bitmap.recycle();
                        if (newBitmap != null) {
                            publishProgress(newBitmap);
                        }
                    }
                } catch (IOException e) {

                }
            }
        }
        cursor.close();
        return null;
    }

    @Override
    public void onProgressUpdate(Bitmap ... bitmaps) {
        listener.onLoadedImage(bitmaps);
    }

    @Override
    protected void onPostExecute(Object result) {
        activity.setProgressBarIndeterminateVisibility(false);
    }
}
