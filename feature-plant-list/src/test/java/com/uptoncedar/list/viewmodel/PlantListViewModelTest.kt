package com.uptoncedar.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.uptoncedar.common.model.Links
import com.uptoncedar.common.model.Meta
import com.uptoncedar.common.model.PlantListEntry
import com.uptoncedar.networking.data.FloraCodexRepository
import com.uptoncedar.networking.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
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
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PlantListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var floraCodexRepository: FloraCodexRepository

    private lateinit var viewModel: PlantListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = PlantListViewModel(floraCodexRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Empty`() = runTest {
        assertEquals(PlantListUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `getPlantsByQuery should update plants state with results on success`() = runTest {
        val query = "rose"
        val expectedPlants = listOf(
            PlantListEntry(
                id = "123", common_name = "Red Rose", image_url = "url1",
                author = "test_author",
                slug = "test_slug",
                scientific_name = "test_scientific_name",
                status = "test_status",
                rank = "test_rank",
                family = "test_family",
                genus = "test_genus",
                genus_id = "test_genus_id",
                links = Links(
                    self = "test_self_link",
                    genus = "test_genus_link",
                    plant = "test_plant_link"
                ),
                meta = Meta(
                    last_modified = "test_last_modified"
                )
            ),
            PlantListEntry(
                id = "456", common_name = "Blue Rose", image_url = "url2",
                author = "test_author",
                slug = "test_slug",
                scientific_name = "test_scientific_name",
                status = "test_status",
                rank = "test_rank",
                family = "test_family",
                genus = "test_genus",
                genus_id = "test_genus_id",
                links = Links(
                    self = "test_self_link",
                    genus = "test_genus_link",
                    plant = "test_plant_link"
                ),
                meta = Meta(
                    last_modified = "test_last_modified"
                )
            ),
        )

        `when`(floraCodexRepository.getPlantsByQuery(query)).thenReturn(
            Result.Success(
                expectedPlants
            )
        )

        viewModel.onPlantQuerySubmitted(query)
        advanceUntilIdle()

        val actualState = viewModel.uiState.value
        assertTrue(actualState is PlantListUiState.Success)
        assertEquals(expectedPlants, actualState.plants)
    }

    @Test
    fun `getPlantsByQuery should update state to Empty when no results are found`() = runTest {
        val query = "nonexistent"
        `when`(floraCodexRepository.getPlantsByQuery(query)).thenReturn(Result.Success(emptyList()))

        viewModel.onPlantQuerySubmitted(query)
        advanceUntilIdle()

        assertEquals(PlantListUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `getPlantsByQuery should update state to Error on failure`() = runTest {
        val query = "error"
        `when`(floraCodexRepository.getPlantsByQuery(query)).thenReturn(Result.Failure(Exception("API Error")))

        viewModel.onPlantQuerySubmitted(query)
        advanceUntilIdle()

        assertEquals(PlantListUiState.Error, viewModel.uiState.value)
    }

    @Test
    fun `getPlantsByQuery should update state to Loading before fetching`() = runTest {
        val query = "test"
        `when`(floraCodexRepository.getPlantsByQuery(query)).thenReturn(Result.Success(emptyList()))
        viewModel.onPlantQuerySubmitted(query)
        assertEquals(PlantListUiState.Loading, viewModel.uiState.value)
        advanceUntilIdle()
    }

    @Test
    fun `getPlantsByQuery should emit Loading state and then Success state`() = runTest {
        val query = "rose"
        val expectedPlants = listOf(
            PlantListEntry(
                id = "123", common_name = "Red Rose", image_url = "url1",
                author = "test_author",
                slug = "test_slug",
                scientific_name = "test_scientific_name",
                status = "test_status",
                rank = "test_rank",
                family = "test_family",
                genus = "test_genus",
                genus_id = "test_genus_id",
                links = Links(
                    self = "test_self_link",
                    genus = "test_genus_link",
                    plant = "test_plant_link"
                ),
                meta = Meta(
                    last_modified = "test_last_modified"
                )
            ),
            PlantListEntry(
                id = "456", common_name = "Blue Rose", image_url = "url2",
                author = "test_author",
                slug = "test_slug",
                scientific_name = "test_scientific_name",
                status = "test_status",
                rank = "test_rank",
                family = "test_family",
                genus = "test_genus",
                genus_id = "test_genus_id",
                links = Links(
                    self = "test_self_link",
                    genus = "test_genus_link",
                    plant = "test_plant_link"
                ),
                meta = Meta(
                    last_modified = "test_last_modified"
                )
            ),
        )
        `when`(floraCodexRepository.getPlantsByQuery(query)).thenReturn(
            Result.Success(
                expectedPlants
            )
        )

        val states = mutableListOf<PlantListUiState>()
        val job = launch {
            viewModel.uiState.collect { states.add(it) }
        }

        viewModel.onPlantQuerySubmitted(query)
        advanceUntilIdle()
        job.cancel()

        assertEquals(PlantListUiState.Loading, states[0])
        assertTrue(states[1] is PlantListUiState.Success)
        assertEquals(expectedPlants, (states[1] as PlantListUiState.Success).plants)
    }

    @Test
    fun `getPlantsByQuery should emit Loading state and then Empty state`() = runTest {
        val query = "test"
        `when`(floraCodexRepository.getPlantsByQuery(query)).thenReturn(Result.Success(emptyList()))

        val states = mutableListOf<PlantListUiState>()
        val job = launch {
            viewModel.uiState.collect { states.add(it) }
        }
        viewModel.onPlantQuerySubmitted(query)

        advanceUntilIdle()
        job.cancel()
        assertEquals(PlantListUiState.Loading, states[0])
        assertEquals(PlantListUiState.Empty, states[1])
    }

    @Test
    fun `getPlantsByQuery should emit Loading state and then Error state`() = runTest {
        val query = "error"
        `when`(floraCodexRepository.getPlantsByQuery(query)).thenReturn(Result.Failure(Exception("API Error")))

        val states = mutableListOf<PlantListUiState>()
        val job = launch {
            viewModel.uiState.collect { states.add(it) }
        }

        viewModel.onPlantQuerySubmitted(query)
        advanceUntilIdle()
        job.cancel()

        assertEquals(PlantListUiState.Loading, states[0])
        assertEquals(PlantListUiState.Error, states[1])
    }
}