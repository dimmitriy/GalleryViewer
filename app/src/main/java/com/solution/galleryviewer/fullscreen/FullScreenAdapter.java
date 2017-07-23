package com.solution.galleryviewer.fullscreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.solution.galleryviewer.R;

import java.util.ArrayList;
import java.util.List;

public class FullScreenAdapter extends PagerAdapter {

    private final LayoutInflater inflater;
    private final Context context;
    private List<Bitmap> photos;

    public FullScreenAdapter(Context context) {
        this.context = context;
        photos = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void update(List<Bitmap> photos){
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
        ((ImageView)view).setImageDrawable( new BitmapDrawable(context.getResources(), photos.get(position)));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView)object);
    }

}

