package com.solution.galleryviewer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.ArrayList;

public class ImagesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Bitmap> photos = new ArrayList<>();

    public ImagesAdapter(Context context) {
        this.context = context;
    }

    public void addPhoto(Bitmap photo) {
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
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setPadding(8, 8, 8, 8);
        imageView.setImageBitmap(photos.get(position));
        return imageView;
    }
}
