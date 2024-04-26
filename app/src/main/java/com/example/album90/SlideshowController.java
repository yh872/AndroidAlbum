package com.example.album90;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SlideshowController extends AppCompatActivity {
    public static int curIndex= 0;
    public static int finalIndex = AlbumController.currentAlbum.numberOfPhotos() -1;

    private Button Back;
    private Button Prev;
    private Button Next;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        curIndex= 0;
        finalIndex = AlbumController.currentAlbum.numberOfPhotos() -1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);
        Back = findViewById(R.id.LastBack);
        Prev =findViewById(R.id.prev);
        Next = findViewById(R.id.next);
        image = findViewById(R.id.ImgView);
        Prev.setEnabled(false);
        if (AlbumController.currentAlbum.numberOfPhotos() < 2){
            Next.setEnabled(false);
        }
        Photo p = AlbumController.currentAlbum.listofPhotos.get(0);
        image.setImageURI(Uri.parse(p.path));

    }

    public void lastgoback(View view){
        curIndex = 0;
        Intent intent = new Intent(this, DisplayController.class);
        startActivity(intent);
    }
    public void Next(View view){
        Prev.setEnabled(true);
        curIndex++;
        Photo p = AlbumController.currentAlbum.listofPhotos.get(curIndex);
        image.setImageURI(Uri.parse(p.path));
        if (curIndex == finalIndex){
            Next.setEnabled(false);
        }
    }
    public void Prev(View view){
        Next.setEnabled(true);
        curIndex--;
        Photo p = AlbumController.currentAlbum.listofPhotos.get(curIndex);
        image.setImageURI(Uri.parse(p.path));
        if (curIndex==0){
            Prev.setEnabled(false);
        }
    }

}
