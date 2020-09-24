# Lazy Count Animation
A convenient and continued number count animation. 
It makes very easier progress TextView or any purposes.

#### It supports not only positive but also negative numbers.

## Preview
<a href="https://github.com/IONsoft-Indonesia/RandomNumberAnimation/blob/master/art/RandomNumberGenerator.gif"><img src="https://github.com/IONsoft-Indonesia/RandomNumberAnimation/blob/master/art/RandomNumberGenerator.gif" width="250px"/></a>
<a href="https://github.com/IONsoft-Indonesia/RandomNumberAnimation/blob/master/art/RandomNumberGenerator.gif"><img src="https://github.com/IONsoft-Indonesia/RandomNumberAnimation/blob/master/art/RandomNumberGenerator.gif" width="250px"/></a>
## Usage
Initate
```
        progressAnimation = LazyCountAnimation.Builder(
            beginProgress = 0,
            targetProgress = 100
        )
            .setCanCountdown(true)
            .setGranularity(3)
            .setFps(30)
            .doOnNextProgress {
                view.text = "$it"
            }
            .build()

```

Set target number
```
progressAnimation.setTargetProgress(77)
```

Set the next target number, you don't need to do extra works. 
Lazy count will animate to the next number upon previous number.
```
progressAnimation.setProgress(86)
```

Set a negative number, you have to enable the countdown when initiating
```
progressAnimation.setProgress(-186)
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
progressAnimation.doOnNextProgress{ currentNumber ->
    // update a TextView
    view.text = currentNumber.toString()
}
```

Set FPS
```
progressAnimation.setFps(30)
```