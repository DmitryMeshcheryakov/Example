package com.android.example.features.mvp

import com.android.example.core.ui.dialogs.BaseDialog
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface BaseMvpView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress(show: Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun showDialog(dialog: BaseDialog.Builder)

    @StateStrategyType(SkipStrategy::class)
    fun onBack()

    @StateStrategyType(SkipStrategy::class)
    fun finishScreen()

    @StateStrategyType(SkipStrategy::class)
    fun showMainScreen()

    @StateStrategyType(SkipStrategy::class)
    fun showStartScreen()

    @StateStrategyType(SkipStrategy::class)
    fun showError(error: String)
}