package com.garrell.co.baseapp.common.permissions;

import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.garrell.co.baseapp.common.observable.BaseObservable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

@UiThread
public class PermissionsHelper extends BaseObservable<PermissionsHelper.Listener> {

    public interface Listener {
        void onRequestPermissionsResult(PermissionsResult result);
        void onPermissionsRequestCancelled();
    }

    public static class PermissionsResult {
        public final List<MyPermission> granted;
        public final List<MyPermission> denied;
        public final List<MyPermission> deniedDoNotAskAgain;

        public PermissionsResult(List<MyPermission> granted, List<MyPermission> denied, List<MyPermission> deniedDoNotAskAgain) {
            this.granted = granted;
            this.denied = denied;
            this.deniedDoNotAskAgain = deniedDoNotAskAgain;
        }
    }

    private final AppCompatActivity mActivity;
    private final ActivityResultLauncher<String []> resultHandler;

    public PermissionsHelper(AppCompatActivity activity) {
        mActivity = activity;

        ActivityResultContracts.RequestMultiplePermissions permissionsRequest = new ActivityResultContracts.RequestMultiplePermissions();
        resultHandler = mActivity.registerForActivityResult(permissionsRequest, newPermissionsResultCallback());
    }

    public ActivityResultCallback<Map<String, Boolean>> newPermissionsResultCallback() {
        return permissionResults -> {
            Timber.d("Permissions request result: %s", permissionResults.toString());

            List<MyPermission> grantedPermissions = new LinkedList<>();
            List<MyPermission> deniedPermissions = new LinkedList<>();
            List<MyPermission> deniedAndDoNotAskAgainPermissions = new LinkedList<>();

            for (Map.Entry<String, Boolean> permissionResult : permissionResults.entrySet()) {

                String androidPermission;
                MyPermission permission;

                androidPermission = permissionResult.getKey();
                permission = MyPermission.fromAndroidPermission(androidPermission);
                if (permissionResult.getValue()) {
                    grantedPermissions.add(permission);
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, androidPermission)) {
                    deniedPermissions.add(permission);
                } else {
                    deniedAndDoNotAskAgainPermissions.add(permission);
                }
            }

            if (grantedPermissions.isEmpty()) {
                notifyPermissionsRequestCancelled();
            } else {
                PermissionsResult result = new PermissionsResult(grantedPermissions, deniedPermissions, deniedAndDoNotAskAgainPermissions);
                notifyPermissionsResult(result);
            }
        };
    }

    public boolean hasPermission(MyPermission permission) {
        return ContextCompat.checkSelfPermission(mActivity, permission.getAndroidPermission()) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean hasAllPermissions(MyPermission[] permissions) {
        for (MyPermission permission : permissions) {
            if (ContextCompat.checkSelfPermission(mActivity, permission.getAndroidPermission()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void requestPermission(MyPermission permission, int requestCode) {
        ActivityCompat.requestPermissions(mActivity, new String[] { permission.getAndroidPermission() }, requestCode);
    }

    public void requestAllPermissions(MyPermission[] permissions, int requestCode) {
        String[] androidPermissions = new String[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            androidPermissions[i] = permissions[i].getAndroidPermission();
        }

        resultHandler.launch(androidPermissions);
    }

    private void notifyPermissionsResult(PermissionsResult permissionsResult) {
        for (Listener listener : getListeners()) {
            listener.onRequestPermissionsResult(permissionsResult);
        }
    }

    private void notifyPermissionsRequestCancelled() {
        for (Listener listener : getListeners()) {
            listener.onPermissionsRequestCancelled();
        }
    }
}
