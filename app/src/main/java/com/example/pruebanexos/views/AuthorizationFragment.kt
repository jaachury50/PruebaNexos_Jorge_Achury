package com.example.pruebanexos.views

import DecimalDigitsTextWatcher
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.pruebanexos.databinding.FragmentAuthorizationBinding
import com.example.pruebanexos.models.AuthorizationModel
import com.example.pruebanexos.viewmodels.AuthorizationViewModel
import java.util.UUID
import kotlin.math.E


class AuthorizationFragment : Fragment() {


    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthorizationViewModel

    private var isEditing = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(AuthorizationViewModel::class.java)
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)

        binding.Amount.addTextChangedListener(DecimalDigitsTextWatcher(binding.Amount))

//        binding.Amount.addTextChangedListener(object: TextWatcher{
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
////                TODO("Not yet implemented")
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
////                TODO("Not yet implemented")
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                val text = s.toString()
//
//                if (!isEditing && text.isNotEmpty()) {
//                    val parts = text.split(".")
//                    if (parts.size == 1) { // Solo hay parte entera
//                        val integerPart = parts[0]
//                        val formattedText = if (integerPart.length > 2) {
//                            val formattedInteger = integerPart.substring(0, integerPart.length - 2)
//                            val decimalPart = integerPart.substring(integerPart.length - 2)
//                            "$formattedInteger.$decimalPart"
//                        } else {
//                            "0.${integerPart.padStart(2, '0')}" // Agregar ceros si no hay suficientes dígitos
//                        }
//
//                        if (formattedText != text) { // Verificar si el texto formateado es diferente al actual
//                            isEditing = true // Establecer la bandera para evitar bucles infinitos
//                            binding.Amount.setText(formattedText)
//                            binding.Amount.setSelection(formattedText.length)
//                            isEditing = false
//                        }
//                    } else if (parts.size == 2) { // Hay parte entera y decimal separadas por un punto
//                        val integerPart = parts[0]
//                        val decimalPart = parts[1].take(2) // Tomar solo los primeros 2 caracteres
//
//                        val formattedText = "$integerPart.$decimalPart"
//                        if (formattedText != text) { // Verificar si el texto formateado es diferente al actual
//                            isEditing = true // Establecer la bandera para evitar bucles infinitos
//                            binding.Amount.setText(formattedText)
//                            binding.Amount.setSelection(formattedText.length)
//                            isEditing = false
//                        }
//                    }
//                }
//            }
//
//
//        })


        binding.sendButton.setOnClickListener {
            val myUuid = UUID.randomUUID()
            val myUuidAsString = myUuid.toString()
            viewModel.getAuthorizations(
                AuthorizationModel(
                    id = myUuidAsString,
                    commerceCode = binding.CommerceCode.text.toString(),
                    terminalCode = binding.TerminalCode.text.toString(),
                    amount = binding.Amount.text.toString(),
                    card = binding.Card.text.toString()
                )
            )


        }

        viewModel.showError.observe(viewLifecycleOwner){
            showMessage(it, viewModel.textError.value.toString())
        }

        viewModel.showAlert.observe(viewLifecycleOwner){
            showAlert(it, viewModel.typeAlert.value!!.toInt(), viewModel.textAlert.value.toString())
        }

        binding.btnCloseAlert0.setOnClickListener{
            closeAlert()
        }

        binding.btnCloseAlert1.setOnClickListener{
            closeAlert()
        }

        return binding.root
    }

    private fun showMessage(show: Boolean,message : String){
        if(show){
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAlert(show: Boolean,type: Int,message : String){
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