package com.example.calorietracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * @constructor Instantiates a new CustomAdapter for the given dataset and RecyclerViewEvent listener
 *
 * @param dataSet is the dataset to be used for the View
 * @param listener is the RecyclerViewEvent being tracked
 */
class CustomAdapter(private val dataSet: Array<String>, private val listener: RecyclerViewEvent):
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Adds an interface that calls the item click function
     */
    interface RecyclerViewEvent {
        fun onItemClick(position: Int)
    }

    /**
     * wrapper around a View that contains the layout for an individual item in the list
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) , View.OnClickListener {
        //Initialize the item in the list
        val text: TextView
        init {
            text = view.findViewById(R.id.item_layout)
        }

        /**
         * Overrides the onClick method for the ViewHolder class
         *
         * @since
         *
         * @return the clicked item's position
         */
        override fun onClick(p0: View?) {
            listener.onItemClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.activity_adjustment_view, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder,
                                  position: Int) {
        viewHolder.text.text = dataSet[position]
    }

    override fun getItemCount() = dataSet.size
}