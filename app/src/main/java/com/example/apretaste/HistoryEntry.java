package com.example.apretaste;

import java.util.Date;

/**
 * Created by Raymond Arteaga on 06/07/2017.
 */
class HistoryEntry {
    public final String service, command, path;
    Date date;

    public HistoryEntry(String service, String command, String path, Date date) {

        this.service = service;
        this.command = command;
        this.path = path;
        this.date = date;
    }
}
