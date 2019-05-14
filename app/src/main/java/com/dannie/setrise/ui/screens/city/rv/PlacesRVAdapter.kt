package com.dannie.setrise.ui.screens.city.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dannie.setrise.R
import com.dannie.setrise.logic.models.local.PlaceInfo
import com.dannie.setrise.logic.models.network.Candidate
import kotlinx.android.synthetic.main.item_city.view.*

class PlacesRVAdapter(val onClick: (PlaceInfo) -> Unit) : RecyclerView.Adapter<PlacesRVAdapter.ViewHolder>() {

    private val items = ArrayList<Candidate>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun addItems(newItems: ArrayList<Candidate>) {
        items.addAll(newItems)
        notifyItemRangeInserted(0, newItems.size)
    }

    fun clearItems() {
        val size = items.size
        items.clear()
        notifyItemRangeRemoved(0, size)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Candidate) {
            itemView.apply {
                setOnClickListener {
                    val placeInfo = PlaceInfo(item.name, item.geometry?.location?.lat, item.geometry?.location?.lng)
                    onClick.invoke(placeInfo)
                }
                txtName.text = item.name
                txtAddress.text = item.formattedAddress
            }
        }
    }

}