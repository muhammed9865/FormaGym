package com.example.formagym.pojo.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.Nullable

@Entity(tableName = "members_table")
data class User(
    var name: String,
    var subscribeStartDate: Long,
    var subscribeEndDate: Long,
    @Nullable
    @ColumnInfo(defaultValue = "0")
    var memberPhoto: Bitmap? = null
):Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readLong(),
        parcel.readParcelable(Bitmap::class.java.classLoader)
    ) {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeLong(subscribeStartDate)
        parcel.writeLong(subscribeEndDate)
        parcel.writeParcelable(memberPhoto, flags)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
