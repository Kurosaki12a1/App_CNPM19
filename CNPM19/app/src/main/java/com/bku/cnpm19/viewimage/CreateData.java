package com.bku.cnpm19.viewimage;

import com.bku.cnpm19.viewimage.model.Profile;
import com.bku.cnpm19.viewimage.view.activity.ViewImageActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Welcome on 3/29/2018.
 */

public class CreateData {
    private static ViewImageActivity view;
    public static List<Profile> createProfile(String userID, String PostID, long liked, ArrayList<String> imageList) {

        List<Profile> profileList = new ArrayList<>();
        profileList.add(create(userID,PostID,liked, imageList));
       /* profileList.add(create(2, "Đào", 22, Arrays.asList(
                "http://imageshack.com/a/img923/2374/jBIAqT.jpg",
                "http://imageshack.com/a/img923/9599/ulegwM.jpg",
                "http://imageshack.com/a/img923/7016/HzEvo8.jpg",
                "http://imageshack.com/a/img922/1641/emfvPy.jpg",
                "http://imageshack.com/a/img923/116/yfyetV.jpg",
                "http://imageshack.com/a/img924/4846/zveut5.jpg",
                "http://imageshack.com/a/img923/472/SB341D.jpg",
                "http://imageshack.com/a/img924/4350/5ghok1.jpg")));
        profileList.add(create(3, "Đào", 22, Arrays.asList(
                "http://imageshack.com/a/img923/3835/q79WKE.jpg",
                "http://imageshack.com/a/img922/3615/sNKPpV.jpg",
                "http://imageshack.com/a/img924/7272/UXID41.jpg",
                "http://imageshack.com/a/img923/1332/sTx6qw.jpg",
                "http://imageshack.com/a/img923/3338/v0hyg5.jpg",
                "http://imageshack.com/a/img922/8435/puIYQa.jpg")));
        profileList.add(create(4, "Đào", 22, Arrays.asList(
                "http://imageshack.com/a/img922/3390/GXOW9e.jpg",
                "http://imageshack.com/a/img922/8017/Qnxgbw.jpg")));
        profileList.add(create(5, "Đào", 22, Arrays.asList(
                "http://imageshack.com/a/img924/331/ujExV6.jpg",
                "http://imageshack.com/a/img923/9503/qgUaHN.jpg",
                "http://imageshack.com/a/img924/5319/RNBJQp.jpg",
                "http://imageshack.com/a/img923/3299/nWdFeZ.jpg",
                "http://imageshack.com/a/img922/461/iIGnvU.jpg",
                "http://imageshack.com/a/img922/3748/XvWQPz.jpg",
                "http://imageshack.com/a/img922/2177/gm9ddy.jpg",
                "http://imageshack.com/a/img924/7344/JBqVbQ.jpg")));
        profileList.add(create(6, "Đào", 22, Arrays.asList(
                "http://imageshack.com/a/img924/6515/JiylGo.jpg",
                "http://imageshack.com/a/img924/6537/AfNnra.jpg",
                "http://imageshack.com/a/img924/802/rBCWs3.jpg",
                "http://imageshack.com/a/img923/9322/bO0hYw.jpg",
                "http://imageshack.com/a/img923/7392/kyiKsh.jpg")));
        profileList.add(create(7, "Đào", 22, Arrays.asList(
                "http://imageshack.com/a/img924/9423/E8Xxky.jpg",
                "http://imageshack.com/a/img922/8366/7TVQZk.jpg",
                "http://imageshack.com/a/img922/8117/3OJydD.jpg")));
        profileList.add(create(8, "Đào", 22, Arrays.asList(
                "http://imageshack.com/a/img923/6430/OK2kfH.jpg",
                "http://imageshack.com/a/img924/8688/j6RaJz.jpg",
                "http://imageshack.com/a/img924/7714/LWTaxv.jpg",
                "http://imageshack.com/a/img922/64/u9LXlR.jpg")));*/
        return profileList;
    }

    public static Profile create(String userID, String PostID, long liked, ArrayList<String> imageList) {
        return new Profile(userID, PostID, liked, imageList);
    }
}
