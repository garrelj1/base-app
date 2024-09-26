package com.garrell.co.baseapp.common.permissions;

import android.Manifest;

public enum MyPermission {
    CALL_LOGS(Manifest.permission.READ_CALL_LOG),
    CONTACTS(Manifest.permission.READ_CONTACTS);

    public static MyPermission fromAndroidPermission(String androidPermission) {
        for (MyPermission permission : MyPermission.values()) {
            if (permission.getAndroidPermission().equals(androidPermission)) {
                return permission;
            }
        }
        throw new RuntimeException("Android permission not supported yet: " + androidPermission);
    }

    private final String mAndroidPermission;

    MyPermission(String androidPermission) {
        mAndroidPermission = androidPermission;
    }

    public String getAndroidPermission() {
        return mAndroidPermission;
    }
}
