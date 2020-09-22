package com.amebaownd.pikohan_niwatori.recordsoundandtext.ui.home

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amebaownd.pikohan_niwatori.recordsoundandtext.data.Constants
import com.amebaownd.pikohan_niwatori.recordsoundandtext.data.Constants.ONE_FRAME_DATA_COUNT
import com.amebaownd.pikohan_niwatori.recordsoundandtext.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(private val repository: Repository) : ViewModel(), AudioRecord.OnRecordPositionUpdateListener {

    private var _startStopButtonText = MutableLiveData<String>("START")
    val startStopButtonText : LiveData<String> = _startStopButtonText

    private var _readText = MutableLiveData<String>("------------")
    val readText : LiveData<String> = _startStopButtonText

    private var isRecording = true
    private var fileName = ""
    private lateinit var recordFile: File
    private val audioBufferSizeInByte = Math.max(
        ONE_FRAME_DATA_COUNT,
        AudioRecord.getMinBufferSize(
            Constants.SAMPLING_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        ))
    private var mAudioRecord: AudioRecord? = null
    private var buffer = ShortArray(audioBufferSizeInByte)

    fun start(id:String) {
        mAudioRecord =
            AudioRecord(
                MediaRecorder.AudioSource.MIC,
                Constants.SAMPLING_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                audioBufferSizeInByte
            )
        Log.d("audio setting","ONE_FRAME_DATA_COUNT: ${Constants.ONE_FRAME_DATA_COUNT}  audioBufferSizeInByte: ${audioBufferSizeInByte}")

        try{
            val df = SimpleDateFormat("yyy.MM.dd-HH.mm.ss.SSS")
            this.fileName = df.format(Date(System.currentTimeMillis())) + "_${id}.txt"
            this.recordFile =
                File(Environment.getExternalStorageDirectory().path + "/" + this.fileName)
            this.recordFile.createNewFile()
            this.recordFile.setReadable(true,false)
            this.recordFile.setWritable(true,true)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mAudioRecord?.let {
            it.positionNotificationPeriod = audioBufferSizeInByte
            it.setRecordPositionUpdateListener(this)
            it.startRecording()
            it.read(buffer,0,audioBufferSizeInByte)
        }
    }

    override fun onPeriodicNotification(recorder: AudioRecord) {
        recorder.read(buffer, 0, audioBufferSizeInByte)
        if(isRecording) {
            viewModelScope.launch(Dispatchers.Default) {
                val readBuffer = buffer.copyOf()
                val df = SimpleDateFormat("yyy.MM.dd-HH.mm.ss.SSS")
                writeFile(df.format(Date(System.currentTimeMillis())), readBuffer)
            }
        }
    }

    private fun writeFile(date_str:String,str:ShortArray){
        val message = StringBuilder()
        message.append("$date_str,")
        str.forEach {
            message.append(it.toString())
        }
        message.append(str)
        val sendMessage = message.substring(0,message.length-1)
        try{
            FileOutputStream(this.recordFile, true).use { outputStream ->
                OutputStreamWriter(outputStream).use { outputStreamWriter ->
                    BufferedWriter(outputStreamWriter).use { bw ->
                        bw.write(sendMessage + "\r\n")
                        bw.flush()
                        Log.d("save file", sendMessage)
                    }
                }
            }
        }catch (e:IOException){
            e.printStackTrace()
        }
    }

    override fun onCleared() {
        super.onCleared()
        isRecording = false
        mAudioRecord?.let {
            it.stop()
            it.release()
        }
        mAudioRecord = null
    }

    fun onStartStopButtonClicked(){
        isRecording = !isRecording
        if(isRecording){
            _startStopButtonText.value = "STOP"
        }else{
            _startStopButtonText.value = "START"
        }
    }

    fun onRetakeButtonClicked(){
        if(isRecording){
            isRecording = false
        }
    }

    fun onSkipButtonClicked(){
        if(isRecording){
            isRecording = false
        }
    }

    override fun onMarkerReached(p0: AudioRecord?) {
    }
}