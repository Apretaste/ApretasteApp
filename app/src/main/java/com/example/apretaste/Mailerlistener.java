package com.example.apretaste;

import javax.mail.Message;

/**
 * Created by Raymond Arteaga on 13/07/2017.
 */
public interface Mailerlistener {
    void onMailSent();

    void onError(Exception e);

    void onResponseArrived(String service, String command, String response, Mailer mailer);
}
