package com.example.admin.padometer

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.LENGTH_INDEFINITE
import android.support.v7.app.AppCompatActivity
import com.opencsv.CSVReader
import kotlinx.android.synthetic.main.activity_graph.*
import timber.log.Timber
import java.io.File
import java.io.FileReader
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.LabelFormatter
import java.text.NumberFormat


class GraphActivity : AppCompatActivity() {

    var numberofSteps: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        intent?.getIntExtra(NUMBER_OF_STEPS, 0)?.let {
            numberofSteps = it
        }
        buildGraph()
    }

    fun buildGraph() {
        val reader = CSVReader(FileReader(File(filesDir, "accelerometer.csv").absoluteFile))

        val acc = ListOfVectors()

        val startTime: Double = reader.readNext()[0].toDouble()
        reader.forEach {
            acc.add(it[1].toFloat(), it[2].toFloat(), it[3].toFloat(), ((it[0].toFloat() - startTime) / 1e9).toFloat())
        }
        Timber.d("${acc.size} points!")


        val peaks = acc.countPeaks()

        Timber.d("Peaks: $peaks")
        stepCount.text = "Step Count: $peaks"

        graph.addSeries(acc.toSeries().setColorVale(Color.GREEN))

        graph.viewport.isScalable = true
        graph.viewport.setScalableY(true)
        graph.viewport.setMinX(0.0)
        val nf = NumberFormat.getInstance()

        nf.minimumFractionDigits = 2
        nf.minimumIntegerDigits = 1

        graph.gridLabelRenderer.labelFormatter = DefaultLabelFormatter(nf, nf) as LabelFormatter?
        graph.gridLabelRenderer.isHorizontalLabelsVisible = true
    }
}