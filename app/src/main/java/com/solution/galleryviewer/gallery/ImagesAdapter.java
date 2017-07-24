package com.solution.galleryviewer.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.ArrayList;

class ImagesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Bitmap> photos = new ArrayList<>();

    ImagesAdapter(Context context) {
        this.context = context;
    }

    void addPhoto(Bitmap photo) {
        photos.add(photo);
    }

    public int getCount() {
        return photos.size();
    }

    public Object getItem(int position) {
        return photos.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(2, 2, 2, 2);
        imageView.setImageDrawable(new BitmapDrawable(context.getResources(), photos.get(position)));
        return imageView;
    }
}
