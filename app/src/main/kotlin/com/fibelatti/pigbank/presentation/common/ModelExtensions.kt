package com.fibelatti.pigbank.presentation.common

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

inline fun <reified T : Parcelable> createParcel(
    crossinline createFromParcel: (Parcel) -> T?): Parcelable.Creator<T> =
    object : Parcelable.Creator<T> {
        override fun createFromParcel(source: Parcel): T? = createFromParcel(source)
        override fun newArray(size: Int): Array<out T?> = arrayOfNulls(size)
    }

fun Parcel.readBoolean() = this.readInt() == 1

fun Parcel.writeDate(date: Date) = this.writeLong(date.time)

fun Parcel.readDate() = Date(this.readLong())
