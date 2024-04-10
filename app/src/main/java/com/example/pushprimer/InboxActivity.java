package com.example.pushprimer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CTInboxStyleConfig;
import com.clevertap.android.sdk.CleverTapAPI;

import java.util.ArrayList;

public class InboxActivity extends AppCompatActivity implements CTInboxListener {

    CleverTapAPI cleverTapDefaultInstance;
    Button inboxButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
        inboxButton = findViewById(R.id.ct_inbox);

        if (cleverTapDefaultInstance != null) {
            cleverTapDefaultInstance.setCTNotificationInboxListener(this);
            cleverTapDefaultInstance.initializeInbox();
        }
    }

    @Override
    public void inboxDidInitialize() {
        inboxButton.setOnClickListener(v -> {
            ArrayList<String> tabs = new ArrayList<>();
            tabs.add("Promotions");
            tabs.add("Offers");//We support upto 2 tabs only. Additional tabs will be ignored

            CTInboxStyleConfig styleConfig = new CTInboxStyleConfig();
            styleConfig.setFirstTabTitle("First Tab");
            styleConfig.setTabs(tabs);//Do not use this if you don't want to use tabs
            styleConfig.setTabBackgroundColor("#FF0000");
            styleConfig.setSelectedTabIndicatorColor("#0000FF");
            styleConfig.setSelectedTabColor("#0000FF");
            styleConfig.setUnselectedTabColor("#FFFFFF");
            styleConfig.setBackButtonColor("#FF0000");
            styleConfig.setNavBarTitleColor("#FF0000");
            styleConfig.setNavBarTitle("MY INBOX");
            styleConfig.setNavBarColor("#FFFFFF");
            styleConfig.setInboxBackgroundColor("#ADD8E6");
            if (cleverTapDefaultInstance != null) {
                cleverTapDefaultInstance.showAppInbox(styleConfig); //With Tabs
            }
        });
    }

    @Override
    public void inboxMessagesDidUpdate() {

    }
}