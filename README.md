# Lazy Count Animation

[![](https://jitpack.io/v/frog1014/lazy_count_animation.svg)](https://jitpack.io/#frog1014/lazy_count_animation)

A convenient and continued number count animation. 
It makes very easier progress TextView or any purposes.

#### It supports not only positive but also negative numbers.

## Preview
<a href="https://raw.githubusercontent.com/frog1014/lazy_count_animation/master/art/countdown.gif"><img src="https://raw.githubusercontent.com/frog1014/lazy_count_animation/master/art/countdown.gif" width="250px"/></a>
<a href="https://github.com/frog1014/lazy_count_animation/blob/master/art/non_countdown.gif?raw=true"><img src="https://github.com/frog1014/lazy_count_animation/blob/master/art/non_countdown.gif?raw=true" width="250px"/></a>
## Usage
Initate
```
        progressAnimation = LazyCountAnimation.Builder(
            beginNumber = 0,
            targetNumber = 100
        )
            .setCanCountdown(true)
            .setGranularity(3)
            .setFps(30)
            .doOnNextNumber {
                view.text = "$it"
            }
            .build()

```

Set target number
```
progressAnimation.setTargetNumber(77)
```

Set the next target number, you don't need to do extra works. 
Lazy count will animate to the next number upon previous number.
```
progressAnimation.setTargetNumber(86)
```

Set a negative number, you have to enable the countdown when initiating
```
progressAnimation.setTargetNumber(-186)
```

Stop
```
progressAnimation.stop()
```

Start
```
progressAnimation.start()
```

Callback, here is a returned current number
```
progressAnimation.doOnNextNumber{ currentNumber ->
    // update a TextView or do something...
    view.text = currentNumber.toString()
}
```

Set FPS
```
progressAnimation.setFps(30)
```