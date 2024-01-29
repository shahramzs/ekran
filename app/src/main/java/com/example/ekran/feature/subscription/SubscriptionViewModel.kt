package com.example.ekran.feature.subscription

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ekran.R
import com.example.ekran.base.EkranSingleObserver
import com.example.ekran.base.EkranViewModel
import com.example.ekran.base.TokenContainer
import com.example.ekran.model.EmptyState
import com.example.ekran.model.Subscription
import com.example.ekran.repository.subscriberRepository.SubscriberRepository
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SubscriptionViewModel(private val subscriberRepository: SubscriberRepository): EkranViewModel() {


    private var _subscriptionLiveData = MutableLiveData<List<Subscription>>()
    val subscriptionLiveData: LiveData<List<Subscription>>
        get() = _subscriptionLiveData

    private val _emptyStateLiveData = MutableLiveData<EmptyState>()
    val emptyStateLiveData: LiveData<EmptyState>
        get() = _emptyStateLiveData

    private fun getSubscription(){
        if(TokenContainer.token.isNullOrEmpty()){
            _emptyStateLiveData.value = EmptyState(true, R.string.empty_state_message,true)
        }else{
            _emptyStateLiveData.value = EmptyState(false)
            progressBarLiveData.value = true
            subscriberRepository.getSubscription(TokenContainer.token!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { progressBarLiveData.value = false }
                .subscribe(object :EkranSingleObserver<List<Subscription>>(compositeDisposable){
                    override fun onSuccess(t: List<Subscription>) {
                        _subscriptionLiveData.value = t
                    }

                })
        }
    }

    fun refresh(){
        getSubscription()
    }

    fun deleteSubscription(subscribe:Subscription): Completable = subscriberRepository.deleteSubscription(subscribe.subscriberToken)
        .doOnComplete{
            Timber.tag("cartItemCount").i(_subscriptionLiveData.value?.size.toString())

            _subscriptionLiveData.value?.let {
                if(it.isEmpty()){
                    _emptyStateLiveData.postValue(EmptyState(true, R.string.emptySubscription))
                }
            }
        }

}