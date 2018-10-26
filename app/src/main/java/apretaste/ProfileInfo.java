package apretaste;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by  on 10/07/2017.
 */

public class ProfileInfo {

    public String token;
    public String username;
    public String timestamp = "";
    public float credit;
    public String[] active = new String[]{};
    public String mailbox;
    public String domain;
    public String img_quality;
    public Profile profile = new Profile();
    public Notifications notifications[] = new Notifications[]{};
    public Services services[] = new Services[]{};


    public void change_un(String us) {
        this.username = us;

    }


    public class Notifications {
        public String received;
        public String service;
        public String text;
        public String link;


    }

    public class Services {
        public String name;
        public String description;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String category;
        public String creator;
        public String updated;
        public String icon;

    }


    public void update(ProfileInfo updateInfo) {

        username = updateInfo.username;
        timestamp = updateInfo.timestamp;
        credit = updateInfo.credit;
        if (updateInfo.profile.full_name != null)
            profile = updateInfo.profile;


        Log.e("pro", "finish update 2");
    }


}
