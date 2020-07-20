package p.com.githubapp.ui.searchusers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_user.view.*
import p.com.githubapp.R
import p.com.githubapp.domain.entity.User
import p.com.githubapp.extension.load

class UserViewHolder (itemView:View): RecyclerView.ViewHolder(itemView) {

    fun bind(user:User){
        itemView.profile_picture.load(user.profilePicture)
        itemView.username.text = user.username
    }

    companion object{
        fun create(parent:ViewGroup):UserViewHolder{
            return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_user, parent, false))
        }
    }
}