package com.vullpes.testing.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.vullpes.testing.MainCoroutineRule
import com.vullpes.testing.getOrAwaitValue
import com.vullpes.testing.repositories.FakeShoppingRepository
import com.vullpes.testing.util.MAX_NAME_LENGTH
import com.vullpes.testing.util.MAX_PRICE_LENGTH
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShoppingViewModelTest{


    //rule to execute livedata
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    //rule to run tests on main thread
    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()


    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setup(){
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }


    @Test
    fun `insert shopping item with empty field, returns error`(){
        viewModel.insertShoppingItem("name","","3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()).isInstanceOf(com.vullpes.testing.util.Resource.Error::class.java)
    }

    @Test
    fun `insert shopping item with too long name, returns error`(){
        val string = buildString {
            for(i in 1..MAX_NAME_LENGTH + 1){
                append(i)
            }
        }
        viewModel.insertShoppingItem(string,"5","3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()).isInstanceOf(com.vullpes.testing.util.Resource.Error::class.java)
    }


    @Test
    fun `insert shopping item with too long price, returns error`(){
        val string = buildString {
            for(i in 1..MAX_PRICE_LENGTH + 1){
                append(i)
            }
        }
        viewModel.insertShoppingItem("name","5",string)

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()).isInstanceOf(com.vullpes.testing.util.Resource.Error::class.java)
    }


    @Test
    fun `insert shopping item with too hight amount, returns error`(){

        viewModel.insertShoppingItem("name","55555555555555555555555555555555555555","3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()).isInstanceOf(com.vullpes.testing.util.Resource.Error::class.java)
    }

    @Test
    fun `insert shopping item with valid input, returns success`(){

        viewModel.insertShoppingItem("name","5","3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()).isInstanceOf(com.vullpes.testing.util.Resource.Success::class.java)
    }


    @Test
    fun `when insert a url set in currentImageUrl value`(){
        viewModel.setCurImageurl("")

        val result = viewModel.currentImageUrl.getOrAwaitValue()

        assertThat(result).isEmpty()
    }



}