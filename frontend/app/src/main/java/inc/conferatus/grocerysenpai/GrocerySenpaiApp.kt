package inc.conferatus.grocerysenpai

import android.app.Application
import android.content.SharedPreferences
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class GrocerySenpaiApp : Application() {

    override fun onCreate() {
        super.onCreate()

        sharedPreferences = getSharedPreferences("AuthTokenPrefs", MODE_PRIVATE)
        sharedPreferencesEditor = sharedPreferences!!.edit()

        val refresh = sharedPreferences!!.getString("refresh_token", null)
        println(refresh)
        if (refresh == null) {
            sdk = YandexAuthSdk.create(YandexAuthOptions(applicationContext))
        }
        else {
            MainActivity.refreshToken = refresh
        }
    }

    companion object {
        var sdk: YandexAuthSdk? = null

        var sharedPreferences: SharedPreferences? = null
        var sharedPreferencesEditor: SharedPreferences.Editor? = null
    }
}