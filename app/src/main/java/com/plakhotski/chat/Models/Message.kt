package com.plakhotski.chat.Models

data class Message(
		var chatUid: String? = "",
		var messageUid: String? = "",
		var senderUid: String? = "",
		var messageText: String? = "",
		var timeStamp: String? = ""
)