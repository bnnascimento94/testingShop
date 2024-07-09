package com.vullpes.testing.repositories

import androidx.lifecycle.LiveData
import com.vullpes.testing.data.local.ShoppingItem
import com.vullpes.testing.data.remote.responses.ImageResponse
import com.vullpes.testing.util.Resource
import retrofit2.Response

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)
    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>
    fun observeTotalPrice(): LiveData<Float>
    suspend fun searchForImage(imageQuery:String): Resource<ImageResponse>

}