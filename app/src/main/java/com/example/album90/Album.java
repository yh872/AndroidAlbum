package com.example.album90;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    private static final long serialVersionUID = 1L;
    public String albumName;
    ArrayList<Photo> listofPhotos = new ArrayList<>();


    public Album(String albumName){
        this.albumName = albumName;
    }
    public void renameAlbum(String n){
        albumName = n;

    }

    public Integer numberOfPhotos(){
        return listofPhotos.size();
    }

    public void addPhoto(Photo p){
        listofPhotos.add(p);
    }

    public void removePhoto(Photo p){
        listofPhotos.remove(p);
    }

    public ArrayList<Photo> getAllPhotos(){
        return listofPhotos;
    }


}
