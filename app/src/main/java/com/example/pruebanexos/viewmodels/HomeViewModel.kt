package com.example.pruebanexos.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.pruebanexos.R

class HomeViewModel: ViewModel() {
    fun navigateToFragment1(fragment: Fragment) {
        val navController = fragment.findNavController()
        navController.navigate(R.id.authorizationFragment)
    }

    fun navigateToFragment2(fragment: Fragment) {
        val navController = fragment.findNavController()
        navController.navigate(R.id.searchFragment)
    }

    fun navigateToFragment3(fragment: Fragment) {
        val navController = fragment.findNavController()
        navController.navigate(R.id.listFragment)
    }

}