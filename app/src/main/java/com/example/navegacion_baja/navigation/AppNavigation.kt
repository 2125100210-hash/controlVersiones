package com.example.navegacion_baja.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay

// Importaciones de tus pantallas
import com.example.navegacion_baja.ui.home.HomeRoute
import com.example.navegacion_baja.ui.favorites.FavoritesRoute
import com.example.navegacion_baja.ui.favorites.detail.FavoriteDetailRoute
import com.example.navegacion_baja.ui.profile.ProfileRoute

@Composable
fun AppNavigation() {
    val destinations = listOf(
        AppDestination.Home,
        AppDestination.Favorites,
        AppDestination.Profile
    )

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val homeBackStack = rememberNavBackStack(AppDestination.Home)
    val favoritesBackStack = rememberNavBackStack(AppDestination.Favorites)
    val profileBackStack = rememberNavBackStack(AppDestination.Profile)

    val currentBackStack = when (selectedIndex) {
        0 -> homeBackStack
        1 -> favoritesBackStack
        else -> profileBackStack
    }

    BackHandler(
        enabled = selectedIndex != 0 && currentBackStack.size == 1
    ) {
        selectedIndex = 0
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                destinations.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            when (destination) {
                                AppDestination.Home -> Icon(Icons.Default.Home, contentDescription = "Inicio")
                                AppDestination.Favorites -> Icon(Icons.Default.Favorite, contentDescription = "Favoritos")
                                AppDestination.Profile -> Icon(Icons.Default.Person, contentDescription = "Perfil")
                                else -> {} // Evita el error al ignorar otros destinos
                            }
                        },
                        label = {
                            Text(
                                text = when (destination) {
                                    AppDestination.Home -> "Inicio"
                                    AppDestination.Favorites -> "Favoritos"
                                    AppDestination.Profile -> "Perfil"
                                    else -> ""
                                }
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            backStack = currentBackStack,
            onBack = {
                if (currentBackStack.size > 1) {
                    currentBackStack.removeLastOrNull()
                } else if (selectedIndex != 0) {
                    selectedIndex = 0
                }
            },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<AppDestination.Home> {
                    HomeRoute()
                }

                entry<AppDestination.Favorites> {
                    FavoritesRoute(
                        onFavoriteClick = { favoriteId ->
                            favoritesBackStack.add(
                                AppDestination.FavoriteDetail(favoriteId = favoriteId)
                            )
                        }
                    )
                }

                entry<AppDestination.FavoriteDetail> { destination ->
                    FavoriteDetailRoute(
                        favoriteId = destination.favoriteId,
                        onBack = {
                            favoritesBackStack.removeLastOrNull()
                        }
                    )
                }

                entry<AppDestination.Profile> {
                    ProfileRoute()
                }
            }
        )
    }
}