package com.example.list_mail

import java.io.Serializable

data class Email(
    val senderName: String,
    val subject: String,
    val preview: String,
    val time: String,
    var starred: Boolean = false
) : Serializable {
    val initial: String get() = senderName.firstOrNull()?.uppercase() ?: "?"
}
