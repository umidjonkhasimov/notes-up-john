package uz.gita.notesup.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import uz.gita.notesup.R


fun FragmentActivity.addFragment(fm: Fragment, isTransition: Boolean) {
    val fragmentTransition = supportFragmentManager.beginTransaction()

    if (isTransition) {
        fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.slide_in_left)
    }

    fragmentTransition
        .add(R.id.fragment_container, fm)
        .commit()
}

fun FragmentActivity.replaceFragment(fm: Fragment, isTransition: Boolean) {
    val fragmentTransition = supportFragmentManager.beginTransaction()

    if (isTransition) {
        fragmentTransition.setCustomAnimations(
            androidx.appcompat.R.anim.abc_fade_in,
            androidx.appcompat.R.anim.abc_fade_out,
            androidx.appcompat.R.anim.abc_fade_in,
            androidx.appcompat.R.anim.abc_fade_out,
        )
    }

    fragmentTransition
        .replace(R.id.fragment_container, fm)
        .commit()
}

fun FragmentActivity.replaceFragmentSaveStack(fm: Fragment, isTransition: Boolean) {
    val fragmentTransition = supportFragmentManager.beginTransaction()

    if (isTransition) {
        fragmentTransition.setCustomAnimations(
//            androidx.appcompat.R.anim.abc_popup_enter,
//            androidx.appcompat.R.anim.abc_popup_exit,
//            androidx.appcompat.R.anim.abc_popup_enter,
//            androidx.appcompat.R.anim.abc_popup_exit,
            androidx.appcompat.R.anim.abc_fade_in,
            androidx.appcompat.R.anim.abc_fade_out,
            androidx.appcompat.R.anim.abc_fade_in,
            androidx.appcompat.R.anim.abc_fade_out,
//            android.R.anim.slide_in_left,
//            android.R.anim.slide_out_right,
//            android.R.anim.slide_in_left,
//            android.R.anim.slide_out_right
        )
    }

    fragmentTransition
        .replace(R.id.fragment_container, fm)
        .addToBackStack(fm::class.java.name)
        .commit()
}

fun FragmentActivity.popBackStack() {
    if (supportFragmentManager.backStackEntryCount == 0) finish()
    else supportFragmentManager.popBackStack()
}


fun Fragment.addFragment(fm: Fragment, isTransition: Boolean) {
    requireActivity().addFragment(fm, isTransition)
}

fun Fragment.replaceFragmentSaveStack(fm: Fragment, isTransition: Boolean) {
    requireActivity().replaceFragmentSaveStack(fm, isTransition)
}

fun Fragment.replaceFragment(fm: Fragment, isTransition: Boolean) {
    requireActivity().replaceFragment(fm, isTransition)
}

fun Fragment.popBackStack() {
    requireActivity().popBackStack()
}