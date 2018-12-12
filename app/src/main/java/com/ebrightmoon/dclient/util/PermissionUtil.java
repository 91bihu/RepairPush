package com.ebrightmoon.dclient.util;

import android.app.Activity;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;


/**
 * @Date 2018/2/8
 * @Author Mr.WEI
 * @Description 权限申请uitl
 **/
public class PermissionUtil {
    private static PermissionUtil permissionUtil = null;
    RxPermissions rxPermissions = null;

    private PermissionUtil() {
    }

    public static PermissionUtil getPerInstans() {
        if (permissionUtil == null) {
            permissionUtil = new PermissionUtil();
        }
        return permissionUtil;
    }

    /**
     * 同时申请多个权限
     *
     * @param activity
     * @param permissions
     */
    public void requestPermssions(final Activity activity, String[] permissions,
                                  final OnPermissionRequestListener onPermissionRequestListener) {
        rxPermissions = new RxPermissions(activity);
        rxPermissions.request(permissions).subscribe(
                new io.reactivex.functions.Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (onPermissionRequestListener != null) {
                            if (aBoolean) {
                                onPermissionRequestListener.onRequestSuccess();
                            } else {
                                onPermissionRequestListener.onRequestFail();
                            }
                        }
                    }
                });
    }

    /**
     * 申请单个权限
     *
     * @param activity
     * @param permission
     * @param onPermissionRequestListener
     */
    public void requestOnePermission(final Activity activity, String permission,
                                     final OnPermissionRequestListener onPermissionRequestListener) {
        rxPermissions = new RxPermissions(activity);
        rxPermissions.request(permission).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (onPermissionRequestListener != null) {
                    if (aBoolean) {
                        onPermissionRequestListener.onRequestSuccess();
                    } else {
                        onPermissionRequestListener.onRequestFail();
                    }
                }

            }
        });
    }

    public interface OnPermissionRequestListener {
        void onRequestSuccess();

        void onRequestFail();
    }
}
