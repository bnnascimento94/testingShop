package com.vullpes.testing.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.vullpes.testing.R
import com.vullpes.testing.data.local.ShoppingItem
import javax.inject.Inject

class ShoppingListAdapter  @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<ShoppingListAdapter.ShoppingItemViewHolder>() {

    class ShoppingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffCallback = object : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var shoppingItems: List<ShoppingItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemViewHolder {
        return ShoppingItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_shopping,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        val shoppingItem = shoppingItems[position]
        holder.itemView.apply {
            val ivShoppingImage = findViewById<ImageView>(R.id.ivShoppingImage)
            glide.load(shoppingItem.imageUrl).into(ivShoppingImage)
            val tvName = findViewById<TextView>(R.id.tvName)
            tvName.text = shoppingItem.name
            val amountText = "${shoppingItem.amount}x"
            val tvShoppingItemAmount = findViewById<TextView>(R.id.tvShoppingItemAmount)
            tvShoppingItemAmount.text = amountText
            val tvShoppingItemPrice = findViewById<TextView>(R.id.tvShoppingItemPrice)
            val priceText = "${shoppingItem.price}€"
            tvShoppingItemPrice.text = priceText
        }
    }
}

