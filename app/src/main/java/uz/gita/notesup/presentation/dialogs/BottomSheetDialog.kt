package uz.gita.notesup.presentation.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.gita.notesup.R
import uz.gita.notesup.databinding.DialogBottomSheetBinding

class BottomSheetDialog : BottomSheetDialogFragment() {
    private val binding by viewBinding(DialogBottomSheetBinding::bind)
    private var selectedColor = "#171C26"

    companion object {
        fun getInstance(): BottomSheetDialog {
            val args = Bundle()
            val fragment = BottomSheetDialog()
            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_sheet, null)
        dialog.setContentView(view)

        val param = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behaviour = param.behavior

        if (behaviour is BottomSheetBehavior<*>) {
            behaviour.addBottomSheetCallback(object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                        behaviour.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                    TODO("Not yet implemented")
                }
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }


    private fun setListeners() {
        binding.apply {
            colorPicker1.setOnClickListener {
                tickColor1.visibility = View.VISIBLE
                tickColor2.visibility = View.GONE
                tickColor3.visibility = View.GONE
                tickColor4.visibility = View.GONE
                tickColor5.visibility = View.GONE
                tickColor6.visibility = View.GONE
                tickColor7.visibility = View.GONE
                selectedColor = "#4E33FF"

                val intent = Intent("bottom_sheet_color_picker_action")
                intent.putExtra("action", "Blue")
                intent.putExtra("selectedColor", selectedColor)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
            colorPicker2.setOnClickListener {
                tickColor1.visibility = View.GONE
                tickColor2.visibility = View.VISIBLE
                tickColor3.visibility = View.GONE
                tickColor4.visibility = View.GONE
                tickColor5.visibility = View.GONE
                tickColor6.visibility = View.GONE
                tickColor7.visibility = View.GONE
                selectedColor = "#FFD633"

                val intent = Intent("bottom_sheet_color_picker_action")
                intent.putExtra("action", "Yellow")
                intent.putExtra("selectedColor", selectedColor)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
            colorPicker3.setOnClickListener {
                tickColor1.visibility = View.GONE
                tickColor2.visibility = View.GONE
                tickColor3.visibility = View.VISIBLE
                tickColor4.visibility = View.GONE
                tickColor5.visibility = View.GONE
                tickColor6.visibility = View.GONE
                tickColor7.visibility = View.GONE
                selectedColor = "#FFFFFF"

                val intent = Intent("bottom_sheet_color_picker_action")
                intent.putExtra("action", "White")
                intent.putExtra("selectedColor", selectedColor)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
            colorPicker4.setOnClickListener {
                tickColor1.visibility = View.GONE
                tickColor2.visibility = View.GONE
                tickColor3.visibility = View.GONE
                tickColor4.visibility = View.VISIBLE
                tickColor5.visibility = View.GONE
                tickColor6.visibility = View.GONE
                tickColor7.visibility = View.GONE
                selectedColor = "#AE3B76"

                val intent = Intent("bottom_sheet_color_picker_action")
                intent.putExtra("action", "Purple")
                intent.putExtra("selectedColor", selectedColor)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
            colorPicker5.setOnClickListener {
                tickColor1.visibility = View.GONE
                tickColor2.visibility = View.GONE
                tickColor3.visibility = View.GONE
                tickColor4.visibility = View.GONE
                tickColor5.visibility = View.VISIBLE
                tickColor6.visibility = View.GONE
                tickColor7.visibility = View.GONE
                selectedColor = "#0AEBAF"

                val intent = Intent("bottom_sheet_color_picker_action")
                intent.putExtra("action", "Green")
                intent.putExtra("selectedColor", selectedColor)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
            colorPicker6.setOnClickListener {
                tickColor1.visibility = View.GONE
                tickColor2.visibility = View.GONE
                tickColor3.visibility = View.GONE
                tickColor4.visibility = View.GONE
                tickColor5.visibility = View.GONE
                tickColor6.visibility = View.VISIBLE
                tickColor7.visibility = View.GONE
                selectedColor = "#FF7746"

                val intent = Intent("bottom_sheet_color_picker_action")
                intent.putExtra("action", "Orange")
                intent.putExtra("selectedColor", selectedColor)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
            colorPicker7.setOnClickListener {
                tickColor1.visibility = View.GONE
                tickColor2.visibility = View.GONE
                tickColor3.visibility = View.GONE
                tickColor4.visibility = View.GONE
                tickColor5.visibility = View.GONE
                tickColor6.visibility = View.GONE
                tickColor7.visibility = View.VISIBLE
                selectedColor = "#202734"

                val intent = Intent("bottom_sheet_color_picker_action")
                intent.putExtra("action", "Black")
                intent.putExtra("selectedColor", selectedColor)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }

            btnImagePick.setOnClickListener {
                val intent = Intent("bottom_sheet_color_picker_action")
                intent.putExtra("action", "Image")
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
        }
    }
}