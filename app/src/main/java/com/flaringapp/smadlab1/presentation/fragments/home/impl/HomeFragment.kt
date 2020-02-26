package com.flaringapp.smadlab1.presentation.fragments.home.impl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.flaringapp.app.utils.observeOnUI
import com.flaringapp.app.utils.onApiThread
import com.flaringapp.smadlab1.R
import com.flaringapp.smadlab1.presentation.fragments.home.HomeContract
import com.flaringapp.smadlab1.presentation.mvp.BaseFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.scope.currentScope
import java.text.DecimalFormat


class HomeFragment: BaseFragment<HomeContract.PresenterContract>(), HomeContract.ViewContract {

    companion object {
        private val resultFormat: DecimalFormat = DecimalFormat("0.0000")

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override val presenter: HomeContract.PresenterContract by currentScope.inject()

    override fun onInitPresenter() {
        presenter.view = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViews() {
        inputNumbers.doAfterTextChanged {
            numbersInputSubject.onNext(it.toString())
        }

        buttonAverage.setOnClickListener { presenter.onAverageClicked() }
        buttonMode.setOnClickListener { presenter.onModeClicked() }
        buttonMedian.setOnClickListener { presenter.onMedianClicked() }
        buttonSampleSize.setOnClickListener { presenter.onSampleSizeClicked() }
        buttonVariance.setOnClickListener { presenter.onVarianceClicked() }
        buttonMeanSquareDeviation.setOnClickListener { presenter.onMeanSquareDeviationClicked() }
        buttonCorrectedVariance.setOnClickListener { presenter.onCorrectedVarianceClicked() }
        buttonCorrectedMeanSquareDeviation.setOnClickListener { presenter.onCorrectedMeanSquareDeviationClicked() }
        buttonVariation.setOnClickListener { presenter.onVariationClicked() }
        buttonAsymmetry.setOnClickListener { presenter.onAsymmetryClicked() }
        buttonKurtosis.setOnClickListener { presenter.onKurtosisClicked() }
        buttonStartingPoint.setOnClickListener { presenter.onStartingPointClicked() }
        buttonCentralPoint.setOnClickListener { presenter.onCentralPointClicked() }
    }

    private val numbersInputSubject = PublishSubject.create<String>()
    override val numbersInputObservable: Observable<String> = numbersInputSubject
        .onApiThread()
        .observeOnUI()

    override fun setNumbersError(error: Int?) {
        layoutNumbersInput.error = error?.let { getString(it) }
    }

    override fun setResult(result: Double) {
        textResult.text = resultFormat.format(result)
    }
}