package com.example.pruebanexos.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.pruebanexos.R
import com.example.pruebanexos.databinding.FragmentDeleteBinding
import com.example.pruebanexos.models.CancelAuthorizationModel
import com.example.pruebanexos.models.ListAdapterAuthorizationModel
import com.example.pruebanexos.viewmodels.DeleteViewModel


class DeleteFragment : Fragment() {


    private var _binding: FragmentDeleteBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DeleteViewModel
    private lateinit var selectedItem: ListAdapterAuthorizationModel
    private var acceptCancel: Boolean = true
    private var fragmentSend: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(DeleteViewModel::class.java)
        _binding = FragmentDeleteBinding.inflate(inflater, container, false)

        arguments?.getSerializable("fragment")?.let {
            if(it == 1){
                fragmentSend = 1
                binding.btnCancelAuthorization.visibility = View.VISIBLE
            }else{
                fragmentSend = 2
                binding.btnCancelAuthorization.visibility = View.GONE
            }
        }

        arguments?.getSerializable("selectedItemId")?.let {
            selectedItem = it as ListAdapterAuthorizationModel

            binding.tvNameTable.setText("DETALLE TRANSACCIÓN - APROBADA")
            binding.tvReceiptId.setText(selectedItem.receiptId)
            binding.tvRrn.setText(selectedItem.rrn)
            binding.tvCommerceCode.setText(selectedItem.commerceCode)
            binding.tvTerminalCode.setText(selectedItem.terminalCode)
            binding.tvCount.setText(selectedItem.amount)
            binding.tvCard.setText(selectedItem.card)

            if(!selectedItem.approvedStatus){
                acceptCancel = false
                binding.btnCancelAuthorization.isEnabled = false
                binding.tvNameTable.setText("DETALLE TRANSACCIÓN - ANULADA")
                showToast("Esta transacción ya se encuentra anulada")
            }
        }

        binding.btnCancelAuthorization.setOnClickListener{
            if(acceptCancel){
                viewModel.cancelAuthorization(CancelAuthorizationModel(
                    commerceCode = selectedItem.commerceCode,
                    terminalCode = selectedItem.terminalCode,
                    receiptId = selectedItem.receiptId,
                    rrn = selectedItem.rrn
                ))
            }
        }

        viewModel.showAlert.observe(viewLifecycleOwner){
            showMessage(it, viewModel.typeAlert.value!!.toInt(), viewModel.textAlert.value.toString())
        }

        binding.btnCloseAlert0.setOnClickListener{
            closeAlert()
        }

        binding.btnCloseAlert1.setOnClickListener{
            closeAlert()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(fragmentSend == 1){
                    findNavController().navigate(R.id.action_deleteFragment_to_searchFragment)
                }else{
                    findNavController().navigate(R.id.action_deleteFragment_to_listFragment)
                }

            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return binding.root
    }

    private fun showToast(message: String){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun showMessage(show: Boolean,type: Int,message : String){
        val fadeIn = AlphaAnimation(0f, 1f)

        if(show){
            if(type == 0){
                fadeIn.duration = 1000
                binding.tvErrorText.setText(message)
                binding.linearAlertError.visibility = View.VISIBLE
                binding.linearAlertError.startAnimation(fadeIn)
            }else if(type == 1){
                fadeIn.duration = 1000
                binding.tvSuccessText.setText(message)
                binding.linearAlertSuccess.visibility = View.VISIBLE
                binding.linearAlertSuccess.startAnimation(fadeIn)
            }

        }
    }

    private fun closeAlert(){
        val navController = requireActivity().findNavController(R.id.nav_host_fragment)
        navController.navigate(R.id.searchFragment)

        val fadeOut = AlphaAnimation(1f, 0f)

        fadeOut.duration = 1000
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                binding.linearAlertError.visibility = View.GONE
                binding.linearAlertSuccess.visibility = View.GONE
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })
        binding.linearAlertError.startAnimation(fadeOut)
        binding.linearAlertSuccess.startAnimation(fadeOut)


    }
}