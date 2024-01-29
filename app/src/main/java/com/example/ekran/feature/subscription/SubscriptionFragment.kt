package com.example.ekran.feature.subscription

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ekran.R
import com.example.ekran.base.EkranCompletableObserver
import com.example.ekran.base.EkranFragment
import com.example.ekran.feature.auth.AuthActivity
import com.example.ekran.feature.subscription.adapter.SubscriptionAdapter
import com.example.ekran.model.Subscription
import com.google.android.material.button.MaterialButton
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class SubscriptionFragment : EkranFragment(),SubscriptionAdapter.SubscriptionOnClickListener {

    private val viewModel:SubscriptionViewModel by viewModel()
    private val adapter:SubscriptionAdapter by inject()
    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subscription, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.subscriptionOnClickListener = this

        val subscribeRV = view.findViewById<RecyclerView>(R.id.subscribe_list)
        subscribeRV.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        subscribeRV.adapter = adapter

        viewModel.subscriptionLiveData.observe(viewLifecycleOwner){
            adapter.subscribeList = it as ArrayList<Subscription>
        }

        viewModel.emptyStateLiveData.observe(viewLifecycleOwner){
            if(it.mustShow){
                val emptyState = showEmptyState(R.layout.view_empty_state)
                emptyState?.let{view ->
                    val text = view.findViewById<TextView>(R.id.emptyStateMassageTv)
                    val btn = view.findViewById<MaterialButton>(R.id.emptyStateButton)
                    text.text = getString(it.messageResId)
                    btn.visibility = if(it.mustShowCallToActionButton) View.VISIBLE else View.GONE
                    btn.setOnClickListener {
                        startActivity(Intent(requireContext(), AuthActivity::class.java))
                    }
                }
            }else{
                val emptyStateRoot = requireActivity().findViewById<View>(R.id.emptyStateRootView)
                emptyStateRoot?.visibility = View.GONE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.refresh()
    }

    override fun deleteSubscription(subscribe:Subscription) {
        viewModel.deleteSubscription(subscribe)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object:EkranCompletableObserver(compositeDisposable){
                override fun onComplete() {
                    adapter.removeSubscription(subscribe)
                }

            })
    }

}