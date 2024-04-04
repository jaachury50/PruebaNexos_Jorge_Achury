package com.example.pruebanexos.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pruebanexos.R
import com.example.pruebanexos.adapter.AuthorizationsAdapter
import com.example.pruebanexos.databinding.FragmentListBinding
import com.example.pruebanexos.viewmodels.ListViewModel

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: ListViewModel
    private lateinit var adapterAuthorizations: AuthorizationsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.getAllAuthorizations()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        _binding = FragmentListBinding.inflate(inflater,container,false)

        setupRecycleView()

        viewModel.listAuthorizations.observe(viewLifecycleOwner){
            adapterAuthorizations.listAuthorizations = it
            adapterAuthorizations.notifyDataSetChanged()
        }

        viewModel.showError.observe(viewLifecycleOwner){
            if(it){
                binding.tvInfo.visibility = View.GONE
                binding.tvErrorText.setText(viewModel.textError.value.toString())
                binding.linearError.visibility = View.VISIBLE
            }else{
                binding.tvInfo.visibility = View.VISIBLE
                binding.linearError.visibility = View.GONE
            }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_listFragment_to_homeFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return binding.root
    }

    private fun setupRecycleView() {
        binding.recyclerList.layoutManager = LinearLayoutManager(activity)
        adapterAuthorizations = AuthorizationsAdapter({ position ->
            val selectedItemId = adapterAuthorizations.listAuthorizations[position]

            val bundle = Bundle().apply {
                putSerializable("selectedItemId", selectedItemId)
                putSerializable("fragment", 2)
            }

            val deleteFragment = DeleteFragment()
            deleteFragment.arguments = bundle

            val navController = requireActivity().findNavController(R.id.nav_host_fragment)
            navController.navigate(R.id.action_listFragment_to_deleteFragment, bundle)

            Log.d("SelectedItemId", selectedItemId.toString())
        }, arrayListOf())
        binding.recyclerList.adapter = adapterAuthorizations

    }
}