package com.example.pushprimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.CTDisplayUnitType;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class NativeDisplayActivity extends AppCompatActivity implements DisplayUnitListener {

    CleverTapAPI cleverTapDefaultInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_display);

        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
        CleverTapAPI.getDefaultInstance(this).setDisplayUnitListener(this);

    }

    @Override
    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> units) {
        LinearLayout parentLayout = findViewById(R.id.linear);

        // Initialize index for tracking the current image
        AtomicInteger index = new AtomicInteger(0);

        // Initialize flag to track whether events have been raised
        AtomicBoolean eventsRaised = new AtomicBoolean(false);

        // Create a Handler for updating the ImageView
        Handler handler = new Handler();

        // Create a Runnable for updating the ImageView every 2 seconds
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Check if units and contents are not empty
                if (!units.isEmpty()) {
                    CleverTapDisplayUnit unit = units.get(0); // Assuming only one unit in the list
                    ArrayList<CleverTapDisplayUnitContent> contents = unit.getContents();

                    // Check if contents are not empty
                    if (!contents.isEmpty()) {
                        // Get the current image URL based on the index
                        CleverTapDisplayUnitContent contentItem = contents.get(index.get());
                        String media = contentItem.getMedia();

                        // Check if the media URL is valid
                        if (media != null && !media.isEmpty()) {
                            // Load the image using Glide or any other image loading library
                            ImageView imageView = new ImageView(NativeDisplayActivity.this);
                            Glide.with(NativeDisplayActivity.this).load(media).into(imageView);

                            // Add the ImageView to the parent layout
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            imageView.setLayoutParams(layoutParams);

                            // Clear the parent layout before adding the new image
                            parentLayout.removeAllViews();
                            parentLayout.addView(imageView);

                            // Check if events have been raised already
                            if (!eventsRaised.get()) {
                                // Track "Notification Viewed" event for the unit
                                cleverTapDefaultInstance.pushDisplayUnitViewedEventForID(unit.getUnitID());

                                // Set the flag to true to indicate that events have been raised
                                eventsRaised.set(true);
                            }
                        }
                    }

                    // Increment the index or reset to 0 if it reaches the last index
                    int nextIndex = (index.get() + 1) % contents.size();
                    index.set(nextIndex);
                }

                // Post the same runnable after 2 seconds
                handler.postDelayed(this, 2000);
            }
        };

        // Start the initial post of the runnable to display the first image
        handler.post(runnable);
    }

}