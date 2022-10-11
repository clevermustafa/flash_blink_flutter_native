package com.example.flash_blink

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel


class MainActivity: FlutterActivity() {
    val BATTERY_CHANNEL = "clevermustafa.com/flash_blink"
    private lateinit var channel: MethodChannel

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, BATTERY_CHANNEL)

        /// Receive data from flutter
        channel.setMethodCallHandler {call, result ->
            if(call.method == "blink") {

                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 200)
                    ActivityCompat.RequestPermissionsRequestCodeValidator { requestCode ->
                        if (requestCode == 200) {
                            var camera = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                            if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                                var light = false
                                var s = "10101010101010"
                                for (i in s.indices) {
                                    light = s[i] == '1'
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        camera.setTorchMode(camera.cameraIdList[0], light)
                                    }

                                }
                            }
                        } else {
                            Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show()
                        }
                    }


                } else {
                    var camera = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                    if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                        var light = false
                        var s = "10101010101010"
                        for (i in s.indices) {
                            light = s[i] == '1'
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                camera.setTorchMode(camera.cameraIdList[0], light)
                            }

                        }
                    }

                }
            }

        }
    }

}
