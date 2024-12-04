package inc.conferatus.grocerysenpai.api

import inc.conferatus.grocerysenpai.GrocerySenpaiApp
import java.time.Instant

object AuthService {
    lateinit var accessToken: String

    var refreshToken: String? = null
        get() = field ?: GrocerySenpaiApp.sharedPreferences!!.getString(
            "refresh_token", null
        )
        set(value) {
            field = value
            GrocerySenpaiApp.sharedPreferencesEditor!!.putString("refresh_token", value).apply()
        }

    var lastUpdatedTime: Instant = Instant.now().minusSeconds(10000)

//    var lastUpdatedTime: Instant? = Instant.EPOCH
//        get() = field ?: Instant.parse(
//            GrocerySenpaiApp.sharedPreferences!!.getString(
//                "last_fetch_time", null
//            )
//        )
//        set(value) {
//            field = value
//            GrocerySenpaiApp.sharedPreferencesEditor!!.putString(
//                "last_fetch_time",
//                value.toString()
//            ).apply()
//        }
}