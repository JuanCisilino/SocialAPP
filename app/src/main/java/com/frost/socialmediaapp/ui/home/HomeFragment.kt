package com.frost.socialmediaapp.ui.home

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.frost.socialmediaapp.HomeViewModel
import com.frost.socialmediaapp.databinding.FragmentHomeBinding
import com.frost.socialmediaapp.entities.Post
import com.frost.socialmediaapp.ui.post.PostActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {

  private lateinit var homeViewModel: HomeViewModel
  private var postList = ArrayList<Post>()
  private var adapter = PostAdapter(postList)
  private lateinit var binding: FragmentHomeBinding
  private var database = FirebaseDatabase.getInstance()
  private var reference = database.getReference("posts")

  override fun onAttach(context: Context) {
    super.onAttach(context)
    homeViewModel = ViewModelProvider(context as FragmentActivity).get(HomeViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View {
    binding = FragmentHomeBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setAddButton()
    getData()
    setUpSwipeRefreshLayout()
    refreshEvery(60)
  }

  private fun getData() {
    reference.addValueEventListener(object : ValueEventListener{
      override fun onDataChange(snapshot: DataSnapshot) {
        for (data in snapshot.children.reversed()){
          val post = data.getValue(Post::class.java)
          postList.add(post as Post)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
      }
      override fun onCancelled(error: DatabaseError) {}

    })
  }

  private fun refreshEvery(seconds: Long){
    GlobalScope.launch {
      while (true) {
        delay(TimeUnit.SECONDS.toMillis(seconds))
        getData()
      }
    }
  }

  private fun setUpSwipeRefreshLayout() {
    binding.thingsSwipeRefreshLayout.setOnRefreshListener {
      getData()
      binding.thingsSwipeRefreshLayout.isRefreshing = false
    }
  }

  private fun setAddButton() {
    binding.floatingActionButton.setOnClickListener { context?.let { PostActivity.startActivityForResult(it as Activity, 10) } }
  }

}