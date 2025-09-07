package com.example.earthquake

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import org.json.JSONObject
import org.tensorflow.lite.Interpreter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), SensorEventListener {

    // ... (All your existing variables are the same) ...
    private lateinit var tvStatus: TextView
    private lateinit var tvPrediction: TextView
    private lateinit var btnToggleService: Button
    private var isMonitoring = false
    private var isPhoneStationary = false
    private var isOnCooldown = false
    private lateinit var sensorManager: SensorManager
    private lateinit var tfliteInterpreter: Interpreter
    private val accelDataWindow = mutableListOf<FloatArray>()
    private val gyroDataWindow = mutableListOf<FloatArray>()
    private var scalingMean: FloatArray? = null
    private var scalingScale: FloatArray? = null
    private val PRE_TRIGGER_THRESHOLD = 0.5f
    private val ENERGY_THRESHOLD = 0.35f
    private val TRIGGER_THRESHOLD = 0.1f
    private val stillnessThreshold = 0.15f
    private val TAG = "EarthquakeAppFinal"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        tvPrediction = findViewById(R.id.tvPrediction)
        btnToggleService = findViewById(R.id.btnToggleService)
        // You may still want notification permission for a real app
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        setupModelAndScaler()
        btnToggleService.setOnClickListener { toggleMonitoring() }
    }

    // --- NEW: saveDataToFile now saves to the app's internal directory ---
    private fun saveDataToFile(data: String) {
        val timestamp = System.currentTimeMillis()
        val fileName = "tremor_data_$timestamp.txt"

        // Get the path to the app's private external files directory
        val file = File(getExternalFilesDir(null), fileName)

        try {
            FileOutputStream(file).use {
                it.write(data.toByteArray())
            }
            // The file path is logged so you know exactly where to find it
            Log.d(TAG, "✅ Data successfully saved to: ${file.absolutePath}")
            runOnUiThread { Toast.makeText(this, "Data file saved!", Toast.LENGTH_SHORT).show() }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error saving data to file", e)
        }
    }

    // --- The rest of your code remains exactly the same ---
    private fun runPrediction() {
        if (accelDataWindow.size < 200 || gyroDataWindow.size < 200) return

        val inputData = Array(200) { i ->
            floatArrayOf(
                accelDataWindow[i][0], accelDataWindow[i][1], accelDataWindow[i][2],
                gyroDataWindow[i][0], gyroDataWindow[i][1], gyroDataWindow[i][2]
            )
        }

        // --- Log RAW Data (as before) ---
        val rawDataString = inputData.joinToString(separator = ",") { it.joinToString(",") }
        Log.d(TAG, "RAW_DATA_FOR_PYTHON: $rawDataString")

        // --- Apply the scaling ---
        val scaledInputData = scaleInputData(inputData)

        // --- NEW: Log SCALED Data ---
        val scaledDataString = scaledInputData.joinToString(separator = ",") { it.joinToString(",") }
        Log.d(TAG, "SCALED_DATA_FROM_APP: $scaledDataString")

        // ----------------------------

        val inputBuffer = ByteBuffer.allocateDirect(1 * 200 * 6 * 4).apply {
            order(ByteOrder.nativeOrder())
            for (i in 0 until 200) {
                for (j in 0 until 6) {
                    putFloat(scaledInputData[i][j])
                }
            }
            rewind()
        }

        val outputBuffer = Array(1) { FloatArray(4) }
        tfliteInterpreter.run(inputBuffer, outputBuffer)
        val predictionIndex = outputBuffer[0].indices.maxByOrNull { outputBuffer[0][it] } ?: -1

        val tremorType = when (predictionIndex) {
            0 -> "No Tremor"; 1 -> "Mild Tremor"; 2 -> "Moderate Tremor"; 3 -> "Strong Tremor"; else -> "Unknown"
        }
        runOnUiThread {
            tvPrediction.text = "Prediction: $tremorType"
        }
        Log.d(TAG, "🧠 CNN Model Prediction: $tremorType (Class $predictionIndex)")
    }

    private fun setupModelAndScaler() {
        try {
            val model = loadModelFile("earthquake_cnn_model_final_v2.tflite")
            tfliteInterpreter = Interpreter(model)
            val jsonString = assets.open("scaling_values_final_v2.json").bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val meanArray = jsonObject.getJSONArray("mean")
            val scaleArray = jsonObject.getJSONArray("scale")
            scalingMean = FloatArray(meanArray.length()) { meanArray.getDouble(it).toFloat() }
            scalingScale = FloatArray(scaleArray.length()) { scaleArray.getDouble(it).toFloat() }
            Log.d(TAG, "✅ Model and Scaler loaded successfully.")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error loading model or scaler", e)
            tvStatus.text = "Error: Model/Scaler not found"
        }
    }

    private fun loadModelFile(fileName: String): ByteBuffer {
        val assetFileDescriptor = assets.openFd(fileName)
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun scaleInputData(data: Array<FloatArray>): Array<FloatArray> {
        // Get the scaling parameters, or return the original data if they're not loaded yet
        val mean = scalingMean ?: return data
        val scale = scalingScale ?: return data

        // Safety check: ensure the scaler has the correct number of features (6)
        if (mean.size != 6 || scale.size != 6) {
            Log.e(TAG, "Scaler has the wrong number of features! Expected 6.")
            return data
        }

        // Create a new array for the scaled data to avoid modifying the original
        val scaledData = Array(200) { FloatArray(6) }

        // Loop through each of the 200 timesteps
        for (i in 0 until 200) {
            // Loop through each of the 6 sensor features (accX, accY, accZ, gyroX, gyroY, gyroZ)
            for (j in 0 until 6) {
                // Apply the formula: (value - mean) / scale
                scaledData[i][j] = (data[i][j] - mean[j]) / scale[j]
            }
        }

        return scaledData
    }

    private fun toggleMonitoring() {
        isMonitoring = !isMonitoring
        if (isMonitoring) { startMonitoring(); btnToggleService.text = "Stop Monitoring"; tvStatus.text = "Status: Monitoring..."
        } else { stopMonitoring(); btnToggleService.text = "Start Monitoring"; tvStatus.text = "Status: Service Stopped" }
    }

    private fun startMonitoring() {
        accelDataWindow.clear(); gyroDataWindow.clear()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    private fun stopMonitoring() {
        if (::sensorManager.isInitialized) { sensorManager.unregisterListener(this) }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (!isMonitoring || event == null) return
        if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            val values = floatArrayOf(event.values[0], event.values[1], event.values[2])
            accelDataWindow.add(values)
            if (accelDataWindow.size > 200) accelDataWindow.removeAt(0)
            checkTrigger(values)
            checkStillness()
        } else if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            gyroDataWindow.add(event.values.clone())
            if (gyroDataWindow.size > 200) gyroDataWindow.removeAt(0)
        }
    }

    private fun checkStillness() {
        if (isOnCooldown) return
        if (accelDataWindow.size < 100) return
        val recentData = accelDataWindow.takeLast(100)
        val xVar = calculateVariance(recentData.map { it[0] })
        val yVar = calculateVariance(recentData.map { it[1] })
        val zVar = calculateVariance(recentData.map { it[2] })
        val wasPreviouslyStationary = isPhoneStationary
        isPhoneStationary = xVar < stillnessThreshold && yVar < stillnessThreshold && zVar < stillnessThreshold
        if (isPhoneStationary && !wasPreviouslyStationary) {
            Log.d(TAG, "✅ Phone is now STATIONARY.")
            runOnUiThread { tvPrediction.text = "Prediction: No Tremor" }
        } else if (!isPhoneStationary && wasPreviouslyStationary) {
            Log.d(TAG, "MOVING. Phone is no longer stationary.")
        }
    }

    private fun checkTrigger(values: FloatArray) {
        val absoluteAccel = sqrt(values[0].pow(2) + values[1].pow(2) + values[2].pow(2))
        if (isPhoneStationary && !isOnCooldown && absoluteAccel > TRIGGER_THRESHOLD && accelDataWindow.size == 200 && gyroDataWindow.size == 200) {
            Log.d(TAG, "🚀 TRIGGER CONDITIONS MET! Running prediction...")
            runPrediction()
            startCooldown()
        }
    }

    private fun startCooldown() {
        isOnCooldown = true
        Log.d(TAG, "❄️ Cooldown started (3 seconds).")
        Handler(Looper.getMainLooper()).postDelayed({
            isOnCooldown = false
            Log.d(TAG, "✅ Cooldown finished.")
        }, 3000)
    }

    private fun calculateVariance(data: List<Float>): Double {
        if (data.size < 2) return 0.0
        val mean = data.average()
        return data.sumOf { (it.toDouble() - mean) * (it.toDouble() - mean) } / data.size
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    override fun onDestroy() {
        super.onDestroy()
        stopMonitoring()
        tfliteInterpreter.close()
    }
    private fun Float.pow(n: Int): Float = this.toDouble().pow(n).toFloat()
}