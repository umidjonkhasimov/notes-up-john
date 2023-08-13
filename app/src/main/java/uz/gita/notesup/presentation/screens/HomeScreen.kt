package uz.gita.notesup.presentation.screens

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import uz.gita.notesup.BaseFragment
import uz.gita.notesup.R
import uz.gita.notesup.data.database.NotesDatabase
import uz.gita.notesup.data.entities.Notes
import uz.gita.notesup.databinding.ScreenHomeBinding
import uz.gita.notesup.presentation.adapters.NotesAdapter
import uz.gita.notesup.utils.replaceFragment
import uz.gita.notesup.utils.replaceFragmentSaveStack


class HomeScreen : BaseFragment(), NavigationView.OnNavigationItemSelectedListener {
    private val binding by viewBinding(ScreenHomeBinding::bind)
    private lateinit var adapter: NotesAdapter
    private var database = NotesDatabase.getDatabase().getDao()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.searchView.apply {
            clearFocus()
            setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = true

                @SuppressLint("NotifyDataSetChanged")
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (!(newText.isNullOrEmpty())) {
                        binding.apply {
                            launch {
                                val list = database.search(newText)
                                if (list.isEmpty()) {
                                    noResultPlaceholder.visibility = View.VISIBLE
                                    emptyListPlaceholder.visibility = View.GONE
                                    mRecyclerView.visibility = View.GONE
                                } else {
                                    noResultPlaceholder.visibility = View.GONE
                                    emptyListPlaceholder.visibility = View.GONE
                                    mRecyclerView.visibility = View.VISIBLE
                                    adapter.submitList(list)
                                    adapter.notifyDataSetChanged()
                                }
                            }
                        }
                    } else {
                        launch {
                            val list = database.getAllNotes()
                            if (list.isEmpty()) {
                                binding.noResultPlaceholder.visibility = View.GONE
                                binding.mRecyclerView.visibility = View.GONE
                                binding.emptyListPlaceholder.visibility = View.VISIBLE
                            } else {
                                binding.noResultPlaceholder.visibility = View.GONE
                                binding.emptyListPlaceholder.visibility = View.GONE
                                binding.mRecyclerView.visibility = View.VISIBLE
                                adapter.submitList(database.getAllNotes())
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                    return true
                }

            })

        }

        binding.apply {
            btnDrawerMenu.setOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }
            navView.setNavigationItemSelectedListener(this@HomeScreen)
        }

        binding.apply {
            mRecyclerView.setHasFixedSize(true)
            mRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = NotesAdapter()

            launch {
                var list = database.getAllNotes()
                if (list.isEmpty()) {
                    noResultPlaceholder.visibility = View.GONE
                    mRecyclerView.visibility = View.GONE
                    emptyListPlaceholder.visibility = View.VISIBLE
                } else {
                    noResultPlaceholder.visibility = View.GONE
                    emptyListPlaceholder.visibility = View.GONE
                    mRecyclerView.visibility = View.VISIBLE
                }
                adapter.submitList(list)
                mRecyclerView.adapter = adapter
            }

            adapter.setOnItemLongClickListener { note ->
                showLongClickDialog(note)
            }

            adapter.setOnItemClickListener { note ->
                val fragment = CreateNoteScreen()
                val bundle = Bundle()
                bundle.putInt("id", note.id)
                fragment.arguments = bundle
                replaceFragmentSaveStack(fragment, true)
            }

            btnAddNote.setOnClickListener {
                replaceFragmentSaveStack(CreateNoteScreen(), true)
            }
        }
    }

    private fun showLongClickDialog(note: Notes) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_bottom_sheet_actions)
        dialog.setCancelable(true)

        val btnEdit = dialog.findViewById<LinearLayout>(R.id.btn_edit_note)
        val btnDelete = dialog.findViewById<LinearLayout>(R.id.btn_delete_note)

        btnEdit.setOnClickListener {
            val fragment = CreateNoteScreen()
            val bundle = Bundle()
            bundle.putInt("id", note.id)
            fragment.arguments = bundle
            replaceFragmentSaveStack(fragment, true)
            dialog.dismiss()
        }

        btnDelete.setOnClickListener {
            dialog.dismiss()
            val deleteDialog = Dialog(requireContext())
            deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            deleteDialog.setContentView(R.layout.dialog_delete_note)
            deleteDialog.setCancelable(true)

            val btnNo = deleteDialog.findViewById<Button>(R.id.btn_negative)
            val btnYes = deleteDialog.findViewById<Button>(R.id.btn_positive)

            btnNo.setOnClickListener {
                deleteDialog.dismiss()
            }
            btnYes.setOnClickListener {
                launch {
                    database.moveToTrash(note.id)
                    val list = database.getAllNotes()
                    if (list.isEmpty()) {
                        binding.noResultPlaceholder.visibility = View.GONE
                        binding.mRecyclerView.visibility = View.GONE
                        binding.emptyListPlaceholder.visibility = View.VISIBLE
                    } else {
                        binding.noResultPlaceholder.visibility = View.GONE
                        binding.emptyListPlaceholder.visibility = View.GONE
                        binding.mRecyclerView.visibility = View.VISIBLE
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

            R.id.nav_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text))
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
                startActivity(Intent.createChooser(intent, "Share via:"))
            }

            R.id.nav_contact -> {
                val selectorIntent = Intent(Intent.ACTION_SENDTO)
                selectorIntent.data = Uri.parse("mailto:")

                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("umidjon.khasimov@gmail.com"))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
                emailIntent.putExtra(Intent.EXTRA_TEXT, "")
                emailIntent.selector = selectorIntent

                startActivity(Intent.createChooser(emailIntent, "Send email..."))
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}
