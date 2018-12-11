package com.example.admin.padometer

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_mode.*
import timber.log.Timber

class SelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant()
        setContentView(R.layout.activity_mode)
        liveButton.setOnClickListener {
            startActivity(Intent(this, StepActivity::class.java))
        }

        offlineButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}