package com.manav.demoapplication

class Posts : ArrayList<PostsItem>()

data class PostsItem(
    val body: String,
    val id: Int,
    val title: String,
    val userId: Int
)