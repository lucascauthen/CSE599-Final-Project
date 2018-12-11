package com.example.admin.padometer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_step.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import timber.log.Timber
import java.util.*
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.math.max


class StepActivity : AppCompatActivity() {

    //Sensor information
    lateinit var sensorManager: SensorManager
    lateinit var accelerometer: Sensor
    lateinit var stepDetector: Sensor

    var status: StepTrackStatus = StepTrackStatus.WAITING

    @Volatile var stepCountInternal = 0
        set(value) {
            runOnUiThread {
                this.stepCountCustom.text = resources.getString(R.string.mine_steps, value)
            }
        }
    var stepCountValueAndroid = 0
        set(value) {
            runOnUiThread {
                this.stepCountAndroid.text = resources.getString(R.string.android_steps, value)
            }
        }

    val addStepListener = { numberOfSteps: Int ->
        Timber.d("Adding: $numberOfSteps Old: $stepCountInternal")
        stepCountInternal += numberOfSteps
    }

    val dataReceiver = DataReceiver(addStepListener, this)


    private val accelerometerListener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

        override fun onSensorChanged(ev: SensorEvent) {
            if (status == StepTrackStatus.RECORDING) {
                dataReceiver.add(ev.timestamp.toFloat(), ev.values[0], ev.values[1], ev.values[2])
            }
        }
    }

    private val stepListener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

        override fun onSensorChanged(ev: SensorEvent) {
            if (status == StepTrackStatus.RECORDING) {
                stepCountValueAndroid++
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        statusButton.setOnClickListener {
            when (status) {
                StepTrackStatus.RECORDING -> {
                    statusButton.setText(R.string.start)
                    status = StepTrackStatus.WAITING
                    dataReceiver.stop()
                }
                StepTrackStatus.WAITING -> {
                    statusButton.setText(R.string.stop)
                    status = StepTrackStatus.RECORDING
                    stepCountInternal = 0
                    stepCountValueAndroid = 0
                    dataReceiver.start()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST)
        sensorManager.registerListener(stepListener, stepDetector, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(accelerometerListener)
        sensorManager.unregisterListener(stepListener)
    }
}

enum class StepTrackStatus {
    WAITING,
    RECORDING
}

class DataReceiver(val addStepListener: (Int) -> Unit, val context: StepActivity) {
    var currentList = ListOfVectors()


    var isStopped = true
    val processQueue : Queue<ListOfVectors> = LinkedList<ListOfVectors>()
    var isProcessing = false
    var steps = 0

    fun add(t: Float, x: Float, y: Float, z: Float) {
        if (!isStopped) {
            currentList.add(x, y, z, t)
        }
    }

    fun tick() {
        val list = currentList
        if (list.isNotEmpty) {
            currentList = ListOfVectors()
            processQueue.add(list)
        }
    }

    fun process(list: ListOfVectors) {
        steps += max(0, list.countPeaks())
        context.stepCountInternal = steps
        isProcessing = false
    }

    fun start() {
        steps = 0
        isStopped = false
        launch(CommonPool + CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->
            Timber.e(throwable)
        }) {
            while (!isStopped) {
                delay(2000)
                tick()
            }
        }
        launch(CommonPool + CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->
            Timber.e(throwable)
        }) {
            while (!isStopped) {
                if(processQueue.isNotEmpty() && !isProcessing) {
                    isProcessing = true
                    process(processQueue.remove())
                }
            }
        }

    }

    fun stop() {
        isStopped = true
    }
}