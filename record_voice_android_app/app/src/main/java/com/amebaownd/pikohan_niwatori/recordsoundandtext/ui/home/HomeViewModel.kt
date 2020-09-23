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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(private val repository: Repository) : ViewModel(),
    AudioRecord.OnRecordPositionUpdateListener {

    private var _startStopButtonText = MutableLiveData<String>("START")
    val startStopButtonText: LiveData<String> = _startStopButtonText

    private var _readText = MutableLiveData<String>("------------")
    val readText: LiveData<String> = _readText

    private var startTime = ""
    private var recordSoundArray = shortArrayOf()
    private var count = 0
    private var kanaArray = JSONArray()
    private var kanjiArray = JSONArray()
    private var romaArray = JSONArray()

    private var isRecording = false
    private var fileName = ""
    private lateinit var recordFile: File
    private val audioBufferSizeInByte = Math.max(
        ONE_FRAME_DATA_COUNT,
        AudioRecord.getMinBufferSize(
            Constants.SAMPLING_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
    )
    private var mAudioRecord: AudioRecord? =
        AudioRecord(
            MediaRecorder.AudioSource.MIC,
            Constants.SAMPLING_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            audioBufferSizeInByte
        )
    private var buffer = ShortArray(audioBufferSizeInByte)

    fun start(id: String) {

        Log.d(
            "audio setting",
            "ONE_FRAME_DATA_COUNT: ${Constants.ONE_FRAME_DATA_COUNT}  audioBufferSizeInByte: ${audioBufferSizeInByte}"
        )

        try {
            val df = SimpleDateFormat("yyy.MM.dd-HH.mm.ss.SSS")
            this.fileName = df.format(Date(System.currentTimeMillis())) + "_${id}.txt"
            this.recordFile =
                File(Environment.getExternalStorageDirectory().path + "/" + this.fileName)
            this.recordFile.createNewFile()
            this.recordFile.setReadable(true, false)
            this.recordFile.setWritable(true, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mAudioRecord?.let {
            it.positionNotificationPeriod = audioBufferSizeInByte
            it.setRecordPositionUpdateListener(this@HomeViewModel)
            it.startRecording()
            it.read(buffer, 0, audioBufferSizeInByte)
        }

        readFile()
        readNext()
    }

    override fun onPeriodicNotification(recorder: AudioRecord) {
        recorder.read(buffer, 0, audioBufferSizeInByte)
        if (isRecording) {
            Log.d("record", "listening")
            viewModelScope.launch(Dispatchers.Default) {
                if (recordSoundArray.size == 0) {
                    startTime =
                        SimpleDateFormat("yyy.MM.dd-HH.mm.ss.SSS").format(Date(System.currentTimeMillis()))
                }
                recordSoundArray += buffer
            }
        }
    }

    private fun writeFile() {
        val message = StringBuilder()
        message.append("$startTime,")
        recordSoundArray.forEach {
            message.append(it.toString())
        }
        val sendMessage = message.substring(0, message.length - 1)
        try {
            FileOutputStream(this.recordFile, true).use { outputStream ->
                OutputStreamWriter(outputStream).use { outputStreamWriter ->
                    BufferedWriter(outputStreamWriter).use { bw ->
                        bw.write(sendMessage + "\r\n")
                        bw.flush()
                        Log.d("save file", "length : ${sendMessage.length}  message: $sendMessage")
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun readFile() {
        try {
            FileInputStream(File("/storage/emulated/0/merosu.json")).use { fileInputStream ->
                InputStreamReader(fileInputStream).use { inputStreamReader ->
                    BufferedReader(inputStreamReader).use {
                        val str = it.readText()
                        val jsonObject = JSONObject(str)
                        romaArray = jsonObject.getJSONArray("roma")
                        kanaArray = jsonObject.getJSONArray("kana")
                        kanjiArray = jsonObject.getJSONArray("kanji")
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun readNext(){
        _readText.value = kanjiArray.getString(count)+"\n"+kanaArray.getString(count)+"\n"+romaArray.getString(count)
        count += 1
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

    fun onStartStopButtonClicked() {
        isRecording = !isRecording
        if (isRecording) {
            _startStopButtonText.value = "STOP"
            this.recordSoundArray = shortArrayOf()
        } else {
            _startStopButtonText.value = "START"
            writeFile()
        }
    }

    fun onRetakeButtonClicked() {
        if (isRecording) {
            isRecording = false
            this.recordSoundArray = shortArrayOf()
            _startStopButtonText.value = "START"
        }
    }

    fun onSkipButtonClicked() {
        isRecording = false
        isRecording = false
        this.recordSoundArray = shortArrayOf()
        _startStopButtonText.value = "START"
        readNext()
    }

    override fun onMarkerReached(p0: AudioRecord?) {

    }
}