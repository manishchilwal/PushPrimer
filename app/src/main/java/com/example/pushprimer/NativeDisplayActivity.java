package com.example.pushprimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;

import java.util.ArrayList;

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
        for (CleverTapDisplayUnit unit : units) {
            for (CleverTapDisplayUnitContent contentItem : unit.getContents()) {
                String media = contentItem.getMedia();
                Log.d("Media", "Media: " + media);
                ImageView imageView = new ImageView(this);
                Glide.with(this).load(media).into(imageView);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                imageView.setLayoutParams(layoutParams);
                parentLayout.addView(imageView);
            }
        }

    }
}