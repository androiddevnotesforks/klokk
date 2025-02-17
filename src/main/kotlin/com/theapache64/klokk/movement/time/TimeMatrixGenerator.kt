package com.theapache64.klokk.movement.time

import com.theapache64.klokk.DIGIT_COLUMNS
import com.theapache64.klokk.DIGIT_ROWS
import com.theapache64.klokk.model.ClockData
import com.theapache64.klokk.movement.StandByMatrixGenerator
import com.theapache64.klokk.movement.core.MatrixGenerator
import com.theapache64.klokk.movement.core.Movement
import java.text.SimpleDateFormat

/**
 * Responsible to show time animation and digits.
 */
class TimeMatrixGenerator(data: Movement.Time) : MatrixGenerator<Movement.Time>(data) {
    companion object {

        private val timeFormat = SimpleDateFormat("HHmm")

        private val h1Position = Pair(1, 1)
        private val h2Position = Pair(1, 4)
        private val m1Position = Pair(1, 8)
        private val m2Position = Pair(1, 11)

        fun getTimeMatrix(time: Movement.Time): List<List<ClockData>> {

            val (h1, h2, m1, m2) = timeFormat.format(time.date)
                .split("")
                .filter { it.isNotEmpty() }
                .map { it.toInt() }

            val h1Matrix = getMatrixFor(h1)
            val h2Matrix = getMatrixFor(h2)
            val m1Matrix = getMatrixFor(m1)
            val m2Matrix = getMatrixFor(m2)

            return mutableListOf<List<ClockData>>().apply {
                val standByMatrix = StandByMatrixGenerator(Movement.StandBy())
                    .getVerifiedMatrix()
                    .map { it.toMutableList() }
                    .toMutableList()

                // first standby

                // Now change what we need

                // First hour
                replace(standByMatrix, h1Matrix, h1Position)
                replace(standByMatrix, h2Matrix, h2Position)
                replace(standByMatrix, m1Matrix, m1Position)
                replace(standByMatrix, m2Matrix, m2Position)


                // Finally add to list
                addAll(standByMatrix)
            }
        }

        private fun replace(
            standByMatrix: MutableList<MutableList<ClockData>>,
            replacementMatrix: List<List<ClockData?>>,
            replacementCoordinate: Pair<Int, Int>,
        ) {
            repeat(DIGIT_ROWS) { i ->
                repeat(DIGIT_COLUMNS) { j ->
                    val element = replacementMatrix[i][j]
                    if (element != null) {
                        val targetX = replacementCoordinate.first + i
                        val targetY = replacementCoordinate.second + j
                        standByMatrix[targetX][targetY] = element
                    }
                }
            }
        }

        private fun getMatrixFor(digit: Int): List<List<ClockData?>> {
            val digitMatrix: DigitMatrix = when (digit) {
                0 -> ZeroMatrix
                1 -> OneMatrix
                2 -> TwoMatrix
                3 -> ThreeMatrix
                4 -> FourMatrix
                5 -> FiveMatrix
                6 -> SixMatrix
                7 -> SevenMatrix
                8 -> EightMatrix
                9 -> NineMatrix
                else -> throw IllegalAccessException("Matrix not defined for $digit")
            }

            return verifyIntegrityAndReturn(digitMatrix.getMatrix())
        }


        private fun verifyIntegrityAndReturn(matrix: List<List<ClockData?>>): List<List<ClockData?>> {
            require(matrix.size == DIGIT_ROWS) {
                "No of digit rows should be $DIGIT_ROWS but found ${matrix.size}"
            }
            for (columns in matrix) {
                require(columns.size == DIGIT_COLUMNS) {
                    "No of digit columns should be $DIGIT_COLUMNS, but found ${columns.size}"
                }
            }
            return matrix
        }

    }

    override fun generateMatrix(): List<List<ClockData>> {
        return getTimeMatrix(data)
    }
}