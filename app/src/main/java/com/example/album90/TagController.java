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

import com.google.android.material.chip.Chip;

import java.io.IOException;
import java.util.ArrayList;

public class TagController extends AppCompatActivity {



    private Button BackButton;
    private Button TagButton;
    public static ArrayList<Tag> listoftags = null;
    public static boolean AddClicked = false;
    public static boolean deleteClicked = false;
    private EditText Tagvalue;

    private Chip PersonChip;
    private Chip locationChip;

    private ListView TagList;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> TagNames = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        BackButton = findViewById(R.id.BackButton500);
        TagButton= findViewById(R.id.addtagbutton);
        Tagvalue = findViewById(R.id.tagname);
        PersonChip = findViewById(R.id.PersonChip);
        locationChip = findViewById(R.id.LocationChip);
        TagList = findViewById(R.id.ListofTags);
        for (Tag t : listoftags){
            TagNames.add(t.type + "=" + t.value);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, TagNames);
        TagList.setAdapter(adapter);
        if (AddClicked){
            TagButton.setText("Add tag");
        }
        else{
            TagButton.setText("Delete tag");
        }

    }
    public void go_backtoalbum(View view){
        AddClicked = false;
        deleteClicked = false;
        Intent intent = new Intent(this, DisplayController.class);
        startActivity(intent);
    }

    public boolean OneChipSelected(){
        int count = 0;
        if (PersonChip.isChecked()){
            count++;
        }
        if (locationChip.isChecked()){
            count++;
        }
        return count == 1;
    }
    public void tagClicked(View view){
        Context context = view.getContext();
        Editable text = Tagvalue.getText();
        String value = text.toString();
        if (!OneChipSelected()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("Must have Exactly 1 Tag Type selected.");
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
        if (value.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("Must Enter a tag value.");
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
        if (deleteClicked){
            String type = "";
            if (PersonChip.isChecked()){
                type = "Person";
            }
            else{
                type = "Location";
            }
            String tag = type + "=" + value;
            for (Tag t: listoftags){
                String fulltag = t.type + "=" + t.value;
                if (fulltag.equals(tag)){
                    listoftags.remove(t);
                    try {
                        Photos.WriteToFile();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Success");
                        builder.setMessage("Tag has been succesfully deleted.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(context, DisplayController.class);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("Tag not found.");
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
        else{
            String type = "";
            if (PersonChip.isChecked()){
                type = "Person";
            }
            else{
                type = "Location";
            }
            String tag = type + "=" + value;
            for (Tag t: listoftags){
                String fulltag = t.type + "=" + t.value;
                if (fulltag.equals(tag)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Error");
                    builder.setMessage("Tag not found.");
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
            listoftags.add(new Tag(type, value));
            try {
                Photos.WriteToFile();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Success");
                builder.setMessage("Tag has been succesfully added.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, DisplayController.class);
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
}
