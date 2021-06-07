package com.plakhotski.chat.Models

data class Chat(
		var chatUid: String? = "",
		var messages: MutableList<Message>? = mutableListOf()
)