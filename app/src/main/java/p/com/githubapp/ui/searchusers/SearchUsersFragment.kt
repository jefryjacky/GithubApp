package p.com.githubapp.ui.searchusers

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_search_users.*
import p.com.githubapp.MainActivity
import p.com.githubapp.R
import p.com.githubapp.commonandroid.BaseFragment
import p.com.githubapp.commonandroid.Status
import p.com.githubapp.ui.searchusers.adapter.UsersAdapter

class SearchUsersFragment: BaseFragment(R.layout.fragment_search_users) {

    private val mViewModel:SearchUsersViewModel by viewModels { viewModelFactory }
    private val mAdapter = UsersAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
        initObserver()
    }

    private fun initview(){
        rv_users.apply {
            setHasFixedSize(true)
            mAdapter.retryCallBack = {
                mViewModel.retry()
            }
            adapter = mAdapter
        }

        btn_search.setOnClickListener {
            val query = et_search.text.toString()
            mViewModel.query(query)
        }
    }

    private fun initObserver(){
       mViewModel.users.observe(viewLifecycleOwner, Observer {
           rv_users.visibility = View.VISIBLE
           mAdapter.submitList(it)
       })

        mViewModel.noMatchingAccountEvent.observe(viewLifecycleOwner, Observer {
            it.contentIfNotHaveBeenHandle?.let {
                AlertDialog.Builder(requireContext())
                    .setMessage(R.string.no_matching_account)
                    .setPositiveButton(R.string.btn_ok) { dialog, _ ->
                        dialog.dismiss()
                    }.create().show()
            }
        })

        mViewModel.networkStateEvent.observe(viewLifecycleOwner, Observer {event->
            mAdapter.setNetworkState(event.peekContent())
            event.contentIfNotHaveBeenHandle?.let {
                if(it.status == Status.FAILED){
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.msg)
                        .setPositiveButton(R.string.btn_ok) { dialog, _ ->
                            dialog.dismiss()
                        }.create().show()
                }
            }
        })
    }
}