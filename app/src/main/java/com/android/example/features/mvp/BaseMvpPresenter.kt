package com.android.example.features.mvp

import com.android.example.App
import com.android.example.R
import com.android.example.core.modules.resources.ResourcesHelper
import com.android.example.core.modules.vibro.VibroHelper
import com.android.example.core.ui.dialogs.Dialog
import com.android.example.features.models.Wrapper
import com.arellomobile.mvp.MvpPresenter
import dagger.Lazy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.net.SocketTimeoutException
import javax.inject.Inject

abstract class BaseMvpPresenter<V : BaseMvpView> : MvpPresenter<V>(), CoroutineScope {

    @Inject
    lateinit var vibro: Lazy<VibroHelper>

    @Inject
    lateinit var resources: ResourcesHelper

    private val ui: CoroutineDispatcher = Dispatchers.Main
    private val bg: CoroutineDispatcher = Dispatchers.IO

    protected var job: Job = Job()
    override val coroutineContext = job + bg

    override fun attachView(view: V) {
        super.attachView(view)
        EventBus.getDefault().register(this)
    }

    override fun detachView(view: V) {
        super.detachView(view)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun listenBus(wrapper: Wrapper) {
    }

    protected open fun handleError(throwable: Throwable) {
        val dialog = Dialog.builder()
            .setTitle(R.string.attention)

        when (throwable) {
            is java.net.ConnectException,
            is SocketTimeoutException -> dialog.setDescription(R.string.default_error_connection)
            is java.net.UnknownHostException -> dialog.setDescription(R.string.default_error_no_internet)
            else -> dialog.setDescription(R.string.default_error_internal)
        }

        viewState.showDialog(dialog)
    }

    protected suspend fun <T> ui(block: suspend CoroutineScope.() -> T): T {
        return withContext(ui, block = block)
    }

    protected fun <R> launch(
        showProgress: Boolean = false,
        handleErrors: Boolean = true,
        onError: ((t: Throwable) -> Unit)? = null,
        block: suspend CoroutineScope.() -> R
    ) {
        launch(context = coroutineContext) {
            try {
                ui { if (showProgress) viewState.showProgress(true) }
                block.invoke(this)
                ui { if (showProgress) viewState.showProgress(false) }
            } catch (t: Throwable) {
                ui {
                    App.log.e(t)
                    viewState.showProgress(false)
                    if (handleErrors) handleError(t)
                    onError?.invoke(t)
                }
            }
        }
    }

    protected fun clearJob() = job.cancel()

    override fun onDestroy() {
        clearJob()
        super.onDestroy()
    }
}