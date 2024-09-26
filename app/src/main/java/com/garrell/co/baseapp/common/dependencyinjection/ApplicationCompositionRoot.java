package com.garrell.co.baseapp.common.dependencyinjection;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;

import com.garrell.co.baseapp.common.eventbus.EventBusPoster;
import com.garrell.co.baseapp.common.eventbus.EventBusSubscriber;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import org.greenrobot.eventbus.EventBus;

public class ApplicationCompositionRoot {

    private final Application mApplication;

    private UiThreadPoster mUiThreadPoster;
    private BackgroundThreadPoster mBackgroundThreadPoster;

    public ApplicationCompositionRoot(Application application) {
        mApplication = application;
    }

    public UiThreadPoster getUiThreadPoster() {
        if (mUiThreadPoster == null) {
            mUiThreadPoster = new UiThreadPoster();
        }
        return mUiThreadPoster;
    }

    public BackgroundThreadPoster getBackgroundThreadPoster() {
        if (mBackgroundThreadPoster == null) {
            mBackgroundThreadPoster = new BackgroundThreadPoster();
        }
        return mBackgroundThreadPoster;
    }

    public ContentResolver getContentResolver() {
        return mApplication.getContentResolver();
    }

    public Context getContext() {
        return mApplication.getApplicationContext();
    }

    public EventBusPoster getEventBusPoster() {
        return new EventBusPoster(EventBus.getDefault());
    }

    public EventBusSubscriber getEventBusSubscriber() {
        return new EventBusSubscriber(EventBus.getDefault());
    }

}
