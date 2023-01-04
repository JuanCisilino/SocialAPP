package com.frost.socialmediaapp.ui.home

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.frost.socialmediaapp.databinding.FragmentHomeBinding
import com.frost.socialmediaapp.entities.Post
import com.frost.socialmediaapp.ui.post.PostActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {

  private val viewModel by lazy { ViewModelProvider(this).get(HomeFragmentViewModel::class.java) }
  private lateinit var adapter : PostAdapter
  private lateinit var binding: FragmentHomeBinding

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View {
    binding = FragmentHomeBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setAddButton()
    viewModel.getData()
    setUpSwipeRefreshLayout()
    refreshEvery(60)
    subscribeToLiveData()
  }

  private fun subscribeToLiveData() {
    viewModel.dataLiveData.observe(viewLifecycleOwner) { showData(it) }
    viewModel.errorLiveData.observe(viewLifecycleOwner) { showError() }
  }

  private fun showError() {
    Toast.makeText(requireContext(),"Error loading data", Toast.LENGTH_SHORT).show()
  }

  private fun showData(postList: ArrayList<Post>) {
    adapter = context?.let { PostAdapter(postList.reversed(), it) }!!
    binding.postListrecyclerView.layoutManager = LinearLayoutManager(context)
    binding.postListrecyclerView.adapter = adapter
  }

  private fun refreshEvery(seconds: Long){
    GlobalScope.launch {
      while (true) {
        delay(TimeUnit.SECONDS.toMillis(seconds))
        viewModel.getData()
      }
    }
  }

  private fun setUpSwipeRefreshLayout() {
    binding.thingsSwipeRefreshLayout.setOnRefreshListener {
      viewModel.getData()
      binding.thingsSwipeRefreshLayout.isRefreshing = false
    }
  }

  private fun setAddButton() {
    binding.floatingActionButton.setOnClickListener { context?.let { PostActivity.startActivityForResult(it as Activity, 10) } }
  }

}