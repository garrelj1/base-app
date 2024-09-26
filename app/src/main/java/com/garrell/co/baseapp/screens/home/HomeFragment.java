package com.garrell.co.baseapp.screens.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.garrell.co.baseapp.screens.common.controller.BaseFragment;
import com.garrell.co.baseapp.screens.common.dialogs.DialogsManager;
import com.garrell.co.baseapp.common.permissions.MyPermission;
import com.garrell.co.baseapp.common.permissions.PermissionsHelper;
import com.garrell.co.baseapp.screens.common.ViewMvcFactory;

import com.garrell.co.baseapp.screens.common.screennavigator.ScreensNavigator;

import timber.log.Timber;

public class HomeFragment extends BaseFragment implements
        HomeViewMvc.Listener,
        PermissionsHelper.Listener {

    private static final MyPermission[] PERMISSIONS = new MyPermission[]{};

    private DialogsManager dialogsManager;
    private ViewMvcFactory viewMvcFactory;
    private PermissionsHelper permissionsHelper;
    private ScreensNavigator navigator;

    private HomeViewMvc mViewMvc;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        viewMvcFactory = getControllerCompositionRoot().getViewMvcFactory();
        permissionsHelper = getControllerCompositionRoot().getPermissionsHelper();
        navigator = getControllerCompositionRoot().getScreenNavigator();
        dialogsManager = getControllerCompositionRoot().getDialogsManager();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewMvc = viewMvcFactory.newHomeViewMvc(container);
        return mViewMvc.getRootView();
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewMvc.registerListener(this);
        permissionsHelper.registerListener(this);

        refreshPermissionsUi();
    }

    @Override
    public void onStop() {
        super.onStop();
        mViewMvc.unregisterListener(this);
        permissionsHelper.unregisterListener(this);
    }

    @Override
    public void onRequestPermissionsClicked() {
        Timber.d("Controller got request permissions click");
        permissionsHelper.requestAllPermissions(PERMISSIONS, 0);
    }


    private void refreshPermissionsUi() {
        if (permissionsHelper.hasAllPermissions(PERMISSIONS)) {
            Timber.d("Has all permissions");
            mViewMvc.disableRequestPermissionsButton();
        } else {
            mViewMvc.enableRequestPermissionsButton();
        }
    }

    @Override
    public void onRequestPermissionsResult(PermissionsHelper.PermissionsResult result) {
        Timber.d("Result of permission request");

        if (!result.deniedDoNotAskAgain.isEmpty()) {
            dialogsManager.showInfoDialog(
                    "Missing permissions",
                    "Permissions denied and we can't ask for them again: "
                            + result.deniedDoNotAskAgain.toString()
                            + "\nPart of app's functionality might not work!",
                    "OK",
                    null
            );
        } else if (!result.denied.isEmpty()) {
            dialogsManager.showInfoDialog(
                    "Missing permissions",
                    "Permissions denied: "
                            + result.denied.toString()
                            + "\n\nWe need these permissions to work!",
                    "OK",
                    null
            );
        } else {
            dialogsManager.showInfoDialog(
                    "",
                    "Permissions granted",
                    "OK",
                    null
            );
        }
        refreshPermissionsUi();
    }

    @Override
    public void onPermissionsRequestCancelled() {
        dialogsManager.showInfoDialog(
                "",
                "Permissions request cancelled",
                "OK",
                null
        );
        refreshPermissionsUi();
    }

}
