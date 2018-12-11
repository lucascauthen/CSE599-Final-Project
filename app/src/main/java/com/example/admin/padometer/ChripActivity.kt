package com.example.admin.padometer

import android.media.AudioTrack
import android.media.AudioManager
import android.os.Bundle
import android.app.Activity
import android.media.AudioFormat
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.experimental.and
import android.media.AudioAttributes


class PlaySound : Activity() {


    internal var handler = Handler()

    var status = Status.STOPPED

    enum class Status {
        PLAYING, STOPPED
    }

    var playingThread = Runnable {
        val SAMPLE_FREQUENCY = 48000
        val duration = 0.00175 * 1 // seconds
        val waitTime = 0.00417 * 2
        val numSamples = (SAMPLE_FREQUENCY * duration).toInt()

        val frequencyToneStart = 18000
        val frequencyToneEnd = 20000
        val player = AudioTrack.Builder()
                .setAudioAttributes(AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build())
                .setAudioFormat(AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_FREQUENCY)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build())
                .setBufferSizeInBytes(numSamples)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()
        player.play()
        val generatedSnd = ByteArray(2 * numSamples)
        val waitSamples : Int = (SAMPLE_FREQUENCY * waitTime).toInt()
        val generatedSilence = ByteArray(2 * waitSamples)
        val samples = DoubleArray(numSamples)
        for (i in 0 until numSamples) {
            val frequency = frequencyToneStart + (i / numSamples.toFloat()) * (frequencyToneEnd - frequencyToneStart)
            samples[i] = Math.sin(2 * Math.PI * i / (SAMPLE_FREQUENCY / frequency)) //Produces value between -1..1
        }
        var indexInOutput = 0
        val ramp = numSamples / 2

        for (i in 0 until ramp) {
            // scale to maximum amplitude
            val `val` = (samples[i] * Short.MAX_VALUE * i / ramp.toFloat()).toShort()
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[indexInOutput++] = (`val` and 0x00ff).toByte()
            generatedSnd[indexInOutput++] = (`val` and 0xff00.toShort()).toInt().ushr(8).toByte()
        }

        for (i in ramp until numSamples - ramp) {
            // scale to maximum amplitude
            val `val` = (samples[i] * Short.MAX_VALUE).toShort()
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[indexInOutput++] = (`val` and 0x00ff).toByte()
            generatedSnd[indexInOutput++] = (`val` and 0xff00.toShort()).toInt().ushr(8).toByte()
        }

        for (i in numSamples - ramp until numSamples) {
            // scale to maximum amplitude
            val `val` = (samples[i] * Short.MAX_VALUE * (numSamples - i) / ramp.toFloat()).toShort()
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[indexInOutput++] = (`val` and 0x00ff).toByte()
            generatedSnd[indexInOutput++] = (`val` and 0xff00.toShort()).toInt().ushr(8).toByte()
        }

        for (i in 0 until waitSamples) {
            generatedSilence[i] = 0
        }
        while (status == Status.PLAYING) {
            player.write(generatedSnd, 0, generatedSnd.size)
            player.write(generatedSilence, 0, generatedSilence.size)
        }
        player.stop()
        player.release()
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        statusButton.setOnClickListener {
            when (status) {
                Status.PLAYING -> {
                    status = Status.STOPPED
                    statusButton.text = "Start"
                }
                Status.STOPPED -> {
                    status = Status.PLAYING
                    Thread(playingThread).start()
                    statusButton.text = "Stop"
                }
            }
        }
    }
}