package com.android.example.core.features.pin

import android.os.Handler
import android.os.Looper

class PinManager {

    companion object {
        fun builder(id: Int) = PinManager().Builder(id)
    }

    private var steps = emptyList<Step>()
    private var currentStep = 0
    var enableControls = true
        set(value) {
            field = value
            view?.enableControls(if (value) getValidDigits() else emptySet())
        }

    var id: Int = -1
    var view: View? = null
    var callback: Callback? = null
    var dotCount: Int = 4
    var validation: (pin: String) -> Boolean = { true }

    fun setSymbols(symbols: String) {
        Handler(Looper.getMainLooper()).post {
            if (validation.invoke(symbols)) {
                enableControls = true
                getCurrentStep().pin =
                    if (symbols.length > dotCount) symbols.substring(0, dotCount) else symbols
                view?.setDots(getCurrentStep().pin.length)

                enableControls = enableControls
                if (getCurrentStep().pin.length == dotCount) {
                    getCurrentStep().attemptsLeft++

                    enableControls = false
                    callback?.endStep(id, currentStep) {
                        enableControls = true
                        goToNextStep()
                    }
                }
            }
        }
    }

    fun addSymbol(symbol: String) {
        Handler(Looper.getMainLooper()).post {
            if (enableControls && validation.invoke(getCurrentStep().pin + symbol)) {
                if (getCurrentStep().pin.length < dotCount) {
                    getCurrentStep().pin += symbol
                }

                if (getCurrentStep().pin.length <= dotCount)
                    view?.setDots(getCurrentStep().pin.length)

                enableControls = enableControls
                if (getCurrentStep().pin.length == dotCount) {
                    getCurrentStep().attemptsLeft++

                    enableControls = false
                    callback?.endStep(id, currentStep) {
                        enableControls = true
                        goToNextStep()
                    }
                }
            }
        }
    }

    fun removeSymbol() {
        Handler(Looper.getMainLooper()).post {
            if (enableControls) {
                getCurrentStep().pin =
                    getCurrentStep().pin.substring(0 until getCurrentStep().pin.length - (if (getCurrentStep().pin.isNotEmpty()) 1 else 0))
                view?.setDots(getCurrentStep().pin.length)
                enableControls = enableControls
            }
        }
    }

    fun isEmpty(): Boolean {
        return steps.isEmpty()
    }

    fun clear() {
        getCurrentStep().pin = ""
        view?.setDots(0)
        enableControls = true
    }

    fun getCurrentStep(): Step = steps[currentStep]

    fun getStep(pos: Int) = steps[pos]

    fun goToNextStep() {
        Handler(Looper.getMainLooper()).post {
            if (steps.size <= currentStep + 1) {
                enableControls = false
                callback?.complete(id)
            } else {
                currentStep++
                view?.setTitle(getCurrentStep().title)
                view?.setDots(getCurrentStep().pin.length)

                enableControls = false
                callback?.startStep(id, currentStep) {
                    enableControls = true
                }
            }
        }
    }

    fun goToPrevStep() {
        Handler(Looper.getMainLooper()).post {
            if (currentStep > 0) {
                getCurrentStep().reset()
                currentStep--
                getCurrentStep().reset()
                view?.setTitle(getCurrentStep().title)
                view?.setDots(getCurrentStep().pin.length)

                enableControls = false
                callback?.startStep(id, currentStep) {
                    enableControls = true
                }
            }
        }
    }

    fun goToFirstStep() {
        Handler(Looper.getMainLooper()).post {
            currentStep = 0
            steps.forEach { it.reset() }
            view?.setTitle(getCurrentStep().title)
            view?.setDots(getCurrentStep().pin.length)

            enableControls = false
            callback?.startStep(id, currentStep) {
                enableControls = true
            }
        }
    }

    private fun getValidDigits(): Set<Int> {
        val currentPin = getCurrentStep().pin
        var result = emptySet<Int>()
        for (i in 0..9) {
            if (validation.invoke("$currentPin$i"))
                result = result.plus(i)
        }
        return result
    }

    interface View {
        fun setTitle(title: String)

        fun setDots(count: Int)

        fun enableControls(availableDigits: Set<Int>)
    }

    interface Callback {

        fun startStep(id: Int, step: Int, startStep: () -> Unit)

        fun endStep(id: Int, step: Int, goToNextStep: () -> Unit)

        fun complete(id: Int)
    }

    class Step(
        val title: String,
        val step: Int,
        var pin: String = "",
        var attemptsLeft: Int = 0
    ) {
        fun reset() {
            pin = ""
            attemptsLeft = 0
        }
    }

    inner class Builder(var id: Int) {

        init {
            enableControls = false
        }

        fun setTitles(titles: List<String>): Builder {
            this@PinManager.steps = titles.indices.map {
                Step(
                    titles[it],
                    it
                )
            }
            return this
        }

        fun setView(view: View): Builder {
            this@PinManager.view = view
            return this
        }

        fun setCallback(callback: Callback): Builder {
            this@PinManager.callback = callback
            return this
        }

        fun setValidation(validation: (pin: String) -> Boolean): Builder {
            this@PinManager.validation = validation
            return this
        }

        fun start(): PinManager {
            this@PinManager.id = id
            goToFirstStep()
            return this@PinManager
        }
    }
}