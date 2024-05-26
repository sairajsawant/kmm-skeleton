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
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
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
        ) {
            BillerScreen()
        }

    }
}

@Composable
fun BillerScreen(
    viewModel: BillerViewModel = viewModel { BillerViewModel() }
) {
    val uiState = viewModel.billerScreenUiState.collectAsState()

    when (uiState.value) {
        is BillerScreenUiState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        is BillerScreenUiState.Loaded -> {
            Column(
                modifier = Modifier.padding(all = 8.dp)
            ) {
                SearchBar(
                    modifier = Modifier.fillMaxWidth(),
                    onSearched = { searchText -> viewModel.search(searchText) }
                )

                Spacer(modifier = Modifier.size(8.dp))

                Billers(
                    (uiState.value as BillerScreenUiState.Loaded).billers
                )

            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearched: (String) -> Unit
) {
    val text = remember { mutableStateOf("") }

    OutlinedTextField(
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        modifier = modifier,
        value = text.value,
        onValueChange = {
            text.value = it
            onSearched(text.value.trim())
        },
        maxLines = 1,
        label = { Text("Search Biller") },
    )
}

data class Biller(
    val name: String
)

@Composable
fun BillerItem(
    biller: Biller,
    shouldShowDivider: Boolean = true
) {
    Row(
        modifier = Modifier
            .then(
                Modifier.fillMaxWidth()
            ),
        horizontalArrangement = Arrangement.Start
    ) {

        Image(
            painter = painterResource(Res.drawable.compose_multiplatform),
            contentDescription = "Biller Img",
            modifier = Modifier
                .size(48.dp)
        )

        Text(
            modifier = Modifier.align(Alignment.CenterVertically)
                .then(Modifier.padding(start = 8.dp)),
            text = biller.name,
            color = MaterialTheme.colors.primary
        )

        if (shouldShowDivider) {
            Divider(
                modifier = Modifier.fillMaxWidth()
            )
        }

    }

}

@Composable
fun Billers(
    billers: List<Biller>
) {
    Card(
        backgroundColor = MaterialTheme.colors.surface,
    ) {
        LazyColumn {
            items(
                items = billers,
                itemContent = { biller ->
                    BillerItem(biller)
                }
            )
        }
    }
}

class BillerViewModel : ViewModel() {

    private val _billerScreenUiState =
        MutableStateFlow<BillerScreenUiState>(BillerScreenUiState.Loading)
    val billerScreenUiState: StateFlow<BillerScreenUiState> = _billerScreenUiState

    private var initialBillers: List<Biller>? = null

    init {
        viewModelScope.launch {
            delay(2000)
            val billers: List<Biller> = List(size = 50) { Biller("MSEDEC Mahavitaran") }
            _billerScreenUiState.value = BillerScreenUiState.Loaded(billers)
            initialBillers = billers
        }
    }

    fun search(searchText: String) {
        initialBillers?.apply {
            _billerScreenUiState.value = BillerScreenUiState.Loaded(
                filter { it.name.contains(searchText, ignoreCase = true) }
            )
        }
    }

}