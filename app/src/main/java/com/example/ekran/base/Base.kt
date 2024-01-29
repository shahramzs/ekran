package com.example.ekran.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ekran.R
import com.example.ekran.feature.auth.AuthActivity
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class EkranActivity:AppCompatActivity(),EkranView{

    override val rootView: CoordinatorLayout?
        get(){
            val viewGroup = window.decorView.findViewById(android.R.id.content) as ViewGroup
            if(viewGroup !is CoordinatorLayout){
                viewGroup.children.forEach {
                    if(it is CoordinatorLayout)
                        return it
                }
                throw  IllegalStateException("RootView must be instance of CoordinatorLayout")
            }else{
                return viewGroup
            }
        }
    override val viewContext: Context?
        get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}

abstract class EkranFragment:Fragment(),EkranView{

    override val rootView: CoordinatorLayout?
        get() = view as CoordinatorLayout

    override val viewContext: Context?
        get() = context

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}

abstract class EkranViewModel:ViewModel(){
    var compositeDisposable = CompositeDisposable()
    val progressBarLiveData = MutableLiveData<Boolean>()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}

interface EkranView{
    val rootView: CoordinatorLayout?
    val viewContext: Context?
    fun setProgressIndicator(mustShow:Boolean){
        rootView?.let {
            viewContext.let { context ->
                var loadingView = it.findViewById<View>(R.id.loading_view)
                if(loadingView == null && mustShow){
                    loadingView =
                        LayoutInflater.from(context).inflate(R.layout.view_loading,it,false)
                    it.addView(loadingView)
                }
                loadingView?.visibility = if(mustShow) View.VISIBLE else View.GONE
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showError(ekranException:EkranException){
        viewContext?.let {
            when(ekranException.type){
                EkranException.Type.SIMPLE -> showSnackBar(ekranException.serverMessage?:it.getString(ekranException.userFriendlyMessage))
                EkranException.Type.AUTH -> {
                    it.startActivity(Intent(it,AuthActivity::class.java))
                    Toast.makeText(it,ekranException.serverMessage,Toast.LENGTH_LONG).show()
                }

                else -> {}
            }
        }
    }

    fun showSnackBar(message:String){
        rootView?.let {
            Snackbar.make(it,message,Snackbar.LENGTH_LONG).show()
        }
    }

    fun showEmptyState(layoutResId:Int):View?{
        rootView?.let {
            viewContext?.let{context->
                var emptyState = it.findViewById<View>(R.id.emptyStateRootView)
                if(emptyState == null) {
                    emptyState = LayoutInflater.from(context).inflate(layoutResId, it, false)
                    it.addView(emptyState)
                }
                emptyState.visibility = View.VISIBLE
                return emptyState
            }
        }
        return null
    }
}

