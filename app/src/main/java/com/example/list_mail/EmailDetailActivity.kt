package com.example.list_mail

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EmailDetailActivity : AppCompatActivity() {
    private var position: Int = -1
    private lateinit var email: Email
    private lateinit var ivStar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_detail)

        position = intent.getIntExtra("email_position", -1)
        email = intent.getSerializableExtra("email_data") as Email

        val tvSender: TextView = findViewById(R.id.detailSender)
        val tvSubject: TextView = findViewById(R.id.detailSubject)
        val tvPreview: TextView = findViewById(R.id.detailPreview)
        val tvTime: TextView = findViewById(R.id.detailTime)
        ivStar = findViewById(R.id.detailStar)

        tvSender.text = email.senderName
        tvSubject.text = email.subject
        tvPreview.text = email.preview
        tvTime.text = email.time
        renderStar()

        ivStar.setOnClickListener {
            email.starred = !email.starred
            renderStar()
        }
    }

    private fun renderStar() {
        ivStar.setImageResource(if (email.starred) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off)
    }

    override fun onBackPressed() {
        val data = Intent().apply {
            putExtra("email_position", position)
            putExtra("email_starred", email.starred)
        }
        setResult(RESULT_OK, data)
        super.onBackPressed()
    }
}
