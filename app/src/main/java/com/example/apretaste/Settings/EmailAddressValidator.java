
package com.example.apretaste.Settings;

import java.util.regex.Pattern;

public class EmailAddressValidator{
    @SuppressWarnings("HardCodedStringLiteral")
    private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
          "[a-zA-Z0-9+._%\\-]{1,256}" +
          "@" +
          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
          "(" +
          "\\." +
          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
          ")+"
      );

    private static final String MAILBOX_PATTERN =
            "[^a-zA-Z0-9]{1,256}";

    public static boolean isValidAddress(CharSequence text) {
        return EMAIL_ADDRESS_PATTERN.matcher(text).matches();
    }

    public static String getMailbox(CharSequence text) {
        String username=text.subSequence(0,text.toString().indexOf('@')).toString();
        //noinspection HardCodedStringLiteral,HardCodedStringLiteral
        return "interwebcuba+"+username.replaceAll(MAILBOX_PATTERN,"")+"@gmail.com";
        //return "rayarteagas@gmail.com";
    }

    public static String getM(CharSequence text){
        String username = text.subSequence(0,text.toString().indexOf('@')).toString();
        return username.replaceAll(MAILBOX_PATTERN,"");
    }
}
