package com.example.myapplication20.util

import android.graphics.Color.argb

data class GoogleArc(
    val startAnge: Float = 0.0f,
    val sweepAngle: Float = 0.0f,
    val paintColor: Int = 0
)

class GoogleArcs {

    private var arcs = mutableListOf<GoogleArc>()

    init {
        arcs = (1..4).map {
            GoogleArc(
                startAnge = START_ANGLES[it% 4],
                sweepAngle = SWEEP_ANGLES[it%4],
                paintColor = COLORS[it%4]
            )
        }.toMutableList()
    }

    fun getArcs() = arcs

    companion object {
        private val COLORS = mutableListOf(
            argb(255, 66, 133, 244), //blue
            argb(255, 52, 168, 83),  //green
            argb(255, 251, 188, 5),  //yellow
            argb(255, 235, 67, 53)   //red
        )

        private val START_ANGLES = mutableListOf(-11.5f, 50f, 155f, 215f)
        private val SWEEP_ANGLES = mutableListOf(62f, 105f, 60f, 100f)

    }
}