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

import com.example.album90.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.internal.MaterialCheckable;

public class SearchController extends AppCompatActivity {

    private EditText SearchTags;
    private Chip Single;
    private Chip Conjunctive;
    private Chip Disjunctive;
    private Button searchButton;

    private Button BackButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SearchTags = findViewById(R.id.Searchtags);
        Single = findViewById(R.id.SingleChip);
        Conjunctive = findViewById(R.id.ConjuctiveChip);
        Disjunctive = findViewById(R.id.DisjunctiveChip);
        searchButton = findViewById(R.id.searchButton);
        BackButton = findViewById(R.id.BackButton5);

    }

    public boolean OneChipSelected(){
        int count = 0;
        if (Single.isChecked()){
            count++;
        }
        if (Conjunctive.isChecked()){
            count++;
        }
        if (Disjunctive.isChecked()){
            count++;
        }
        return count == 1;
    }

    public void BackClicked(View view){
        Intent intent = new Intent(this, Photos.class);
        startActivity(intent);
    }
    public void SingleClicked(View view){
        System.out.println("hello");
    }
    public void ConjunctiveClicked(View view){

    }
    public void DisjunctiveClicked(View view){

    }
    public String getType(String s){
        return "";
    }

    public void SearchTag(View view){
        Context context = view.getContext();
        Editable text = SearchTags.getText();
        String search = text.toString();
        if (!OneChipSelected()){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Error");
            builder.setMessage("Must have Exactly 1 option selected.");
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
        if (search.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Error");
            builder.setMessage("Search not found.");
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

        if (Single.isChecked()){

        }
        else if (Conjunctive.isChecked()){

        }
        else if (Disjunctive.isChecked()){

        }
    }


}