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

import java.io.IOException;
import java.util.ArrayList;

public class MoveController extends AppCompatActivity {

    public static Photo photo = null;
    private Button Back;
    private Button Move;
    private ListView albums;
    private EditText album_name;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> albumnames = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);
        Back = findViewById(R.id.backButton100);
        Move = findViewById(R.id.moveButton);
        albums = findViewById(R.id.allAlbs);
        album_name = findViewById(R.id.albumName);
        if (!User.listofAlbums.isEmpty()) {
            for (Album a : User.listofAlbums) {
                if (!a.albumName.equals(AlbumController.currentAlbum.albumName)) {albumnames.add(a.albumName);}
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albumnames);
            albums.setAdapter(adapter);
        }
    }
    public void Back_Clicked(View view){
        Intent intent = new Intent(this, AlbumController.class);
        startActivity(intent);
    }
    public void Move(View view){
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
                a.listofPhotos.add(photo);
                AlbumController.currentAlbum.removePhoto(photo);
                try {
                    Photos.WriteToFile();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Success");
                    builder.setMessage("Photo has been successfully moved.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(context, AlbumController.class);
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