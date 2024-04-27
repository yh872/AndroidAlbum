package com.example.album90;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SlideshowController extends AppCompatActivity {
    public static int curIndex= 0;

    public static boolean fromSearch = false;
    public static Album currentAlb = new Album("");
    public static int finalIndex = currentAlb.numberOfPhotos() -1;

    private Button Back;
    private Button Prev;
    private Button Next;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        curIndex= 0;
        finalIndex = currentAlb.numberOfPhotos() -1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);
        Back = findViewById(R.id.LastBack);
        Prev =findViewById(R.id.prev);
        Next = findViewById(R.id.next);
        image = findViewById(R.id.ImgView);
        Prev.setEnabled(false);
        if (currentAlb.numberOfPhotos() < 2){
            Next.setEnabled(false);
        }
        Photo p = currentAlb.listofPhotos.get(0);
        image.setImageURI(Uri.parse(p.path));

    }

    public void lastgoback(View view){
        if (fromSearch){
            fromSearch = false;
            curIndex = 0;
            Intent intent = new Intent(this, SearchController.class);
            startActivity(intent);
            return;
        }

        curIndex = 0;
        Intent intent = new Intent(this, DisplayController.class);
        startActivity(intent);
    }
    public void Next(View view){
        Prev.setEnabled(true);
        curIndex++;
        Photo p =currentAlb.listofPhotos.get(curIndex);
        image.setImageURI(Uri.parse(p.path));
        if (curIndex == finalIndex){
            Next.setEnabled(false);
        }
    }
    public void Prev(View view){
        Next.setEnabled(true);
        curIndex--;
        Photo p = currentAlb.listofPhotos.get(curIndex);
        image.setImageURI(Uri.parse(p.path));
        if (curIndex==0){
            Prev.setEnabled(false);
        }
    }

}
