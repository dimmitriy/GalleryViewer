package com.solution.galleryviewer.gallery;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import com.solution.galleryviewer.utils.BitmapUtils;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

class GalleryPresenter implements GalleryContract.Presenter {

    private final GalleryContract.View view;

    GalleryPresenter(GalleryContract.View view) {
        this.view = view;
    }

    @Override
    public void loadImages(final int imageSize) {
        view.showProgress(true);
        List<String> uris = BitmapUtils.getGalleryFullSize((Activity)view);
        Observable.just(uris).subscribe(new Observer<List<String>>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                view.showProgress(false);
            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull List<String> bitmaps) {
                view.showImages(bitmaps, imageSize);
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                view.showProgress(false);
            }

            @Override
            public void onComplete() {

            }
        });
    }

}
