package com.ppp.pegasussociety.Model

data class AddChildRequest(
    val ChildrenName: String,
    val ParentId: String,
    val Interest: List<String>  ,
    val FocusArea: List<String>,
    val Gender: String,
    val DOB: String
)

data class AddChildResponse(
    val message: String,
    val childrenId: String
)

data class ChildrenResponse(
    val parentId: String,
    val totalChildren: Int,
    val children: List<Child>
)

data class Child(
    val childrenId: String,
    val childrenName: String,
    val dob: String,
    val gender: String,
    val interests: List<String>,
    val focusAreas: List<String>
)


/*
data class ChildProfileResponse(
    val name: String,
    val DOB: String,
    val gender: String,
    val hobbies: String?,
    val notes: String?,
    val avatarResId: Int? = null
)*/
