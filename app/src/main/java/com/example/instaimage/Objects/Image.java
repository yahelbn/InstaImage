package com.example.instaimage.Objects;

import java.util.ArrayList;

public class Image {


//    private String name;
//    private String id;
//    private String info;
//    public ArrayList<String> comments;

    public String name;
    public String ImageURL;
    public String info;
    public Double lat;
    public Double lng;

    public Image() {

    }

    public Image(String name,String ImageURL) {
        //super();
        this.name = name;
        this.ImageURL=ImageURL;
    }

    public Image(String name,String ImageURL,String info,Double lat,Double lng) {
        //super();
        this.name = name;
        this.ImageURL=ImageURL;
        this.info=info;
        this.lat=lat;
        this.lng=lng;
    }



    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}