package sexy.catgirlsare.android.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import androidx.core.content.edit
import sexy.catgirlsare.android.api.login
import sexy.catgirlsare.android.prefs
import kotlin.concurrent.thread

class LoginViewModel : ViewModel() {

    val message: LiveData<String> = MutableLiveData()
    private val mutableMessage: MutableLiveData<String>
        get() = message as MutableLiveData<String>

    fun attemptLogin(username: String, password: String) {
        thread {
            val response = login(username, password)

            val body = response.body()

            mutableMessage.postValue(body?.data?.message ?: "An unknown error has occurred")

            if (body?.data?.key != null) prefs.edit {
                putString("key", body.data.key)
            }
        }
    }
}
