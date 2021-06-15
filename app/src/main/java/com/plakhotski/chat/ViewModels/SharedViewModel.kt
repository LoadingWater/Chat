package com.plakhotski.chat.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SharedViewModel: ViewModel()
{
	val firebaseAuthInstance = MutableLiveData<FirebaseAuth>()
	fun getAuthInstance(): FirebaseAuth
	{
		if (firebaseAuthInstance.value == null)
		{
			firebaseAuthInstance.value = FirebaseAuth.getInstance()
		}
		return firebaseAuthInstance.value!!
	}

	val firebaseDatabaseInstance = MutableLiveData<FirebaseDatabase>()
	fun getDatabaseInstance(): FirebaseDatabase
	{
		if (firebaseDatabaseInstance.value == null)
		{
			firebaseDatabaseInstance.value = FirebaseDatabase.getInstance()
		}
		return firebaseDatabaseInstance.value!!
	}

	val currentChatUid = MutableLiveData<String>()
	fun getCurrentChatUid(): String
	{
		return currentChatUid.value?: ""
	}
	fun setCurrentChatUid(value: String)
	{
		currentChatUid.value = value
	}
}