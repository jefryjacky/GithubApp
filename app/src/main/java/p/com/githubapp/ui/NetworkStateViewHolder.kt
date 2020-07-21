package p.com.githubapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import p.com.githubapp.R

class NetworkStateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    companion object{
        fun create(parent: ViewGroup):NetworkStateViewHolder{
            return NetworkStateViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_network_state, parent, false))
        }
    }
}