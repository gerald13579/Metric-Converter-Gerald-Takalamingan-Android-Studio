package com.example.metricconverter

import android.os.Bundle
import android.view.View // Tambahkan ini
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerFromMetric: Spinner
    private lateinit var spinnerFromUnit: Spinner
    private lateinit var spinnerToUnit: Spinner
    private lateinit var etInputValue: EditText
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi komponen UI
        spinnerFromMetric = findViewById(R.id.spinnerFromMetric)
        spinnerFromUnit = findViewById(R.id.spinnerFromUnit)
        spinnerToUnit = findViewById(R.id.spinnerToUnit)
        etInputValue = findViewById(R.id.etInputValue)
        tvResult = findViewById(R.id.tvResult)

        // Daftar metric untuk dropdown "Pilih Metrik"
        val metrics = listOf("Panjang", "Massa", "Suhu")
        val adapterMetrics = ArrayAdapter(this, android.R.layout.simple_spinner_item, metrics)
        adapterMetrics.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFromMetric.adapter = adapterMetrics

        // Handle saat metrik dipilih
        spinnerFromMetric.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (metrics[position]) {
                    "Panjang" -> setUnits(listOf("Meter", "Kilometer", "Centimeter"))
                    "Massa" -> setUnits(listOf("Gram", "Kilogram", "Miligram"))
                    "Suhu" -> setUnits(listOf("Celcius", "Fahrenheit", "Kelvin"))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Konversi nilai saat ada input
        etInputValue.setOnEditorActionListener { _, _, _ ->
            convertValue()
            true
        }
    }

    // Fungsi untuk mengatur satuan yang ditampilkan di dropdown
    private fun setUnits(units: List<String>) {
        val adapterUnits = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        adapterUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFromUnit.adapter = adapterUnits
        spinnerToUnit.adapter = adapterUnits
    }

    // Fungsi untuk konversi nilai
    private fun convertValue() {
        val fromUnit = spinnerFromUnit.selectedItem.toString()
        val toUnit = spinnerToUnit.selectedItem.toString()
        val inputText = etInputValue.text.toString()

        // Cek apakah input berupa angka
        if (inputText.isEmpty()) {
            Toast.makeText(this, "Masukkan nilai yang valid", Toast.LENGTH_SHORT).show()
            return
        }

        val inputValue = inputText.toDoubleOrNull()
        if (inputValue == null) {
            Toast.makeText(this, "Nilai harus numerik", Toast.LENGTH_SHORT).show()
            return
        }

        val result = when (spinnerFromMetric.selectedItem.toString()) {
            "Panjang" -> convertLength(inputValue, fromUnit, toUnit)
            "Massa" -> convertMass(inputValue, fromUnit, toUnit)
            "Suhu" -> convertTemperature(inputValue, fromUnit, toUnit)
            else -> null
        }

        if (result != null) {
            tvResult.text = "Hasil: $result"
        } else {
            Toast.makeText(this, "Konversi tidak valid", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi konversi untuk Panjang
    private fun convertLength(value: Double, fromUnit: String, toUnit: String): Double? {
        val meters = when (fromUnit) {
            "Meter" -> value
            "Kilometer" -> value * 1000
            "Centimeter" -> value / 100
            else -> return null
        }
        return when (toUnit) {
            "Meter" -> meters
            "Kilometer" -> meters / 1000
            "Centimeter" -> meters * 100
            else -> null
        }
    }

    // Fungsi konversi untuk Massa
    private fun convertMass(value: Double, fromUnit: String, toUnit: String): Double? {
        val grams = when (fromUnit) {
            "Gram" -> value
            "Kilogram" -> value * 1000
            "Miligram" -> value / 1000
            else -> return null
        }
        return when (toUnit) {
            "Gram" -> grams
            "Kilogram" -> grams / 1000
            "Miligram" -> grams * 1000
            else -> null
        }
    }

    // Fungsi konversi untuk Suhu
    private fun convertTemperature(value: Double, fromUnit: String, toUnit: String): Double? {
        val celsius = when (fromUnit) {
            "Celcius" -> value
            "Fahrenheit" -> (value - 32) * 5 / 9
            "Kelvin" -> value - 273.15
            else -> return null
        }
        return when (toUnit) {
            "Celcius" -> celsius
            "Fahrenheit" -> (celsius * 9 / 5) + 32
            "Kelvin" -> celsius + 273.15
            else -> null
        }
    }
}
