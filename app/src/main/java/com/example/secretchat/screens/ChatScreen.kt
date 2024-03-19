package com.example.secretchat.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.secretchat.models.Message
import com.example.secretchat.ui.theme.Pink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(name:String,viewModel: ChatViewModel = viewModel()){

    LaunchedEffect(Unit){
        viewModel.connect()
    }
    DisposableEffect(Unit){
        onDispose {
            viewModel.shutdown()
        }
    }

    val messages by viewModel.messages.collectAsState()

    Column(modifier= Modifier.fillMaxSize()) {
        LazyColumn(modifier= Modifier
            .fillMaxSize()
            .weight(1f)){
            items(messages){message->
                val fromMe = message.name == name
                val shape = RoundedCornerShape(
                    topStart = if (fromMe) 12.dp else 2.dp,
                    topEnd = if (fromMe) 2.dp else 12.dp,
                    bottomEnd = 12.dp,
                    bottomStart = 12.dp
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                    , horizontalArrangement = if (fromMe) Arrangement.End else Arrangement.Start
                ) {
                    Box(
                        modifier= Modifier
                            .shadow(8.dp, shape = shape)
                            .clip(shape)
                            .background(if (fromMe) Pink else MaterialTheme.colorScheme.secondary)
                            .padding(
                                top = 5.dp,
                                bottom = 8.dp,
                                end = 10.dp,
                                start = 10.dp
                            )
                    ){
                        Column {
                            Text(
                                text = if (fromMe) "You" else message.name,
                                fontSize = 12.sp,
                                color = if (fromMe) Color(0xffeeeeee) else Color.Gray
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = message.text, color = Color.White)
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .shadow(4.dp, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val (text, setText) = remember {
                mutableStateOf("")
            }
            TextField(
                value = text,
                onValueChange = {
                    setText(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledTextColor = Color.White,
                    containerColor = Color.Transparent
                ), placeholder = {
                    Text("Message...")
                }
            )
            IconButton(onClick = {
                if (text.isNotEmpty()) {
                    viewModel.sendMessage(Message(name = name,text = text))
                    setText("")

                }
            }, modifier = Modifier
                .size(40.dp)
                .shadow(8.dp, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Send,
                    contentDescription = "send"
                )
            }
        }
    }
}