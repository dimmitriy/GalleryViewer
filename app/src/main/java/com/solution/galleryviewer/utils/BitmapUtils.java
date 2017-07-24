package com.solution.galleryviewer.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BitmapUtils {

    public static void deleteImage(Activity activity, String path){
        // Set up the projection (we only need the ID)
        String[] projection = { MediaStore.Images.Media._ID };

        // Match on the file path
        String selection = MediaStore.Images.Media.DATA + " = ?";
        String[] selectionArgs = new String[] { path };

        // Query for the ID of the media matching the file path
        Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = activity.getContentResolver();
        Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
        if (c.moveToFirst()) {
            // We found the ID. Deleting the item via the content provider will also remove the file
            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            contentResolver.delete(deleteUri, null, null);
        } else {
            // File not found in media store DB
        }
        c.close();

    }

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
