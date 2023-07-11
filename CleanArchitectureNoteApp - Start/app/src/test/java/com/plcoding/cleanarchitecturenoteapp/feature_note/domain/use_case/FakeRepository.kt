package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case

import androidx.compose.ui.text.toUpperCase
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import java.util.*

class FakeRepository:NoteRepository {

    private val notes  = mutableListOf<Note>()

    init {
        ('a'..'z').forEachIndexed { index, alphabet ->
            notes.add(Note(
                title = alphabet.toString().toUpperCase(Locale.getDefault()),
                content = alphabet.toString(),
                timestamp = index.toLong(),
                color = index
            ))
        }

    }

    override fun getNotes(): Flow<List<Note>> = flow { emit(notes) }

    override suspend fun getNoteById(id: Int): Note? {
        return notes.find { note ->
            note.id == id
        }
    }

    override suspend fun insertNote(note: Note) {
         notes.add(note)
    }

    override suspend fun deleteNote(note: Note) {
        notes.remove(note)
    }
}