package com.solution.galleryviewer.fullscreen;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.solution.galleryviewer.R;

import java.util.ArrayList;
import java.util.List;

public class FullScreenAdapter extends PagerAdapter {

    private final LayoutInflater inflater;
    private final Context context;
    private final BitmapFactory.Options options;
    private List<String> photos;

    public FullScreenAdapter(Context context) {
        this.context = context;
        photos = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        options = new BitmapFactory.Options();
        options.inSampleSize = 4;
    }

    public void update(List<String> photos){
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.fullscreen_page, null);
        String path = photos.get(position);

        Glide
                .with(context)
                .load(path)
                .into((ImageView)view);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView)object);
    }

}

