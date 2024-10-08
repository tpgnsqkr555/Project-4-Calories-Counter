package com.example.calorietracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdjustmentView : AppCompatActivity() {

    // Declare UI components
    private lateinit var tvCurrentAmount: TextView
    private lateinit var tvItemType: TextView
    private lateinit var btnDecrease: Button
    private lateinit var btnIncrease: Button
    private lateinit var btnUpdate: Button

    // Variables to hold the current amount and item type
    private var currentAmount = 0
    private lateinit var itemType: String
    private var stepSize = 1 // Default step size for adjustment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adjustment_view)

        // Initialize views, retrieve intent data, set up button click listeners, and update the display
        initializeViews()
        getIntentData()
        setupClickListeners()
        updateDisplay()
    }

    // Function to initialize views from the layout
    private fun initializeViews() {
        tvCurrentAmount = findViewById(R.id.tvCurrentAmount)
        tvItemType = findViewById(R.id.tvItemType)
        btnDecrease = findViewById(R.id.btnDecrease)
        btnIncrease = findViewById(R.id.btnIncrease)
        btnUpdate = findViewById(R.id.btnUpdate)
    }

    // Function to retrieve data from the intent passed to this activity
    private fun getIntentData() {
        // Get the item type (Meals, Water, Snacks) and current amount from the intent
        itemType = intent.getStringExtra("ITEM_TYPE") ?: "Unknown"
        currentAmount = intent.getIntExtra("CURRENT_AMOUNT", 0)

        // Set the step size based on the item type. Water increments/decrements by 1, while calories by 10
        stepSize = if (itemType == "Water") 1 else 10
    }

    // Set up click listeners for the buttons to increase, decrease, and update the amount
    private fun setupClickListeners() {
        // Decrease button listener
        btnDecrease.setOnClickListener {
            if (currentAmount >= stepSize) {
                currentAmount -= stepSize  // Decrease by step size if the current amount is greater than or equal to the step size
                updateDisplay()
            }
        }

        // Increase button listener
        btnIncrease.setOnClickListener {
            currentAmount += stepSize  // Increase by step size
            updateDisplay()
        }

        // Update button listener, returns the new amount to the previous activity
        btnUpdate.setOnClickListener {
            val intent = Intent().apply {
                putExtra("NEW_AMOUNT", currentAmount)  // Put the new adjusted amount in the intent
            }
            setResult(RESULT_OK, intent)  // Set the result as OK to indicate the update was successful
            finish()  // Close the activity and return to the main activity
        }
    }

    // Function to update the display based on the current amount and item type
    private fun updateDisplay() {
        // Set the text views with the current values
        tvCurrentAmount.text = currentAmount.toString()
        tvItemType.text = itemType

        // Update button labels based on whether it's water or calories
        if (itemType == "Water") {
            btnDecrease.text = getString(R.string.decrease_water)  // Label for water decrease
            btnIncrease.text = getString(R.string.increase_water)  // Label for water increase
        } else {
            btnDecrease.text = getString(R.string.decrease_calories)  // Label for calories decrease
            btnIncrease.text = getString(R.string.increase_calories)  // Label for calories increase
        }
    }
}
