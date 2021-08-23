package com.frost.socialapp.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.frost.socialapp.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(ContactsViewModel::class.java) }
    private var _binding: FragmentContactsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        viewModel.text.observe(viewLifecycleOwner, { textView.text = it })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}