package com.agrima.spendzyy.data.repository

import com.agrima.spendzyy.data.local.dao.NoteDao
import com.agrima.spendzyy.data.local.entity.NoteEntity

class NoteRepository(
    private val noteDao: NoteDao
) {
    fun getAllNotes() = noteDao.getAllNotes()

    suspend fun addNote(note: NoteEntity) {
        noteDao.insertNote(note)
    }

    suspend fun deleteNote(note: NoteEntity) {
        noteDao.deleteNote(note)
    }
    suspend fun updateNote(note: NoteEntity) {
        noteDao.updateNote(note)
    }
}
