package uz.gita.notesup.presentation.screens

import android.app.ActionBar
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import uz.gita.notesup.BaseFragment
import uz.gita.notesup.R
import uz.gita.notesup.data.database.NotesDatabase
import uz.gita.notesup.data.entities.Notes
import uz.gita.notesup.databinding.ScreenTrashBinding
import uz.gita.notesup.presentation.adapters.NotesAdapter
import uz.gita.notesup.utils.replaceFragment
import uz.gita.notesup.utils.replaceFragmentSaveStack

class TrashScreen : BaseFragment(), NavigationView.OnNavigationItemSelectedListener {
    private val database = NotesDatabase.getDatabase().getDao()
    private val binding by viewBinding(ScreenTrashBinding::bind)
    private lateinit var adapter: NotesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_trash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NotesAdapter()

        adapter.setOnItemClickListener {

        }

        adapter.setOnItemLongClickListener { note ->
            showLongClickDialog(note)
        }

        binding.apply {
            btnDelete.setOnClickListener {
                val deleteDialog = Dialog(requireContext())
                deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                deleteDialog.setContentView(R.layout.dialog_delete_note)
                deleteDialog.setCancelable(true)

                val title = deleteDialog.findViewById<TextView>(R.id.tv_title_edit)
                title.text = "Permanently delete all?"
                val btnNo = deleteDialog.findViewById<Button>(R.id.btn_negative)
                val btnYes = deleteDialog.findViewById<Button>(R.id.btn_positive)

                btnNo.setOnClickListener {
                    deleteDialog.dismiss()
                }
                btnYes.setOnClickListener {
                    launch {
                        database.deleteAllTrashedNotes()
                        val list = database.getAllTrashedNotes()
                        if (list.isEmpty()) {
                            emptyListPlaceholder.visibility = View.VISIBLE
                            mRecyclerView.visibility = View.GONE
                        } else {
                            emptyListPlaceholder.visibility = View.GONE
                            mRecyclerView.visibility = View.VISIBLE
                        }
                        adapter.submitList(list)
                        adapter.notifyDataSetChanged()
                    }
                    deleteDialog.dismiss()
                }

                deleteDialog.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
                deleteDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                deleteDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
                deleteDialog.window?.setGravity(Gravity.BOTTOM)
                deleteDialog.show()
            }

            btnDrawerMenu.setOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }
            navView.setNavigationItemSelectedListener(this@TrashScreen)
        }

        binding.apply {
            launch {
                val list = database.getAllTrashedNotes()
                if (list.isEmpty()) {
                    emptyListPlaceholder.visibility = View.VISIBLE
                    mRecyclerView.visibility = View.GONE
                } else {
                    emptyListPlaceholder.visibility = View.GONE
                    mRecyclerView.visibility = View.VISIBLE
                }
                adapter.submitList(list)
                adapter.notifyDataSetChanged()
                mRecyclerView.setHasFixedSize(true)
                mRecyclerView.adapter = adapter
                mRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            }
        }
    }

    private fun showLongClickDialog(note: Notes) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_bottom_sheet_actions)
        dialog.setCancelable(true)

        val btnRestore = dialog.findViewById<LinearLayout>(R.id.btn_edit_note)
        val btnDelete = dialog.findViewById<LinearLayout>(R.id.btn_delete_note)

        val iconRestore = dialog.findViewById<ImageView>(R.id.ic_img1)
        val iconDelete = dialog.findViewById<ImageView>(R.id.ic_img2)

        val textRestore = dialog.findViewById<TextView>(R.id.tv_title_edit)
        val textDelete = dialog.findViewById<TextView>(R.id.tv_title_delete)

        textRestore.text = "Restore note"
        textDelete.text = "Delete permanently"

        iconRestore.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_restore))
        iconDelete.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete))

        btnRestore.setOnClickListener {
            launch {
                database.restoreNote(note.id)
                adapter.submitList(database.getAllTrashedNotes())
                adapter.notifyDataSetChanged()
            }
            dialog.dismiss()
        }

        btnDelete.setOnClickListener {
            dialog.dismiss()
            val deleteDialog = Dialog(requireContext())
            deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            deleteDialog.setContentView(R.layout.dialog_delete_note)
            deleteDialog.setCancelable(true)

            val title = deleteDialog.findViewById<TextView>(R.id.tv_title_edit)
            title.text = "Delete permanently?"
            val btnNo = deleteDialog.findViewById<Button>(R.id.btn_negative)
            val btnYes = deleteDialog.findViewById<Button>(R.id.btn_positive)

            btnNo.setOnClickListener {
                deleteDialog.dismiss()
            }
            btnYes.setOnClickListener {
                launch {
                    database.deleteNote(note)
                    adapter.submitList(database.getAllTrashedNotes())
                    adapter.notifyDataSetChanged()
                }
                deleteDialog.dismiss()
            }

            deleteDialog.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
            deleteDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            deleteDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            deleteDialog.window?.setGravity(Gravity.BOTTOM)
            deleteDialog.show()
        }
        dialog.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_all -> {
                Log.d("GGG", "onNavigationItemSelected: nav_all")
                replaceFragment(HomeScreen(), true)
            }

            R.id.nav_trash -> {
                Log.d("GGG", "onNavigationItemSelected: nav_trash")
                replaceFragment(TrashScreen(), true)
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
