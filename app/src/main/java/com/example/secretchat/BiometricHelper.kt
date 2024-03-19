package com.example.secretchat

import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

object BiometricHelper {
    fun createBiometricPrompt(
        activity: AppCompatActivity,
        processSuccess: (BiometricPrompt.AuthenticationResult) -> Unit
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                processSuccess(result)
            }
        }
        return BiometricPrompt(activity, executor, callback)
    }

    fun createPromptInfo(): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder()
            .apply {
                setTitle("Secret Messages")
                setDescription("Use your fingerprint or face to unlock app and see secret messages")
                setConfirmationRequired(false)
                setNegativeButtonText("Cancel")
            }.build()

    fun showPrompt(activity: AppCompatActivity,onSuccess:()->Unit){
        createBiometricPrompt(activity = activity){
            onSuccess()
        }
            .authenticate(createPromptInfo())
    }
}