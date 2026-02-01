package com.example.fightbadhabits.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fightbadhabits.R
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchByTextRequest
import com.google.android.libraries.places.api.model.CircularBounds

@SuppressLint("MissingPermission")
@Composable
fun SupportMapScreen() {
    val context = LocalContext.current
    val apiKey = "AIzaSyCAGD3vxiZKMbAjH-z_5nCfqA5bDT87zCs"

    //Initialisation of Places API
    val placesClient: PlacesClient = remember {
        if (!Places.isInitialized()) {
            Places.initialize(context, apiKey)
        }
        Places.createClient(context)
    }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var deviceLocation by remember { mutableStateOf(LatLng(52.2297, 21.0122)) }
    var nearbyClinics by remember { mutableStateOf(listOf<Place>()) }
    var isSearching by remember { mutableStateOf(false) }
    var hasLocationPermission by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(deviceLocation, 11f)
    }

    val searchNearby: (LatLng) -> Unit = remember {
        { location ->
            isSearching = true
            val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)

            val categories = try {
                context.resources.getStringArray(R.array.category_array)
            } catch (e: Exception) {
                arrayOf("clinic", "rehabilitation center","psychologist")
            }

            val allResults = mutableListOf<Place>()
            var completedRequests = 0

            categories.forEach { category ->
                val searchRequest = SearchByTextRequest.builder(category, placeFields)
                    .setMaxResultCount(5)
                    .setLocationBias(CircularBounds.newInstance(location, 3000.0))
                    .build()

                placesClient.searchByText(searchRequest)
                    .addOnSuccessListener { response ->
                        allResults.addAll(response.places)
                    }
                    .addOnCompleteListener {
                        completedRequests++
                        if (completedRequests == categories.size) {
                            nearbyClinics = allResults.distinctBy { it.id }
                            isSearching = false
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("PLACES_ERROR", "Error for $category: ${e.message}")
                        if (completedRequests == categories.size) isSearching = false
                    }
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.all { it }
        hasLocationPermission = granted
        if (granted) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    deviceLocation = currentLatLng
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLatLng, 14f)
                    searchNearby(currentLatLng)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = hasLocationPermission,
                mapType = MapType.NORMAL
            ),
            uiSettings = MapUiSettings(myLocationButtonEnabled = true)
        ) {
            nearbyClinics.forEach { place ->
                place.latLng?.let { latLng ->
                    Marker(
                        state = MarkerState(position = latLng),
                        title = place.name,
                        snippet = place.address
                    )
                }
            }
        }

        if (isSearching) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
                    .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Looking for professional help in the area...", fontSize = 14.sp, color = Color.Black)
                }
            }
        }
    }
}