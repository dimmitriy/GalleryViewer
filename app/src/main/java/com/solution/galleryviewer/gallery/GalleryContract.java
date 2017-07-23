package com.solution.galleryviewer.gallery;

import android.graphics.Bitmap;

import java.util.List;

public interface GalleryContract {

    interface Presenter {

        void loadImages();

    }

    interface View {

        void showImages(List<Bitmap> bitmaps);

    }

}
