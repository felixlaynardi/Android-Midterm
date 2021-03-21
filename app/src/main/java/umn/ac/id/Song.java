package umn.ac.id;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;
    private String artist;
    private String songURI;

    //Constructor
    public Song(String title, String artist, String songURI){
        this.title = title;
        this.artist = artist;
        this.songURI = songURI;
    }

    //get method
    public String getTitle(){
        return this.title;
    }
    public String getArtist(){
        return this.artist;
    }
    public String getSongURI(){
        return this.songURI;
    }

    //set method
    public void setContent(String title, String artist, String songURI){
        this.title = title;
        this.artist = artist;
        this.songURI = songURI;
    }

}
