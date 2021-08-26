package com.frost.socialmediaapp.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.frost.socialmediaapp.HomeViewModel
import com.frost.socialmediaapp.LoginActivity
import com.frost.socialmediaapp.R
import com.frost.socialmediaapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

  private lateinit var homeViewModel: HomeViewModel
  private lateinit var binding: FragmentProfileBinding
  private val db = Firebase.firestore

  override fun onAttach(context: Context) {
    super.onAttach(context)
    homeViewModel = ViewModelProvider(context as FragmentActivity).get(HomeViewModel::class.java)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentProfileBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setProfileView()
  }

  override fun onResume() {
    super.onResume()
    setData()
  }

  private fun setProfileView() {
    binding.logOutButton.setOnClickListener { logOutAndFinish() }
    binding.saveButton.setOnClickListener { saveData() }
    setData()
  }

  private fun setData(){
    binding.nameTextView.text = homeViewModel.userData?.name?:""
    binding.emailTextView.text = homeViewModel.userData?.email?:""
    binding.phoneEditText.hint = homeViewModel.userData?.phone?:"+541124562155"
    Picasso.get().load(homeViewModel.userData?.photo).into(binding.photoImageView)
  }

  private fun saveData() {
    homeViewModel.userData?.phone = binding.phoneEditText.text.toString()
    saveDatabase()
    Toast.makeText(context, "Actualizado", Toast.LENGTH_SHORT).show()
    setData()
    this.context?.let { view?.let { view -> hideKeyboard(it, view) } }
  }

  private fun saveDatabase(){
    db.collection("users").document(homeViewModel.userData?.email!!).set(
      hashMapOf( "name" to homeViewModel.userData?.name,
        "email" to homeViewModel.userData?.email,
        "photo_url" to homeViewModel.userData?.photo.toString(),
        "phone" to homeViewModel.userData?.phone))
  }


  @SuppressLint("WrongConstant")
  private fun hideKeyboard(context: Context, view: View) {
    val imm = context.getSystemService("input_method") as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
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

}