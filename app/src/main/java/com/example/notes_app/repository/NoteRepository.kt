package com.example.notes_app.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notes_app.api.NotesApi
import com.example.notes_app.models.NoteRequest
import com.example.notes_app.models.NoteResponse
import com.example.notes_app.utils.Constants.TAG
import com.example.notes_app.utils.NetworkResult
import com.example.notes_app.utils.ResponseHandler
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val notesApi: NotesApi) {

    private val _notesResponseLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val notesResponseLiveData: LiveData<NetworkResult<List<NoteResponse>>>
        get() = _notesResponseLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData: LiveData<NetworkResult<String>>
        get() = _statusLiveData


    private val responseHandler = ResponseHandler()

    suspend fun getNotes() {
        _notesResponseLiveData.postValue(NetworkResult.Loading())
        val response = notesApi.getNotes()
        Log.e(TAG, "getNotes: ${response.body()} ")
        _notesResponseLiveData.postValue(responseHandler.handleUserResponse(response))
    }

    suspend fun updateNote(noteId: String, noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = notesApi.updateNote(noteId, noteRequest)
        handleNoteResponse(response, "Success,Update")
    }

    suspend fun createNote(noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = notesApi.createNote(noteRequest)
        handleNoteResponse(response, "Success Create")
    }

    suspend fun deleteNote(noteId: String) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = notesApi.deleteNote(noteId)
        handleNoteResponse(response, "Success Delete")
    }

    private fun handleNoteResponse(response: Response<NoteResponse>, message: String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(message))
        } else {
            _statusLiveData.postValue(NetworkResult.Error("Failure"))
        }
    }

}