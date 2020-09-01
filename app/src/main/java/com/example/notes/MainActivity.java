package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{

    RecyclerView notesRecyclerView;
    static MyRecyclerViewAdapter adapter;
    static ArrayList<String> notes = new ArrayList<>();

    SharedPreferences sharedPreferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.addNote){
            Intent intent = new Intent(getApplicationContext(), EditNote.class);
            intent.putExtra("newNote",true);
            startActivity(intent);
            return true;
        }else if(item.getItemId()==R.id.deleteAll){
            new AlertDialog.Builder(this)
                    .setTitle("Delete Notes")
                    .setMessage("Are you sure you want to delete all the notes?")
                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            deleteAll();
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        try {
            notes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("notes",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        notesRecyclerView = findViewById(R.id.notesList);
        addNote();
        //addNote("Welcome user!");
        //addNote("New note!");
    }

    private void addNote() {
        //notes.add(note);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this,notes);
        adapter.setClickListener(this);
        notesRecyclerView.setAdapter(adapter);
        try {
            sharedPreferences.edit().putString("notes",ObjectSerializer.serialize(notes)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void deleteNote(int position) {
        notes.remove(position);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this,notes);
        adapter.setClickListener(this);
        notesRecyclerView.setAdapter(adapter);
        try {
            sharedPreferences.edit().putString("notes",ObjectSerializer.serialize(notes)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void deleteAll() {
        notes.clear();
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this,notes);
        adapter.setClickListener(this);
        notesRecyclerView.setAdapter(adapter);
        try {
            sharedPreferences.edit().putString("notes",ObjectSerializer.serialize(notes)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "Note Clicked"+adapter.getItem(position), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), EditNote.class);
        intent.putExtra("newNote",false);
        intent.putExtra("note",adapter.getItem(position));
        startActivity(intent);
    }

    @Override
    public void onLongClick(View view, final int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        deleteNote(position);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}