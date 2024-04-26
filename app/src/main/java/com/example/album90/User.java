package com.example.album90;

import com.example.album90.Album;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    public static ArrayList<Album> listofAlbums = new ArrayList<>();

    public static void AddAlbum(String s){
        Album a = new Album(s);
        listofAlbums.add(a);
    }


}