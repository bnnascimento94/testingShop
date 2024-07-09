package com.vullpes.testing.ui

import android.icu.lang.UCharacter.IndicPositionalCategory.LEFT
import android.icu.lang.UCharacter.IndicPositionalCategory.RIGHT
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.vullpes.testing.R
import com.vullpes.testing.adapters.ShoppingListAdapter
import javax.inject.Inject

class ShoppingFragment @Inject constructor(
    val shoppingItemAdapter: ShoppingListAdapter,
    var viewModel: ShoppingViewModel ? = null
) : Fragment(R.layout.fragment_shopping) {

    lateinit var rvShoppingItems : RecyclerView
    lateinit var tvShoppingItemPrice: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = viewModel ?: ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]
        rvShoppingItems = view.findViewById(R.id.rvShoppingItems)
        tvShoppingItemPrice = view.findViewById(R.id.tvShoppingItemPrice)
        subscribeToObservers()
        setupRecyclerView()

        val fabAddShoppingItem: FloatingActionButton = view.findViewById(R.id.fabAddShoppingItem)

        fabAddShoppingItem.setOnClickListener {
            findNavController().navigate(R.id.action_shoppingFragment_to_addShoppinItemFragment)
        }

    }

    private val itemTouchCallback = object: ItemTouchHelper.SimpleCallback(
        0,LEFT or RIGHT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.layoutPosition
            val item = shoppingItemAdapter.shoppingItems[pos]
            viewModel?.deleteShoppingItem(item)
            Snackbar.make(requireView(),"Successfully deleted item", Snackbar.LENGTH_LONG).apply {
                setAction("Undo"){
                    viewModel?.insertShoppingItemIntoDb(item)
                }
                show()
            }
        }
    }


    private fun subscribeToObservers(){
        viewModel?.shoppingItems?.observe(viewLifecycleOwner){
            shoppingItemAdapter.shoppingItems = it
        }

        viewModel?.totalPrice?.observe(viewLifecycleOwner) {
            val price = it ?: 0f
            val priceText = "Total Price $price€"
            tvShoppingItemPrice.text = priceText
        }

    }

    private fun setupRecyclerView(){
        rvShoppingItems.apply {
            adapter = shoppingItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        }
    }
}