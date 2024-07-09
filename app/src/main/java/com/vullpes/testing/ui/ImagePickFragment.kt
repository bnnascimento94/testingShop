package com.vullpes.testing.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.vullpes.testing.R
import com.vullpes.testing.adapters.ImageAdapter
import com.vullpes.testing.util.GRID_SPAN_COUNT
import javax.inject.Inject

class ImagePickFragment @Inject constructor(
    val imageAdapter: ImageAdapter
): Fragment(R.layout.fragment_image_pick) {

    lateinit var viewModel: ShoppingViewModel
    lateinit var  rvImages: RecyclerView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]
        rvImages = view.findViewById(R.id.rvImages)

        imageAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setCurImageurl(it)
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        rvImages.apply {
            layoutManager = GridLayoutManager(requireContext(),GRID_SPAN_COUNT)
            adapter = imageAdapter
        }
    }
}