package com.uptoncedar.plant.details.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.uptoncedar.common.model.Images
import com.uptoncedar.common.model.Links
import com.uptoncedar.common.model.Meta
import com.uptoncedar.common.model.PlantDetails
import com.uptoncedar.common.model.Species
import com.uptoncedar.plant.details.domain.GetPlantByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PlantDetailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getPlantByIdUseCase: GetPlantByIdUseCase

    private lateinit var viewModel: PlantDetailsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = PlantDetailsViewModel(getPlantByIdUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `PlantDetails should update plantDetails, isLoading, and clear errorMessage on success`() =
        runTest {
            val plantId = "123"
            val expectedPlantDetails = PlantDetails(
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
            `when`(getPlantByIdUseCase(plantId)).thenReturn(expectedPlantDetails)

            viewModel.fetchPlantDetails(plantId)
            assertTrue(viewModel.isLoading.first())
            advanceUntilIdle()

            assertEquals(expectedPlantDetails, viewModel.plantDetails.first())
            assertEquals(false, viewModel.isLoading.first())
            assertNull(viewModel.errorMessage.first())
        }

    @Test
    fun `PlantDetails should update errorMessage and isLoading on error`() = runTest {
        val plantId = "456"
        val errorMessage = "Failed to fetch plant details"
        `when`(getPlantByIdUseCase(plantId)).thenThrow(RuntimeException(errorMessage))

        viewModel.fetchPlantDetails(plantId)
        assertTrue(viewModel.isLoading.first())
        advanceUntilIdle()

        assertNull(viewModel.plantDetails.first())
        assertEquals(false, viewModel.isLoading.first())
        assertEquals(errorMessage, viewModel.errorMessage.first())
    }

    @Test
    fun `PlantDetails should handle localizedMessage when exception has one`() = runTest {
        val plantId = "789"
        val localizedErrorMessage = "Network error"
        val exception = RuntimeException("Generic error")
        `when`(exception.localizedMessage).thenReturn(localizedErrorMessage)
        `when`(getPlantByIdUseCase(plantId)).thenThrow(exception)

        viewModel.fetchPlantDetails(plantId)
        advanceUntilIdle()

        assertEquals(localizedErrorMessage, viewModel.errorMessage.first())
    }

    @Test
    fun `PlantDetails should handle generic error message when localizedMessage is null`() =
        runTest {
            val plantId = "987"
            val exception = RuntimeException("Generic error with no localized message")
            `when`(exception.localizedMessage).thenReturn(null)
            `when`(getPlantByIdUseCase(plantId)).thenThrow(exception)

            viewModel.fetchPlantDetails(plantId)
            advanceUntilIdle()

            assertEquals("An unexpected error occurred", viewModel.errorMessage.first())
        }

    @Test
    fun `clearError should set errorMessage to null`() = runTest {
        val plantId = "111"
        val errorMessage = "An error occurred"
        `when`(getPlantByIdUseCase(plantId)).thenThrow(RuntimeException(errorMessage))
        viewModel.fetchPlantDetails(plantId)
        advanceUntilIdle()
        assertEquals(
            errorMessage,
            viewModel.errorMessage.first()
        )

        viewModel.clearError()
        advanceUntilIdle()

        assertNull(viewModel.errorMessage.first())
    }
}