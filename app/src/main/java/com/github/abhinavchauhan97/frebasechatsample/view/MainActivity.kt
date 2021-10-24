package com.github.abhinavchauhan97.frebasechatsample.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.abhinavchauhan97.frebasechatsample.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val signInLauncher = registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res -> handleSignInResult(res) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(FirebaseAuth.getInstance().currentUser == null){
            signInLauncher.launch(getAuthIntent())
        }else{
            setContentView(R.layout.activity_main)
        }
    }

    private fun getAuthIntent(): Intent {
        val authProviders = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setIsSmartLockEnabled(false)
            .setAvailableProviders(authProviders)
            .build()
    }

    private fun handleSignInResult(authenticationResult:FirebaseAuthUIAuthenticationResult){
        if(authenticationResult.resultCode == RESULT_OK){
             setContentView(R.layout.activity_main)
        }else{
           finish()
        }
    }
}