package com.example.countercomposeatiqq

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel

class CounterViewModel(
    application: Application
) : AndroidViewModel(application) {
    // 1. Membuka/membuat file SharedPreferences
    private val prefs = application.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )
    // 2. State awal dibaca dari SharedPreferences (default 0)
    private val _counter = mutableIntStateOf(
        prefs.getInt(KEY_COUNTER, 0)
    )
    val counter: State<Int> = _counter

    // State untuk menampung teks yang sedang diketik pada form input "Nama"
    private val _nameInput = mutableStateOf("")
    val nameInput: State<String> = _nameInput

    // State untuk menyimpan nama yang tampil di bawah counter (dibaca dari SharedPreferences)
    private val _savedName = mutableStateOf(
        prefs.getString(KEY_NAME, "") ?: ""
    )
    val savedName: State<String> = _savedName

    // 3. Fungsi yang dipanggil dari UI
    fun increment() = updateCounter(_counter.intValue + 1)
    fun decrement() = updateCounter(_counter.intValue - 1)
    fun reset() = updateCounter(0)

    // Memperbarui state teks input setiap kali pengguna mengetik di form "Nama"
    fun onNameInputChange(newName: String) {
        _nameInput.value = newName
    }

    // Menyimpan isi form input ke state savedName & SharedPreferences saat tombol Simpan diklik
    fun saveName() {
        _savedName.value = _nameInput.value
        prefs.edit { putString(KEY_NAME, _nameInput.value) }
    }

    // 4. Ubah state lalu simpan ke SharedPreferences
    private fun updateCounter(newValue: Int) {
        _counter.intValue = newValue
        prefs.edit { putInt(KEY_COUNTER, newValue) }
    }
    companion object {
        private const val PREFS_NAME = "counter_prefs"
        private const val KEY_COUNTER = "counter_value"
        // Key SharedPreferences untuk menyimpan data nama
        private const val KEY_NAME = "saved_name"
    }
}