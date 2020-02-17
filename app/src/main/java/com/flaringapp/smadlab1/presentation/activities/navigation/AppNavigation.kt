package com.flaringapp.smadlab1.presentation.activities.navigation

interface AppNavigation {

    fun navigateTo(
        screen: Screen,
        data: Any? = null
    )

}