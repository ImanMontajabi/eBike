package de.hsos.ma.erange

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.hsos.ma.erange.ui.theme.ERangeTheme
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ERangeTheme {
                val navController = rememberNavController()

                var menuExpanded by remember { mutableStateOf(false) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),

                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text(stringResource(id = R.string.app_name))
                            },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = { menuExpanded = true }) {
                                    Icon(
                                        imageVector = Icons.Filled.Menu,
                                        contentDescription = "Menu"
                                    )
                                }
                                DropdownMenu(
                                    expanded = menuExpanded,
                                    onDismissRequest = { menuExpanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Berechnen") },
                                        onClick = {
                                            navController.navigate("home")
                                            menuExpanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Info") },
                                        onClick = {
                                            navController.navigate("info")
                                            menuExpanded = false
                                        }
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomAppBar(
                            actions = {
                                IconButton(onClick = { navController.navigate("home") }) {
                                    Icon(
                                        Icons.Filled.PlayArrow,
                                        contentDescription = "Calculate",
                                    )
                                }
                                IconButton(onClick = { navController.navigate("info") }) {
                                    Icon(
                                        Icons.Filled.Info,
                                        contentDescription = "App Info",
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            ERange(
                                modifier = Modifier,
                                navController = navController
                            )
                        }
                        composable("info") {
                            ERangeInfo(
                                navController = navController,
                                modifier = Modifier
                            )
                        }

                        composable(
                            route = "result/{rangeResult}",
                            arguments = listOf(navArgument("rangeResult") {
                                type = NavType.StringType
                                nullable = true
                            })
                        ) { backStackEntry ->
                            val result = backStackEntry.arguments?.getString("rangeResult")

                            ResultScreen(
                                navController = navController,
                                rangeResult = result,
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InputBox(title: String, txt: MutableState<String>) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier
                .padding(10.dp, 2.dp, 4.dp, 0.dp)
                .weight(2F),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        OutlinedTextField(
            value = txt.value,
            label = {},
            modifier = Modifier
                .padding(4.dp, 0.dp, 12.dp, 0.dp)
                .weight(3f),
            textStyle = MaterialTheme.typography.headlineMedium,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            onValueChange = { txt.value = it }
        )
    }
}

@Composable
fun CalculateButton(
    weight: MutableState<String>,
    selectedCapacityIndex: MutableState<Int>,
    isFlatTourProfile: MutableState<Boolean>,
    navController: NavController
) {
    val capacities = listOf("600 Wh", "620 Wh", "640 Wh", "660 Wh")
        Button(
            onClick = {
                val w = weight.value.toDoubleOrNull() ?: 0.0
                val capacityStr = capacities[selectedCapacityIndex.value].replace(" Wh", "")
                val c = capacityStr.toDoubleOrNull() ?: 0.0
                val r = range(w, c, isFlatTourProfile.value)
                val resultString = "Your range (weight = $w, capacity = $c, flat = ${isFlatTourProfile.value}) is: %.3f km.".format(r)
                navController.navigate("result/${resultString.replace("/", "-")}")
            },
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                "Calculate",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.inversePrimary
            )
        }
    }

@Composable
fun SwitchBox(title: String, isFlatTourProfile: MutableState<Boolean>) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier
                .padding(10.dp, 2.dp, 4.dp, 0.dp)
                .weight(2F),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Switch(
            checked = isFlatTourProfile.value,
            onCheckedChange = {
                isFlatTourProfile.value = it
            },
            modifier = Modifier
                .padding(4.dp, 0.dp, 12.dp, 0.dp)
                .weight(3f)
        )
    }
}

@Composable
fun DropDownSelection(
    title: String,
    itemPosition: MutableState<Int>,
    isDropDownExpanded: MutableState<Boolean>
) {
    val capacities = listOf("600 Wh", "620 Wh", "640 Wh", "660 Wh")

    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier
                .padding(10.dp, 2.dp, 4.dp, 0.dp)
                .weight(2F),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Column(
            Modifier
                .padding(4.dp, 0.dp, 12.dp, 0.dp)
                .weight(3f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isDropDownExpanded.value = true }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = capacities[itemPosition.value],
                    style = MaterialTheme.typography.headlineMedium
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    modifier = Modifier.size(36.dp),
                    contentDescription = "Show options"
                )
            }
            DropdownMenu(
                expanded = isDropDownExpanded.value,
                onDismissRequest = { isDropDownExpanded.value = false }
            ) {
                capacities.forEachIndexed { index, capacity ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = capacity,
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        onClick = {
                            isDropDownExpanded.value = false
                            itemPosition.value = index
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun AppHeader() {
    val imagePrinter = painterResource(id = R.drawable.bicycle)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.requiredHeight(10.dp))
        Image(
            painter = imagePrinter,
            contentDescription = stringResource(R.string.app_name) + " icon",
            modifier = Modifier.size(120.dp)
        )
    }
}

@Composable
fun RoundedOutputWindow(output: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            text = output,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun BackToHomeButton(navController: NavController) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding((16.dp))
        ) {
            Text("Back")
        }
    }
}

@Composable
fun ERangeInfo(navController: NavController, modifier: Modifier = Modifier) {
    val output = "Diese App berechnet die ungefähre Reichweite Ihres E-Bikes basierend auf bla bla bla"

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader()
        Spacer(Modifier.requiredHeight(40.dp))
        RoundedOutputWindow(output = output)
        Spacer(Modifier.requiredHeight(10.dp))
        BackToHomeButton(navController = navController)
    }
}

@Composable
fun ResultScreen(
    navController: NavController,
    rangeResult: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader()
        Spacer(Modifier.requiredHeight(40.dp))
        RoundedOutputWindow(output = rangeResult ?: "No result calculated")
        Spacer(Modifier.requiredHeight(10.dp))
        BackToHomeButton(navController = navController)
    }
}

@Composable
fun ERange(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val weight = rememberSaveable { mutableStateOf("80") }
    val isFlatTourProfile = rememberSaveable { mutableStateOf(true) }
    val selectedCapacityIndex = rememberSaveable { mutableIntStateOf(0) }
    val isDropDownExpanded = rememberSaveable { mutableStateOf(false) }

    Column(modifier.padding(10.dp)) {
        AppHeader()
        Spacer(Modifier.requiredHeight(10.dp))
        Spacer(Modifier.requiredHeight(10.dp))
        InputBox("Your weight [kg]: ", txt = weight)
        Spacer(Modifier.requiredHeight(10.dp))
        DropDownSelection(
            "Battery capacity [Wh]: ",
            itemPosition = selectedCapacityIndex,
            isDropDownExpanded = isDropDownExpanded
        )
        Spacer(Modifier.requiredHeight(10.dp))
        SwitchBox("Flat tour profile: ", isFlatTourProfile = isFlatTourProfile)
        Spacer(Modifier.requiredHeight(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CalculateButton(
                weight,
                selectedCapacityIndex,
                isFlatTourProfile,
                navController
            )

            Button(
                onClick = {
                    navController.navigate("info")
                },
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    "Info",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            }
        }
    }
}

fun range(weight: Double, capacity: Double, isFlat: Boolean): Double {
    val consumption = 7.0
    val normWeight = 80.0
    var range = (capacity / consumption * (normWeight / weight))
    range = range / 2.0
    if (!isFlat) range *= 0.7
    return range
}

@Preview(showBackground = true)
@Composable
fun ERangePreview() {
    val navController = rememberNavController()
    ERangeTheme {
        ERange(
            modifier = Modifier,
            navController = navController
        )
    }
}