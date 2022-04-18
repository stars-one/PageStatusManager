# PageStatusManager

<img id="badge" src="https://jitpack.io/v/stars-one/PageStatusManager.svg">

无缝为Activity、Fragment、任何View设置加载（loading）、重试(retry)和无数据（empty）页面。

基于[hongyangAndroid/LoadingAndRetryManager](https://github.com/hongyangAndroid/LoadingAndRetryManager)进行Kotlin的相关优化

发布在Jitpack可快速引用

## 引入

```
implementation 'com.github.stars-one:PageStatusManager:0.1'
```

## 使用

### 1.初始化对应状态布局
如果多个页面共享加载和重试页面，建议全局设置个基本的。比如在Application中：

```java
public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        //设置布局
        LoadingAndRetryManager.BASE_RETRY_LAYOUT_ID = R.layout.base_retry; //重试
        LoadingAndRetryManager.BASE_LOADING_LAYOUT_ID = R.layout.base_loading; //加载中
        LoadingAndRetryManager.BASE_EMPTY_LAYOUT_ID = R.layout.base_empty;//空数据
    }
}
```

> PS: 如果只是单个Activity或Fragment用到，也可以在对应的Activity或Fragment进行布局的设置

### 2.Activity初始化对象
在Activity中：

```java
class MainActivity : AppCompatActivity() {
    lateinit var mLoadingAndRetryManager: LoadingAndRetryManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mLoadingAndRetryManager = LoadingAndRetryManager.generate(this,object :
            OnLoadingAndRetryListener() {
            override fun setRetryEvent(retryView: View?) {
                //设置重试页面的点击事件
                retryView?.findViewById<Button>(R.id.id_btn_retry)?.setOnClickListener {
                   loadData()
                }
            }
        })

        loadData()
    }

    //模拟加载数据
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
```
* 在Fragment中与Activity中用法一致。

* 为任何View添加，只需要将`LoadingAndRetryManager.generate()`的第一个参数改成对应的View即可。


如果需要针对单个Activity、Fragment、View定制页面，重写接口的回调方法：

```java
public View generateLoadingLayout()
{
   return null;
}

public View generateRetryLayout()
{
   return null;
}

public View generateEmptyLayout()
{
   return null;
}
```
即可，针对每个页面都有对应的设置事件的回调，如果有需求直接复写。

### API

* mLoadingAndRetryManager.showContent();
* mLoadingAndRetryManager.showRetry();
* mLoadingAndRetryManager.showLoading();
* mLoadingAndRetryManager.showEmpty();
