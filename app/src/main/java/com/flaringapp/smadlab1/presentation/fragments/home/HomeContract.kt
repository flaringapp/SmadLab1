package com.flaringapp.smadlab1.presentation.fragments.home

import com.flaringapp.smadlab1.presentation.mvp.IBaseFragment
import com.flaringapp.smadlab1.presentation.mvp.IBasePresenter
import io.reactivex.Observable

interface HomeContract {

    interface ViewContract: IBaseFragment {
        val numbersInputObservable: Observable<String>

        fun setNumbersError(error: Int?)

        fun setResult(result: Double)
    }

    interface PresenterContract: IBasePresenter<ViewContract> {
        fun onAverageClicked()
        fun onModeClicked()
        fun onMedianClicked()
        fun onSampleSizeClicked()
        fun onVarianceClicked()
        fun onMeanSquareDeviationClicked()
        fun onCorrectedVarianceClicked()
        fun onCorrectedMeanSquareDeviationClicked()
        fun onVariationClicked()
        fun onAsymmetryClicked()
        fun onKurtosisClicked()
        fun onStartingPointClicked()
        fun onCentralPointClicked()
    }

}