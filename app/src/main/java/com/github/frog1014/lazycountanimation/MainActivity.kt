package com.github.frog1014.lazycountanimation

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.github.frog1014.lazycountanimation.lib.LazyCountAnimation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private var progressAnimation: LazyCountAnimation? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buildProgressAnimation()
        submit.setOnClickListener {
            progressAnimation?.stop()
            progress.text = startProgress.text.toString().takeIf(String::isNotBlank) ?: "0"
            target.setText("")
            buildProgressAnimation()
        }

        reset.setOnClickListener {
            progressAnimation?.stop()
            buildProgressAnimation()
            progress.text = "0"
            startProgress.setText("")
            // endProgress.setText("")
            target.setText("")
        }

        random.setOnClickListener {
            target.setText((1..1000).random().toString())
        }

        val handler = Handler()
        target.doOnTextChanged { text, _, _, _ ->
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                Log.d(TAG, "onCreate: doOnTextChanged")
                text?.toString()?.takeIf(String::isNotBlank)?.toInt()?.let {
                    progressAnimation?.setTargetProgress(it.toString().toLong())
                }
            }, 1000)
        }
    }

    private fun buildProgressAnimation() {
        progressAnimation = LazyCountAnimation.Builder(
            startProgress.text.toString().takeIf(String::isNotBlank)?.toLong() ?: 0,
            target.text.toString().takeIf(String::isNotBlank)?.toLong() ?: 100
        )
            .setFps(fps.text.toString().takeIf(String::isNotBlank)?.toInt() ?: 25)
            .doOnNextProgress {
                progress.text = "$it"
            }
            .build()
    }
}