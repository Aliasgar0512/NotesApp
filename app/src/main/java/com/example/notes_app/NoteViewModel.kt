package com.example.notes_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes_app.models.NoteRequest
import com.example.notes_app.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {

    val statusLiveData get() = noteRepository.statusLiveData
    val noteResponseLiveData get() = noteRepository.notesResponseLiveData

    fun getNotes() {
        viewModelScope.launch {
            noteRepository.getNotes()
        }
    }

    fun createNote(noteRequest: NoteRequest) {
        viewModelScope.launch {
            noteRepository.createNote(noteRequest)
        }
    }

    fun deleteNotes(noteId: String) {
        viewModelScope.launch {
            noteRepository.deleteNote(noteId)
        }
    }

    fun updateNote(noteId: String, noteRequest: NoteRequest) {
        viewModelScope.launch {
            noteRepository.updateNote(noteId, noteRequest)
        }
    }
}