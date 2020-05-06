package com.flaringapp.smadlab1.data.calculatior

import com.flaringapp.smadlab1.app.utils.observeOnUI
import com.flaringapp.smadlab1.app.utils.onApiThread
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
        numbers.sorted().let {
            if (it.size % 2 == 0) {
                val middle = it.size / 2
                (it[middle - 1] + it[middle]) / 2
            } else {
                it[(it.size - 1) / 2]
            }
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
        correctedMeanSquareDeviation(*numbers),
        averageEmpirical(*numbers),
        BiFunction<Double, Double, Double> { meanSquareDeviation, average ->
            meanSquareDeviation / average
        }
    )

    override fun asymmetry(vararg numbers: Double) = Single.zip(
        centralPoint(3, *numbers),
        meanSquareDeviation(*numbers),
        BiFunction<Double, Double, Double> { centralPoint, meanSquareDeviation ->
            centralPoint / meanSquareDeviation.pow(3)
        }
    )

    override fun kurtosis(vararg numbers: Double) = Single.zip(
        centralPoint(4, *numbers),
        meanSquareDeviation(*numbers),
        BiFunction<Double, Double, Double> { centralPoint, meanSquareDeviation ->
            (centralPoint / meanSquareDeviation.pow(4)) - 3
        }
    )

    override fun startingPoint(power: Int, vararg numbers: Double) = rxCalculation {
        var result = 0.0
        numbers.groupBy { it }
            .mapValues { it.value.size }.let {
                for (model in it) {
                    result += model.key.pow(power) * model.value
                }
            }
        result / numbers.size
    }

    override fun centralPoint(power: Int, vararg numbers: Double) = averageEmpirical(*numbers)
        .map { average ->
            var result = 0.0
            numbers.groupBy { it }
                .mapValues { it.value.size }.let {
                    for (model in it) {
                        result += (model.key - average).pow(power) * model.value
                    }
                }
            result / numbers.size
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