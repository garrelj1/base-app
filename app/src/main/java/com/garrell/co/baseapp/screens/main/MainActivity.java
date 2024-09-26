package com.garrell.co.baseapp.screens.main;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.garrell.co.baseapp.R;
import com.garrell.co.baseapp.screens.common.controller.BackPressDispatcher;
import com.garrell.co.baseapp.screens.common.controller.BackPressListener;
import com.garrell.co.baseapp.screens.common.controller.BaseActivity;
import com.garrell.co.baseapp.screens.common.screennavigator.ScreensNavigator;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends BaseActivity implements BackPressDispatcher {

    private ScreensNavigator screensNavigator;

    private final Set<BackPressListener> backPressListeners = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        screensNavigator = getCompositionRoot().getScreenNavigator();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_main);

        screensNavigator.init(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        boolean backPressListenerConsumedTheEvent = false;
        for (BackPressListener backPressListener : backPressListeners) {
            if (backPressListener.onBackPressed())
                backPressListenerConsumedTheEvent = true;
        }

        if (backPressListenerConsumedTheEvent || screensNavigator.navigateBack())
            return;

        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        screensNavigator.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void registerListener(BackPressListener listener) {
        backPressListeners.add(listener);
    }

    @Override
    public void unregisterListener(BackPressListener listener) {
        backPressListeners.remove(listener);
    }

}