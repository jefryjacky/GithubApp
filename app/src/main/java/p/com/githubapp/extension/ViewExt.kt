package p.com.githubapp.extension

import android.view.View

fun View.visibility(visible:Boolean){
    visibility = if(visible) {
        View.VISIBLE
    } else{
        View.GONE
    }
}