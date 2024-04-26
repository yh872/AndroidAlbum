package com.example.album90;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Photos extends AppCompatActivity implements Serializable {

    private Button open_album;
    private Button delete_album;
    private Button create_album;
    private Button rename_album;
    private Button search_button;

    public static File directory;

    private ListView AlbumList;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> albumnames = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        directory = getFilesDir();
        try {
            Initiliaze_albums();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        open_album = findViewById(R.id.opealb);
        delete_album = findViewById(R.id.delalb);
        create_album = findViewById(R.id.crealb);
        rename_album = findViewById(R.id.renalb);
        search_button = findViewById(R.id.searchb);
        AlbumList = findViewById(R.id.amfg);
        if (!User.listofAlbums.isEmpty()) {
            for (Album a : User.listofAlbums) {
                albumnames.add(a.albumName);
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albumnames);
            AlbumList.setAdapter(adapter);
        }

    }

    public void Initiliaze_albums() throws IOException, ClassNotFoundException {
        File dir = getFilesDir();
        File file = new File(dir, "albumList.ser");
        if (!file.exists()) {
            file.createNewFile();
            getData();
            return;
        }
        getData();

    }
    public static void WriteToFile() throws IOException {
        File file = new File(directory, "albumList.ser");
        if(!file.exists()){

            file.createNewFile();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(User.listofAlbums);
            oos.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void getData() throws IOException, ClassNotFoundException {
        File file = new File(directory, "albumList.ser");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Album> deserializedAlbumList = (ArrayList<Album>) ois.readObject();
            ois.close();
            fis.close();
            // do something with the deserializedAlbumList
            User.listofAlbums=deserializedAlbumList;

            if(User.listofAlbums == null){
                User.listofAlbums = new ArrayList<Album>();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public void openClicked(View view) {
        Intent intent = new Intent(this, OpenController.class);
        startActivity(intent);

    }

    public void createClicked(View view) {
        Intent intent = new Intent(this, CreateController.class);
        startActivity(intent);

    }

    public void deleteClicked(View view) {
        Intent intent = new Intent(this, DeleteController.class);
        startActivity(intent);
    }

    public void renameClicked(View view) {
        Intent intent = new Intent(this, RenameController.class);
        startActivity(intent);
    }
    public void searchClicked(View view) {
        Intent intent = new Intent(this, SearchController.class);
        startActivity(intent);

    }
}
