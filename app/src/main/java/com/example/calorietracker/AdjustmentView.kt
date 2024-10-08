package com.example.calorietracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdjustmentView : AppCompatActivity() {
    private lateinit var tvCurrentAmount: TextView
    private lateinit var tvItemType: TextView
    private lateinit var btnDecrease: Button
    private lateinit var btnIncrease: Button
    private lateinit var btnUpdate: Button

    private var currentAmount = 0
    private lateinit var itemType: String
    private var stepSize = 1 // Default step size

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adjustment_view)

        initializeViews()
        getIntentData()
        setupClickListeners()
        updateDisplay()
    }

    private fun initializeViews() {
        tvCurrentAmount = findViewById(R.id.tvCurrentAmount)
        tvItemType = findViewById(R.id.tvItemType)
        btnDecrease = findViewById(R.id.btnDecrease)
        btnIncrease = findViewById(R.id.btnIncrease)
        btnUpdate = findViewById(R.id.btnUpdate)
    }

    private fun getIntentData() {
        itemType = intent.getStringExtra("ITEM_TYPE") ?: "Unknown"
        currentAmount = intent.getIntExtra("CURRENT_AMOUNT", 0)
        stepSize = if (itemType == "Water") 1 else 10
    }

    private fun setupClickListeners() {
        btnDecrease.setOnClickListener {
            if (currentAmount >= stepSize) {
                currentAmount -= stepSize
                updateDisplay()
            }
        }
        btnIncrease.setOnClickListener {
            currentAmount += stepSize
            updateDisplay()
        }
        btnUpdate.setOnClickListener {
            val intent = Intent().apply {
                putExtra("NEW_AMOUNT", currentAmount)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun updateDisplay() {
        tvCurrentAmount.text = currentAmount.toString()
        tvItemType.text = itemType

        if (itemType == "Water") {
            btnDecrease.text = getString(R.string.decrease_water)
            btnIncrease.text = getString(R.string.increase_water)
        } else {
            btnDecrease.text = getString(R.string.decrease_calories)
            btnIncrease.text = getString(R.string.increase_calories)
        }
    }
}