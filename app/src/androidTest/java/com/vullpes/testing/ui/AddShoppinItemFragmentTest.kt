package com.vullpes.testing.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth
import com.vullpes.testing.R
import com.vullpes.testing.data.local.ShoppingItem
import com.vullpes.testing.getOrAwaitValue
import com.vullpes.testing.launchFragmentInHiltContainer
import com.vullpes.testing.repositories.FakeShoppingRepositoryAndroidTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class AddShoppinItemFragmentTest(){

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory : ShoppingFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun pressBackButton_popBackStack(){
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<AddShoppinItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
        pressBack()
        verify(navController).popBackStack()
    }

    @Test
    fun pressBackButton_setCurImageurl_called(){
        val navController = mock(NavController::class.java)
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
        launchFragmentInHiltContainer<AddShoppinItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
        }
        pressBack()
        Truth.assertThat(testViewModel.currentImageUrl.getOrAwaitValue()).isEqualTo("")
    }


    @Test
    fun ivShoppingImage_goes_to_image_pickImage(){
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<AddShoppinItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.ivShoppingImage)).perform(ViewActions.click())

        verify(navController).navigate(
            R.id.action_addShoppinItemFragment_to_imagePickFragment
        )
    }


    @Test
    fun btnAddShoppingItem_saveItemShooped(){
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
        launchFragmentInHiltContainer<AddShoppinItemFragment>(fragmentFactory = fragmentFactory) {
            viewModel = testViewModel

        }


        onView(withId(R.id.etShoppingItemName)).perform(replaceText("shopping"))
        onView(withId(R.id.etShoppingItemAmount)).perform(replaceText("2"))
        onView(withId(R.id.etShoppingItemPrice)).perform(replaceText("2.0"))
        onView(withId(R.id.btnAddShoppingItem)).perform(ViewActions.click())

        Truth.assertThat(testViewModel.shoppingItems.getOrAwaitValue())
            .contains(
                ShoppingItem("shopping",2,2.0f,"")
            )
    }



}