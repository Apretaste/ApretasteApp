package apretaste;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by on 17/07/2017.
 */
public class Profile implements Cloneable {
    public String full_name;
    public String date_of_birth;
    public String gender;
    public String phone;
    public String eyes;
    public String skin;
    public String body_type;
    public String hair;
    public String province;
    public String city;
    public String highest_school_level;
    public String occupation;
    public String marital_status;
    public String sexual_orientation;
    public String religion;
    public String picture;
    public String[] interests=new String[]{};



    @Override
    public Profile clone(){
        Profile clon=new Profile();
        clon.full_name=full_name;
        clon.date_of_birth=date_of_birth;
        clon.gender=gender;
        clon.phone = phone;
        clon.eyes=eyes;
        clon.skin=skin;
        clon.body_type=body_type;
        clon.hair=hair;
        clon.province=province;
        clon.city=city;
        clon.highest_school_level=highest_school_level;
        clon.occupation=occupation;
        clon.marital_status=marital_status;
        clon.sexual_orientation=sexual_orientation;
        clon.religion=religion;
        clon.picture=picture;
        clon.interests=interests;
        return clon;
    }
}
