package com.udacity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        val fileName= intent.extras?.getString("fileName")
        val download_status= intent.extras?.getString("status")

        file_name.setText(fileName)
        status.setText(download_status)
        btn_ok.setOnClickListener {
            close()
        }
    }

    private fun close(){
        val intent= Intent(applicationContext,MainActivity::class.java)
        startActivity(intent)
    }

}
