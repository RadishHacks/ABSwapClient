package tech.r7chakra.abswapclient.adapters

import androidx.recyclerview.widget.DiffUtil

class DiffCallback<T>(private val oldList : ArrayList<T>,
                      private val newList : ArrayList<T>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]?.equals(newList[newItemPosition]) ?: false
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]?.equals(newList[newItemPosition]) ?: false
    }
}