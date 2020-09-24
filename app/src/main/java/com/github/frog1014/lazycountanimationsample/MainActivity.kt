package com.github.frog1014.lazycountanimationsample

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.github.frog1014.lazycountanimation.LazyCountAnimation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private val handler by lazy { Handler() }
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

        stop.setOnClickListener {
            progressAnimation?.stop()
        }

        reset.setOnClickListener {
            progressAnimation?.stop()
            buildProgressAnimation()
            countdown.check(R.id.notCountdown)
            progress.text = "0"
            startProgress.setText("")
            granularity.setText("")
            // endProgress.setText("")
            target.setText("")
        }

        random.setOnClickListener {
            target.setText((1..3000).random().toString())
        }

        resume.setOnClickListener {
            target.text = target.text
        }

        target.doOnTextChanged { text, _, _, _ ->
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                Log.d(TAG, "onCreate: doOnTextChanged")
                text?.toString()?.takeIf(String::isNotBlank)?.toInt()?.let {
                    progressAnimation?.setTargetNumber(it.toString().toLong())
                }
            }, 1000)
        }
    }

    private fun buildProgressAnimation() {
        progressAnimation = LazyCountAnimation.Builder(
            startProgress.text.toString().takeIf(String::isNotBlank)?.toLong() ?: 0,
            target.text.toString().takeIf(String::isNotBlank)?.toLong() ?: 100
        )
            .setCanCountdown(isCountdown.isChecked)
            .setGranularity(granularity.text.toString().takeIf(String::isNotBlank)?.toInt() ?: 3)
            .setFps(fps.text.toString().takeIf(String::isNotBlank)?.toInt() ?: 25)
            .doOnNextNumber {
                progress.text = "$it"
            }
            .build()
    }
}