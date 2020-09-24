package com.github.frog1014.lazycountanimation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.github.frog1014.lazycountanimation.lib.LazyCountAnimation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var progressAnimation: LazyCountAnimation? = null
    private var debounceTs: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buildProgressAnimation()
        submit.setOnClickListener {
            progressAnimation?.stop()
            progress.text = "0"
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

        target.doOnTextChanged { text, start, before, count ->
            if ((debounceTs + 1000) > System.currentTimeMillis()) {
                text?.toString()?.takeIf(String::isNotBlank)?.toInt()?.let {
                    progressAnimation?.setTargetProgress(it.toString().toLong())
                }
            }
            debounceTs = System.currentTimeMillis()
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