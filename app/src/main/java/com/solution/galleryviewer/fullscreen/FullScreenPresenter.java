package com.solution.galleryviewer.fullscreen;

import android.app.Activity;
import android.graphics.Bitmap;

import com.solution.galleryviewer.gallery.GalleryContract;
import com.solution.galleryviewer.utils.BitmapUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class FullScreenPresenter implements FullScreenContract.Presenter {

    private final FullScreenContract.View view;

    public FullScreenPresenter(FullScreenContract.View view) {
        this.view = view;
    }

    @Override
    public void loadImages() {
        List<Bitmap> bitmaps = BitmapUtils.getGalleryBitmaps((Activity)view);
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
