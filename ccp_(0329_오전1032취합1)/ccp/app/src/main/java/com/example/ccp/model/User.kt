package com.example.ccp.model

import android.os.Parcel
import android.os.Parcelable

data class User(
    var id: Long?,
    var username: String,
    var name: String?,
    var password: String?,
    var email: String?,
    var role: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(username)
        parcel.writeString(name)
        parcel.writeString(password)
        parcel.writeString(email)
        parcel.writeString(role)
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

data class UserResponse(
    var message: String
) {
    fun isSuccess(): Boolean {
        return true
    }
}

data class LoginRequest(
    var username: String,
    var password: String
)

data class LoginResponse(
    var message: String,
    var user: User?
)
