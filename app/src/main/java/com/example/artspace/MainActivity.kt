package com.example.artspace

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.artspace.ui.theme.ArtSpaceTheme

data class ArtSpaceUiState(
    val currentStep: Int = 1
)

class ArtSpaceViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ArtSpaceUiState())
    val uiState: StateFlow<ArtSpaceUiState> = _uiState.asStateFlow()

    fun previousArtwork() {
        _uiState.update { currentState ->
            val nextStep = if (currentState.currentStep > 1) currentState.currentStep - 1 else 3
            currentState.copy(currentStep = nextStep)
        }
    }

    fun nextArtwork() {
        _uiState.update { currentState ->
            val nextStep = if (currentState.currentStep < 3) currentState.currentStep + 1 else 1
            currentState.copy(currentStep = nextStep)
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtSpaceTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ArtSpaceApp()
                }
            }
        }
    }
}

@Composable
fun ArtSpaceApp(viewModel: ArtSpaceViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val currentStep = uiState.currentStep

    val imageResource = when (currentStep) {
        1 -> R.drawable.artwork_1
        2 -> R.drawable.artwork_2
        else -> R.drawable.artwork_3
    }
    val titleResource = when (currentStep) {
        1 -> R.string.artwork1_title
        2 -> R.string.artwork2_title
        else -> R.string.artwork3_title
    }
    val artistResource = when (currentStep) {
        1 -> R.string.artwork1_artist
        2 -> R.string.artwork2_artist
        else -> R.string.artwork3_artist
    }
    val yearResource = when (currentStep) {
        1 -> R.string.artwork1_year
        2 -> R.string.artwork2_year
        else -> R.string.artwork3_year
    }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp).safeDrawingPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f).padding(16.dp).border(2.dp, Color.Gray).padding(16.dp)
            ) {
                Image(
                    painter = painterResource(imageResource),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier.weight(1f).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ArtworkDescriptor(titleResource, artistResource, yearResource)
                Spacer(modifier = Modifier.height(24.dp))
                DisplayController(
                    onPrevClick = { viewModel.previousArtwork() },
                    onNextClick = { viewModel.nextArtwork() }
                )
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp).statusBarsPadding().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp).border(2.dp, Color.LightGray).padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(imageResource),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            ArtworkDescriptor(titleResource, artistResource, yearResource)
            Spacer(modifier = Modifier.height(24.dp))
            DisplayController(
                onPrevClick = { viewModel.previousArtwork() },
                onNextClick = { viewModel.nextArtwork() }
            )
        }
    }
}

@Composable
fun ArtworkDescriptor(titleRes: Int, artistRes: Int, yearRes: Int) {
    Column(
        modifier = Modifier.background(Color(0xFFECEFF1)).padding(16.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(titleRes), fontSize = 24.sp, fontWeight = FontWeight.Light, modifier = Modifier.padding(bottom = 8.dp))
        Row {
            Text(text = stringResource(artistRes), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "(${stringResource(yearRes)})", fontSize = 16.sp)
        }
    }
}

@Composable
fun DisplayController(onPrevClick: () -> Unit, onNextClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = onPrevClick, modifier = Modifier.weight(1f)) {
            Text(text = stringResource(R.string.previous))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(onClick = onNextClick, modifier = Modifier.weight(1f)) {
            Text(text = stringResource(R.string.next))
        }
    }
}