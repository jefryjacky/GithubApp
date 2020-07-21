package p.com.githubapp.ui

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BasePageListAdapter<T,VH: RecyclerView.ViewHolder>(diffCallback: DiffUtil.ItemCallback<T>)
    : PagedListAdapter<T, VH>(diffCallback) {

    private var networkState: NetworkState? = null
    override fun getItemCount(): Int {
        return super.getItemCount()+ if (hasExtraRow()) 1 else 0
    }

    protected fun hasExtraRow():Boolean{
        return networkState != null && networkState != NetworkState.LOADED
    }

    fun setNetworkState(newNetworkState:NetworkState){
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}