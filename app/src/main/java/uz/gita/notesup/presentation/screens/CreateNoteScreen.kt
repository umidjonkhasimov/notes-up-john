package uz.gita.notesup.presentation.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import uz.gita.notesup.BaseFragment
import uz.gita.notesup.R
import uz.gita.notesup.data.database.NotesDatabase
import uz.gita.notesup.databinding.ScreenCreateNoteBinding
import uz.gita.notesup.data.entities.Notes
import uz.gita.notesup.presentation.dialogs.BottomSheetDialog
import uz.gita.notesup.utils.popBackStack
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date

class CreateNoteScreen : BaseFragment(), EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    private val binding by viewBinding(ScreenCreateNoteBinding::bind)
    private val database = NotesDatabase.getDatabase().getDao()
    private lateinit var currentDate: String
    private lateinit var hideActionBarListener: ((Boolean) -> Unit)
    private var selectedColor = "#171C26"
    private var selectedImgPath = ""
    private var READ_STORAGE_PERM = 123
    private var REQUEST_CODE_IMAGE = 456
    private var id: Int? = null
    private var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        id = arguments?.getInt("id", -1)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_create_note, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnDeleteClickListener()
        if (id != null && id != -1) {
            isEdit = true
            launch {
                val note = database.getNotesById(id!!)
                binding.apply {
                    etNoteTitle.setText(note.title)
                    etNoteSubtitle.setText(note.subtitle)
                    etNoteDesc.setText(note.noteText)
                    tvDateTime.text = note.dateTime
                    selectedColor = note.color!!
                    val image = note.imgPath
                    if (!image.isNullOrEmpty()) {
                        imgCreateNoteLayout.visibility = View.VISIBLE
                        imgCreateNote.setImageBitmap(BitmapFactory.decodeFile(note.imgPath))
                    } else {
                        imgCreateNoteLayout.visibility = View.GONE
                    }

                    if (!(note.color.isNullOrEmpty()))
                        pickedColorView.setBackgroundColor(Color.parseColor(note.color))
                    else
                        pickedColorView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_black))
                }
            }
        }

        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(broadcastReceiver, IntentFilter("bottom_sheet_color_picker_action"))

        binding.pickedColorView.setBackgroundColor(Color.parseColor(selectedColor))

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        currentDate = sdf.format(Date())
        binding.apply {
            if (isEdit) {
                btnTick.setOnClickListener {
                    if (updateNote(id!!)) {
                        popBackStack()
                    }
                }
            } else {
                tvDateTime.text = currentDate
                btnTick.setOnClickListener {
                    if (saveNote()) {
                        popBackStack()
                    }
                }
            }
            btnBack.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
            btnMore.setOnClickListener {
                val bottomSheetDialog = BottomSheetDialog.getInstance()
                bottomSheetDialog.show(requireActivity().supportFragmentManager, "Note Bottom Sheet Dialog")
            }
        }
    }

    private fun setOnDeleteClickListener() {
        binding.apply {
            btnDelete.setOnClickListener {
                selectedImgPath = ""
                imgCreateNote.setImageResource(0)
                imgCreateNoteLayout.visibility = View.GONE
            }
        }
    }

    private fun updateNote(id: Int): Boolean {
        binding.apply {
            if (etNoteTitle.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Note Title is Required", Toast.LENGTH_SHORT).show()
                return false
            } else if (etNoteDesc.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Note Description cannot be empty", Toast.LENGTH_SHORT).show()
                return false
            }

            launch {
                val notes = Notes()
                notes.id = id
                notes.title = etNoteTitle.text.toString()
                notes.subtitle = etNoteSubtitle.text.toString()
                notes.noteText = etNoteDesc.text.toString()
                notes.dateTime = currentDate
                notes.color = selectedColor
                notes.imgPath = selectedImgPath

                database.insertNotes(notes)
            }
        }
        return true
    }


    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent!!.getStringExtra("action")
            when (action) {
                "Blue" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    binding.pickedColorView.setBackgroundColor(Color.parseColor(selectedColor))
                }

                "Yellow" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    binding.pickedColorView.setBackgroundColor(Color.parseColor(selectedColor))
                }

                "White" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    binding.pickedColorView.setBackgroundColor(Color.parseColor(selectedColor))
                }

                "Purple" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    binding.pickedColorView.setBackgroundColor(Color.parseColor(selectedColor))
                }

                "Green" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    binding.pickedColorView.setBackgroundColor(Color.parseColor(selectedColor))
                }

                "Orange" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    binding.pickedColorView.setBackgroundColor(Color.parseColor(selectedColor))
                }

                "Black" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    binding.pickedColorView.setBackgroundColor(Color.parseColor(selectedColor))
                }

                "Image" -> {
                    readStorageTask()
                }
            }
        }
    }

    private fun saveNote(): Boolean {
        binding.apply {
            if (etNoteTitle.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Note Title is Required", Toast.LENGTH_SHORT).show()
                return false
            } else if (etNoteDesc.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Note Description cannot be empty", Toast.LENGTH_SHORT).show()
                return false
            }

            launch {
                val notes = Notes()
                notes.title = etNoteTitle.text.toString()
                notes.subtitle = etNoteSubtitle.text.toString()
                notes.noteText = etNoteDesc.text.toString()
                notes.dateTime = currentDate
                notes.color = selectedColor
                notes.imgPath = selectedImgPath
                database.insertNotes(notes)
            }
        }
        return true
    }

    private fun hasReadStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.hasPermissions(requireContext(), Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            EasyPermissions.hasPermissions(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun readStorageTask() {
        if (hasReadStoragePermission()) {
            pickImageFromGallery()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                EasyPermissions.requestPermissions(
                    requireActivity(),
                    getString(R.string.storage_read_perm_text),
                    READ_STORAGE_PERM,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            } else {
                EasyPermissions.requestPermissions(
                    requireActivity(),
                    getString(R.string.storage_read_perm_text),
                    READ_STORAGE_PERM,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if ((intent.resolveActivity(requireActivity().packageManager) != null)) {
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }

    private fun getImageFromUri(contentUri: Uri): String? {
        var filePath: String? = null
        val cursor = requireActivity().contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path
        } else {
            cursor.moveToFirst()
            var index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                val selectedImageUri = data.data
                if (selectedImageUri != null) {
                    try {
                        val inputStream = requireActivity().contentResolver.openInputStream(selectedImageUri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        binding.imgCreateNote.setImageBitmap(bitmap)
                        binding.imgCreateNoteLayout.visibility = View.VISIBLE

                        selectedImgPath = getImageFromUri(selectedImageUri)!!
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, requireActivity())
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(requireActivity()).build().show()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
//        TODO("Not yet implemented")
    }

    override fun onRationaleAccepted(requestCode: Int) {
//        TODO("Not yet implemented")
    }

    override fun onRationaleDenied(requestCode: Int) {
//        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }
}