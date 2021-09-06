package com.example.foodbook.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Food (
    var _id:String="",
    var Foodtitle : String? = null,
    var foodimg : String? = null,
    var Fooddesc : String? = null,
    var foodcategory : String? = null,
    var foodtype:String?=null,
    var preptime:String?=null,
):Parcelable{
    @PrimaryKey(autoGenerate = true)
    var FoodId : Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()) {
        FoodId = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(Foodtitle)
        parcel.writeString(foodimg)
        parcel.writeString(Fooddesc)
        parcel.writeString(foodcategory)
        parcel.writeString(foodtype)
        parcel.writeString(preptime)
        parcel.writeInt(FoodId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Food> {
        override fun createFromParcel(parcel: Parcel): Food {
            return Food(parcel)
        }

        override fun newArray(size: Int): Array<Food?> {
            return arrayOfNulls(size)
        }
    }
}