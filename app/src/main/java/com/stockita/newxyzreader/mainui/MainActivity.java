package com.stockita.newxyzreader.mainui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.stockita.newxyzreader.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * This is where the app launches and here.
 */
public class MainActivity extends AppCompatActivity implements
        MainFragment.UpdateHeaderImage, AppBarLayout.OnOffsetChangedListener {

    // The views
    @Bind(R.id.container) FrameLayout mContainer;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.appbar_layout) AppBarLayout mAppBarLayout;
    @Bind(R.id.collapsingToolbarLayout) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.headerImage) ImageView mHeaderImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the layout
        setContentView(R.layout.activity_main);

        // Instantiate the ButterKnife
        ButterKnife.bind(this);

        // The toolbar and the header
        if (mToolbar != null) {

            mCollapsingToolbarLayout.setTitle("XYZ Reader");
            mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.ActionBarText);

            setSupportActionBar(mToolbar);

        }

        // Check if null, get the fragment
        if (savedInstanceState == null) {

            // Instantiate the fragment object
            MainFragment fragment = new MainFragment();

            // Manage the fragment
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }

    /**
     * Callbacks method from the fragment to pass the image url
     * @param url       String url
     */
    @Override
    public void updateHeaderImage(String url) {

        // Download the image using Picasso
        Picasso.with(this).load(url).into(mHeaderImage);

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        // Simple nothing here.
    }
}
