package com.solution.galleryviewer;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import com.solution.galleryviewer.GalleryContract;
import com.solution.galleryviewer.extras.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class GalleryPresenter implements GalleryContract.Presenter {

    private final GalleryContract.View view;

    public GalleryPresenter(GalleryContract.View view) {
        this.view = view;
    }

    @Override
    public void loadImages() {
        Bitmap bitmap;
        Bitmap newBitmap;
        Uri uri;
        List<Bitmap> bitmaps = new ArrayList<>();

        String[] projection = {MediaStore.Images.Thumbnails._ID};
        Cursor cursor = ((Activity)view).managedQuery(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
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
                    bitmap = BitmapFactory.decodeStream(((Activity)view).getContentResolver().openInputStream(uri));
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
        Observable.just(bitmaps).subscribe(new Observer<List<Bitmap>>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull List<Bitmap> bitmaps) {
                view.showImages(bitmaps);
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
