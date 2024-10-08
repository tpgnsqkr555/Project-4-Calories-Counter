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

    private lateinit var dateText: TextView
    private lateinit var mealsCalories: TextView
    private lateinit var waterCups: TextView
    private lateinit var snacksCalories: TextView
    private lateinit var totalCalories: TextView
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val REQUEST_CODE_UPDATE_MEALS = 1
        const val REQUEST_CODE_UPDATE_WATER = 2
        const val REQUEST_CODE_UPDATE_SNACKS = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("CalorieTrackerPrefs", MODE_PRIVATE)

        initializeViews()
        loadSavedData()
        setupClickListeners()
        updateTotalCalories()
    }

    private fun initializeViews() {
        dateText = findViewById(R.id.dateText)
        mealsCalories = findViewById(R.id.mealsCalories)
        waterCups = findViewById(R.id.waterCups)
        snacksCalories = findViewById(R.id.snacksCalories)
        totalCalories = findViewById(R.id.totalCalories)
    }

    private fun loadSavedData() {
        val savedDate = sharedPreferences.getLong("selected_date", System.currentTimeMillis())
        val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        dateText.text = getString(R.string.date_label_format, dateFormat.format(Date(savedDate)))

        mealsCalories.text = sharedPreferences.getInt("meals_calories", 0).toString()
        waterCups.text = getString(R.string.water_cups_format, sharedPreferences.getInt("water_cups", 0))
        snacksCalories.text = sharedPreferences.getInt("snacks_calories", 0).toString()
    }

    private fun setupClickListeners() {
        findViewById<Button>(R.id.changeDateButton).setOnClickListener {
            showDatePicker()
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

    private fun showDatePicker() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            dateText.text = getString(R.string.date_label_format, formattedDate)

            sharedPreferences.edit().putLong("selected_date", selectedDate.timeInMillis).apply()
        }, year, month, day).show()
    }

    private fun openAdjustmentView(itemType: String, currentAmount: Int, requestCode: Int) {
        val intent = Intent(this, AdjustmentView::class.java).apply {
            putExtra("ITEM_TYPE", itemType)
            putExtra("CURRENT_AMOUNT", currentAmount)
        }
        startActivityForResult(intent, requestCode)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val newAmount = data?.getIntExtra("NEW_AMOUNT", 0) ?: 0
            when (requestCode) {
                REQUEST_CODE_UPDATE_MEALS -> updateMealsCalories(newAmount)
                REQUEST_CODE_UPDATE_WATER -> updateWaterCups(newAmount)
                REQUEST_CODE_UPDATE_SNACKS -> updateSnacksCalories(newAmount)
            }
            updateTotalCalories()
        }
    }

    private fun updateMealsCalories(newAmount: Int) {
        mealsCalories.text = newAmount.toString()
        sharedPreferences.edit().putInt("meals_calories", newAmount).apply()
    }

    private fun updateWaterCups(newAmount: Int) {
        waterCups.text = getString(R.string.water_cups_format, newAmount)
        sharedPreferences.edit().putInt("water_cups", newAmount).apply()
    }

    private fun updateSnacksCalories(newAmount: Int) {
        snacksCalories.text = newAmount.toString()
        sharedPreferences.edit().putInt("snacks_calories", newAmount).apply()
    }

    private fun updateTotalCalories() {
        val meals = mealsCalories.text.toString().toIntOrNull() ?: 0
        val snacks = snacksCalories.text.toString().toIntOrNull() ?: 0
        val total = meals + snacks
        totalCalories.text = getString(R.string.total_calories_format, total)
    }
}