package com.theapache64.klokk

import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.theapache64.klokk.composable.Clock
import com.theapache64.klokk.movement.core.Movement
import com.theapache64.klokk.theme.Black
import com.theapache64.klokk.theme.CodGray
import kotlinx.coroutines.delay


// Configs
const val COLUMNS = 15
const val ROWS = 8
const val DIGIT_COLUMNS = 3
const val DIGIT_ROWS = 6
const val PADDING = 100
const val CLOCK_SIZE = 60
const val CLOCKS_CONTAINER_WIDTH = CLOCK_SIZE * COLUMNS
const val CLOCKS_CONTAINER_HEIGHT = CLOCK_SIZE * ROWS
const val ENJOY_TIME_IN_MILLIS = 500L
val BACKGROUND_COLOR = Black
val CLOCK_BACKGROUND = CodGray


fun main() {

    Window(
        title = "Klokk",
        // Clock container plus the padding we need
        size = IntSize(CLOCKS_CONTAINER_WIDTH + PADDING, CLOCKS_CONTAINER_HEIGHT + PADDING),
    ) {

        // To hold and control movement transition
        var activeMovement by remember { mutableStateOf<Movement>(Movement.StandBy()) }

        // Generating degree matrix using the active movement
        val degreeMatrix = activeMovement.getMatrixGenerator().getVerifiedMatrix()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BACKGROUND_COLOR),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Building clock matrix
            repeat(ROWS) { i ->
                Row {
                    repeat(COLUMNS) { j ->
                        val clockData = degreeMatrix[i][j]
                        Clock(
                            _needleOneDegree = clockData.degreeOne,
                            _needleTwoDegree = clockData.degreeTwo,
                            durationInMillis = activeMovement.durationInMillis,
                            modifier = Modifier.requiredSize(CLOCK_SIZE.dp)
                        )
                    }
                }
            }

            // The animation loop
            LaunchedEffect(Unit) {
                println("Animation loop created and started..")
                val trance = Movement.Trance()
                val waitTime = trance.durationInMillis.toLong() + ENJOY_TIME_IN_MILLIS

                while (true) {
                    delay(ENJOY_TIME_IN_MILLIS * 3)
                    activeMovement = Movement.Trance(Movement.Trance.To.SQUARE) // Show square
                    delay(waitTime)

                    activeMovement = Movement.Trance(to = Movement.Trance.To.FLOWER) // Then flower
                    delay(waitTime - ENJOY_TIME_IN_MILLIS)

                    activeMovement = Movement.Trance(to = Movement.Trance.To.STAR) // To star, through circle (auto)
                    delay(waitTime)

                    activeMovement = Movement.Trance(to = Movement.Trance.To.FLY) // then fly
                    delay(waitTime)

                    activeMovement = Movement.Ripple(to = Movement.Ripple.To.START) // then ripple start
                    delay(activeMovement.durationInMillis + ENJOY_TIME_IN_MILLIS)

                    activeMovement = Movement.Ripple(to = Movement.Ripple.To.END) // then ripple end
                    delay(activeMovement.durationInMillis + ENJOY_TIME_IN_MILLIS)

                    activeMovement = Movement.Time() // then show time
                    delay(activeMovement.durationInMillis + ENJOY_TIME_IN_MILLIS)
                }
            }
        }
    }
}




