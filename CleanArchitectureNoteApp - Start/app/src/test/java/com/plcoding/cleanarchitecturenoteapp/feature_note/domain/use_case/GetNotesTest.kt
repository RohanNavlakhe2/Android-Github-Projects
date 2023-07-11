package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case

import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class GetNotesTest {

    private lateinit var notesUseCases: NoteUseCases
    private lateinit var repository: FakeRepository


    @Before
    fun before() {
        repository = FakeRepository()
        notesUseCases = NoteUseCases(
            getNote = GetNote(repository),
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository)
        )
    }

    @Test
    fun `getNotesOrder`() = runBlocking {
        assertEquals(notesUseCases.getNotes().single().size, 26)
    }
}