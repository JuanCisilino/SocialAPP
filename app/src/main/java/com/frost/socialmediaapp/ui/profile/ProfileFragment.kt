package com.frost.socialmediaapp.ui.profile

import android.content.Context
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.frost.socialmediaapp.HomeActivity
import com.frost.socialmediaapp.HomeViewModel
import com.frost.socialmediaapp.LoginActivity
import com.frost.socialmediaapp.R
import com.frost.socialmediaapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

  private lateinit var homeViewModel: HomeViewModel
  private var homeActivity = HomeActivity()
  private var _binding: FragmentProfileBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onAttach(context: Context) {
    super.onAttach(context)
    homeViewModel = ViewModelProvider(context as FragmentActivity).get(HomeViewModel::class.java)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentProfileBinding.inflate(inflater, container, false)
    setProfileView()
    return binding.root
  }

  private fun setProfileView() {
    binding.nameTextView.text = homeViewModel.userData?.name?:""
    binding.emailTextView.text = homeViewModel.userData?.email?:""
    binding.phoneTextView.text = homeViewModel.userData?.phone?:""
    Picasso.get().load(homeViewModel.userData?.photo).into(binding.photoImageView)
    binding.logOutButton.setOnClickListener { logOutAndFinish() }
  }

  private fun logOutAndFinish(){
    clearSharedPrefs()
    FirebaseAuth.getInstance().signOut()
    this.context?.let { LoginActivity.start(it) }
  }

  private fun clearSharedPrefs(){
    val prefs = activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
    prefs?.edit()?.clear()?.apply()
  }

  override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}