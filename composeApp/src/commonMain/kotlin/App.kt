import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.primarySurface
        ) {
            Spacer(Modifier.size(12.dp))

            Messages(
                listOf(
                    Message("Sairaj", "PhonePe"),
                    Message("Sairaj2", "PhonePe"),
                    Message("Sairaj3", "PhonePe"),
                    Message("Sairaj4", "PhonePe"),
                )
            )
        }

//        var showContent by remember { mutableStateOf(false) }
//        Spacer(Modifier.fillMaxWidth())
//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click this!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }

    }
}

data class Message(val author: String, val body: String)

@Composable
fun MessageCard(
    msg: Message,
    viewModel: MessageViewModel = androidx.lifecycle.viewmodel.compose.viewModel { MessageViewModel() }
) {
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .then(
                Modifier.background(MaterialTheme.colors.secondaryVariant)
            )
            .then(
                Modifier.fillMaxWidth()
            ),
        horizontalArrangement = Arrangement.Start
    ) {
        val count = remember { mutableStateOf(0) }
        val uiState = viewModel.messageUiState.collectAsState()

        if (uiState.value == MessageUiState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.width(32.dp),
                color = MaterialTheme.colors.primaryVariant,
            )
        } else {
            Image(
                painter = painterResource(Res.drawable.compose_multiplatform),
                contentDescription = "Contact profile picture",
                modifier = Modifier
                    .size(48.dp)
            )

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${msg.author} drank ${count.value} times",
                    color = MaterialTheme.colors.primary
                )

                Spacer(modifier = Modifier.width(4.dp))

                if (count.value > 0) {
                    Text(
                        text = msg.body,
                        color = MaterialTheme.colors.secondary
                    )
                }
            }

            Button(
                onClick = {
                    count.value++
                },
                modifier = Modifier.padding(start = 24.dp)
            ) {
                Text(
                    text = "Drink",
                )
            }
        }


    }

}

@Composable
fun Messages(
    messages: List<Message>
) {
    LazyColumn(
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = messages,
            itemContent = { message ->
                MessageCard(message)
            }
        )
    }
}

sealed class MessageUiState {
    data object Loading : MessageUiState()
    data object Loaded : MessageUiState()
}

class MessageViewModel : ViewModel() {

    private val _messageUiState = MutableStateFlow<MessageUiState>(MessageUiState.Loading)
    val messageUiState: StateFlow<MessageUiState> = _messageUiState

    init {
        viewModelScope.launch {
            delay(2000)
            _messageUiState.value = MessageUiState.Loaded
        }
    }

}