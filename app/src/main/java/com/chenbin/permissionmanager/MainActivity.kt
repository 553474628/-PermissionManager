package com.chenbin.permissionmanager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    private lateinit var adapter: MyAdapter
    private val data = ArrayList<ItemBean>()
    private val permissionNameList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 28个危险权限
        val dangerPermissions = listOf(
            Permission.READ_CALENDAR,
            Permission.WRITE_CALENDAR,
            Permission.CAMERA,
            Permission.GET_ACCOUNTS,
            Permission.READ_CONTACTS,
            Permission.WRITE_CONTACTS,
            Permission.ACCESS_FINE_LOCATION,
            Permission.ACCESS_COARSE_LOCATION,
            Permission.ACCESS_BACKGROUND_LOCATION,
            Permission.RECORD_AUDIO,
            Permission.READ_PHONE_STATE,
            Permission.CALL_PHONE,
            Permission.USE_SIP,
            Permission.READ_PHONE_NUMBERS,
            Permission.ANSWER_PHONE_CALLS,
            Permission.ADD_VOICEMAIL,
            Permission.READ_CALL_LOG,
            Permission.WRITE_CALL_LOG,
            Permission.PROCESS_OUTGOING_CALLS,
            Permission.BODY_SENSORS,
            Permission.ACTIVITY_RECOGNITION,
            Permission.SEND_SMS,
            Permission.RECEIVE_SMS,
            Permission.READ_SMS,
            Permission.RECEIVE_WAP_PUSH,
            Permission.RECEIVE_MMS,
            Permission.READ_EXTERNAL_STORAGE,
            Permission.WRITE_EXTERNAL_STORAGE
        )

        val manifestPermissions = PermissionHelper.getManifestPermissions(this)

        if (manifestPermissions != null) {
            for (permission in manifestPermissions) {
                val transformText = Permission.transformText(this, permission)
                // 过滤出危险的权限
                if (dangerPermissions.contains(permission) && !permissionNameList.contains(transformText[0])) {
                    permissionNameList.add(transformText[0])
                    data.add(ItemBean(transformText[0], AndPermission.hasPermissions(this, permission)))
                }
            }
        }

        val layoutManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = layoutManager
        adapter = MyAdapter(this, data)
        adapter.setOnItemClick {
            PermissionHelper.gotoSettingPermission(this)
        }
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        // 应该加个权限名和权限组的映射  然后去判断
        for (item in data) {
            try {
                item.granted = AndPermission.hasPermissions(this, transformPermissionGroup(item.name))
            } catch (e: Throwable) {
                e.printStackTrace()
                Log.e("chenbin", "onResume: error")
            }
        }
        adapter.notifyDataSetChanged()
    }

    fun transformPermissionGroup(name: String): Array<String>? =
        when (name) {
            "相机" -> Permission.Group.CAMERA
            "存储空间" -> Permission.Group.STORAGE
            "日历" -> Permission.Group.CALENDAR
            else -> null
        }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("chenbin", "onDestroy")
    }
}