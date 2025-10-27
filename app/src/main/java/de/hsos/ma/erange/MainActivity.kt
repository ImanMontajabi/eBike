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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.hsos.ma.erange.ui.theme.ERangeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val weight = rememberSaveable { mutableStateOf("80") }
            val isFlatTourProfile = rememberSaveable { mutableStateOf(true) }
            val selectedCapacityIndex = rememberSaveable { mutableStateOf(0) }
            val isDropDownExpanded = rememberSaveable { mutableStateOf(false) }
            ERangeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ERange(
                        modifier = Modifier.padding(innerPadding),
                        weight = weight,
                        isFlatTourProfile = isFlatTourProfile,
                        selectedCapacityIndex = selectedCapacityIndex,
                        isDropDownExpanded = isDropDownExpanded
                    )
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
    output: MutableState<String>
) {
    val capacities = listOf("600 Wh", "620 Wh", "640 Wh", "660 Wh")

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(
            onClick = {
                val w = weight.value.toDoubleOrNull() ?: 0.0
                val capacityStr = capacities[selectedCapacityIndex.value].replace(" Wh", "")
                val c = capacityStr.toDoubleOrNull() ?: 0.0
                val r = range(w, c, isFlatTourProfile.value)
                output.value = "Range is: %.1f km".format(r)
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
fun ERangeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun HeaderIcon(modifier: Modifier = Modifier) {
    val imagePointer = painterResource(id = R.drawable.bicycle)
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter =imagePointer,
            contentDescription = "App Header Logo",
            modifier = modifier.size(144.dp)
        )
    }
}

@Composable
fun ERangeInfo() {

}

@Composable
fun ERange(
    modifier: Modifier = Modifier,
    weight: MutableState<String>,
    isFlatTourProfile: MutableState<Boolean>,
    selectedCapacityIndex: MutableState<Int>,
    isDropDownExpanded: MutableState<Boolean>
) {
    val output = rememberSaveable { mutableStateOf("") }
    Column(modifier.padding(10.dp)) {
        ERangeHeader()
        Spacer(Modifier.requiredHeight(10.dp))
        HeaderIcon()
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
        CalculateButton(weight, selectedCapacityIndex, isFlatTourProfile, output)
        // TODO: Replace this with RoundedOutputWindow()
        if (output.value.isNotEmpty()) {
            Text(
                output.value,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

fun range(weight: Double, capacity: Double, isFlat: Boolean): Double {
    val consumption = 7.0 // Wh per km
    val normWeight = 80.0 // kg
    var range = (capacity / consumption * (normWeight / weight))
    range = range / 2.0
    if (!isFlat) range *= 0.7 // penalty mountainous
    return range
}

@Preview(showBackground = true)
@Composable
fun ERangePreview() {
    val weight = rememberSaveable { mutableStateOf("80") }
    val isFlatTourProfile = rememberSaveable { mutableStateOf(true) }
    val isDropDownExpanded = rememberSaveable { mutableStateOf(false) }
    val selectedCapacityIndex = rememberSaveable { mutableStateOf(0) }
    ERangeTheme {
        ERange(
            Modifier,
            weight,
            isFlatTourProfile,
            selectedCapacityIndex,
            isDropDownExpanded
        )
    }
}