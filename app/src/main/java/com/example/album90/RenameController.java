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

import java.io.IOException;
import java.util.ArrayList;

public class RenameController extends AppCompatActivity {

    private Button BackButton;
    private EditText old_name;
    private EditText new_name;

    private Button RenameButton;
    private ListView AlbumList;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> albumnames = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename);
        BackButton = findViewById(R.id.backButton345);
        old_name = findViewById(R.id.oldalbedittext);
        new_name = findViewById(R.id.albedittext);
        RenameButton = findViewById(R.id.rname);
        AlbumList = findViewById(R.id.rlist);
        if (!User.listofAlbums.isEmpty()) {
            for (Album a : User.listofAlbums) {
                albumnames.add(a.albumName);
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albumnames);
            AlbumList.setAdapter(adapter);
        }

    }
    public void go_back(View view){
        Intent intent = new Intent(this, Photos.class);
        startActivity(intent);
    }

    public void Rename(View view){
        Context context = view.getContext();
        Editable text1 = old_name.getText();
        Editable text2 = new_name.getText();
        String oldname = text1.toString();
        String newname = text2.toString();
        if (oldname.isEmpty() || newname.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Error");
            builder.setMessage("Required field(s) empty.");
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
        boolean OldNameFound = false;
        for (Album a: User.listofAlbums){
            if (a.albumName.equals(oldname)){
                OldNameFound = true;
            }
        }
        if (!OldNameFound){
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
        for (Album a: User.listofAlbums){
            if (a.albumName.equals(newname)){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Error");
                builder.setMessage("An Album with the new name already exists.");
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
        for (Album a: User.listofAlbums){
            if (a.albumName.equals(oldname)){
                a.albumName = newname;
                try {
                    Photos.WriteToFile();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Success");
                    builder.setMessage("Album has been successfully renamed.");
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
    }


}
