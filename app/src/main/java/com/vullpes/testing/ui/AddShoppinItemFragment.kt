package com.vullpes.testing.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.vullpes.testing.R
import com.vullpes.testing.util.Resource
import javax.inject.Inject

class AddShoppinItemFragment @Inject constructor(
    private val glide: RequestManager
) : Fragment(R.layout.fragment_add_shopping_item) {

    lateinit var viewModel: ShoppingViewModel
    private lateinit var ivShoppingImage: ImageView
    private lateinit var btnAddShoppingItem: MaterialButton
    private lateinit var etShoppingItemName: TextInputEditText
    private lateinit var etShoppingItemAmount: TextInputEditText
    private lateinit var etShoppingItemPrice: TextInputEditText
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]
        subscribeToObservers()
        ivShoppingImage = view.findViewById(R.id.ivShoppingImage)
        btnAddShoppingItem = view.findViewById(R.id.btnAddShoppingItem)
        etShoppingItemName = view.findViewById(R.id.etShoppingItemName)
        etShoppingItemAmount = view.findViewById(R.id.etShoppingItemAmount)
        etShoppingItemPrice = view.findViewById(R.id.etShoppingItemPrice)


        btnAddShoppingItem.setOnClickListener {
            viewModel.insertShoppingItem(
                etShoppingItemName.text.toString(),
                etShoppingItemAmount.text.toString(),
                etShoppingItemPrice.text.toString()
            )
        }

        ivShoppingImage.setOnClickListener {
            findNavController().navigate(R.id.action_addShoppinItemFragment_to_imagePickFragment)
        }

        val callback = object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                viewModel.setCurImageurl("")
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

    }

    private fun subscribeToObservers(){
        viewModel.currentImageUrl.observe(viewLifecycleOwner){
            glide.load(it).into(ivShoppingImage)
        }

        viewModel.insertShoppingItemStatus.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let { result ->
                when(result){
                    is Resource.Error -> {
                        Snackbar.make(
                            requireView(),
                             result.message?:"An unknown error occured",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        Snackbar.make(
                            requireView(),
                            "Added Shopping Item",
                            Snackbar.LENGTH_LONG
                        ).show()
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }


}