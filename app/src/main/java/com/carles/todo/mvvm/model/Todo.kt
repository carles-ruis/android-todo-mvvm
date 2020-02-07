package com.carles.todo.mvvm.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var name: String,
    var date: Long,
    @Embedded
    var location: TodoLocation
) : Parcelable

@Parcelize
data class TodoLocation(val latitude: Double, val longitude: Double) : Parcelable
