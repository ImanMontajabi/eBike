package de.hsos.ma.erange

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.hsos.ma.erange.ui.theme.ERangeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ERangeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ERange(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ERange(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Composable
fun InputBox(title: String, txt: MutableState<String>) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Column(
            Modifier
                .padding(0.dp, 2.dp, 4.dp, 0.dp)
                .weight(2F),
            horizontalAlignment = Alignment.End
        )
        {
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
            onValueChange = {txt.value = it}
        )
    }
}

@Composable
fun ERange(modifier: Modifier, weight: MutableState<String>, capacity: MutableState<String>) {
    Column(modifier.padding(10.dp))
    {
        InputBox("Your weight [kg]: ", txt = weight)
        Spacer(Modifier.requiredHeight(10.dp))
        InputBox("Battery capacity [Wh]: ", txt = capacity)
        Spacer(Modifier.requiredHeight(10.dp))
    }
}

fun range() {
    // Wh per km
    val compution = 7.0f

}

@Preview(showBackground=true)
@Composable
fun ERangePreview() {
    val weight = rememberSaveable { mutableStateOf("80") }
    val capacity = rememberSaveable { mutableStateOf("670")}
    val modifier = Modifier
    ERangeTheme {
        ERange(modifier, weight, capacity)
    }
}

