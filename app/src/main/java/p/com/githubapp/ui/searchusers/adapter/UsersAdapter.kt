package p.com.githubapp.ui.searchusers.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import p.com.githubapp.R
import p.com.githubapp.domain.entity.User
import p.com.githubapp.commonandroid.BasePageListAdapter
import p.com.githubapp.commonandroid.NetworkStateViewHolder

class UsersAdapter:
    BasePageListAdapter<User, RecyclerView.ViewHolder>(UserDiffUtil()) {

    var retryCallBack:(()->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            R.layout.list_item_network_state -> NetworkStateViewHolder.create(parent)
            R.layout.list_item_user ->  UserViewHolder.create(parent)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            R.layout.list_item_user ->{
                val user = getItem(position)
                user?.let {
                    (holder as UserViewHolder).bind(it)
                }
            }
            R.layout.list_item_network_state->{
                getNetworkState()?.let {
                    (holder as NetworkStateViewHolder).bind(it, retryCallBack)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.list_item_network_state
        } else {
            R.layout.list_item_user
        }
    }

    class UserDiffUtil: DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id != newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }
}