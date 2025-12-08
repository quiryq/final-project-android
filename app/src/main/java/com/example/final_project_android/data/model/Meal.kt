package com.example.final_project_android.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "meals_table")
data class Meal(
    @PrimaryKey
    @SerializedName("idMeal")
    val id: String,

    @SerializedName("strMeal")
    val name: String,

    @SerializedName("strCategory")
    val category: String?,

    @SerializedName("strArea")
    val area: String?,

    @SerializedName("strInstructions")
    val instructions: String?,

    @SerializedName("strMealThumb")
    val thumbnail: String?,

    @SerializedName("strYoutube")
    val youtubeLink: String?
) : Parcelable