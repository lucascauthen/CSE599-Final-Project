package com.example.admin.padometer

import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.DataPointInterface
import com.jjoe64.graphview.series.LineGraphSeries
import timber.log.Timber
import kotlin.math.pow
import kotlin.math.sqrt

class ListOfPoints {

    val points = arrayListOf<Point>()
    val isEmpty
        get() = points.isEmpty()
    val isNotEmpty
        get() = !points.isEmpty()

    val size: Int
        get() = points.size

    fun add(x: Float, y: Float, z: Float, t: Float) {
        points.add(Point(t, sqrt(x.pow(2) + y.pow(2) + z.pow(2))))
    }

    fun add(p: Point) {
        points.add(p)
    }

    fun mean() {
        if (points.size > 0) {
            var sum = 0.0f
            points.forEach {
                sum += it.y
            }
            val mean = sum / points.size
            points.forEach {
                it.y = it.y - mean
            }
        }
    }

    fun movmean(step: Int = 100, pass: Int = 1) {
        repeat(pass) {
            points.windowed(step) { points ->
                var sum = 0.0f
                points.forEach {
                    sum += it.y
                }
                val average = sum / points.size
                points.forEach {
                    it.y = average
                }

            }
        }
    }

    fun toSeries(): LineGraphSeries<DataPoint> {
        if (points.size > 0) {
            val data = ArrayList<DataPoint>()
            points.forEach {
                data.add(it.toDataPoint())
            }
            return LineGraphSeries(data.toTypedArray())
        }
        return LineGraphSeries()
    }

    fun trimFront(seconds: Float) {
        val newPoints = points.filter {
            it.x >= seconds
        }
        points.clear()
        points.addAll(newPoints)
    }

    fun trimBack(seconds: Float) {
        val value = points.last().x - seconds
        val newPoints = points.filter {
            it.x <= value
        }
        points.clear()
        points.addAll(newPoints)
    }

    fun trim(secondsFront: Float = 1.0f, secondsBack: Float = 1.0f) {
        Timber.d(points.last().x.toString())
        trimFront(secondsFront)
        Timber.d(points.last().x.toString())
        trimBack(secondsBack)
    }

    fun zeroCrosses(): Int {
        var lastPointIsPositive = points.first().y > 0
        var crosses = 0
        points.forEach {
            val isPositive = it.y > 0
            if (lastPointIsPositive != isPositive) {
                lastPointIsPositive = isPositive
                crosses++
            }
        }
        return crosses
    }
}

class ListOfVectors {

    val vectors = arrayListOf<Vector>()
    var points = arrayListOf<Point>()

    val isEmpty
        get() = vectors.isEmpty()
    val isNotEmpty
        get() = !vectors.isEmpty()

    val size: Int
        get() = vectors.size

    fun add(x: Float, y: Float, z: Float, t: Float) {
        vectors.add(Vector(x, y, z, t))
    }

    fun applyMeanOffset() {
        points.clear()
        var xSum = 0.0
        var ySum = 0.0
        var zSum = 0.0
        val pointsToReturn = arrayListOf<Point>()
        vectors.forEach {
            xSum += it.x
            ySum += it.y
            zSum += it.z
        }
        vectors.forEach {
            points.add(Point(it.t, sqrt((it.x - (xSum / vectors.size)).pow(2) +
                    (it.y - (ySum / vectors.size)).pow(2) +
                    (it.z - (zSum / vectors.size)).pow(2)).toFloat()))
        }
    }

    fun movmean(step: Int = 100, pass: Int = 1) {
        repeat(pass) {
            points.windowed(step) { points ->
                var sum = 0.0f
                points.forEach {
                    sum += it.y
                }
                val average = sum / points.size
                points.forEach {
                    it.y = average
                }

            }
        }
    }

    fun countPeaks(): Int {
        applyMeanOffset()
        movmean(20, 6)

        val maxima = arrayListOf<Point>()
        points.windowed(3) {
            if(it[0].y < it[1].y && it[1].y > it[2].y) {
                maxima.add(it[1])
            }
        }

        //Filter by threshold
        return maxima.filter {
            it.y > 2
        }.size
    }

    fun toSeries(): LineGraphSeries<DataPoint> {
        if (points.size > 0) {
            val data = ArrayList<DataPoint>()
            points.forEach {
                data.add(it.toDataPoint())
            }
            return LineGraphSeries(data.toTypedArray())
        }
        return LineGraphSeries()
    }

    fun trimFront(seconds: Float) {
        val newPoints = points.filter {
            it.x >= seconds
        }
        points.clear()
        points.addAll(newPoints)
    }

    fun trimBack(seconds: Float) {
        val value = points.last().x - seconds
        val newPoints = points.filter {
            it.x <= value
        }
        points.clear()
        points.addAll(newPoints)
    }

    fun trim(secondsFront: Float = 1.0f, secondsBack: Float = 1.0f) {
        Timber.d(points.last().x.toString())
        trimFront(secondsFront)
        Timber.d(points.last().x.toString())
        trimBack(secondsBack)
    }

    fun zeroCrosses(): Int {
        var lastPointIsPositive = points.first().y > 0
        var crosses = 0
        points.forEach {
            val isPositive = it.y > 0
            if (lastPointIsPositive != isPositive) {
                lastPointIsPositive = isPositive
                crosses++
            }
        }
        return crosses
    }
}

class Point(var x: Float, var y: Float) {
    fun toDataPoint() = DataPoint(x.toDouble(), y.toDouble())

}

class Vector(var x: Float, var y: Float, var z: Float, val t: Float) {

}

fun <T : DataPointInterface> LineGraphSeries<T>.setColorVale(color: Int): LineGraphSeries<T> {
    this.color = color
    return this
}