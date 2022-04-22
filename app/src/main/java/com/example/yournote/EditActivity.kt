package com.example.yournote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.yournote.database.NoteDao
import com.example.yournote.database.NoteRoomDatabase
import com.example.yournote.databinding.ActivityEditBinding
import com.example.yournote.model.Note

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    val EDIT_NOTE_EXTRA = "edit_note_extra"
    private lateinit var note: Note
    private var isUpdate = false
    private lateinit var database: NoteRoomDatabase
    private lateinit var dao: NoteDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = NoteRoomDatabase.getDatabase(applicationContext)
        dao = database.getNoteDao()

        if (intent.getParcelableExtra<Note>(EDIT_NOTE_EXTRA) != null){
            binding.buttonDelete.visibility = View.VISIBLE
            isUpdate = true
            note = intent.getParcelableExtra(EDIT_NOTE_EXTRA)!!
            binding.editTextTitle.setText(note.title)
            binding.editTextBody.setText(note.body)

            binding.editTextTitle.setSelection(note.title.length)

        }

        binding.buttonSave.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val body = binding.editTextBody.text.toString()

            if (title.isEmpty() && body.isEmpty()){
                Toast.makeText(applicationContext, "Note cannot be empty", Toast.LENGTH_SHORT).show()
            }
            else{
                if (isUpdate){
                    saveNote(Note(id = note.id, title = title, body = body))
                }
                else{
                    saveNote(Note(title = title, body = body))
                }
            }
            finish()
        }

        binding.buttonDelete.setOnClickListener {
            deleteNote(note)
            finish()
        }
    }

    private fun saveNote(note: Note){
        if (dao.getById(note.id).isEmpty()){
            dao.insert(note)
        }
        else{
            dao.update(note)
        }
        Toast.makeText(applicationContext, "Note saved", Toast.LENGTH_SHORT).show()
    }

    private fun deleteNote(note: Note){
        dao.delete(note)
        Toast.makeText(applicationContext, "Note removed", Toast.LENGTH_SHORT).show()
    }
}