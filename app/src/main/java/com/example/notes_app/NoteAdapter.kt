package com.example.notes_app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_app.databinding.NoteItemBinding
import com.example.notes_app.models.NoteResponse

class NoteAdapter(private val onNoteClicked: (NoteResponse) -> Unit) :
    ListAdapter<NoteResponse, NoteAdapter.NoteViewHolder>(ComparatorDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        note?.let {
            holder.bind(note)
        }
    }

    inner class NoteViewHolder(private val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: NoteResponse) {
            binding.title.text = note.title
            binding.desc.text = note.description
            binding.root.setOnClickListener {
                onNoteClicked(note)
            }
        }
    }

    class ComparatorDiffUtils() : DiffUtil.ItemCallback<NoteResponse>() {
        override fun areItemsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
            return oldItem == newItem
        }

    }
}