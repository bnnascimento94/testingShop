package com.vullpes.testing.repositories

import androidx.lifecycle.LiveData
import com.vullpes.testing.data.local.ShoppingDao
import com.vullpes.testing.data.local.ShoppingItem
import com.vullpes.testing.data.remote.PixabayAPI
import com.vullpes.testing.data.remote.responses.ImageResponse
import com.vullpes.testing.util.Resource
import retrofit2.Response
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
): ShoppingRepository {
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try{
            val response = pixabayAPI.searchForImage(imageQuery)
            if(response.isSuccessful){
                response.body()?.let {
                    return@let Resource.Success(it)
                }?: Resource.Error("An unknown error occured")
            }else{
                Resource.Error("An unknown error occured")
            }
        }catch (e: Exception){
            Resource.Error("Couldn't reach the server. Check your internet connection.")
        }
    }
}