package com.solution.galleryviewer.fullscreen;

import android.graphics.Bitmap;

import java.util.List;

public interface FullScreenContract {

    interface Presenter {

        void loadImages();

    }

    interface View {

        void showImages(List<Bitmap> bitmaps);

    }

}
