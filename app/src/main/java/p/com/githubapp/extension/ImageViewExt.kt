package p.com.githubapp.extension

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.load(url:String){
    Glide.with(this)
        .load(url)
        .centerCrop()
        .into(this)
}