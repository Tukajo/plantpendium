package com.uptoncedar.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.uptoncedar.common.model.Links
import com.uptoncedar.common.model.Meta
import com.uptoncedar.common.model.PlantListEntry
import com.uptoncedar.list.domain.GetPlantsByQueryUseCase
import junit.framework.TestCase.assertEquals
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

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PlantListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getPlantsByQueryUseCase: GetPlantsByQueryUseCase

    private lateinit var viewModel: PlantListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = PlantListViewModel(getPlantsByQueryUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `searchPlants should update plants state with results on success`() = runTest {
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

        `when`(getPlantsByQueryUseCase(query)).thenReturn(expectedPlants)

        viewModel.searchPlants(query)
        advanceUntilIdle()

        val actualPlants = viewModel.plants.first()
        assertEquals(expectedPlants, actualPlants)
    }

    @Test
    fun `searchPlants should emit empty list when use case returns empty list`() = runTest {
        val query = "nonexistent"
        val expectedPlants = emptyList<PlantListEntry>()
        `when`(getPlantsByQueryUseCase(query)).thenReturn(expectedPlants)

        viewModel.searchPlants(query)
        advanceUntilIdle()

        val actualPlants = viewModel.plants.first()
        assertEquals(expectedPlants, actualPlants)
    }

    @Test
    fun `searchPlants should not update plants state on error`() = runTest {
        val query = "error"
        val errorMessage = "Failed to fetch plants"
        `when`(getPlantsByQueryUseCase(query)).thenThrow(RuntimeException(errorMessage))

        val initialPlants = viewModel.plants.first()
        viewModel.searchPlants(query)
        advanceUntilIdle()

        val actualPlants = viewModel.plants.first()
        assertEquals(initialPlants, actualPlants)
    }
}