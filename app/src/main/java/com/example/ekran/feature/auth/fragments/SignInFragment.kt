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

class SignInFragment : EkranFragment() {

    private val authViewModel: AuthViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progress = view.findViewById<LinearProgressIndicator>(R.id.signIn_progressBar)
        val username = view.findViewById<TextInputEditText>(R.id.textInputEditTextUsernameSignIn)
        val password = view.findViewById<TextInputEditText>(R.id.textInputEditTextPasswordSignIn)
        val send = view.findViewById<Button>(R.id.iconButtonSignIn)
        val goToSignUp = view.findViewById<Button>(R.id.textButtonGoToSignUp)

        send.setOnClickListener {
            authViewModel.signIn(username.text.toString(),password.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:EkranCompletableObserver(compositeDisposable){
                    override fun onComplete() {
                       requireActivity().finish()
                    }

                })
        }

        goToSignUp.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerAuth,SignUpFragment())
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