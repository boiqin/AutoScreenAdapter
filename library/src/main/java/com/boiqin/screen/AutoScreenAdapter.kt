package com.boiqin.screen

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.annotation.NonNull

/**
 * 屏幕适配方案
 *
 * 提供 dp、sp 以及 pt 作为适配单位，建议开发中以 dp、sp 适配为主，pt 可作为 dp、sp 的适配补充
 *
 * 由今日头条适配方案修改而来
 */
object AutoScreenAdapter {
    /**
     * 适配信息
     */
    data class MatchInfo(
        val screenWidth: Int = 0,
        val screenHeight: Int = 0,
        val appDensity: Float = 0f,
        val appDensityDpi: Float = 0f,
        var appScaledDensity: Float = 0f,
        val appXdpi: Float = 0f
    )

    /**
     * 屏幕适配的基准
     */
    enum class MatchBase {
        WIDTH, HEIGHT
    }
    /**
     * 适配单位
     */
    enum class MatchUnit {
        DP, PT
    }

    // 适配信息
    private var matchInfo: MatchInfo? = null
    // Activity 的生命周期监测
    private var mActivityLifecycleCallback: ActivityLifecycleCallbacks? = null

    /**
     * 初始化
     *
     * @param application
     */
    private fun init(@NonNull application: Application) {
        val displayMetrics = application.resources.displayMetrics
        // 记录系统的原始值
        if(null == matchInfo) {
            matchInfo = MatchInfo(
                displayMetrics.widthPixels,
                displayMetrics.heightPixels,
                displayMetrics.density,
                displayMetrics.densityDpi.toFloat(),
                displayMetrics.scaledDensity,
                displayMetrics.xdpi
            )
            // 添加字体变化的监听
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(newConfig: Configuration?) { // 字体改变后,将 appScaledDensity 重新赋值
                    if (newConfig != null && newConfig.fontScale > 0) {
                        matchInfo?.run {
                            appScaledDensity = application.resources.displayMetrics.scaledDensity
                        }
                    }
                }

                override fun onLowMemory() {}
            })
        }

    }

    /**
     * 在 application 中全局激活适配（也可单独使用 match() 方法在指定页面中配置适配）
     */
    fun register(
        @NonNull application: Application, designSize: Float,
        matchBase: MatchBase = MatchBase.WIDTH,
        matchUnit: MatchUnit = MatchUnit.DP
    ) {
        init(application)
        mActivityLifecycleCallback = mActivityLifecycleCallback ?:
                object : ActivityLifecycleCallbacks {
                    override fun onActivityCreated(
                        activity: Activity,
                        savedInstanceState: Bundle?
                    ) {
                        match(activity, designSize, matchBase, matchUnit)
                    }

                    override fun onActivityStarted(activity: Activity) {}
                    override fun onActivityResumed(activity: Activity) {}
                    override fun onActivityPaused(activity: Activity) {}
                    override fun onActivityStopped(activity: Activity) {}
                    override fun onActivitySaveInstanceState(
                        activity: Activity,
                        outState: Bundle
                    ) {
                    }

                    override fun onActivityDestroyed(activity: Activity) {}
                }
        application.registerActivityLifecycleCallbacks(mActivityLifecycleCallback)
    }

    /**
     * 全局取消适配
     */
    fun unregister(@NonNull application: Application, @NonNull matchUnit: MatchUnit) {
        if (mActivityLifecycleCallback != null) {
            application.unregisterActivityLifecycleCallbacks(mActivityLifecycleCallback)
            mActivityLifecycleCallback = null
        }

        cancelMatch(application, matchUnit)

    }
    /**
     * 适配屏幕（放在 Activity 的 setContentView() 之前执行）
     *
     * @param context
     * @param designSize 设计图的尺寸
     * @param matchBase  适配基准
     * @param matchUnit  使用的适配单位
     */
    /**
     * 适配屏幕（放在 Activity 的 setContentView() 之前执行）
     *
     * @param context
     * @param designSize
     */
    /**
     * 适配屏幕（放在 Activity 的 setContentView() 之前执行）
     *
     * @param context
     * @param designSize
     * @param matchBase
     */
    @JvmOverloads
    fun match(
        @NonNull context: Context, designSize: Float,
        matchBase: MatchBase = MatchBase.WIDTH,
        matchUnit: MatchUnit = MatchUnit.DP
    ) {
        init(context.applicationContext as Application)
        if (designSize == 0f) {
            throw UnsupportedOperationException("The designSize cannot be equal to 0")
        }
        if (matchUnit == MatchUnit.DP) {
            matchByDP(context, designSize, matchBase)
        } else if (matchUnit == MatchUnit.PT) {
            matchByPT(context, designSize, matchBase)
        }
    }

    /**
     * 重置适配信息，取消适配
     */
    fun cancelMatch(@NonNull context: Context) {
        cancelMatch(context, MatchUnit.DP)
        cancelMatch(context, MatchUnit.PT)
    }

    /**
     * 重置适配信息，取消适配
     *
     * @param context
     * @param matchUnit 需要取消适配的单位
     */
    fun cancelMatch(@NonNull context: Context, matchUnit: MatchUnit) {
        matchInfo?.let {
            val displayMetrics = context.resources.displayMetrics
            if (matchUnit == MatchUnit.DP) {
                if (displayMetrics.density != it.appDensity) {
                    displayMetrics.density = it.appDensity
                }
                if (displayMetrics.densityDpi.toFloat() != it.appDensityDpi) {
                    displayMetrics.densityDpi =
                        it.appDensityDpi.toInt()
                }
                if (displayMetrics.scaledDensity != it.appScaledDensity) {
                    displayMetrics.scaledDensity =
                        it.appScaledDensity
                }
            } else if (matchUnit == MatchUnit.PT) {
                if (displayMetrics.xdpi != it.appXdpi) {
                    displayMetrics.xdpi = it.appXdpi
                }
            }
        }
    }

    /**
     * 使用 dp 作为适配单位（适合在新项目中使用，在老项目中使用会对原来既有的 dp 值产生影响）
     * <br></br>
     *
     * dp 与 px 之间的换算:
     *  *  px = density * dp
     *  *  density = dpi / 160
     *  *  px = dp * (dpi / 160)
     *
     *
     * @param context
     * @param designSize 设计图的宽/高（单位: dp）
     * @param base       适配基准
     */
    private fun matchByDP(
        @NonNull context: Context, designSize: Float,
        matchBase: MatchBase
    ) {
        matchInfo?.let {
            val targetDensity = when (matchBase) {
                MatchBase.WIDTH -> {
                    it.screenWidth * 1f / designSize
                }
                MatchBase.HEIGHT -> {
                    it.screenHeight * 1f / designSize
                }
            }
            val targetDensityDpi = (targetDensity * 160).toInt()
            val targetScaledDensity =
                targetDensity * (it.appScaledDensity / it.appDensity)
            val displayMetrics = context.resources.displayMetrics
            displayMetrics.density = targetDensity
            displayMetrics.densityDpi = targetDensityDpi
            displayMetrics.scaledDensity = targetScaledDensity
        }
    }

    /**
     * 使用 pt 作为适配单位（因为 pt 比较冷门，新老项目皆适合使用；也可作为 dp 适配的补充，
     * 在需要同时适配宽度和高度时，使用 pt 来适配 dp 未适配的宽度或高度）
     * <br></br>
     *
     *  pt 转 px 算法: pt * metrics.xdpi * (1.0f/72)
     *
     * @param context
     * @param designSize 设计图的宽/高（单位: pt）
     * @param base       适配基准
     */
    private fun matchByPT(
        @NonNull context: Context, designSize: Float,
        matchBase: MatchBase
    ) {
        matchInfo?.let {
            val targetXdpi = when (matchBase) {
                MatchBase.WIDTH -> {
                    it.screenWidth * 72f / designSize
                }
                MatchBase.HEIGHT -> {
                    it.screenHeight * 72f / designSize
                }
            }
            val displayMetrics = context.resources.displayMetrics
            displayMetrics.xdpi = targetXdpi
        }
    }
}