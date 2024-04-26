package com.example.album90;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Photo implements Serializable {
    public ArrayList<Tag> listofTags;
    public String path;


    public Photo(String path){
        this.path = path;
        listofTags = new ArrayList<>();
    }

    public void addTag(Tag t) throws Exception{
        for (int i = 0; i < listofTags.size(); i++){
            if (t.equals(listofTags.get(i))){
                throw new Exception("Tag already present");
            }
        }
        listofTags.add(t);
    }





}