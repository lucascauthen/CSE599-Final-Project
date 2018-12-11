package com.example.admin.padometer

/*
var bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
        AudioFormat.CHANNEL_IN_DEFAULT,
        AudioFormat.ENCODING_PCM_16BIT)
if (bufferSize == AudioTrack.ERROR || bufferSize == AudioTrack.ERROR_BAD_VALUE) {
    bufferSize = SAMPLE_RATE * 2
}
val player = AudioTrack.Builder()
        .setAudioAttributes(AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build())
        .setAudioFormat(AudioFormat.Builder()
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setSampleRate(SAMPLE_RATE)
                .setChannelMask(AudioFormat.CHANNEL_IN_DEFAULT)
                .build())
        .setBufferSizeInBytes(bufferSize)
        .setTransferMode(AudioTrack.MODE_STREAM)
        .build()
player.play()

val buffsize = AudioTrack.getMinBufferSize(SAMPLE_RATE, 4, 2)
val audioTrack = AudioTrack(3, SAMPLE_RATE, 4, 2, buffsize, 1)
audioTrack.play()

val amp = 10000
val twopi = 8.0 * Math.atan(1.0)
var ph = 0.0

for (i in 0 until buffsize) {
    samples[i] = (amp.toDouble() * Math.sin(ph)).toInt().toShort()
    ph += twopi * 20000 / SAMPLE_RATE.toDouble()
}

for (i in 0 until numSamples) {
    val frequency = frequencyToneStart + (i / numSamples.toFloat()) * (frequencyToneEnd - frequencyToneStart)
    samples[i] = (Short.MAX_VALUE * Math.sin(2 * Math.PI * i * (20000 / SAMPLE_RATE))).toShort() //Produces value between -1..1
}

val ramp = 0//numSamples / 20

for (i in 0 until ramp) {
    // scale to maximum amplitude
    val `val` = (samples[i] * Short.MAX_VALUE * i / ramp.toFloat()).toShort()
    // in 16 bit wav PCM, first byte is the low order byte
    //generatedSnd[indexInOutput++] = (`val` and 0x00ff).toByte()
    //generatedSnd[indexInOutput++] = (`val` and 0xff00.toShort()).toInt().ushr(8).toByte()
}

for (i in ramp until numSamples - ramp) {
    // scale to maximum amplitude
    val `val` = (samples[i] * Short.MAX_VALUE).toShort()
    // in 16 bit wav PCM, first byte is the low order byte
    //generatedSnd[indexInOutput++] = (`val` and 0x00ff).toByte()
    //generatedSnd[indexInOutput++] = (`val` and 0xff00.toShort()).toInt().ushr(8).toByte()
}

for (i in numSamples - ramp until numSamples) {
    // scale to maximum amplitude
    val `val` = (samples[i] * Short.MAX_VALUE * (numSamples - i) / ramp.toFloat()).toShort()
    // in 16 bit wav PCM, first byte is the low order byte
    //generatedSnd[indexInOutput++] = (`val` and 0x00ff).toByte()
    //generatedSnd[indexInOutput++] = (`val` and 0xff00.toShort()).toInt().ushr(8).toByte()
}

for (i in 0 until waitSamplesShort) {
    generatedSilenceShort[i] = 0
}

for (i in 0 until waitSamplesLong) {
    generatedSilenceLong[i] = 0
}
while (status == Status.RECORDING) {
    repeat(4) {
        audioTrack.write(samples, 0, samples.size)
        audioTrack.write(generatedSilenceShort, 0, generatedSilenceShort.size)
    }
    audioTrack.write(samples, 0, samples.size, WRITE_BLOCKING)
    audioTrack.write(generatedSilenceLong, 0, generatedSilenceLong.size)
}

player.stop()
player.release()
*/