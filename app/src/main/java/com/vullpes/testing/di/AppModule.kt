package com.vullpes.testing.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vullpes.testing.R
import com.vullpes.testing.data.local.ShoppingDao
import com.vullpes.testing.data.local.ShoppingItemDatabase
import com.vullpes.testing.data.remote.PixabayAPI
import com.vullpes.testing.repositories.DefaultShoppingRepository
import com.vullpes.testing.repositories.ShoppingRepository
import com.vullpes.testing.util.BASE_URL
import com.vullpes.testing.util.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ShoppingItemDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideShoppingDao(
        database: ShoppingItemDatabase
    )= database.shoppingDao()

    @Singleton
    @Provides
    fun providesPixabayAPI(): PixabayAPI{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
    )

    @Provides
    @Singleton
    fun provideDefaultShoppingRepository(
        shoppingDao: ShoppingDao, pixabayAPI: PixabayAPI
    )=  DefaultShoppingRepository(shoppingDao, pixabayAPI) as ShoppingRepository

}