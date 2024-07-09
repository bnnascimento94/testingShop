package com.vullpes.testing.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vullpes.testing.data.local.ShoppingItem
import com.vullpes.testing.data.remote.responses.ImageResponse
import com.vullpes.testing.repositories.ShoppingRepository
import com.vullpes.testing.util.Event
import com.vullpes.testing.util.MAX_NAME_LENGTH
import com.vullpes.testing.util.MAX_PRICE_LENGTH
import com.vullpes.testing.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(private val shoppingRepository: ShoppingRepository): ViewModel() {

    val shoppingItems = shoppingRepository.observeAllShoppingItems()
    val totalPrice = shoppingRepository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> = _images

    private val _currentImageUrl = MutableLiveData<String>()
    val currentImageUrl: LiveData<String> = _currentImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemStatus

    fun setCurImageurl(url:String){
        _currentImageUrl.value = url
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        shoppingRepository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        shoppingRepository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name:String, amountString:String, priceString:String){
        if(name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()){
            _insertShoppingItemStatus.postValue(Event(Resource.Error("The Fields must not be empty", null)))
            return
        }
        if(name.length > MAX_NAME_LENGTH){
            _insertShoppingItemStatus.postValue(Event(Resource.Error("The Name of the Item must not exceed $MAX_NAME_LENGTH characters", null)))
            return
        }
        if(priceString.length > MAX_PRICE_LENGTH){
            _insertShoppingItemStatus.postValue(Event(Resource.Error("The price of the Item must not exceed $MAX_PRICE_LENGTH characters", null)))
            return
        }

        val amount = try{
            amountString.toInt()
        }catch (e: Exception){
            _insertShoppingItemStatus.postValue(Event(Resource.Error("Please enter with a valid amount", null)))
            return
        }

        val shoppingItem = ShoppingItem(name, amount, priceString.toFloat(), _currentImageUrl.value ?: "")
        insertShoppingItemIntoDb(shoppingItem)
        setCurImageurl("")
        _insertShoppingItemStatus.postValue(Event(Resource.Success(shoppingItem)))
    }

    fun searchForImage(imageQuery:String){
        if(imageQuery.isEmpty()){
            return
        }

        _images.value = Event(Resource.Loading(null))
        viewModelScope.launch {
            val response = shoppingRepository.searchForImage(imageQuery)
            _images.value = Event(response)
        }

    }
}