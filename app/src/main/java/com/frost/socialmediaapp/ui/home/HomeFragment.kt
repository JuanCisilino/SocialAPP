package com.frost.socialmediaapp.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.frost.socialmediaapp.HomeViewModel
import com.frost.socialmediaapp.databinding.FragmentHomeBinding
import com.frost.socialmediaapp.ui.post.PostActivity

class HomeFragment : Fragment() {

  private lateinit var homeViewModel: HomeViewModel
  private var _binding: FragmentHomeBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onAttach(context: Context) {
    super.onAttach(context)
    homeViewModel = ViewModelProvider(context as FragmentActivity).get(HomeViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View {
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    setAddButton()
    return binding.root
  }

  private fun setAddButton() {
    binding.floatingActionButton.setOnClickListener { context?.let { PostActivity.start(it) } }
  }

  override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}