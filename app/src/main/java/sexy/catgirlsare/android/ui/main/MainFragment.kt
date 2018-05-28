package sexy.catgirlsare.android.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.toast
import kotlinx.android.synthetic.main.main_fragment.*
import sexy.catgirlsare.android.R
import sexy.catgirlsare.android.prefs

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.main_fragment, container, false)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        if (prefs.getString("key", "").isEmpty()) {
            loginContainer.isVisible = true
        }

        loginButton.setOnClickListener {
            viewModel.attemptLogin(usernameField.text.toString(), passwordField.text.toString())
        }

        viewModel.message.observe(this::getLifecycle) { message ->
            if (message == null || message.isBlank()) return@observe
            context?.toast(message)
        }
    }
}
