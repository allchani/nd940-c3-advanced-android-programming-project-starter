package com.udacity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var fileName: String
    private lateinit var status: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        val bundle = intent.extras
        if(bundle != null) {
            fileName = bundle.getString("fileName")!!
            status = bundle.getString("status")!!
        }

        file_name_field.text = fileName
        status_field.text = status

        button_ok.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

}
