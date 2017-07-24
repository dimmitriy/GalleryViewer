package com.solution.galleryviewer.fullscreen;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.solution.galleryviewer.R;

import java.util.ArrayList;
import java.util.List;

class FullScreenAdapter extends PagerAdapter {

    private final LayoutInflater inflater;
    private final Context context;
    private List<String> paths;

    FullScreenAdapter(Context context) {
        this.context = context;
        paths = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    void update(List<String> photos){
        this.paths = photos;
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.fullscreen_page, null);
        String path = paths.get(position);

        Glide
                .with(context)
                .load(path)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into((ImageView)view);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView)object);
    }

}

