package com.github.frog1014.lazycountanimation.lib

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
        const val TAG = "ProgressAnimation"
    }

    private var progress = 0L
    private var interval = 50L
    private var granularity = 3
    private var canCountdown = false
    private var timer: Timer? = null
    private var doOnNextProgress: ((Long) -> Unit)? = null
    private var endProgress = 100L
    private var targetProgress = 0L
    private var handler = Handler(Looper.getMainLooper())
    fun doOnNextProgress(fn: ((Long) -> Unit)?) {
        doOnNextProgress = fn
    }

    @MainThread
    private fun onNextProgress(progress: Long) {
        doOnNextProgress?.invoke(progress)
    }

    fun isRunning() = timer != null
    fun start() {
        if (isRunning()) return

        stop()
        timer = Timer().apply {
            val signedDistance = targetProgress - progress
            val distance = if (canCountdown) abs(signedDistance) else signedDistance
            (ceil(distance / granularity.toDouble()).toLong()).takeIf { it > 0 }
                ?.let {
                    var count = 0L
                    schedule(0L, interval) {
                        count++
                        val isCountdown = canCountdown && targetProgress < progress
                        if (isCountdown) progress -= granularity else progress += granularity
                        if ((!canCountdown && progress > targetProgress) || (isCountdown && progress < targetProgress)) progress =
                            targetProgress
                        // if (progress > endProgress) progress = endProgress
                        postProgress()
                        if (count == it || /*progress >= endProgress ||*/ (!canCountdown && progress >= targetProgress) || (isCountdown && progress <= targetProgress)) {
                            stop()
                            return@schedule
                        }
                    }
                } ?: run {
                stop()
                postProgress()
            }
        }
    }

    private fun postProgress() {
        handler.post {
            onNextProgress(progress)
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

    fun setTargetProgress(progress: Long) {
        if (isRunning()) stop()
        targetProgress = progress
        // if (targetProgress > endProgress) {
        //     targetProgress = endProgress
        // }
        start()
    }

    private fun setEndProgress(progress: Long) {
        endProgress = progress
    }

    private fun initTargetProgress(progress: Long) {
        targetProgress = progress
    }

    // fun getEndProgress() = progress

    fun setBeginProgress(number: Long) {
        progress = number
    }

    fun getProgress() = progress
    fun stop() {
        Log.d(TAG, "stop: ")
        timer?.cancel()
        timer = null
    }

    private fun setFps(fps: Int) {
        interval = (1000 / fps).toLong()
    }

    fun getFps() = (1000L / interval).toInt()
    data class Builder(val beginProgress: Long, val targetProgress: Long) {
        private var fps = 30
        private var granularity = 3
        private var canCountdown = false
        private var onNextProgress: ((Long) -> Unit)? = null
        fun doOnNextProgress(fn: (Long) -> Unit) = apply {
            onNextProgress = fn
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
            initTargetProgress(this@Builder.targetProgress)
            setFps(fps)
            setCanCountdown(this@Builder.canCountdown)
            setBeginProgress(beginProgress)
            doOnNextProgress(onNextProgress)
        }
    }

}
