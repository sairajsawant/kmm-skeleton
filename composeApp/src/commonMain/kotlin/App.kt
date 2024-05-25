import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
        ) {
            BillerScreen()
        }

    }
}

@Composable
fun BillerScreen(
    viewModel: BillerViewModel = androidx.lifecycle.viewmodel.compose.viewModel { BillerViewModel() }
) {
    val uiState = viewModel.billerScreenUiState.collectAsState()

    when (uiState.value) {
        is BillerScreenUiState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier.width(8.dp)
            )
        }

        is BillerScreenUiState.Loaded -> {
            Billers(
                (uiState.value as BillerScreenUiState.Loaded).billers
            )
        }
    }
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
            .padding(all = 8.dp)
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

sealed class BillerScreenUiState {

    data object Loading : BillerScreenUiState()

    data class Loaded(
        val billers: List<Biller>
    ) : BillerScreenUiState()

}

class BillerViewModel : ViewModel() {

    private val _billerScreenUiState =
        MutableStateFlow<BillerScreenUiState>(BillerScreenUiState.Loading)
    val billerScreenUiState: StateFlow<BillerScreenUiState> = _billerScreenUiState

    init {
        viewModelScope.launch {
            delay(2000)
            val billers: List<Biller> = List(size = 100) { biller -> Biller("MSEDEC Mahavitaran")}
            _billerScreenUiState.value = BillerScreenUiState.Loaded(billers)
        }
    }

}