package com.vullpes.testing.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.vullpes.testing.adapters.ImageAdapter
import com.vullpes.testing.adapters.ShoppingListAdapter
import javax.inject.Inject

class ShoppingFragmentFactory @Inject constructor(
    private val imageAdapter: ImageAdapter,
    private val glide: RequestManager,
    private val shoppingItemAdapter: ShoppingListAdapter
): FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            ImagePickFragment::class.java.name -> ImagePickFragment(imageAdapter)
            AddShoppinItemFragment::class.java.name -> AddShoppinItemFragment(glide)
            ShoppingFragment::class.java.name -> ShoppingFragment(
                shoppingItemAdapter
            )
            else -> super.instantiate(classLoader, className)
        }
    }

}