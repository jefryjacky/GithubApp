package p.com.githubapp.commonandroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_network_state.view.*
import p.com.githubapp.R
import p.com.githubapp.extension.visibility

class NetworkStateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(networkState: NetworkState, retryCallBack:(()->Unit)?){
        itemView.btn_retry.visibility(networkState.status == Status.FAILED)
        itemView.porgress_bar.visibility(networkState == NetworkState.LOADING)
        itemView.btn_retry.setOnClickListener {
            retryCallBack?.invoke()
        }
    }

    companion object{
        fun create(parent: ViewGroup): NetworkStateViewHolder {
            return NetworkStateViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_network_state, parent, false)
            )
        }
    }
}