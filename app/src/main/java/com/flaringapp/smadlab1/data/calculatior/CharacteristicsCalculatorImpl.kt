package com.flaringapp.smadlab1.data.calculatior

import com.flaringapp.app.utils.observeOnUI
import com.flaringapp.app.utils.onApiThread
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import kotlin.math.pow
import kotlin.math.sqrt


typealias Algorithm<T> = () -> T

class CharacteristicsCalculatorImpl : CharacteristicsCalculator {

    override fun averageEmpirical(vararg numbers: Double) = rxCalculation {
        numbers.sum() / numbers.size
    }

    override fun mode(vararg numbers: Double) = rxCalculation {
        numbers.sorted()
            .groupBy { it }
            .mapValues { it.value.size }
            .maxBy { it.value }!!.key
    }

    override fun median(vararg numbers: Double) = rxCalculation {
        if (numbers.size % 2 == 0) {
            val middle = numbers.size / 2
            (numbers[middle - 1] + numbers[middle]) / 2
        } else {
            numbers[(numbers.size - 1) / 2]
        }
    }

    override fun sampleSize(vararg numbers: Double) = rxCalculation {
        numbers.max()!! - numbers.min()!!
    }

    override fun variance(vararg numbers: Double) = averageEmpirical(*numbers)
        .map { average ->
            numbers.map {
                (it - average).let { value ->
                    value * value
                }
            }.sum() / numbers.size
        }

    override fun meanSquareDeviation(vararg numbers: Double) = variance(*numbers)
        .map { variance ->
            sqrt(variance)
        }

    override fun correctedVariance(vararg numbers: Double) = variance(*numbers)
        .map { variance ->
            numbers.size * variance / (numbers.size - 1)
        }

    override fun correctedMeanSquareDeviation(vararg numbers: Double) = correctedVariance(*numbers)
        .map { correctedVariance ->
            sqrt(correctedVariance)
        }

    override fun variation(vararg numbers: Double) = Single.zip(
        meanSquareDeviation(*numbers),
        averageEmpirical(*numbers),
        BiFunction<Double, Double, Double> { meanSquareDeviation, average ->
            meanSquareDeviation / average
        }
    )

    override fun asymmetry(vararg numbers: Double) = Single.zip(
        averageEmpirical(*numbers),
        meanSquareDeviation(*numbers),
        BiFunction<Double, Double, Double> { average, meanSquareDeviation ->
            numbers.map {
                (it - average).pow(3)
            }.sum() / (meanSquareDeviation.pow(3) * numbers.size)
        }
    )

    override fun kurtosis(vararg numbers: Double) = Single.zip(
        averageEmpirical(*numbers),
        meanSquareDeviation(*numbers),
        BiFunction<Double, Double, Double> { average, meanSquareDeviation ->
            numbers.map {
                (it - average).pow(4)
            }.sum() / (meanSquareDeviation.pow(4) * numbers.size)
        }
    ).map {
        it - 3
    }

    override fun startingPoint(power: Int, vararg numbers: Double) = rxCalculation {
        numbers.map { it.pow(power) }.sum()
    }

    override fun centralPoint(power: Int, vararg numbers: Double) = averageEmpirical(*numbers)
        .map { average ->
            numbers.map { (it - average).pow(power) }
                .sum()
        }

    private fun <T> rxCalculation(algorithm: Algorithm<T>): Single<T> {
        return Single.create<T> { emitter ->
            val result = algorithm.invoke()
            emitter.onSuccess(result)
        }
            .onApiThread()
            .observeOnUI()
    }
}