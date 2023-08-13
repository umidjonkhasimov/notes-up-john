package uz.gita.notesup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.gita.notesup.presentation.screens.SplashScreen
import uz.gita.notesup.utils.addFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addFragment(SplashScreen(), false)
    }
}