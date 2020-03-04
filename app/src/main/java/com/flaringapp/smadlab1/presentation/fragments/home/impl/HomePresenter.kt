package com.flaringapp.smadlab1.presentation.fragments.home.impl

import androidx.core.text.isDigitsOnly
import com.flaringapp.smadlab1.R
import com.flaringapp.smadlab1.data.calculatior.CharacteristicsCalculator
import com.flaringapp.smadlab1.presentation.fragments.home.HomeContract
import com.flaringapp.smadlab1.presentation.mvp.BasePresenter
import io.reactivex.Single
import io.reactivex.disposables.Disposable

private typealias Calculation = (DoubleArray) -> Single<Double>

class HomePresenter(
    private val calculator: CharacteristicsCalculator
) : BasePresenter<HomeContract.ViewContract>(), HomeContract.PresenterContract {

    companion object {
        private const val SPACE = " "

        private const val DEFINED_INPUT = "0.65, 0.79, 0.57, 0.34, 0.25, 0.00"
    }

    private var numbers: String = ""

    private var numbersInputDisposable: Disposable? = null
    private var calculationRequest: Disposable? = null

    private var pendingNumberInputAction: ((Int) -> Unit)? = null

    override fun onStart() {
        super.onStart()

        numbersInputDisposable = view!!.numbersInputObservable
            .subscribe {
                numbers = it
                view?.setNumbersError(null)
            }

        view?.initInput(DEFINED_INPUT)
    }

    override fun release() {
        calculationRequest?.dispose()
        super.release()
    }

    override fun onAverageClicked() {
        calculateWithUiResult { calculator.averageEmpirical(*it) }
    }

    override fun onModeClicked() {
        calculateWithUiResult { calculator.mode(*it) }
    }

    override fun onMedianClicked() {
        calculateWithUiResult { calculator.median(*it) }
    }

    override fun onSampleSizeClicked() {
        calculateWithUiResult { calculator.sampleSize(*it) }
    }

    override fun onVarianceClicked() {
        calculateWithUiResult { calculator.variance(*it) }
    }

    override fun onMeanSquareDeviationClicked() {
        calculateWithUiResult { calculator.meanSquareDeviation(*it) }
    }

    override fun onCorrectedVarianceClicked() {
        calculateWithUiResult { calculator.correctedVariance(*it) }
    }

    override fun onCorrectedMeanSquareDeviationClicked() {
        calculateWithUiResult { calculator.correctedMeanSquareDeviation(*it) }
    }

    override fun onVariationClicked() {
        calculateWithUiResult { calculator.variation(*it) }
    }

    override fun onAsymmetryClicked() {
        calculateWithUiResult { calculator.asymmetry(*it) }
    }

    override fun onKurtosisClicked() {
        calculateWithUiResult { calculator.kurtosis(*it) }
    }

    override fun onStartingPointClicked() {
        pendingNumberInputAction = { power ->
            calculateWithUiResult { calculator.startingPoint(power, *it) }
        }
        view?.openNumberInputDialog()
    }

    override fun onCentralPointClicked() {
        pendingNumberInputAction = { power ->
            calculateWithUiResult { calculator.centralPoint(power, *it) }
        }
        view?.openNumberInputDialog()
    }

    override fun onInput(input: String) {
        if (!input.isDigitsOnly()) return
        pendingNumberInputAction?.invoke(input.toInt())
    }

    private fun calculateWithUiResult(calculation: Calculation) {
        if (!validateNumbers()) {
            view?.setNumbersError(R.string.error_invalid_input)
            return
        }

        if (calculationRequest != null && !calculationRequest!!.isDisposed) return

        val doubleNumbers = numbers.trim()
            .split(SPACE)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { it.toDouble() }.toDoubleArray()

        calculationRequest = calculation.invoke(doubleNumbers)
            .subscribe(
                { view?.setResult(it) },
                { view?.handleError(it) }
            )
    }

    private fun validateNumbers(): Boolean {
        if (numbers.trim().isEmpty()) return false

        if (numbers.trim().split(SPACE).isEmpty()) return false

        return true
    }
}