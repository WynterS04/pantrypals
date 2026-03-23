package com.example.pantrypals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pantrypals.ui.theme.PantryPalsTheme
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.SelectInstance

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PantryPalsTheme() {
                GeminiSimpleScreen()
            }
        }
    }
}

@Composable
fun GeminiSimpleScreen() {
    var responseText by remember { mutableStateOf("Ready to ask Gemini") }
    var isLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val generativeModel = remember{
        GenerativeModel(
            //model we want to use
            modelName = "gemini-3.1-flash-lite-preview",
            //api key
            apiKey = "AIzaSyBCQ1pI_HNvAr9CPNY1bIW7QTDpeVev0AM"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.weight(.8f)
                .weight(.5f)
                .fillMaxWidth(),
            contentAlignment =  Alignment.Center
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        responseText = "Thinking..."

                        try{
                            val prompt = "Tell me a funny joke"
                            val response = generativeModel.generateContent(prompt)

                            responseText = response.text ?: "No response generated."

                        } catch (e: Exception){
                            responseText = "Error: ${e.localizedMessage}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading
            ) {
                Text("Send prompt")
            }
        }
        Box(
            modifier = Modifier.weight(.8f)
                .weight(.5f)
                .fillMaxWidth(),
            contentAlignment =  Alignment.Center
        ) {
            Text(
                text = responseText,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}