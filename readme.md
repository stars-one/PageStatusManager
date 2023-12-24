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
            //加载失败的显示(相应的,加载成功调用showContent即可)
            runOnUiThread {
                mLoadingAndRetryManager.showRetry()
            }
        }
    }
}
```
* 在Fragment中与Activity中用法一致。

* 为任何View添加，只需要将`LoadingAndRetryManager.generate()`的第一个参数改成对应的View即可。

PS: 目前如果是在约束布局中使用有坑,

比如说,下面代码(省略了根布局的约束布局),到时候调用showContent的时候,是无法显示出此View的,原因是设置LayoutParam的高度计算不对(暂时还没有细究,解决方法在下面)
```xml
<androidx.recyclerview.widget.RecyclerView
    android:id="@+id/rv"
    android:layout_width="match_parent"
    app:layout_constraintTop_toBottomOf="@id/toolbar"
    app:layout_constraintBottom_toBottomOf="parent"
    android:layout_height="0dp"
    tools:listitem="@layout/rv_item_ncmfile" />
```

我们给View外层包裹一个布局即可(什么布局都可),然后View占满此布局即可解决

```xml
<FrameLayout
        android:layout_width="match_parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintTop_toBottomOf="@id/toolbar"
        android:layout_height="0dp">
        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/rv"
            android:background="@color/red"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            tools:listitem="@layout/rv_item_ncmfile" />
    </FrameLayout>
```

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
