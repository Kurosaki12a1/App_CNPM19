package com.bku.cnpm19.info;

/**
 * Created by AndroidJSon.com on 6/10/2017.
 */


public class ImageUploadInfo {


    public String imageURL;

    private String caption;

    public long liked;

    private String userId;

    public ImageUploadInfo( String imageURL, long liked,String caption,String userId) {

        this.imageURL = imageURL;
        this.liked = liked;
        this.caption=caption;
        this.userId=userId;
    }


    public ImageUploadInfo() {

    }





    public String getImageURL() {
        return imageURL;
    }


    public long getLiked() {return liked; }

    public String getCaption(){ return caption;}

    public String getUserId(){return userId;}
}
