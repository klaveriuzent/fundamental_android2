package com.example.fundamentalandroid2

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelData (
        var avatar: String? = null,
        var username: String? = null,
        var name : String? = null,
        var followers: String? = null,
        var following: String? = null,
        var company: String? = null,
        var location: String? = null,
        var repository: String? = null,
        var userid: String? = null,
        var type: String? = null
) : Parcelable