package com.solution.galleryviewer.utils;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BitmapUtils {

    public static List<String> getGalleryFullSize(Activity activity){
        Uri uri;
        Cursor cursor;
        int index_data;

        ArrayList<String> imagePaths = new ArrayList<>();
        String absolutePathOfImage;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA};

        cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(index_data);
            imagePaths.add(absolutePathOfImage);
        }
        return imagePaths;
    }

    public static List<Bitmap> getGalleryThumbnails(Activity activity, int imageSize) {
        Bitmap bitmap;
        Bitmap scaledBitmap;
        Uri uri;
        List<Bitmap> bitmaps = new ArrayList<>();

        String[] projection = {MediaStore.Images.Thumbnails._ID};
        Cursor cursor = activity.getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
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
                        scaledBitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, true);
                        bitmap.recycle();
                        if (scaledBitmap != null) {
                            bitmaps.add(scaledBitmap);
                        }
                    }
                } catch (IOException e) {

                }
            }
        }
        cursor.close();
        return bitmaps;
    }

}
