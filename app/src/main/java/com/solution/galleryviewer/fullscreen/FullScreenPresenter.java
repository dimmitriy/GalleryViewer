package com.solution.galleryviewer.fullscreen;

import android.app.Activity;

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
        List<String> bitmaps = BitmapUtils.getGalleryFullSize((Activity)view);
        Observable.just(bitmaps).subscribe(new Observer<List<String>>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull List<String> bitmaps) {
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

    @Override
    public void delete(String path) {
        BitmapUtils.deleteImage((Activity)view, path);
        List<String> bitmaps = BitmapUtils.getGalleryFullSize((Activity)view);
        Observable.just(bitmaps).subscribe(new Observer<List<String>>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull List<String> bitmaps) {
                view.updateImages(bitmaps);
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
