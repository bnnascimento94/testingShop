package com.vullpes.testing.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vullpes.testing.data.local.ShoppingItem
import com.vullpes.testing.data.remote.responses.ImageResponse
import com.vullpes.testing.util.Resource

class FakeShoppingRepositoryAndroidTest: ShoppingRepository {
    private val shoppingItems = mutableListOf<ShoppingItem>()
    private val observableShoppingItems = MutableLiveData<List<ShoppingItem>>(shoppingItems)
    private val observableTotalPrive = MutableLiveData<Float>()
    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean){
        shouldReturnNetworkError = value
    }

    private fun refreshShoppingItens(){
        observableShoppingItems.postValue(shoppingItems)
        observableTotalPrive.postValue(getTotalPrice())
    }

    private fun getTotalPrice(): Float {
        return shoppingItems.sumOf { it.price.toDouble()}.toFloat()
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem)
        refreshShoppingItens()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem)
        refreshShoppingItens()
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return observableShoppingItems
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return observableTotalPrive
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return if(shouldReturnNetworkError){
            Resource.Error("Error", null)
        }else{
            Resource.Success(ImageResponse(listOf(), 0, 0))
        }
    }

}