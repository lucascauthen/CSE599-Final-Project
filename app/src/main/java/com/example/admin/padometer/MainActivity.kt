package com.example.admin.padometer

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.media.*
import android.media.AudioRecord.READ_BLOCKING
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.DataPointInterface
import timber.log.Timber
import java.io.File
import com.paramsen.noise.Noise
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.send_dialog.*
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.*
import java.lang.Math.abs
import kotlin.collections.ArrayList
import com.jjoe64.graphview.series.LineGraphSeries




const val NUMBER_OF_STEPS = "number_of_steps"
const val WINDOWING_PRIORITY = 0
const val RECORDING_PRIORITY = 2000

//File Information
const val SAMPLE_RATE = 48000
const val PROCESSING_PERIOD: Float = 1.0f
const val WINDOW_SIZE = PROCESSING_PERIOD //Must be smaller than processing period, ideally should be a power of 2
const val PROCESSING_STEP_SIZE: Float = WINDOW_SIZE //Must be smaller than processing perIdeally should be a power of 2

class MainActivity : AppCompatActivity() {

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    val WRITE_TO_FILE = true
    val files: MutableList<File> = ArrayList()
    lateinit var chirpFile: File

    var status: Status = Status.WAITING
    private var counter = 0

    val duration = 0.00175 * 1 // seconds
    val waitTime = 0.00417 * 2
    val chripGap: Double = 0.5
    val numSamples = (SAMPLE_RATE * duration).toInt()

    val frequencyToneStart = 18000
    val frequencyToneEnd = 20000
    val generatedSnd = ShortArray(numSamples)
    val waitSamplesShort: Int = (SAMPLE_RATE * waitTime).toInt()
    val waitSamplesLong: Int = (SAMPLE_RATE * chripGap).toInt()
    val generatedSilenceShort = ShortArray(waitSamplesShort)
    val generatedSilenceLong = ShortArray(waitSamplesLong)
    val samples = ShortArray(numSamples)


    private val series: LineGraphSeries<DataPoint> = LineGraphSeries()

    val emailTask: (String) -> Unit = { name: String ->
        if (WRITE_TO_FILE) {
            val fOutChirp = FileOutputStream(chirpFile)
            val myOutWriterChirp = OutputStreamWriter(fOutChirp)
            generatedSnd.forEach {
                myOutWriterChirp.append(it.toString())
                myOutWriterChirp.append("\n")
            }
            myOutWriterChirp.close()
            fOutChirp.flush()
            fOutChirp.close()
        }
        for (i in 0 until files.size) {
            val newFile = File(filesDir, "$name$i.txt")
            files[i].renameTo(newFile)
            files[i] = newFile
        }
        //files.add(chirpFile)
        BackgroundMail.newBuilder(this)
                .withUsername("lucashctest@gmail.com")
                .withPassword("uwcse599n1")
                .withMailto("vloyko@uw.edu")
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject(name)
                .withBody(name)
                .withAttachments(*files.map { it.absolutePath }.toTypedArray())
                .withOnSuccessCallback {
                    this.toast("Success!")
                }
                .withOnFailCallback {
                    this.toast("Failure!")
                }
                .send()

    }
    var noise = Noise.real()
            .optimized()
            .init((WINDOW_SIZE * SAMPLE_RATE).toInt(), true)

    var playingThread = Runnable {
        val buffsize = (AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT))
        val audioTrack = AudioTrack(3, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffsize, 1)
        audioTrack.play()

        while (status == Status.RECORDING) {
            repeat(5) {
                val samples = ShortArray(((SAMPLE_RATE * duration).toInt()))
                for (i in 0 until samples.size) {
                    val frequency = frequencyToneStart + (i / samples.size.toFloat()) * (frequencyToneEnd - frequencyToneStart)
                    samples[i] = (Short.MAX_VALUE * Math.sin(2.0 * Math.PI * i / (SAMPLE_RATE / frequency))).toShort()
                }
                if (samples.size > buffsize) {
                    var index = 0
                    while (index < samples.size) {
                        audioTrack.write(samples.sliceArray(index until Math.min(samples.size, index + buffsize)), 0, buffsize)
                        index += buffsize
                    }
                } else {
                    audioTrack.write(samples, 0, samples.size)
                }
                audioTrack.write(ShortArray(waitSamplesShort), 0, waitSamplesShort)
            }
            //audioTrack.write(ShortArray(waitSamplesLong), 0, waitSamplesLong)
        }
        audioTrack.stop()
        audioTrack.release()
    }

    val dataQueue: PriorityQueue<ProcessingEntry> = PriorityQueue(compareBy(ProcessingEntry::priority))


    fun testFourier() {
        val samples = DoubleArray(SAMPLE_RATE)
        for(i in 0 until samples.size) {
            samples[i] = (Math.sin(2.0 * Math.PI * i / (SAMPLE_RATE / 1000.0)))
        }
        val results = fftnative(samples, samples.size)

        val a = results.maxIndex()!!

        Timber.d("Max frequency: $a")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        chirpFile = File(filesDir, "chirp.txt")

        testFourier()

        sendDataButton.background.mutate().alpha = (255 * 0.25).toInt()
        statusButton.setOnClickListener {
            when (status) {
                Status.WAITING -> {
                    statusButton.text = getString(R.string.stop)
                    status = Status.RECORDING
                    startRecording()
                }
                Status.RECORDING -> {
                    statusButton.text = getString(R.string.start)
                    sendDataButton.isEnabled = true
                    sendDataButton.background.mutate().alpha = 255
                    status = Status.WAITING
                    counter++
                }
            }
        }
        sendDataButton.setOnClickListener {
            NameDialog(this, "recording", emailTask).show()
            counter = 0
            sendDataButton.isEnabled = false
            sendDataButton.background.mutate().alpha = (255 * 0.25).toInt()
        }
    }

    private fun startRecording() {
        Thread(playingThread).start()
        Thread(Runnable {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO)

            // buffer size in bytes
            var bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_FLOAT)

            if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
                bufferSize = SAMPLE_RATE * 2
            }

            val audioBuffer = FloatArray(bufferSize / 2)

            val record = AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_FLOAT,
                    bufferSize)

            if (record.state != AudioRecord.STATE_INITIALIZED) {
                Timber.d("Audio Record can't initialize!")
                return@Runnable
            }
            record.startRecording()

            Timber.d("Start recording")

            var shortsRead: Long = 0
            val processingBuffer = FloatArray((SAMPLE_RATE * PROCESSING_PERIOD).toInt())
            var processingBufferIndex = 0

            val recording = File(filesDir, "recording$counter.txt")
            val fOut = FileOutputStream(recording)
            val myOutWriter = OutputStreamWriter(fOut)

            while (status == Status.RECORDING) {
                val numberOfShort = record.read(audioBuffer, 0, audioBuffer.size, READ_BLOCKING)
                shortsRead += numberOfShort.toLong()
                for (it in audioBuffer) {
                    if (processingBufferIndex < processingBuffer.size) {
                        processingBuffer[processingBufferIndex++] = it
                    } else {
                        dataQueue.add(ProcessingEntry(processingBuffer.copyOf(), RECORDING_PRIORITY))
                        processingBufferIndex = 0
                    }
                }
                //Write to file
                if (WRITE_TO_FILE) {
                    audioBuffer.forEach {
                        myOutWriter.append(it.toString())
                        myOutWriter.append("\n")
                    }
                }
            }
            myOutWriter.close()
            fOut.flush()
            fOut.close()
            files.add(recording)
            record.stop()
            record.release()

            Timber.d(String.format("Recording stopped. Samples read: %d", shortsRead))
        }).start()
        Thread(dataProcessingRunnable).start()
    }

    external fun fftnative(data: DoubleArray, N: Int): DoubleArray
    external fun fftcomplexoutnative(data: DoubleArray, N: Int): Array<DoubleArray>
    external fun ifftnative(data: Array<DoubleArray>): DoubleArray


    private val dataProcessingRunnable = Runnable {
        val previousPeriodOverlap = FloatArray((SAMPLE_RATE * (WINDOW_SIZE - PROCESSING_STEP_SIZE)).toInt())
        var hasOverlap = false
        while (status == Status.RECORDING) {
            if (dataQueue.any()) {
                while (dataQueue.isNotEmpty()) {
                    val item = dataQueue.poll()
                    item?.let { item ->
                        if (item.priority < RECORDING_PRIORITY) { //Items that are from this thread
                            processData(item.fftData)
                        } else if (item.priority == RECORDING_PRIORITY) { //Items that are from the recording thread, in groups of size PROCESSING_PERIOD
                            //This section should split up the recording data into several chunks that are windowed
                            val data = if (hasOverlap) {
                                previousPeriodOverlap + item.data
                            } else {
                                item.data
                            }
                            var windowIndex = 0
                            val windowSize: Int = (SAMPLE_RATE * WINDOW_SIZE).toInt()
                            val step: Int = (SAMPLE_RATE * PROCESSING_STEP_SIZE).toInt()
                            val range: Int = data.size
                            var priority = WINDOWING_PRIORITY
                            while ((windowIndex + windowSize) < range) {
                                val window = DoubleArray(windowSize)
                                var j = 0
                                for (i in windowIndex until (windowIndex + windowSize)) {
                                    window[j++] = data[i].toDouble()
                                }
                                dataQueue.add(ProcessingEntry(priority = priority++, name = "$windowIndex-${windowIndex + windowSize}", fftData = window))
                                windowIndex += step
                            }
                            var j = 0
                            for (i in (range - windowSize + step) until range) {
                                previousPeriodOverlap[j++] = data[i]
                            }
                            hasOverlap = true
                        }
                    }
                }
            }
        }
    }

    fun processData(data: DoubleArray) {
        val results = fftnative(data, data.size)
        val a = results.maxIndex()
        Timber.d("Max: $a")
    }
}


enum class Status {
    RECORDING,
    WAITING
}

class ApplicationClass : Application() {
    init {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, msg, duration).show()
}

class NameDialog(context: Context, private val defaultName: String, private val onSendChecked: (filename: String) -> Unit) : Dialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.send_dialog)
        emailName.setText(defaultName)

        sendButton.setOnClickListener {
            this.dismiss()
            onSendChecked.invoke(emailName.text.toString())
        }
        cancelButton.setOnClickListener {
            cancel()
        }
    }
}


data class ProcessingEntry(val data: FloatArray = FloatArray(0), val priority: Int, val name: String = "", val fftData: DoubleArray = DoubleArray(0))


