package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.Constants.DOWNLOAD_STATUS
import com.udacity.Constants.FILE_NAME
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val notificationManager =
                ContextCompat.getSystemService(applicationContext, NotificationManager::class.java)
        val downloadedFile =intent.extras!!.getString(FILE_NAME)
        val status = intent.extras!!.getString(DOWNLOAD_STATUS)

        tv_file_name.text  = downloadedFile
        tv_status.text = status

        when(status){
            "Success"-> tv_status.setTextColor(getColor(R.color.colorPrimaryDark))
            "Failed" ->tv_status.setTextColor(getColor(R.color.red))
        }

            returnToMainActivity()

        notificationManager?.cancelAllNotifications()

    }

    private fun returnToMainActivity(){
        btn_ok.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }



}
