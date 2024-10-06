package com.example.calorietracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    val types = arrayOf("Meals","Water","Snacks")
    val counts = arrayOf(0,0,0)
    var total = 0
    private val adjustmentLauncher = registerForActivityResult(//Instance of the launcher that receives the data
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        //Handle result
        if (result.resultCode == Activity.RESULT_OK) {
            var receivedType = result.data?.getStringExtra("TYPE")//instance of result
            val typeView: TextView = findViewById(R.id.type)//type textview in other activity
            typeView.setText(receivedType)//save the value of the result instance to the textview

            var receivedValue = result.data?.getStringExtra("VALUE")
            val valueView: TextView = findViewById(R.id.count)
            valueView.setText(receivedValue)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Log.i("ActivityCallback", "onCreate was triggered")
        val upMeals: Button = findViewById(R.id.updateMealsButton)
        val upWater: Button = findViewById(R.id.updateWaterButton)
        val upSnacks: Button = findViewById(R.id.updateSnacksButton)

        upMeals.setOnClickListener{
            Log.i("Listeners", "Update Meals Button Pressed")
            val intent = Intent(this, AdjustmentView::class.java)
            startActivity(intent)
        }
        upWater.setOnClickListener{
            Log.i("Listeners", "Update Water Button Pressed")
            val intent = Intent(this, AdjustmentView::class.java)
            startActivity(intent)
        }
        upSnacks.setOnClickListener{
            Log.i("Listeners", "Update Snacks Button Pressed")
            val intent = Intent(this, AdjustmentView::class.java)
            startActivity(intent)
        }

    }

    /* TODO Fix this method override
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        // Always call the superclass so it can restore the view hierarchy.
        if (savedInstanceState != null) {
            super.onRestoreInstanceState(savedInstanceState)
        }
        // Restore state members from saved instance.
        savedInstanceState?.run {
            countM = getString("MEALS").toString()
            countW = getString("WATER").toString()
            countS = getString("SNACKS").toString()
        }
    }
     */

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("ActivityCallback", "Saved Instance State")

        outState.run {
            putInt("MEALS", counts[0])
            putInt("WATER", counts[1])
            putInt("SNACKS", counts[2])
        }
        super.onSaveInstanceState(outState)
    }

    companion object {
        var countM = "MEALS"
        var countW = "WATER"
        var countS = "SNACKS"
    }
    override fun onStart() {
        super.onStart()

        Log.i("ActivityCallback", "onStart was triggered")
    }
    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing()) {
            Log.i("ActivityCallback", "onDestroy was triggered")
        }
    }
}