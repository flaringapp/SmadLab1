package com.flaringapp.smadlab1.data.calculatior

import io.reactivex.Single

interface CharacteristicsCalculator {

    fun averageEmpirical(vararg numbers: Double): Single<Double>

    fun mode(vararg numbers: Double): Single<Double>

    fun median(vararg numbers: Double): Single<Double>

    fun sampleSize(vararg numbers: Double): Single<Double>

    fun variance(vararg numbers: Double): Single<Double>

    fun meanSquareDeviation(vararg numbers: Double): Single<Double>

    fun correctedVariance(vararg numbers: Double): Single<Double>

    fun correctedMeanSquareDeviation(vararg numbers: Double): Single<Double>

    fun variation(vararg numbers: Double): Single<Double>

    fun asymmetry(vararg numbers: Double): Single<Double>

    fun kurtosis(vararg numbers: Double): Single<Double>

    fun startingPoint(power: Int, vararg numbers: Double): Single<Double>

    fun centralPoint(power: Int, vararg numbers: Double): Single<Double>
}