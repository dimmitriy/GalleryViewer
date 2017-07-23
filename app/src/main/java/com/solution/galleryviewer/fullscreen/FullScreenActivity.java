package com.solution.galleryviewer.fullscreen;

import android.content.Intent;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.solution.galleryviewer.R;
import com.solution.galleryviewer.extras.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FullScreenActivity extends AppCompatActivity {

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.title_strip)
    PagerTitleStrip titleStrip;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        position = intent.getIntExtra(Constants.POSITION, 0);
    }
}
