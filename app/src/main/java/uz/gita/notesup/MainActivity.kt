package uz.gita.notesup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.navigation.NavigationView
import uz.gita.notesup.databinding.ActivityMainBinding
import uz.gita.notesup.presentation.screens.HomeScreen
import uz.gita.notesup.presentation.screens.SplashScreen
import uz.gita.notesup.presentation.screens.TrashScreen
import uz.gita.notesup.utils.addFragment
import uz.gita.notesup.utils.replaceFragment

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addFragment(SplashScreen(), false)
    }
}