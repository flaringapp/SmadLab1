package com.flaringapp.smadlab1.presentation.mvp

interface IBaseDialog: IBaseView {
    val dialogTag: String?

    fun close()
}