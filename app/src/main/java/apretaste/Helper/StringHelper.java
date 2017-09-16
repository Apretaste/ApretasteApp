package apretaste.Helper;

/**
 * Created by cjam on 9/8/2017.
 */

public class StringHelper {
    public  String clearString(String str1){

        return str1.toLowerCase().trim().replaceAll("\\s+", " ");
    }
}
