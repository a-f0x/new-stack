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
     *Функция добавляет перехватчика ошибок которые возникают когда корутина которую отменили завершает свое выполнение
     *@param taskKey - ключ по которому  можно будет достать эту джобу если нужно
     *@param block  - суспенд функция из которой билдится корутина
     * @return  - вернет эту же джобу
     * */
    protected fun launchOnUI(taskKey: String? = null, block: suspend () -> Unit): Job {
        val job = GlobalScope.launch(Dispatchers.Main + handler) {
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
}