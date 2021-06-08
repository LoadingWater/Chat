package com.plakhotski.chat.Fragments.Controllers

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.plakhotski.chat.ViewModels.SharedViewModel

open class BaseController(fragment: Fragment)
{
	protected val TAG = fragment::class.java.simpleName
	protected val sharedViewModel = ViewModelProvider(fragment).get(SharedViewModel::class.java)
	protected val dbRef = sharedViewModel.getDatabaseInstance().reference
	protected val authIns = sharedViewModel.getAuthInstance()
	protected val activity = fragment.requireActivity()
	protected val view = fragment.requireView()
}