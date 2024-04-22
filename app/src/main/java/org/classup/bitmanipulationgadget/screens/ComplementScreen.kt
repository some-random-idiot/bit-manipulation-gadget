package org.classup.bitmanipulationgadget.screens
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.classup.bitmanipulationgadget.bmgStringIsValid
import org.classup.bitmanipulationgadget.inputTo64Binary
import org.classup.bitmanipulationgadget.layouts.PageIndicatorLayout
import org.classup.bitmanipulationgadget.layouts.ResultLayout
import org.classup.bitmanipulationgadget.padBinary16Divisible
import org.classup.bitmanipulationgadget.spaceEvery4th
import org.classup.bitmanipulationgadget.ui.theme.BMGOrangeBrighter
import org.classup.bitmanipulationgadget.ui.theme.kufam

@Composable
fun ComplementScreen(input: String, rememberInput: (String) -> Unit) {
    val inputBinary = inputTo64Binary(input)
    val result = if (bmgStringIsValid(inputBinary)) inputBinary.toULong(2).inv().toString(2) else "Invalid input!"

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    )
    {
        InputCard(input) { new ->
            rememberInput(new)
        }
        SolutionCard(inputBinary, result)
        ResultLayout(result)
    }
}

@Composable
private fun InputCard(input: String, rememberInput: (String) -> Unit) {
    val textFieldColors = TextFieldDefaults.colors(
        unfocusedContainerColor = Color.Transparent,
        focusedContainerColor = Color.Transparent
    )
    val textFieldTextStyle = TextStyle(fontSize = 24.sp, fontFamily = kufam, color = Color.Black, textAlign = TextAlign.Center)

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(9.dp),
        colors = CardDefaults.cardColors(containerColor = BMGOrangeBrighter)
    )
    {
        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "COMPLEMENT",
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            TextField(
                value = input,
                textStyle = textFieldTextStyle,
                onValueChange = { rememberInput(it) },
                colors = textFieldColors,
                singleLine = true,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SolutionCard(inputBinary: String, result: String) {
    val inputBinaryPadded = padBinary16Divisible(inputBinary)
    val resultPadded = padBinary16Divisible(result)

    val pages = (inputBinaryPadded.length / 16).coerceAtLeast(1)  // Doesn't really matter which one you use since they are all pre-padded.
    val pagerState = rememberPagerState(pageCount = { pages })

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(9.dp),
        colors = CardDefaults.cardColors(containerColor = BMGOrangeBrighter)
    )
    {
        HorizontalPager(state = pagerState) { page ->
            val formattedResult: AnnotatedString = if (bmgStringIsValid(resultPadded)) {
                val splittedResult = spaceEvery4th(resultPadded.substring(16 * page, 16 * (page + 1)))

                buildAnnotatedString {
                    for (i in splittedResult.indices) {
                        withStyle(style = SpanStyle(color = if (splittedResult[i] == '1') Color.Green else Color.Red)) {
                            append(splittedResult[i])
                        }
                    }
                }
            } else {
                buildAnnotatedString { append("RESULT") }
            }

            Column {
                Text(
                    text = "COMPLEMENT",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = if (bmgStringIsValid(inputBinaryPadded)) spaceEvery4th(
                        inputBinaryPadded.substring(
                            16 * page,
                            16 * (page + 1)
                        )
                    ) else "SECOND INPUT",
                    fontSize = 26.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = formattedResult,
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        PageIndicatorLayout(pages, pagerState)
    }
}