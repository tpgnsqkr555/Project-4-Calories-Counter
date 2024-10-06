package com.example.calorietracker

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Description: This View stores the adjustment buttons and data
 */

class AdjustmentView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_adjustment_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val type = intent.getStringExtra("TYPE")
        val displayType: TextView = findViewById(R.id.type)
        displayType.setText(type)


        val count = intent.getStringExtra("VALUE")
        val displayCount: TextView = findViewById(R.id.count)
        displayCount.setText(count)
    }
}