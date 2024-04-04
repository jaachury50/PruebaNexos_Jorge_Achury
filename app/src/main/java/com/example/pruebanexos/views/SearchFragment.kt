package com.example.pruebanexos.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.pruebanexos.databinding.FragmentSearchBinding
import com.example.pruebanexos.viewmodels.SearchViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapterAuthorizations: AuthorizationsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.getAllTransactions()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        setupRecycleView()

        viewModel.listAuthorizations.observe(viewLifecycleOwner){
            adapterAuthorizations.listAuthorizations = it
            adapterAuthorizations.notifyDataSetChanged()
        }


        binding.edtFilter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                viewModel.setSearchText(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        viewModel.searchText.observe(viewLifecycleOwner) { searchText ->
            val filteredList = viewModel.listAuthorizations.value
                ?.filter { it.receiptId.contains(searchText, ignoreCase = true) }
                ?: emptyList()
            viewModel.setFilteredList(filteredList)
        }

        viewModel.filteredList.observe(viewLifecycleOwner) { filteredList ->
            adapterAuthorizations.listAuthorizations = filteredList
            adapterAuthorizations.notifyDataSetChanged()

            if(filteredList.isEmpty()){
                binding.tvInfo.visibility = View.GONE
                binding.linearError.visibility = View.VISIBLE
            }else{
                binding.tvInfo.visibility = View.VISIBLE
                binding.linearError.visibility = View.GONE
            }
        }

        viewModel.showError.observe(viewLifecycleOwner){
            if(it){
                binding.linearError.visibility = View.VISIBLE
            }else{
                binding.linearError.visibility = View.GONE
            }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        return binding.root
    }


    private fun setupRecycleView() {
        binding.recyclerTables.layoutManager = LinearLayoutManager(activity)
        adapterAuthorizations = AuthorizationsAdapter({ position ->
            val selectedItemId = adapterAuthorizations.listAuthorizations[position]

            val bundle = Bundle().apply {
                putSerializable("selectedItemId", selectedItemId)
                putSerializable("fragment", 1)
            }

            val deleteFragment = DeleteFragment()
            deleteFragment.arguments = bundle

            val navController = requireActivity().findNavController(R.id.nav_host_fragment)
            navController.navigate(R.id.action_searchFragment_to_deleteFragment, bundle)
        }, arrayListOf())
        binding.recyclerTables.adapter = adapterAuthorizations

    }

}