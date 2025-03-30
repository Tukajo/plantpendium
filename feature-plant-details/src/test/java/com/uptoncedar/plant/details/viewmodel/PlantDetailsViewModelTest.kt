import com.uptoncedar.common.model.Images
import com.uptoncedar.common.model.Links
import com.uptoncedar.common.model.Meta
import com.uptoncedar.common.model.PlantDetails
import com.uptoncedar.common.model.Species
import com.uptoncedar.plant.details.domain.GetPlantByIdUseCase
import com.uptoncedar.plant.details.viewmodel.PlantDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class PlantDetailsViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var getPlantByIdUseCase: GetPlantByIdUseCase

    private lateinit var viewModel: PlantDetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = PlantDetailsViewModel(getPlantByIdUseCase)
    }

    @Test
    fun `fetchPlantDetails updates plantDetails on success`() = runTest {
        val plantDetails = PlantDetails(
            id = "123",
            author = "na",
            common_name = "Rose",
            slug = "test_slug",
            scientific_name = "test_scientific_name",
            genus_id = "test_genus_id",
            links = Links(
                self = "test_self_link",
                genus = "test_genus_link",
                plant = "test_plant_link"
            ),
            meta = Meta(
                last_modified = "test_last_modified"
            ),
            main_species_id = "test_main_species_id",
            main_species = Species(
                status = "test_status",
                rank = "test_rank",
                family = "test_family",
                genus = "test_genus",
                image_url = "test_image_url",
                specifications = emptyMap(),
                growth = emptyMap(),
                images = Images(
                    other = emptyList()
                ),
                common_names = emptyList()
            )
        )
        `when`(getPlantByIdUseCase("1")).thenReturn(plantDetails)

        viewModel.fetchPlantDetails("1")
        advanceUntilIdle()

        assertEquals(plantDetails, viewModel.plantDetails.first())
        assertFalse(viewModel.isLoading.first())
        assertNull(viewModel.errorMessage.first())
    }

    @Test
    fun `fetchPlantDetails updates errorMessage on failure`() = runTest {
        val errorMessage = "Network error"
        `when`(getPlantByIdUseCase("1")).thenThrow(RuntimeException(errorMessage))

        viewModel.fetchPlantDetails("1")
        advanceUntilIdle()

        assertNull(viewModel.plantDetails.first())
        assertFalse(viewModel.isLoading.first())
        assertEquals(errorMessage, viewModel.errorMessage.first())
    }

    @Test
    fun `clearError clears errorMessage`() = runTest {
        viewModel.clearError()
        assertNull(viewModel.errorMessage.first())
    }
}
