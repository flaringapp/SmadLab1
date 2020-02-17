package com.flaringapp.smadlab1.app.di

import com.flaringapp.smadlab1.data.calculatior.CharacteristicsCalculator
import com.flaringapp.smadlab1.data.calculatior.CharacteristicsCalculatorImpl
import org.koin.dsl.module

val dataModule = module {

    single { CharacteristicsCalculatorImpl() as CharacteristicsCalculator }

}