package com.solution.galleryviewer.utils;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.solution.galleryviewer.extras.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmytro Ogirenko rangerover12@gmail.com
 * @since 23.07.2017
 */

public class BitmapUtils {

    public static List<Bitmap> getGalleryBitmaps(Activity activity) {
        Bitmap bitmap;
        Bitmap newBitmap;
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
                        newBitmap = Bitmap.createScaledBitmap(bitmap, Constants.IMAGE_SIZE, Constants.IMAGE_SIZE, true);
                        bitmap.recycle();
                        if (newBitmap != null) {
                            bitmaps.add(newBitmap);
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
