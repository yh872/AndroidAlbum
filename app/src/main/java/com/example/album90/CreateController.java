package com.example.album90;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class CreateController extends AppCompatActivity {

    private Button backButton;
    private Button createButton;

    private EditText albumname;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        backButton = findViewById(R.id.backButton);
        createButton = findViewById(R.id.create);
        albumname = findViewById(R.id.albumname);
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, Photos.class);
        startActivity(intent);

    }

    public void create(View view) {
        Context context = view.getContext();
        Editable text = albumname.getText();
        String name = text.toString();
        if (name.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Error");
            builder.setMessage("No name found");
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
        for (Album a : User.listofAlbums) {
            if (a.albumName.equals(name)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Error");
                builder.setMessage("Album name is already taken");
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
        User.AddAlbum(name);
        try {
            Photos.WriteToFile();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Success");
            builder.setMessage("Album has been successfully created");
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
    }

}