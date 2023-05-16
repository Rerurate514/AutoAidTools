package com.example.autoaidtools

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.example.autoaidtools.databinding.ActivityMainBinding
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel : ViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        requestOverlayPermission()

        openFragment()
        //openOverlay()
    }

    private fun openFragment(){
        val tag = "controlFragment"
        var fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = ControlFragment()
            supportFragmentManager.beginTransaction().apply {
                add(R.id.fragmentContainer, fragment, tag)
            }.commit()
        }
    }

    fun openOverlay(){
        val tag = "overlayFragment"
        var fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = OverlayFragment()
            supportFragmentManager.beginTransaction().apply {
                add(R.id.overlayContainer, fragment, tag)
            }.commit()
        }
    }

    private fun requestOverlayPermission() {
        if (isOverlayGranted()) return
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(intent, 1)
    }

    private fun isOverlayGranted() =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                Settings.canDrawOverlays(this)

}