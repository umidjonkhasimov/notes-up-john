package uz.gita.notesup.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import uz.gita.notesup.R
import uz.gita.notesup.utils.replaceFragment

@SuppressLint("CustomSplashScreen")
class SplashScreen : Fragment(R.layout.screen_splash) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            replaceFragment(HomeScreen(), true)
        }, 1500)
    }
}