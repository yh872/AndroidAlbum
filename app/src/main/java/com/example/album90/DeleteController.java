package com.example.album90;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.album90.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DeleteController extends AppCompatActivity {
    private Button Delete;
    private EditText album_name;
    private Button BackButton;

    private ListView albums;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> albumnames = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        BackButton = findViewById(R.id.backButton2);
        Delete = findViewById(R.id.deleteButton);
        album_name = findViewById(R.id.album_name2);
        albums = findViewById(R.id.listofalbs);
        if (!User.listofAlbums.isEmpty()) {
            for (Album a : User.listofAlbums) {
                albumnames.add(a.albumName);
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albumnames);
            albums.setAdapter(adapter);
        }

    }

    public void Back(View view){
        Intent intent = new Intent(this, Photos.class);
        startActivity(intent);
    }

    public void Delete(View view){

        Context context = view.getContext();
        Editable text = album_name.getText();
        String name = text.toString();
        if (name.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Error");
            builder.setMessage("Name not found.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return;
        }
        for (Album a: User.listofAlbums){
            if (a.albumName.equals(name)){
                User.listofAlbums.remove(a);
                try {
                    Photos.WriteToFile();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Success");
                    builder.setMessage("Album has been successfully deleted.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(context, Photos.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error");
        builder.setMessage("Album does not exist.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return;

    }

}
