package com.epicodus.myrestaurants.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.myrestaurants.Constants;
import com.epicodus.myrestaurants.R;
import com.epicodus.myrestaurants.models.Restaurant;
import com.epicodus.myrestaurants.ui.RestaurantDetailActivity;
import com.epicodus.myrestaurants.ui.RestaurantDetailFragment;
import com.epicodus.myrestaurants.util.ItemTouchHelperViewHolder;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RestaurantViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;
    private int mOrientation;
    private Integer mPosition;

    @Bind(R.id.restaurantImageView) ImageView mRestaurantImageView;
    @Bind(R.id.restaurantNameTextView) TextView mNameTextView;
    @Bind(R.id.categoryTextView) TextView mCategoryTextView;
    @Bind(R.id.ratingTextView) TextView mRatingTextView;

    private Context mContext;
    private ArrayList<Restaurant> mRestaurants = new ArrayList<>();

    public RestaurantViewHolder(View itemView, ArrayList<Restaurant> restaurants) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        mRestaurants = restaurants;
        // Determines the current orientation of the device:
        mOrientation = itemView.getResources().getConfiguration().orientation;
        // Checks if the recorded orientation matches Android's landscape configuration.
        // if so, we create a new DetailFragment to display in our special landscape layout:
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            createDetailFragment(0);
        }
        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mPosition = getLayoutPosition();

                if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    createDetailFragment(mPosition);
                } else {
                    Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_POSITION, mPosition.toString());
                    intent.putExtra(Constants.EXTRA_KEY_RESTAURANTS, Parcels.wrap(mRestaurants));
                    mContext.startActivity(intent);
                }

            }

        });
    }

    public void bindRestaurant(Restaurant restaurant) {
        Picasso.with(mContext).load(restaurant.getImageUrl()).into(mRestaurantImageView);

        Picasso.with(mContext)
                .load(restaurant.getImageUrl())
                .resize(MAX_WIDTH, MAX_HEIGHT)
                .centerCrop()
                .into(mRestaurantImageView);

        mNameTextView.setText(restaurant.getName());
        mCategoryTextView.setText(restaurant.getCategories().get(0));
        mRatingTextView.setText("Rating: " + restaurant.getRating() + "/5");
    }

    @Override
    public void onItemSelected() {
       itemView.animate()
               .alpha(0.7f)
               .scaleX(0.9f)
               .scaleY(0.9f)
               .setDuration(500);
    }

    @Override
    public void onItemClear() {
        itemView.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f);

    }
    // Takes position of restaurant in list as parameter:
    private void createDetailFragment(int position) {
        // Creates new RestaurantDetailFragment with the given position:
        RestaurantDetailFragment detailFragment = RestaurantDetailFragment.newInstance(mRestaurants, position);
        // Gathers necessary components to replace the FrameLayout in the layout with the RestaurantDetailFragment:
        FragmentTransaction ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
        //  Replaces the FrameLayout with the RestaurantDetailFragment:
        ft.replace(R.id.restaurantDetailContainer, detailFragment);
        // Commits these changes:
        ft.commit();
    }
}