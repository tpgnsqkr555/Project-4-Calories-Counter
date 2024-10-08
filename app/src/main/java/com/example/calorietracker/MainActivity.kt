package com.example.calorietracker

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    // Declare views and shared preferences
    private lateinit var dateText: TextView
    private lateinit var mealsCalories: TextView
    private lateinit var waterCups: TextView
    private lateinit var snacksCalories: TextView
    private lateinit var totalCalories: TextView
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        // Request codes for tracking updates from different activities
        const val REQUEST_CODE_UPDATE_MEALS = 1
        const val REQUEST_CODE_UPDATE_WATER = 2
        const val REQUEST_CODE_UPDATE_SNACKS = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize SharedPreferences to store app data
        sharedPreferences = getSharedPreferences("CalorieTrackerPrefs", MODE_PRIVATE)

        // Initialize views and setup listeners for button clicks
        initializeViews()
        loadSavedData()  // Load the saved data or update the date to today
        setupClickListeners()
        updateTotalCalories()  // Update the total calories when the app starts
    }

    // Function to initialize views from the layout
    private fun initializeViews() {
        dateText = findViewById(R.id.dateText)
        mealsCalories = findViewById(R.id.mealsCalories)
        waterCups = findViewById(R.id.waterCups)
        snacksCalories = findViewById(R.id.snacksCalories)
        totalCalories = findViewById(R.id.totalCalories)
    }

    // Load saved data from SharedPreferences and update date if necessary
    private fun loadSavedData() {
        val savedDateMillis = sharedPreferences.getLong("selected_date", 0L)
        val savedDate = Calendar.getInstance()
        savedDate.timeInMillis = savedDateMillis

        val today = Calendar.getInstance()

        // Check if the saved date is older than today, and update it if necessary
        if (savedDate.get(Calendar.YEAR) != today.get(Calendar.YEAR) ||
            savedDate.get(Calendar.DAY_OF_YEAR) != today.get(Calendar.DAY_OF_YEAR)) {
            // Update to today's date
            sharedPreferences.edit().putLong("selected_date", today.timeInMillis).apply()
            updateDateText(today)  // Display today's date
        } else {
            // Show the saved date
            updateDateText(savedDate)
        }

        // Load saved values for calories and water cups
        mealsCalories.text = sharedPreferences.getInt("meals_calories", 0).toString()
        waterCups.text = getString(R.string.water_cups_format, sharedPreferences.getInt("water_cups", 0))
        snacksCalories.text = sharedPreferences.getInt("snacks_calories", 0).toString()
    }

    // Helper function to update the dateText view with a given Calendar date
    private fun updateDateText(calendar: Calendar) {
        val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        dateText.text = getString(R.string.date_label_format, dateFormat.format(calendar.time))
    }

    // Set up button click listeners for updating date, meals, water, and snacks
    private fun setupClickListeners() {
        findViewById<Button>(R.id.changeDateButton).setOnClickListener {
            showDatePicker()  // Show the date picker when user clicks on change date button
        }

        findViewById<Button>(R.id.updateMealsButton).setOnClickListener {
            openAdjustmentView("Meals", mealsCalories.text.toString().toIntOrNull() ?: 0, REQUEST_CODE_UPDATE_MEALS)
        }

        findViewById<Button>(R.id.updateWaterButton).setOnClickListener {
            openAdjustmentView("Water", waterCups.text.toString().split(" ")[0].toIntOrNull() ?: 0, REQUEST_CODE_UPDATE_WATER)
        }

        findViewById<Button>(R.id.updateSnacksButton).setOnClickListener {
            openAdjustmentView("Snacks", snacksCalories.text.toString().toIntOrNull() ?: 0, REQUEST_CODE_UPDATE_SNACKS)
        }
    }

    // Function to show the DatePickerDialog and save selected date
    private fun showDatePicker() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        // Create and show DatePickerDialog
        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)

            // Update the displayed date
            updateDateText(selectedDate)

            // Save selected date to SharedPreferences
            sharedPreferences.edit().putLong("selected_date", selectedDate.timeInMillis).apply()
        }, year, month, day).show()
    }

    // Function to open the adjustment view for meals, water, or snacks
    private fun openAdjustmentView(itemType: String, currentAmount: Int, requestCode: Int) {
        val intent = Intent(this, AdjustmentView::class.java).apply {
            putExtra("ITEM_TYPE", itemType)  // Pass the item type (Meals, Water, Snacks) to the adjustment activity
            putExtra("CURRENT_AMOUNT", currentAmount)  // Pass the current value to adjust
        }
        startActivityForResult(intent, requestCode)  // Start the adjustment activity
    }

    // Handle result from the adjustment activity and update values accordingly
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val newAmount = data?.getIntExtra("NEW_AMOUNT", 0) ?: 0
            when (requestCode) {
                REQUEST_CODE_UPDATE_MEALS -> updateMealsCalories(newAmount)  // Update meals calories
                REQUEST_CODE_UPDATE_WATER -> updateWaterCups(newAmount)  // Update water cups
                REQUEST_CODE_UPDATE_SNACKS -> updateSnacksCalories(newAmount)  // Update snacks calories
            }
            updateTotalCalories()  // Recalculate total calories after update
        }
    }

    // Update meals calories and save to SharedPreferences
    private fun updateMealsCalories(newAmount: Int) {
        mealsCalories.text = newAmount.toString()
        sharedPreferences.edit().putInt("meals_calories", newAmount).apply()
    }

    // Update water cups and save to SharedPreferences
    private fun updateWaterCups(newAmount: Int) {
        waterCups.text = getString(R.string.water_cups_format, newAmount)
        sharedPreferences.edit().putInt("water_cups", newAmount).apply()
    }

    // Update snacks calories and save to SharedPreferences
    private fun updateSnacksCalories(newAmount: Int) {
        snacksCalories.text = newAmount.toString()
        sharedPreferences.edit().putInt("snacks_calories", newAmount).apply()
    }

    // Update total calories by summing meals and snacks
    private fun updateTotalCalories() {
        val meals = mealsCalories.text.toString().toIntOrNull() ?: 0
        val snacks = snacksCalories.text.toString().toIntOrNull() ?: 0
        val total = meals + snacks
        totalCalories.text = getString(R.string.total_calories_format, total)  // Display total calories
    }
}
