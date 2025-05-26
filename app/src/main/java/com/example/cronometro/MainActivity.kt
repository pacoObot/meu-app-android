package com.example.cronometro

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

class MainActivity : AppCompatActivity() {

    private var timeSelected: Int = 0
    private var timeCountDown: CountDownTimer? = null
    private var timeProgress = 0
    private var pauseOffset: Long = 0
    private var isRunning = false

    private lateinit var tvTempo: TextView
    private lateinit var temporstante: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnPlayPause: Button
    private lateinit var btnReset: ImageButton
    private lateinit var btnAdd: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_main)

        tvTempo = findViewById(R.id.tvTempo)
        temporstante = findViewById(R.id.temporstante)
        progressBar = findViewById(R.id.pbTempo)
        btnPlayPause = findViewById(R.id.btPlayPause)
        btnReset = findViewById(R.id.ib_reset)
        btnAdd = findViewById(R.id.btnAdd)

        // Simulando uma "API mockada" com atraso
        Handler().postDelayed({
            Toast.makeText(applicationContext, "Dados carregados com sucesso!", Toast.LENGTH_SHORT).show()
        }, 2000)

        btnAdd.setOnClickListener {
            abrirDialogoTempo()
        }

        btnPlayPause.setOnClickListener {
            if (timeSelected > 0) {
                if (isRunning) {
                    pausarTemporizador()
                } else {
                    iniciarTemporizador()
                }
            } else {
                Toast.makeText(this, "Adiciona tempo primeiro!", Toast.LENGTH_SHORT).show()
            }
        }

        btnReset.setOnClickListener {
            reiniciarTemporizador()
        }
    }

    private fun abrirDialogoTempo() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.add_dialog)

        val etGetTime = dialog.findViewById<EditText>(R.id.etGetTime)
        val btnOk = dialog.findViewById<Button>(R.id.btnOk)

        btnOk.setOnClickListener {
            val tempoTexto = etGetTime.text.toString()
            if (tempoTexto.isNotEmpty()) {
                timeSelected = tempoTexto.toInt()
                timeProgress = 0
                progressBar.isIndeterminate = false
                progressBar.max = timeSelected
                progressBar.progress = timeSelected
                tvTempo.text = timeSelected.toString()
                temporstante.text = "segundos restantes"
                btnPlayPause.text = "Iniciar"
                pauseOffset = 0
                isRunning = false
                timeCountDown?.cancel()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Insira o tempo", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun iniciarTemporizador() {
        timeCountDown = object : CountDownTimer(((timeSelected - timeProgress) * 1000L), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeProgress++
                val tempoRestante = timeSelected - timeProgress
                tvTempo.text = tempoRestante.toString()
                progressBar.progress = tempoRestante
            }

            override fun onFinish() {
                tvTempo.text = "0"
                progressBar.progress = 0
                isRunning = false
                btnPlayPause.text = "Iniciar"
                Toast.makeText(this@MainActivity, "Tempo terminado!", Toast.LENGTH_SHORT).show()
            }
        }.start()

        isRunning = true
        btnPlayPause.text = "Pausar"
    }

    private fun pausarTemporizador() {
        timeCountDown?.cancel()
        isRunning = false
        btnPlayPause.text = "Continuar"
    }

    private fun reiniciarTemporizador() {
        timeCountDown?.cancel()
        timeProgress = 0
        isRunning = false
        progressBar.progress = timeSelected
        tvTempo.text = timeSelected.toString()
        btnPlayPause.text = "Iniciar"
    }
}