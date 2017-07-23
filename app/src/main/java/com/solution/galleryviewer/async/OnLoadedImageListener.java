package com.solution.galleryviewer.async;

import android.graphics.Bitmap;

import com.solution.galleryviewer.MainActivity;

public interface OnLoadedImageListener {

    void onLoadedImage(Bitmap... image);

}
