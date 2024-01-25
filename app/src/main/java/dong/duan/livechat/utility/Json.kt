package dong.duan.livechat.utility

import android.os.Build
import android.os.Bundle
import com.google.gson.Gson
import java.io.Serializable

fun <A> String.fromJson(type: Class<A>): A {
    return Gson().fromJson(this, type)
}
fun <A> A.toJson(): String? {
    return Gson().toJson(this)
}

@Suppress("DEPRECATION")
inline fun <reified T : Serializable> Bundle.getSerializable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) getSerializable(key, T::class.java)
    else getSerializable(key) as? T
}