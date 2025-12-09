package com.example.final_project_android.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.final_project_android.R
import com.example.final_project_android.data.model.Meal

class MealAdapter : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {


    inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgMeal: ImageView = itemView.findViewById(R.id.img_meal)
        val tvMealName: TextView = itemView.findViewById(R.id.tv_meal_name)
    }


    private val differCallback = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
        return MealViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = differ.currentList[position]

        holder.tvMealName.text = meal.name


        holder.imgMeal.load(meal.thumbnail) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
        }


        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(meal) }
        }
    }


    private var onItemClickListener: ((Meal) -> Unit)? = null

    fun setOnItemClickListener(listener: (Meal) -> Unit) {
        onItemClickListener = listener
    }
}