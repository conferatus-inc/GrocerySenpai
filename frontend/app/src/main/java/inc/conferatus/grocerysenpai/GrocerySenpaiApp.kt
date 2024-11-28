package inc.conferatus.grocerysenpai

import android.app.Application
import android.content.Context
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class GrocerySenpaiApp : Application() {

    override fun onCreate() {
        super.onCreate()
        sdk = YandexAuthSdk.create(YandexAuthOptions(applicationContext))
    }

    companion object {
        var sdk: YandexAuthSdk? = null
    }
}