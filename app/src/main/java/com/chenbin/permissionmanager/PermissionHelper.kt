package com.chenbin.permissionmanager

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils

/**
 * @author chenbin
 * @date 12/8/21
 */
object PermissionHelper {

    fun gotoSettingPermission(context: Context) {
        val brand = Build.BRAND//手机厂商
        if (TextUtils.equals(brand.toLowerCase(), "redmi") || TextUtils.equals(brand.toLowerCase(), "xiaomi")) {
            gotoMiuiPermission(context)
        } else if (TextUtils.equals(brand.toLowerCase(), "meizu")) {
            gotoMeizuPermission(context)
        } else if (TextUtils.equals(brand.toLowerCase(), "huawei") || TextUtils.equals(brand.toLowerCase(), "honor")) {
            gotoHuaweiPermission(context)
        } else if (TextUtils.equals(brand.toLowerCase(), "vivo")) {
            gotoVivoPermission(context)
        } else if (TextUtils.equals(brand.toLowerCase(), "oppo")) {
            gotoOppoPermission(context)
        } else {
            gotoIntentSetting(context)
        }
    }

    private fun gotoMiuiPermission(context: Context) {
        // 小米可以
        try {
            val localIntent = Intent("miui.intent.action.APP_PERM_EDITOR")
            localIntent.setClassName(
                "com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity"
            )
            localIntent.putExtra("extra_pkgname", context.packageName)
            context.startActivity(localIntent)
        } catch (e: Exception) {
            gotoIntentSetting(context)
        }
    }

    private fun gotoHuaweiPermission(context: Context) {
        try {
            val intent = Intent(context.packageName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val comp = ComponentName(
                "com.android.permissioncontroller",
                "com.android.packageinstaller.permission.ui.ManagePermissionsActivity"
            )
            // ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity")
            intent.component = comp
            context.startActivity(intent)
        } catch (e: Exception) {
            gotoIntentSetting(context)
        }
    }

    private fun gotoMeizuPermission(context: Context) {
        try {
            val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("packageName", context.packageName)
            context.startActivity(intent)
        } catch (e: Exception) {
            gotoIntentSetting(context)
        }
    }

    private fun gotoVivoPermission(context: Context) {
        try {
            var intent = Intent()
            if (((Build.MODEL.contains("Y85")) && (!Build.MODEL.contains("Y85A"))) || (Build.MODEL.contains("vivo Y53L"))) {
                intent.setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.PurviewTabActivity")
                intent.putExtra("packagename", context.packageName)
                intent.putExtra("tabId", "1")
            } else {
                intent.action = "secure.intent.action.softPermissionDetail"
                intent.setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity")
                intent.putExtra("packagename", context.packageName)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            gotoIntentSetting(context)
        }
    }

    private fun gotoOppoPermission(context: Context) {
        try {
            val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("packageName", context.packageName)
            val comp = ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.PermissionManagerActivity")//R9SK 6.0.1  os-v3.0
//            val comp = ComponentName("com.coloros.securitypermission", "com.coloros.securitypermission.permission.PermissionGroupsActivity")
            intent.component = comp
            context.startActivity(intent)
        } catch (e: Exception) {
            gotoIntentSetting(context)
        }
    }

    /**
     * 跳到应用详情页面
     */
    private fun gotoIntentSetting(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        try {
            context.startActivity(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取Manifest 申请的权限
     */
    @Synchronized
    fun getManifestPermissions(activity: Activity): Array<String>? {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = activity.packageManager.getPackageInfo(
                activity.packageName,
                PackageManager.GET_PERMISSIONS
            )
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo != null) {
            return packageInfo.requestedPermissions
        }
        return null
    }
}