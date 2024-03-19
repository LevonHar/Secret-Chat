package com.example.secretchat

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.secretchat.ui.theme.SecretMessageTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val authorized = remember {
                mutableStateOf(false)
            }

            val authorize: () -> Unit = {
                BiometricHelper.showPrompt(this) {
                    authorized.value = true
                }
            }

            LaunchedEffect(Unit) {
                authorize()
            }

            SecretMessageTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (authorized.value) {
                        Navigation()
                    } else {
                        Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
                            Column {
                                AnimatedVisibility(visible = !authorized.value) {
                                    FloatingActionButton(
                                        onClick = authorize,
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }) { paddings ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddings)
                            )
                        }
                    }
                }
            }
        }
    }
}