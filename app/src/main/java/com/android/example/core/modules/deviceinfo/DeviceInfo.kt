package com.android.example.core.modules.deviceinfo

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.annotation.StringRes
import com.android.example.R
import com.scottyab.rootbeer.RootBeer

class DeviceInfo(private val context: Context) {

    //Magic number
    val clientKind: String
        get() = "0"

    val platform: String
        get() = "Android"

    val platformVersion: String
        get() = Build.VERSION.RELEASE

    val browser: String
        get() = Build.DEVICE

    val browserVersion: String
        get() = Build.MODEL + " (" + Build.PRODUCT + ")"

    val packageName: String
        get() = context.packageName

    val deviceUDID: String
        @SuppressLint("HardwareIds")
        get() = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    val applicationID: String
        get() {
            return try { context.packageManager.getPackageInfo(context.packageName, 0).versionName }
            catch (e: Exception) { "0.00" }
        }

    val applicationVersionAsDouble: Double
        get() {
            return try { context.packageManager.getPackageInfo(context.packageName, 0).versionName.toDouble() }
            catch (e: Exception) { 0.0 }
        }

    val isRooted: Boolean
        get() {
            return kotlin.runCatching { RootBeer(context).isRooted }.getOrNull() ?: false
        }

    val networkInfo: NetworkInfo?
        @SuppressLint("MissingPermission")
        get() {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager?
            return cm?.activeNetworkInfo
        }

    val connectionType: ConnectionType
        get() {
            val info = networkInfo
            if (info == null || !info.isConnected)
                return ConnectionType.NONE

            if (info.type == ConnectivityManager.TYPE_WIFI)
                return ConnectionType.WIFI

            if (info.type == ConnectivityManager.TYPE_MOBILE) {
                val networkType = info.subtype
                return when (networkType) {
                    TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN //api<8 : replace by 11
                    -> ConnectionType.EDGE

                    TelephonyManager.NETWORK_TYPE_UMTS,
                    TelephonyManager.NETWORK_TYPE_EVDO_0,
                    TelephonyManager.NETWORK_TYPE_EVDO_A,
                    TelephonyManager.NETWORK_TYPE_HSDPA,
                    TelephonyManager.NETWORK_TYPE_HSUPA,
                    TelephonyManager.NETWORK_TYPE_HSPA,
                    TelephonyManager.NETWORK_TYPE_EVDO_B,
                    TelephonyManager.NETWORK_TYPE_EHRPD,
                    TelephonyManager.NETWORK_TYPE_HSPAP,
                    TelephonyManager.NETWORK_TYPE_TD_SCDMA
                    -> ConnectionType.G3

                    TelephonyManager.NETWORK_TYPE_LTE,
                    TelephonyManager.NETWORK_TYPE_IWLAN,
                    19
                    -> ConnectionType.LTE

                    else -> ConnectionType.OTHER
                }
            }
            return ConnectionType.OTHER
        }

    val operator: Operator
        get() {
            val telemanger = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager?
            val info = telemanger?.networkOperatorName.orEmpty()
            return when {
                info.contains("mts") -> Operator.MTS
                info.contains("velc") -> Operator.VELCOM
                info.contains("life") -> Operator.LIFE
                else -> Operator.OTHER
            }
        }

    enum class ConnectionType(@StringRes val title: Int) {
        WIFI(R.string.connection_type_wifi),
        G3(R.string.connection_type_3g),
        LTE(R.string.connection_type_lte),
        EDGE(R.string.connection_type_edge),
        NONE(R.string.connection_type_no),
        OTHER(R.string.connection_type_other);
    }

    enum class Operator(@StringRes val title: Int) {
        MTS(R.string.operator_mts),
        VELCOM(R.string.operator_velcom),
        LIFE(R.string.operator_life),
        OTHER(R.string.operator_other);
    }
}