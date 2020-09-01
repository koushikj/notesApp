package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class EditNote extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ArrayList<String> notes = new ArrayList<>();
    EditText textView;
    String oldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        try {
            sharedPreferences = this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
            notes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("notes", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = getIntent();
        String note = intent.getStringExtra("note");
        oldText = note;
        boolean newNote = intent.getBooleanExtra("newNote", false);
        textView = findViewById(R.id.noteId);
        if (newNote) {
            textView.setText("");
        } else {
            notes.remove(note);
            textView.setText(note);
        }
    }

    @Override
    public void onBackPressed() {
        addNote(textView.getText().toString());
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        //MainActivity.adapter.notifyDataSetChanged();
        //super.onBackPressed();
    }


//    @Override
//    public void onBackPressed() {
//        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
//    }
    private void addNote(String note) {
        if (!note.equals("")) {
            notes.add(note);
            try {
                sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(notes)).apply();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}