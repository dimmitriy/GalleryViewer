package com.solution.galleryviewer.fullscreen;

import android.graphics.Bitmap;

import java.util.List;

public interface FullScreenContract {

    interface Presenter {

        void loadImages();

        void delete(String path);
    }

    interface View {

        void showImages(List<String> bitmaps);

        void updateImages(List<String> bitmaps);
    }

}
