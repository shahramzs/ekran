package com.example.ekran.utils


import android.content.Context
import android.content.res.Resources
import android.text.format.Formatter
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.net.NetworkInterface
import java.net.SocketException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

fun convertDpToPixel(dp: Float, context: Context?): Float {
    return if (context != null) {
        val resources = context.resources
        val metrics = resources.displayMetrics
        dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    } else {
        val metrics = Resources.getSystem().displayMetrics
        dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}

fun View.implementSpringAnimationTrait() {
    val scaleXAnim = SpringAnimation(this, DynamicAnimation.SCALE_X, 0.90f)
    val scaleYAnim = SpringAnimation(this, DynamicAnimation.SCALE_Y, 0.90f)

    setOnTouchListener { v, event ->
        Timber.i(event.action.toString())
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                scaleXAnim.spring.stiffness = SpringForce.STIFFNESS_LOW
                scaleXAnim.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
                scaleXAnim.start()

                scaleYAnim.spring.stiffness = SpringForce.STIFFNESS_LOW
                scaleYAnim.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
                scaleYAnim.start()

            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                scaleXAnim.cancel()
                scaleYAnim.cancel()
                val reverseScaleXAnim = SpringAnimation(this, DynamicAnimation.SCALE_X, 1f)
                reverseScaleXAnim.spring.stiffness = SpringForce.STIFFNESS_LOW
                reverseScaleXAnim.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
                reverseScaleXAnim.start()

                val reverseScaleYAnim = SpringAnimation(this, DynamicAnimation.SCALE_Y, 1f)
                reverseScaleYAnim.spring.stiffness = SpringForce.STIFFNESS_LOW
                reverseScaleYAnim.spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
                reverseScaleYAnim.start()


            }
        }

        false
    }
}

fun <T> Single<T>.asyncNetworkRequest(): Single<T> {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

//fun openUrlInCustomTab(context: Context, url: String) {
//    try {
//        val uri = Uri.parse(url)
//        val intentBuilder = CustomTabsIntent.Builder()
//        intentBuilder.setStartAnimations(context, android.R.anim.fade_in, android.R.anim.fade_out)
//        intentBuilder.setExitAnimations(context, android.R.anim.fade_in, android.R.anim.fade_out)
//        val customTabsIntent = intentBuilder.build()
//        customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
//        customTabsIntent.launchUrl(context, uri)
//
//    } catch (e: Exception) {
//        Timber.tag("browser").e(e)
//    }
//
//}

fun convertTimeToText(dataDate: String): String? {
    var convTime: String? = null
    val prefix = ""
    val suffix = "قبل"
    try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        val pasTime = dateFormat.parse(dataDate)
        val nowTime = Date()
        val dateDiff = nowTime.time - pasTime.time
        val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
        val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
        val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
        val day = TimeUnit.MILLISECONDS.toDays(dateDiff)
        if (second < 60) {
            convTime = "$second ثانیه $suffix"
        } else if (minute < 60) {
            convTime = "$minute دقیقه $suffix"
        } else if (hour < 24) {
            convTime = "$hour ساعت $suffix"
        } else if (day >= 7) {
            convTime = if (day > 360) {
                (day / 360).toString() + " سال " + suffix
            } else if (day > 30) {
                (day / 30).toString() + " ماه " + suffix
            } else {
                (day / 7).toString() + " هفته " + suffix
            }
        } else if (day < 7) {
            convTime = "$day روز $suffix"
        }
    } catch (e: ParseException) {
        e.printStackTrace()
        Log.e("ConvTimeE", e.message!!)
    }
    return convTime
}

fun bytesIntoHumanReadable(bytes: Long): String? {
    val kilobyte: Long = 1024
    val megabyte = kilobyte * 1024
    val gigabyte = megabyte * 1024
    val terabyte = gigabyte * 1024
    return if (bytes in 0 until kilobyte) {
        "$bytes B"
    } else if (bytes in kilobyte until megabyte) {
        (bytes / kilobyte).toString() + " KB"
    } else if (bytes in megabyte until gigabyte) {
        (bytes / megabyte).toString() + " MB"
    } else if (bytes in gigabyte until terabyte) {
        (bytes / gigabyte).toString() + " GB"
    } else if (bytes >= terabyte) {
        (bytes / terabyte).toString() + " TB"
    } else {
        "$bytes Bytes"
    }
}

fun timeFormater(millis: Long): String {
    val formatter: DateFormat =
        SimpleDateFormat("HH:mm:ss", Locale.US)
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}

fun convertTime(date: String?): String? {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US)
        val outputFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US)
        val datee = date?.let { inputFormat.parse(it) }
      datee?.let{outputFormat.format(it)}
    } catch (e: ParseException) {
        e.printStackTrace()
        ""
    }
}

fun randomCode(n: Int): String {
    val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"
    val stringBuilder = StringBuilder(n)
    for (i in 0 until n) {
        val index = (alphabet.length * Math.random()).toInt()
        stringBuilder.append(alphabet[index])
    }
    return stringBuilder.toString()
}

fun getLocalIpAddress(): String? {
    try {
        val en = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val intf = en.nextElement()
            val enumIpAddr = intf.inetAddresses
            while (enumIpAddr.hasMoreElements()) {
                val inetAddress = enumIpAddr.nextElement()
                if (!inetAddress.isLoopbackAddress) {
                    val ip = Formatter.formatIpAddress(inetAddress.hashCode())
                    Timber.tag("ip").i("***** IP=%s", ip)
                    return ip
                }
            }
        }
    } catch (ex: SocketException) {
        Timber.tag("ip").e(ex.toString())
    }
    return null
}

