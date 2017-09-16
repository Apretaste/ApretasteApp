package apretaste;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Raymond Arteaga on 10/07/2017.
 */

public class ProfileInfo  {
    public String username;
    public String timestamp="";
    public float credit;
    public Profile profile=new Profile();
    public Notifications notifications[]=new Notifications[]{};
    public Services services[]=new Services[]{};

    public void change_un(String us){
        this.username = us;

    }

    public class active{
        String active[];
    }
    public class Notifications
    {
        public String received;
        public String service;
        public String text;
        public String link;
        private boolean read=false;
        public boolean isRead()
        {return read;}
        public void toggleRead()
        {
            read=!read;
        }
        public void setRead()
        {
            read=true;
        }

    }
    public class Services
    {
        public String name;
        public String description;

        String category;
        String creator;
        String updated;
        public String icon;
        Boolean used=false;
        public void setUsed()
        {
            used=true;
        }

        public boolean isUsed()
        {
            return used==null?false:used;
        }
    }


    public void update(ProfileInfo updateInfo)
    {
        if(updateInfo.notifications.length>0)
        {
            List<Notifications> arrl=new ArrayList<Notifications>();
            Collections.addAll(arrl, updateInfo.notifications);
            Collections.addAll(arrl, notifications);
            notifications=arrl.toArray(notifications);
        }

        if(updateInfo.services.length>0)
        {
            List<Services> arrl=new ArrayList<>();
            Collections.addAll(arrl,services);
            for(Services service:updateInfo.services)
            {
                boolean exists=false;
                for(int i=0;i<arrl.size();i++)
                {
                    if (!arrl.get(i).name.equals(service.name))
                        continue;
                    arrl.set(i,service);
                    exists=true;
                    break;
                }
                if(!exists)

                    arrl.add(service);

            }

            services=arrl.toArray(services);

            Log.e("pro","finish update");
        }

        username=updateInfo.username;
        timestamp=updateInfo.timestamp;
        credit=updateInfo.credit;
        if(updateInfo.profile.full_name!=null)
            profile=updateInfo.profile;

        profile.humanizeData();
        Log.e("pro","finish update 2");
    }


}
