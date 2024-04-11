package org.classup.bitmanipulationgadget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.classup.bitmanipulationgadget.ui.theme.BMGOrangeBrighter
import org.classup.bitmanipulationgadget.ui.theme.BMGText
import org.classup.bitmanipulationgadget.ui.theme.BitManipulationGadgetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BitManipulationGadgetTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                )
                {
                   NavBar()
                }
            }
        }
    }
}

@Composable
fun NavBar() {
    val navController = rememberNavController()
    var navSelectedItem by remember {
        // TODO: Does mutableIntStateOf cause problems? If so, fallback to mutableStateOf.
        mutableIntStateOf(0)
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                NavItemInfo().getAllNavItems().forEachIndexed {
                    index, itemInfo -> NavigationBarItem(
                        selected = index == navSelectedItem,
                        onClick = {
                            navSelectedItem = index
                            navController.navigate(itemInfo.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {Icon(imageVector = itemInfo.icon, contentDescription = itemInfo.label, tint = BMGText, modifier = Modifier.size(16.dp))},
                        label = {Text(text = itemInfo.label, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)},
                        colors = NavigationBarItemDefaults.colors(
                            // No idea what part of a theme actually changes the indicator so this will do.
                            indicatorColor = BMGOrangeBrighter
                        )
                    )
                }
            }
        }
    )
    {
        paddingValues -> NavHost(
            navController = navController,
            startDestination = DestinationScreens.And.route,
            modifier = Modifier.padding(paddingValues)
        )
        {
            composable(route = DestinationScreens.And.route) {}
            composable(route = DestinationScreens.Or.route) {}
            composable(route = DestinationScreens.Xor.route) {}
            composable(route = DestinationScreens.Complement.route) {}
            composable(route = DestinationScreens.Shift.route) {}
        }
    }
}