package com.solution.galleryviewer.gallery;

import android.app.Activity;
import android.graphics.Bitmap;
import com.solution.galleryviewer.utils.BitmapUtils;
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
    public void loadImages(int imageSize) {
        List<Bitmap> bitmaps = BitmapUtils.getGalleryThumbnails((Activity)view, imageSize);
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
