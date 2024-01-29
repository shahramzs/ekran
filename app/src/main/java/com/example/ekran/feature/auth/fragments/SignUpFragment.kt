package com.example.ekran.feature.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.ekran.R
import com.example.ekran.base.EkranCompletableObserver
import com.example.ekran.base.EkranFragment
import com.example.ekran.feature.auth.AuthViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : EkranFragment() {

    private val authViewModel:AuthViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progress = view.findViewById<LinearProgressIndicator>(R.id.sign_up_progressBar)
        val username = view.findViewById<TextInputEditText>(R.id.textInputUsername)
        val email = view.findViewById<TextInputEditText>(R.id.textInputEmail)
        val password = view.findViewById<TextInputEditText>(R.id.textInputPassword)
        val send = view.findViewById<Button>(R.id.iconButtonSignUp)
        val gotoSignIn = view.findViewById<Button>(R.id.textButtonGoToSignIn)

        send.setOnClickListener {
            authViewModel.signUp(username.text.toString(),email.text.toString(),password.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:EkranCompletableObserver(compositeDisposable){
                    override fun onComplete() {
                        requireActivity().finish()
                    }
                })
        }

        gotoSignIn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerAuth,SignInFragment())
            }.commit()
        }

        authViewModel.progressBarLiveData.observe(viewLifecycleOwner){
            progress.isEnabled = it
        }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }



}