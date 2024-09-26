package com.garrell.co.baseapp.screens.common.screennavigator;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.garrell.co.baseapp.R;
import com.garrell.co.baseapp.screens.home.HomeFragment;

public class ScreensNavigator {

    private final AppCompatActivity activity;

    public ScreensNavigator(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void onSaveInstanceState(Bundle saveInstanceState) {
    }

    public void navigateToRequestPermissions(Bundle savedInsanceState) {
        this.activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_view, HomeFragment.newInstance())
                .commit();
    }

    public boolean navigateBack() {
        return false;
    }

    public void init(Bundle savedInstanceState) {
        navigateToRequestPermissions(savedInstanceState);
    }
}
