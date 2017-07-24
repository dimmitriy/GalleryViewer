package com.solution.galleryviewer.gallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

class GalleryAdapter extends BaseAdapter {

    private final int size;
    private Context context;
    private ArrayList<String> uris = new ArrayList<>();

    GalleryAdapter(Context context, int size) {
        this.context = context;
        this.size = size;
    }

    void addPhoto(String path) {
        uris.add(path);
    }

    public int getCount() {
        return uris.size();
    }

    public Object getItem(int position) {
        return uris.get(position);
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
        imageView.setPadding(2, 2, 2, 2);

        Glide.with(context)
                .load(uris.get(position))
                .override(size, size)
                .centerCrop()
                .into(imageView);

        return imageView;
    }
}
