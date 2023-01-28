package com.manav.demoapplication.response

import android.os.Parcelable

data class ContactInfo(
    var _id: String? = null,
    var isLogin: Boolean? = null,
    var userId: String? = null,
    @Transient var name: String? = null,
    var firstName: String? = null,
    var image: String? = null,
    var lastName: String? = null,
    @Transient var contactId: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var familyRelationWithPic: String? = null,

    @Transient var isSelected: Boolean = false,
)