package com.vullpes.testing.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.vullpes.testing.data.local.ShoppingDao
import com.vullpes.testing.data.local.ShoppingItem
import com.vullpes.testing.data.local.ShoppingItemDatabase
import com.vullpes.testing.getOrAwaitValue
import com.vullpes.testing.launchFragmentInHiltContainer
import com.vullpes.testing.ui.ShoppingFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ShoppingDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.shoppingDao()
    }

    @After
    fun teardown() {
        database.close()
    }




    @Test
    fun insertShoppingItem() = runTest {
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).contains(shoppingItem)
    }

    @Test
    fun deleteShoppingItem() = runTest {
        val shoppingItem = ShoppingItem("name",1,1f,"url",id =1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).doesNotContain(shoppingItem)
    }

    @Test
    fun observeTotalPriceSum() = runTest {
        val shoppingItem = ShoppingItem("name",1,1f,"url",id =1)
        dao.insertShoppingItem(shoppingItem)
        val shoppingItem1 = ShoppingItem("name",1,1f,"url",id =2)
        dao.insertShoppingItem(shoppingItem1)
        val shoppingItem2 = ShoppingItem("name",1,1f,"url",id =3)
        dao.insertShoppingItem(shoppingItem2)
        val shoppingItem3 = ShoppingItem("name",1,1f,"url",id =4)
        dao.insertShoppingItem(shoppingItem3)
        val shoppingItem4 = ShoppingItem("name",1,1f,"url",id =5)
        dao.insertShoppingItem(shoppingItem4)

        val result = dao.observeTotalPrice().getOrAwaitValue()

        assertThat(result).isEqualTo(5)

    }







}