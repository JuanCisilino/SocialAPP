package com.frost.socialmediaapp.ui.contacts

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.frost.socialmediaapp.HomeViewModel
import com.frost.socialmediaapp.databinding.FragmentContactsBinding
import com.frost.socialmediaapp.entities.Post
import com.frost.socialmediaapp.entities.UserData
import com.frost.socialmediaapp.ui.home.PostAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ContactsFragment : Fragment() {

  private lateinit var binding: FragmentContactsBinding
  private lateinit var adapter : UsersAdapter
  private val db = Firebase.firestore
  private var userDataList = ArrayList<UserData>()
  private var database = FirebaseDatabase.getInstance()
  private var reference = database.getReference("users")

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View {
    binding = FragmentContactsBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    adapter = context?.let { UsersAdapter(userDataList, it) }!!
  }

  override fun onResume() {
    super.onResume()
    getData()
  }

  private fun getData() {
    reference.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        for (data in snapshot.children.reversed()){
          val user = data.getValue(UserData::class.java)
          userDataList.add(user as UserData)
        }
        binding.userListRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.userListRecyclerView.adapter = adapter
      }
      override fun onCancelled(error: DatabaseError) {}

    })
  }

}