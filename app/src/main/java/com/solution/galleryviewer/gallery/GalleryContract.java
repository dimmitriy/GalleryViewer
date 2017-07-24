package com.solution.galleryviewer.gallery;

import java.util.List;

interface GalleryContract {

    interface Presenter {

        void loadImages(int imageSize);

    }

    interface View {

        void showImages(List<String> paths, int imageSize);

        void showProgress(boolean isShow);

    }

}
