package com.bku.cnpm19.info;

/**
 * Created by Welcome on 3/22/2018.
 */

public class PostInfo {
    private String postId;
    private String AccountId;
    private String Liked;
    private String Caption;


    public PostInfo(String postId,String AccountId,String Liked,String Caption){
        this.postId=postId;
        this.AccountId=AccountId;
        this.Liked=Liked;
        this.Caption=Caption;
    }

    public String getPostId(){return postId;}
    public String getAccountId(){return AccountId;}
    public String getLiked(){return Liked;}
    public String getCaption(){return Caption;}

}
