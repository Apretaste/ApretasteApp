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
    public String picture;
    public String img_quality;
    public Notifications notifications[] = new Notifications[]{};
    public Services services[] = new Services[]{};


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


}
