package com.github.frog1014.lazycountanimation

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.MainThread
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.abs
import kotlin.math.ceil

class LazyCountAnimation private constructor() {

    companion object {
        const val TAG = "LazyCountAnimation"
    }

    private var progress = 0L
    private var interval = 50L
    private var granularity = 3
    private var canCountdown = false
    private var timer: Timer? = null
    private var doOnNextNumber: ((Long) -> Unit)? = null
    private var endNumber = 100L
    private var targetNumber = 0L
    private var handler = Handler(Looper.getMainLooper())
    fun doOnNextNumber(fn: ((Long) -> Unit)?) {
        doOnNextNumber = fn
    }

    @MainThread
    private fun onNextNumber(number: Long) {
        doOnNextNumber?.invoke(number)
    }

    fun isRunning() = timer != null
    fun start() {
        if (isRunning()) return

        stop()
        timer = Timer().apply {
            val signedDistance = targetNumber - progress
            val distance = if (canCountdown) abs(signedDistance) else signedDistance
            (ceil(distance / granularity.toDouble()).toLong()).takeIf { it > 0 }
                ?.let {
                    var count = 0L
                    schedule(0L, interval) {
                        count++
                        val isCountdown = canCountdown && targetNumber < progress
                        if (isCountdown) progress -= granularity else progress += granularity
                        if ((!isCountdown && progress > targetNumber)
                            || (isCountdown && progress < targetNumber)
                        )
                            progress = targetNumber
                        // if (progress > endProgress) progress = endProgress
                        postProgress(progress)
                        if (count == it || /*progress >= endProgress ||*/ (!isCountdown && progress == targetNumber) || (isCountdown && progress == targetNumber)) {
                            stop()
                            return@schedule
                        }
                    }
                } ?: run {
                stop()
                postProgress(progress)
            }
        }
    }

    private fun postProgress(progress: Long) {
        Log.d(TAG, "postProgress() called with: progress = $progress")
        handler.post {
            onNextNumber(progress)
        }
    }

    private fun setGranularity(granularity: Int) {
        this.granularity = granularity
    }

    fun getGranularity() = granularity
    fun getCanCountdown() = canCountdown
    fun setCanCountdown(boolean: Boolean) {
        canCountdown = boolean
    }

    fun setTargetNumber(number: Long) {
        if (isRunning()) stop()
        targetNumber = number
        // if (targetProgress > endProgress) {
        //     targetProgress = endProgress
        // }
        start()
    }

    private fun setEndNumber(progress: Long) {
        endNumber = progress
    }

    private fun initTargetNumber(progress: Long) {
        targetNumber = progress
    }

    // fun getEndProgress() = progress

    fun setBeginNumber(number: Long) {
        progress = number
    }

    fun getCurrentNumber() = progress
    fun stop() {
        Log.d(TAG, "stop: ")
        timer?.cancel()
        timer = null
    }

    private fun setFps(fps: Int) {
        interval = (1000 / fps).toLong()
    }

    fun getFps() = (1000L / interval).toInt()
    data class Builder(val beginNumber: Long, val targetNumber: Long) {
        private var fps = 30
        private var granularity = 3
        private var canCountdown = false
        private var onNextNumber: ((Long) -> Unit)? = null
        fun doOnNextNumber(fn: (Long) -> Unit) = apply {
            onNextNumber = fn
        }

        fun setGranularity(granularity: Int = this.granularity) = apply {
            this.granularity = granularity
        }

        fun setCanCountdown(boolean: Boolean = this.canCountdown) = apply {
            this.canCountdown = boolean
        }

        fun setFps(fps: Int = this.fps): Builder = apply {
            this.fps = fps
        }

        fun build(): LazyCountAnimation = LazyCountAnimation().apply {
            setGranularity(this@Builder.granularity)
            initTargetNumber(this@Builder.targetNumber)
            setFps(fps)
            setCanCountdown(this@Builder.canCountdown)
            setBeginNumber(beginNumber)
            doOnNextNumber(onNextNumber)
        }
    }

}
