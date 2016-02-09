package com.stockita.newxyzreader.mainui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stockita.newxyzreader.R;
import com.stockita.newxyzreader.data.ItemsContract;
import com.stockita.newxyzreader.data.ModelItems;
import com.stockita.newxyzreader.data.UpdaterService;

import java.util.ArrayList;


import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * This is a main Fragment that has a list of grid of items, using recycler view
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    // Constant
    private static final int LOADER_ZERO = 0;

    // View items
    @Bind(R.id.recyclerViewFragment) RecyclerView mRecyclerViewFragment;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

    // Member field
    private MainAdapter mAdapter;

    /**
     * Empty Constructor
     */
    public MainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if null
        if (savedInstanceState == null) {

            // Fetch the data
            getActivity().startService(new Intent(getActivity(), UpdaterService.class));

        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ZERO, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Initialize the view
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Initialize the butterKnife
        ButterKnife.bind(this, view);

        // Instantiate the adapter
        mAdapter = new MainAdapter(getActivity());

        // For screen size, it has different number of span
        int columnCount = getResources().getInteger(R.integer.list_column_count);

        // Set the layout manager
        mRecyclerViewFragment.setLayoutManager(
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL));

        // Set the adapter
        mRecyclerViewFragment.setAdapter(mAdapter);

        // Swipe listener, It will fetch again
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().startService(new Intent(getActivity(), UpdaterService.class));
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        // Return the view object
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mRefreshingReceiver,
                new IntentFilter(UpdaterService.BROADCAST_ACTION_STATE_CHANGE));
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mRefreshingReceiver);

    }

    /**
     * BroadcastReceiver instance
     */
    private BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UpdaterService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                boolean isRefreshing = intent.getBooleanExtra(UpdaterService.EXTRA_REFRESHING, false);
                mSwipeRefreshLayout.setRefreshing(isRefreshing);

            }
        }
    };


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader loader = null;

        switch (id) {
            case LOADER_ZERO:
                loader = new CursorLoader(getActivity(),
                        ItemsContract.Items.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
                break;
        }

        return loader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // Check the cursor
        if (data != null && data.moveToFirst()) {

            // Get the number of records
            int dataSize = data.getCount();

            // Initialize the array list as container to pass the data into an adapter
            ArrayList<ModelItems> list = new ArrayList<>(dataSize);

            // Iterate for each data and pack them in to a POJO
            for (int i=0; i < dataSize; i++) {

                // Instantiate an object
                ModelItems modelItems = new ModelItems();

                // Set the field
                modelItems.set_id(data.getString(ItemsContract.Items.col_ID));
                modelItems.setServerId(data.getString(ItemsContract.Items.col_SERVER_ID));
                modelItems.setTitle(data.getString(ItemsContract.Items.col_TITLE));
                modelItems.setAuthor(data.getString(ItemsContract.Items.col_AUTHOR));
                modelItems.setBody(data.getString(ItemsContract.Items.col_BODY));
                modelItems.setThumbUrl(data.getString(ItemsContract.Items.col_THUMB_URL));
                modelItems.setPhotoUrl(data.getString(ItemsContract.Items.col_PHOTO_URL));
                modelItems.setAspectRatio(data.getString(ItemsContract.Items.col_ASPECT_RATIO));
                modelItems.setPublishedDate(data.getString(ItemsContract.Items.col_PUBLISH_DATE));

                // Move the cursor to next row
                data.moveToNext();

                // Add the model object to the container
                list.add(modelItems);

            }

            // Pass the data to the adapter, this data will populate the recyclerView
            mAdapter.swapData(list);

            // Move the cursor to first, because we want to get the first image available
            // for the header in the UI
            data.moveToFirst();

            // Get the first record for photo url then use it for the header image in the main activity
            // Pass the String uri using interface (callbacks)
            ((UpdateHeaderImage) getActivity()).updateHeaderImage(data.getString(ItemsContract.Items.col_PHOTO_URL));

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;
    }

    /**
     * Callbacks interface to pass the Photo Url for the image header
     * in the main activity
     */
    public interface UpdateHeaderImage {

        /**
         * Callbacks methods to pass the url to the calling activity
         * @param url       String url of the image
         */
        void updateHeaderImage(String url);
    }
}
