package com.example.notes_app.api

import com.example.notes_app.models.NoteRequest
import com.example.notes_app.models.NoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotesApi {
    // these all are Authenticated endpoints which means we have to add authorization token when network calling

    @GET("/note")
    suspend fun getNotes(): Response<List<NoteResponse>>

    @POST("/note")
    suspend fun createNote(@Body noteRequest: NoteRequest): Response<NoteResponse>

    // we use 'PUT' for dynamic endpoint for updating and for the value we use them in '{}'
    // and in parameter we have to
    // tell retrofit that endpoint and we use "Path" for that and it take variable name as parameter
    @PUT("/note/{noteId}")
    suspend fun updateNote(@Path("noteId") noteId: String, @Body noteRequest: NoteRequest): Response<NoteResponse>

    @DELETE("/note/{noteId}")
    suspend fun deleteNote(@Path("noteId") noteId: String): Response<NoteResponse>
}