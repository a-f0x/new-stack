package ru.f0xdev.core.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.f0xdev.core.coroutines.CompositeJob

abstract class BasePresenter<View> : ViewModel(), LifecycleObserver {
    protected var view: View? = null
    private val compositeJob = CompositeJob()


    /**
     * нужен для перехвата эксепшенов в уже отмененных корутинах
     * иначе падает
     * */
    private val handler = CoroutineExceptionHandler(handler = { _, error ->
        error.printStackTrace()
    })

    /**
     * Прицепить вью к презентеру
     * @param view  - сама вьюха
     * */
    fun attachView(view: View) {
        this.view = view
        onFirstAttached()
    }

    /**
     * сохранить джобу (сбилженную корутину)
     * что бы при детаче вью не забыть ее отменить.
     * иначе будет либо мемори лик либо падение
     * @param job - сбилженная корутина
     * @param taskKey - ключ по которому ее можно будет достать если нужно
     * */

    protected fun addJob(job: Job, taskKey: String? = null) {
        taskKey?.let {
            compositeJob.add(job, it)
            return
        }
        compositeJob.add(job)
    }

    /**
     * отменить джобу по конкретному ключу
     * @param taskKey ключ который был передан в [addJob]
     * */
    protected fun cancelJob(taskKey: String) {
        compositeJob.cancel(taskKey)
    }

    /**
     * отменить все джобы разом
     * */
    protected fun cancelAllJobs() {
        compositeJob.cancel()
    }

    /**
     *выполнить корутину и вернуть результат в ЮУ
     * */
    protected fun launchOnUI(taskKey: String? = null, block: suspend () -> Unit): Job {
        val job = GlobalScope.launch(Dispatchers.Main + handler) {
            block.invoke()
        }
        addJob(job, taskKey)
        return job

    }

    /**
     *выполнить корутину в другом потоке
     * */

    protected fun <T> launchBackGround(taskKey: String? = null, block: suspend () -> T): Deferred<T> {
        val job = GlobalScope.async {
            block.invoke()
        }
        addJob(job, taskKey)
        return job
    }

    /**
     * Обработка жизненного цикла вью
     * срабатывает когда вью будет уничтожена
     *
     * */

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onViewDestroyed() {
        view = null
        cancelAllJobs()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStarted() {
    }

    open fun onFirstAttached() {}
}