package com.stockita.newxyzreader.ui;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateUtils;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.stockita.newxyzreader.R;
import com.stockita.newxyzreader.data.ModelItems;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArticleDetailActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private int mCurrentItemPosition;
    private ArrayList<ModelItems> mModelList;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    @Bind(R.id.container) ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        // ButterKnife
        ButterKnife.bind(this);

        // Get data from Main Activity and set the initial state
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mCurrentItemPosition = intent.getIntExtra("one", 0);
            mModelList = intent.getParcelableArrayListExtra("two");

        }

        // Restore state
        if (savedInstanceState != null) {
            mModelList = savedInstanceState.getParcelableArrayList("oneData");
            mCurrentItemPosition = savedInstanceState.getInt("twoData");
        }

        // Pass the list to the adapter
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), mModelList);

        // Set up the view pager adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Set the current page position if any.
        mViewPager.setCurrentItem(mCurrentItemPosition);

        // Set transformer when the user swipe between pagers
        mViewPager.setPageTransformer(true, new InOutTransformation());

        // This is important to avoid crash
        mSectionsPagerAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("oneData", mModelList);
        outState.putInt("twoData", mCurrentItemPosition);
    }


    /**
     * A placeholder fragment class containing a simple view.
     */
    public static class PlaceholderFragment extends android.app.Fragment {

        // Constant
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_MODEL_LIST = "model_list";
        private static final String KEY_ONE = "one";
        private static final String KEY_TWO = "two";

        // The views
        @Bind(R.id.coordinator) CoordinatorLayout mCoordinator;
        @Bind(R.id.toolbar) Toolbar mToolbar;
        @Bind(R.id.toolbar_layout) CollapsingToolbarLayout mCollapsingToolbarLayout;
        @Bind(R.id.fab) FloatingActionButton mFab;
        @Bind(R.id.article_title) TextView mTitle;
        @Bind(R.id.article_subtitle) TextView mSubtitle;
        @Bind(R.id.body) TextView mBody;
        @Bind(R.id.thumbnail) ImageView thumbnail;

        // The id
        private int mSelectionId;

        // Model
        private ModelItems mModel;

        /**
         * Constructor
         */
        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, ModelItems model) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putParcelable(ARG_MODEL_LIST, model);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Set the menu to true
            setHasOptionsMenu(true);

            // Check if null, get new data
            if (savedInstanceState == null) {
                mSelectionId = getArguments().getInt(ARG_SECTION_NUMBER);
                mModel = getArguments().getParcelable(ARG_MODEL_LIST);

            }

            // Check if not null, restore state
            if (savedInstanceState != null) {
                mSelectionId = savedInstanceState.getInt(KEY_ONE);
                mModel = savedInstanceState.getParcelable(KEY_TWO);
            }

        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            outState.putInt(KEY_ONE, mSelectionId);
            outState.putParcelable(KEY_TWO, mModel);
            super.onSaveInstanceState(outState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            // Initialize the view and inflate the layout xml
            View rootView = inflater.inflate(R.layout.fragment_article_details, container, false);

            // Initialize the ButterKnife
            ButterKnife.bind(this, rootView);

            // Toolbar
            ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);

            // Toolbar navigation area back button arrow
            mToolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });

            // Set the image
            String imageUrl = mModel.getPhotoUrl();
            Picasso.with(getActivity()).load(imageUrl).into(thumbnail);

            // Author on the toolbar
            String author = mModel.getAuthor();
            mCollapsingToolbarLayout.setTitle(author);
            mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.ActionBarText);

            // Title of the article
            String title = mModel.getTitle();
            mTitle.setText(title);

            // Subtitle which is the date
            String date = mModel.getPublishedDate();

            // Reformat the date
            String dateString =  DateUtils.getRelativeTimeSpanString(
                    Long.parseLong(date),
                    System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_ALL).toString();
            mSubtitle.setText(dateString);

            // Set the body of the article, use Html.fromHtml()
            String body = mModel.getBody();
            mBody.setText(Html.fromHtml(body));

            // Return the view
            return rootView;
        }

        /**
         * FAB to share button
         * @param view      The View object
         */
        @OnClick(R.id.fab)
        public void onClickFab(View view) {

            // This is the way to use implicit share intent
            startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                    .setType("text/plain")
                    .setText(mModel.getBody())
                    .getIntent(), getString(R.string.action_share)));
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.menu_main, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_settings:

                    // Use the Snackbar API to display a message for the UI
                    Snackbar snackbar = Snackbar.make(mCoordinator, "Setting", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        // Field
        private ArrayList<ModelItems> mModel;

        /**
         * Constructor
         * @param fm         FragmentManager object
         * @param modelList  ModelItems object
         */
        public SectionsPagerAdapter(FragmentManager fm, ArrayList<ModelItems> modelList) {
            super(fm);
            mModel = modelList;
        }

        @Override
        public android.app.Fragment getItem(int position) {

            return PlaceholderFragment.newInstance(position, mModel.get(position));
        }

        @Override
        public int getCount() {
            // Get the number of pages
            return mModel != null ? mModel.size() : 0;
        }

    }

}
