package org.classup.bitmanipulationgadget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.classup.bitmanipulationgadget.ui.theme.BMGOrangeBrighter
import org.classup.bitmanipulationgadget.ui.theme.BMGPageIndicator
import org.classup.bitmanipulationgadget.ui.theme.BMGText
import org.classup.bitmanipulationgadget.ui.theme.BMGTextBrighter
import kotlin.math.ceil

@Composable
private fun BinaryResult(result: String) {
    val padBits = "0000 0000 0000 0000"

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(10.dp),
        colors = CardDefaults.cardColors(containerColor = BMGOrangeBrighter)
    )
    {
        val coloredResult = buildAnnotatedString {
            val rowCount = if (bmgTextIsValid(result)) ceil((result.length / 16).toDouble()).toInt() else 0

            // Keep calm and do the padding first.
            for (i in 0 until 4 - rowCount) {
                withStyle(style = SpanStyle(color = BMGText)) {
                    append(padBits + if (i == 3) "" else "\n")
                }
            }

            if (rowCount > 0) {
                // Then, draw the answer after.
                for (i in 0 until rowCount) {
                    withStyle(style = SpanStyle(color = BMGTextBrighter)) {
                        append(spaceEvery4th(result.substring(i * 16, i * 16 + 16)) + if (i == rowCount - 1) "" else "\n")
                    }
                }
            }
        }
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = coloredResult,
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 9.dp)
            )
        }
    }
}

@Composable
private fun DecimalResult(result: String) {
    val decimalResult: String = if (bmgTextIsValid(result)) {
        result.toULong(2).toLong().toString()
    }
    else {
        "DECIMAL RESULT"
    }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(10.dp),
        colors = CardDefaults.cardColors(containerColor = BMGOrangeBrighter)
    )
    {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = decimalResult,
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 9.dp)
            )
        }
    }
}

@Composable
private fun HexResult(result: String) {
    var hexResult: String

    hexResult = if (bmgTextIsValid(result)) {
        "0x" + result.toULong(2).toString(16)
    }
    else {
        "HEXADECIMAL RESULT"
    }

    if (hexResult == "0x") hexResult = "0x0"  // Lol

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(9.dp),
        colors = CardDefaults.cardColors(containerColor = BMGOrangeBrighter)
    )
    {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = hexResult,
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 9.dp)
            )
        }
    }
}

@Composable
fun ResultLayout(result: String) {
    BinaryResult(result)
    DecimalResult(result)
    HexResult(result)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageIndicator (pages: Int, pagerState: PagerState) {
    if (pages < 2) { return }

    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = 18.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    )
    {
        repeat(pagerState.pageCount) { iteration ->
            val color =
                if (pagerState.currentPage == iteration) BMGText else BMGPageIndicator
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(10.dp)
            )
        }
    }
}