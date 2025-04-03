import com.uptoncedar.common.model.Images
import com.uptoncedar.common.model.Links
import com.uptoncedar.common.model.Meta
import com.uptoncedar.common.model.PlantDetails
import com.uptoncedar.common.model.Species
import com.uptoncedar.plant.details.domain.GetPlantByIdUseCase
import com.uptoncedar.plant.details.domain.PlantDetailsError
import com.uptoncedar.plant.details.viewmodel.PlantDetailsUiState
import com.uptoncedar.plant.details.viewmodel.PlantDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.io.IOException
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class PlantDetailsViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var getPlantByIdUseCase: GetPlantByIdUseCase

    private lateinit var viewModel: PlantDetailsViewModel

    private val testPlantDetails = PlantDetails(
        id = "123",
        common_name = "Test Plant",
        scientific_name = "Testitus Plantus",
        main_species = Species(
            family = "Testaceae",
            genus = "Testus",
            image_url = "http://example.com/image.jpg",
            status = "",
            rank = "",
            specifications = emptyMap(),
            growth = emptyMap(),
            images = Images(
                other = emptyList()
            ),
            common_names = emptyList()
        ),
        author = "auth1",
        slug = "slug1",
        genus_id = "",
        links = Links(
            self = "",
            genus = "",
            plant = ""
        ),
        meta = Meta(
            last_modified = ""
        ),
        main_species_id = ""
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PlantDetailsViewModel(getPlantByIdUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        val states = mutableListOf<PlantDetailsUiState>()
        val plantId = "1"
        `when`(getPlantByIdUseCase.invoke(plantId)).thenReturn(Result.success(testPlantDetails))
        val job = launch {
            viewModel.uiState.collect { states.add(it) }
        }

        viewModel.fetchPlantDetails(plantId)
        advanceUntilIdle()
        job.cancel()
        assertTrue(states[0] is PlantDetailsUiState.Loading, "Initial state should be Loading")
    }


    @Test
    fun `PlantDetailsViewModel transitions Loading to Success on successful fetch`() =
        runTest(testDispatcher) {
            val plantId = "1"
            `when`(getPlantByIdUseCase.invoke(plantId)).thenReturn(Result.success(testPlantDetails))

            viewModel.fetchPlantDetails(plantId)

            assertTrue(viewModel.uiState.value is PlantDetailsUiState.Loading)

            advanceUntilIdle()

            val finalState = viewModel.uiState.value
            assertTrue(finalState is PlantDetailsUiState.Success)
            assertEquals(testPlantDetails, finalState.plantDetails)
        }

    @Test
    fun `PlantDetailsViewModel transitions Loading to Error on PlantNotFound failure`() =
        runTest(testDispatcher) {
            val plantId = "2"
            val expectedError = PlantDetailsError.PlantNotFound
            `when`(getPlantByIdUseCase.invoke(plantId)).thenReturn(Result.failure(expectedError))

            viewModel.fetchPlantDetails(plantId)

            assertTrue(viewModel.uiState.value is PlantDetailsUiState.Loading)
            advanceUntilIdle()
            val finalState = viewModel.uiState.value
            assertTrue(finalState is PlantDetailsUiState.Error)
            assertEquals(expectedError, finalState.error)
        }

    @Test
    fun `PlantDetailsViewModel transitions Loading to Error on NetworkError failure`() =
        runTest(testDispatcher) {
            val plantId = "3"
            val networkException = IOException("Network failed")
            val expectedError = PlantDetailsError.NetworkError(networkException)
            `when`(getPlantByIdUseCase.invoke(plantId)).thenReturn(Result.failure(expectedError))

            viewModel.fetchPlantDetails(plantId)

            assertTrue(viewModel.uiState.value is PlantDetailsUiState.Loading)
            advanceUntilIdle()
            val finalState = viewModel.uiState.value
            assertTrue(finalState is PlantDetailsUiState.Error)
            assertEquals(expectedError, finalState.error)
            assertEquals(
                networkException,
                (finalState.error as PlantDetailsError.NetworkError).cause
            )
        }

    @Test
    fun `PlantDetailsViewModel transitions Loading to Error on UnknownError failure`() =
        runTest(testDispatcher) {
            val plantId = "4"
            val unknownException = RuntimeException("Something unexpected happened")
            val expectedError = PlantDetailsError.UnknownError(unknownException)
            `when`(getPlantByIdUseCase.invoke(plantId)).thenReturn(Result.failure(expectedError))

            viewModel.fetchPlantDetails(plantId)

            assertTrue(viewModel.uiState.value is PlantDetailsUiState.Loading)
            advanceUntilIdle()
            val finalState = viewModel.uiState.value
            assertTrue(finalState is PlantDetailsUiState.Error)
            assertEquals(expectedError, finalState.error)
            assertEquals(
                unknownException,
                (finalState.error as PlantDetailsError.UnknownError).cause
            )
        }
}