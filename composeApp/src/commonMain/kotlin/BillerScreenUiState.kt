sealed interface BillerScreenUiState {

    data object Loading : BillerScreenUiState

    data class Loaded(
        val billers: List<Biller>
    ) : BillerScreenUiState

}