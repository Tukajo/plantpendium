package com.uptoncedar.list.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.uptoncedar.common.model.Links // Placeholder - adjust if needed
import com.uptoncedar.common.model.Meta // Placeholder - adjust if needed
import com.uptoncedar.common.model.PlantListEntry
import com.uptoncedar.list.viewmodel.PlantListUiState
import com.uptoncedar.list.viewmodel.PlantListViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock // Use Mockito annotation
import org.mockito.Mockito // Use Mockito static methods
import org.mockito.MockitoAnnotations // To initialize mocks annotated with @Mock

// Interface for navigation callback mocking with Mockito
interface NavigateCallback {
    fun onNavigate(plantId: String)
}

@RunWith(AndroidJUnit4::class)
class PlantListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Mock
    private lateinit var mockViewModel: PlantListViewModel

    @Mock
    private lateinit var mockNavigateCallback: NavigateCallback

    private lateinit var uiStateFlow: MutableStateFlow<PlantListUiState>

    private val samplePlant1 = PlantListEntry(
        id = "plant1", author = "Auth1", common_name = "Test Plant 1", slug = "test-plant-1",
        scientific_name = "Scientificus Testus Unus", status = "ok", rank = "species",
        family = "Testaceae", genus = "Testus", genus_id = "g1", image_url = "",
        links = Links("", "", ""), meta = Meta(last_modified = "")
    )
    private val samplePlant2 = PlantListEntry(
        id = "plant2", author = "Auth2", common_name = "Test Plant 2", slug = "test-plant-2",
        scientific_name = "Scientificus Testus Duo", status = "ok", rank = "species",
        family = "Testaceae", genus = "Testus", genus_id = "g1", image_url = "",
        links = Links("", "", ""), meta = Meta(last_modified = "")
    )
    private val samplePlants = listOf(samplePlant1, samplePlant2)

    @Before
    fun setUp() {
        // Initialize mocks annotated with @Mock
        MockitoAnnotations.openMocks(this)

        // Initialize the state flow with a default state (e.g., Empty)
        uiStateFlow = MutableStateFlow(PlantListUiState.Empty)

        // Configure the mock ViewModel to return our controllable state flow using Mockito.when
        Mockito.`when`(mockViewModel.uiState).thenReturn(uiStateFlow)

        // No need to explicitly stub onPlantQuerySubmitted unless verifying args strictly,
        // as Mockito mocks return default values (null, 0, false) for non-stubbed methods.
        // If the method returned non-Unit, you'd stub it like the uiState.
        // For void methods, if you need to ensure they don't throw exceptions:
        // Mockito.doNothing().`when`(mockViewModel).onPlantQuerySubmitted(Mockito.anyString())
    }

    // --- Test Helper Function ---
    private fun setContent() {
        composeTestRule.setContent {
            MaterialTheme { // Apply theme if your composable relies on it
                PlantListScreen(
                    viewModel = mockViewModel,
                    // Pass a lambda that calls the mock interface method
                    onNavigateToDetails = { plantId -> mockNavigateCallback.onNavigate(plantId) }
                )
            }
        }
    }

    // --- Test Cases (Assertions remain the same, only setup and verification change) ---

    @Test
    fun whenLoading_loadingIndicatorIsDisplayed() {
        // Arrange
        uiStateFlow.value = PlantListUiState.Loading
        setContent()

        // Assert
        composeTestRule.onNodeWithTag("loadingIndicator").assertIsDisplayed()
        composeTestRule.onNodeWithTag("plantList", useUnmergedTree = true).assertDoesNotExist()
        composeTestRule.onNodeWithTag("errorMessage", useUnmergedTree = true).assertDoesNotExist()
        composeTestRule.onNodeWithTag("emptyMessage", useUnmergedTree = true).assertDoesNotExist()
    }

    @Test
    fun whenError_errorMessageIsDisplayed() {
        // Arrange
        uiStateFlow.value = PlantListUiState.Error
        setContent()

        // Assert
        composeTestRule.onNodeWithTag("errorMessage").assertIsDisplayed()
        composeTestRule.onNodeWithTag("plantList", useUnmergedTree = true).assertDoesNotExist()
        composeTestRule.onNodeWithTag("loadingIndicator", useUnmergedTree = true).assertDoesNotExist()
        composeTestRule.onNodeWithTag("emptyMessage", useUnmergedTree = true).assertDoesNotExist()
    }

    @Test
    fun whenEmpty_emptyMessageIsDisplayed() {
        // Arrange
        uiStateFlow.value = PlantListUiState.Empty
        setContent()

        // Assert
        composeTestRule.onNodeWithTag("emptyMessage").assertIsDisplayed()
        composeTestRule.onNodeWithTag("plantList", useUnmergedTree = true).assertDoesNotExist()
        composeTestRule.onNodeWithTag("loadingIndicator", useUnmergedTree = true).assertDoesNotExist()
        composeTestRule.onNodeWithTag("errorMessage", useUnmergedTree = true).assertDoesNotExist()
    }

    @Test
    fun whenSuccess_plantListIsDisplayed() {
        // Arrange
        uiStateFlow.value = PlantListUiState.Success(samplePlants)
        setContent()

        // Assert
        composeTestRule.onNodeWithTag("searchTextField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("plantList").assertIsDisplayed()
        composeTestRule.onNodeWithTag("plantListItem_${samplePlant1.id}").assertIsDisplayed()
        composeTestRule.onNodeWithText(samplePlant1.common_name!!).assertIsDisplayed()
        composeTestRule.onNodeWithTag("plantListItem_${samplePlant2.id}").assertIsDisplayed()
        composeTestRule.onNodeWithText(samplePlant2.common_name!!).assertIsDisplayed()
        composeTestRule.onNodeWithTag("loadingIndicator", useUnmergedTree = true).assertDoesNotExist()
        composeTestRule.onNodeWithTag("errorMessage", useUnmergedTree = true).assertDoesNotExist()
        composeTestRule.onNodeWithTag("emptyMessage", useUnmergedTree = true).assertDoesNotExist()
    }

    @Test
    fun whenPlantItemClicked_onNavigateToDetailsIsCalled() {
        // Arrange
        uiStateFlow.value = PlantListUiState.Success(samplePlants)
        setContent()

        // Act
        val plantNode = composeTestRule.onNodeWithTag("plantListItem_${samplePlant1.id}")
        plantNode.assertIsDisplayed()
        plantNode.performClick()

        // Assert: Verify using Mockito.verify and the mock interface method
        Mockito.verify(mockNavigateCallback, Mockito.times(1)).onNavigate(samplePlant1.id)
        // Verify it wasn't called for the other plant ID
        Mockito.verify(mockNavigateCallback, Mockito.never()).onNavigate(samplePlant2.id)
        // Verify it wasn't called with any other string
        Mockito.verify(mockNavigateCallback, Mockito.never()).onNavigate(Mockito.argThat { it != samplePlant1.id })
    }

    @Test
    fun whenSearchSubmitted_viewModelOnPlantQuerySubmittedIsCalled() {
        // Arrange
        uiStateFlow.value = PlantListUiState.Success(samplePlants) // Initial state
        setContent()
        val searchQuery = "rose"

        // Act
        val searchField = composeTestRule.onNodeWithTag("searchTextField")
        searchField.performTextInput(searchQuery)
        searchField.performImeAction()

        // Assert: Verify using Mockito.verify
        Mockito.verify(mockViewModel, Mockito.times(1)).onPlantQuerySubmitted(searchQuery)
        // Optional: Verify it wasn't called with any other query
        Mockito.verify(mockViewModel, Mockito.never()).onPlantQuerySubmitted(Mockito.argThat { it != searchQuery })
    }
}