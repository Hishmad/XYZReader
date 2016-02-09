package com.stockita.newxyzreader.mainui;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stockita.newxyzreader.R;
import com.stockita.newxyzreader.data.ItemsContract;
import com.stockita.newxyzreader.data.ModelItems;
import com.stockita.newxyzreader.ui.ArticleDetailActivity;
import com.stockita.newxyzreader.ui.DynamicHeightNetworkImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * This is the adapter that populate the recycler view in the main activity UI
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    // Member variables
    private final Activity mContext;
    private ArrayList<ModelItems> mModel;

    /**
     * Constructor
     * @param context           Activity object
     */
    public MainAdapter(Activity context) {
        mContext = context;
    }

    /**
     * Get the data feed
     * @param modelList     ArrayList of type ModelItems
     */
    public void swapData(ArrayList<ModelItems> modelList) {
        mModel = modelList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        return R.layout.adapter_main;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Initialize the view
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        // Initialize the view holder
        final ViewHolder vh = new ViewHolder(view);

        // Return the view
        return vh;
    }

    @Override
    public void onBindViewHolder(final MainAdapter.ViewHolder holder, final int position) {

        if (mModel != null) {

            // Title
            String title = mModel.get(position).getTitle();
            holder.titleView.setText(title);


            // Subtitle
            String author = mModel.get(position).getAuthor();
            String date = mModel.get(position).getPublishedDate();


            // Reformat the date
            String dateString =  DateUtils.getRelativeTimeSpanString(
                    Long.parseLong(date),
                    System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_ALL).toString();

            // Use string build to concatenate string
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(dateString);
            stringBuilder.append(" by ");
            stringBuilder.append(author);
            String subtitle = stringBuilder.toString();
            holder.subtitleView.setText(subtitle);

            // Image thumb
            String thumbUrl = mModel.get(position).getThumbUrl();
            Picasso.with(mContext).load(thumbUrl).into(holder.thumbnail);
            float aspectRatio = Float.parseFloat(mModel.get(position).getAspectRatio());
            holder.thumbnail.setAspectRatio(aspectRatio);
        }

        // This is when the user on an item it will start the detail ui
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the adapter position
                int adapterPosition = holder.getAdapterPosition();

                // Pack the intent
                Intent intent = new Intent(mContext, ArticleDetailActivity.class);

                // Only for Lollipop or above, otherwise go to else statement.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    // Pass the selected position
                    intent.putExtra("one", adapterPosition);

                    // Pass the ArrayList the has the Model
                    intent.putParcelableArrayListExtra("two", mModel);

                    // Set the scene transition and animation
                    ActivityOptionsCompat optionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(mContext, holder.root, "root");

                    // Start the activity, with the bundle for the transition to take effect.
                    mContext.startActivity(intent, optionsCompat.toBundle());
                } else
                    mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mModel != null ? mModel.size() : 0;
    }

    /**
     * The view holder class to support the adapter
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        // Views
        @Bind(R.id.root)
        CardView root;
        @Bind(R.id.thumbnail)
        DynamicHeightNetworkImageView thumbnail;
        @Bind(R.id.article_title)
        TextView titleView;
        @Bind(R.id.article_subtitle)
        TextView subtitleView;

        /**
         * Constructor
         * @param itemView      View object
         */
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
