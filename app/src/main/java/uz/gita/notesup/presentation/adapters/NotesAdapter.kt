package uz.gita.notesup.presentation.adapters

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.gita.notesup.R
import uz.gita.notesup.databinding.ItemNoteBinding
import uz.gita.notesup.data.entities.Notes

class NotesAdapter() : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    private var list = ArrayList<Notes>()
    private lateinit var onItemClickListener: ((Notes) -> Unit)
    private lateinit var onItemLongClickListener: ((Notes) -> Unit)

    fun setOnItemClickListener(action: (Notes) -> Unit) {
        onItemClickListener = action
    }

    fun setOnItemLongClickListener(action: (Notes) -> Unit) {
        onItemLongClickListener = action
    }

    fun submitList(list: List<Notes>) {
        this.list = list as ArrayList<Notes>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder = NotesViewHolder(
        ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) = holder.bind()

    inner class NotesViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnLongClickListener {
                onItemLongClickListener.invoke(list[adapterPosition])
                true
            }
            binding.root.setOnClickListener {
                onItemClickListener.invoke(list[adapterPosition])
            }
        }

        fun bind() {
            binding.apply {
                tvTitle.text = list[adapterPosition].title
                tvDesc.text = list[adapterPosition].noteText
                tvDateTime.text = list[adapterPosition].dateTime
                val color = list[adapterPosition].color
                if (color != null)
                    noteBackground.setCardBackgroundColor(Color.parseColor(color))
                else
                    noteBackground.setCardBackgroundColor(Color.parseColor((R.color.light_black).toString()))
                if (list[adapterPosition].imgPath != null) {
                    imgNote.setImageBitmap(BitmapFactory.decodeFile(list[adapterPosition].imgPath))
                    imgNote.visibility = View.VISIBLE
                } else {
                    imgNote.visibility = View.GONE
                }
            }
        }
    }
}