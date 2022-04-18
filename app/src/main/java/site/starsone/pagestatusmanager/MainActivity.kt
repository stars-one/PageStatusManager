package site.starsone.pagestatusmanager

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import site.starsone.statusmanager.LoadingAndRetryManager
import site.starsone.statusmanager.OnLoadingAndRetryListener

import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    lateinit var mLoadingAndRetryManager: LoadingAndRetryManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mLoadingAndRetryManager = LoadingAndRetryManager.generate(this,object :
            OnLoadingAndRetryListener() {
            override fun setRetryEvent(retryView: View?) {
                retryView?.findViewById<Button>(R.id.id_btn_retry)?.setOnClickListener {
                   loadData()
                }
            }
        })

        loadData()

    }
    fun loadData() {
        thread {
            mLoadingAndRetryManager.showLoading()
            Thread.sleep(2000)
            runOnUiThread {
                mLoadingAndRetryManager.showRetry()
            }
        }
    }
}