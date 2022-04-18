package site.starsone.pagestatusmanager

import android.app.Application
import site.starsone.statusmanager.LoadingAndRetryManager

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        LoadingAndRetryManager.BASE_RETRY_LAYOUT_ID = R.layout.base_retry
        LoadingAndRetryManager.BASE_LOADING_LAYOUT_ID = R.layout.base_loading
        LoadingAndRetryManager.BASE_EMPTY_LAYOUT_ID = R.layout.base_empty
    }
}