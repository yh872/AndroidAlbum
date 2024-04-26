package com.example.album90;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DisplayController extends AppCompatActivity {

    public static Photo photo = null;
    public static int index;

    private Button BackButton;
    private Button addTagButton;
    private Button deleteTagButton;
    private Button slideshowButton;

    private ImageView image;

    private ListView TagList;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> TagNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        BackButton = findViewById(R.id.BackButton200);
        addTagButton = findViewById(R.id.addTagButton);
        deleteTagButton = findViewById(R.id.deleteTagButton);
        slideshowButton = findViewById(R.id.slideshowButton);
        image = findViewById(R.id.displayImage);
        TagList = findViewById(R.id.TagList);
        Photo p = AlbumController.currentAlbum.listofPhotos.get((AlbumController.curPage * 6) + index);
        image.setImageURI(Uri.parse(p.path));
        for (Tag tag: p.listofTags){
            String s = tag.type + "=" + tag.value;
            TagNames.add(s);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, TagNames);
        TagList.setAdapter(adapter);

    }

    public void Go_Last(View view){
        index = 0;
        photo = null;
        Intent intent = new Intent(this, AlbumController.class);
        startActivity(intent);

    }

    public void addTag(View view){
        TagController.listoftags = photo.listofTags;
        TagController.AddClicked = true;
        TagController.deleteClicked = false;
        Intent intent = new Intent(this, TagController.class);
        startActivity(intent);


    }

    public void deleteTag(View view){
        TagController.listoftags = photo.listofTags;
        TagController.AddClicked = false;
        TagController.deleteClicked = true;
        Intent intent = new Intent(this, TagController.class);
        startActivity(intent);

    }

    public void slideshow(View view){
        Intent intent = new Intent(this, SlideshowController.class);
        startActivity(intent);


    }


}
