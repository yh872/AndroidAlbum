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

import java.util.ArrayList;

public class OpenController  extends AppCompatActivity {
    private Button Open;
    private EditText album_name;
    private Button BackButton;

    private ListView albums;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> albumnames = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        Open = findViewById(R.id.open);
        album_name = findViewById(R.id.albname);
        BackButton = findViewById(R.id.backButton4);
        albums = findViewById(R.id.OpenList);
        if (!User.listofAlbums.isEmpty()) {
            for (Album a : User.listofAlbums) {
                albumnames.add(a.albumName);
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albumnames);
            albums.setAdapter(adapter);
        }

    }
    public void Go_Back(View view){
        Intent intent = new Intent(this, Photos.class);
        startActivity(intent);
    }

    public void Open(View view){

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
        for (Album a : User.listofAlbums){
            if (a.albumName.equals(name)){
                AlbumController.currentAlbum = a;
                Intent intent = new Intent(this, AlbumController.class);
                startActivity(intent);
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


    }


}
