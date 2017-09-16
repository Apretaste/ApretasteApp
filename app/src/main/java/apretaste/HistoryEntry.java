package apretaste;

import java.util.Date;


public class HistoryEntry {
    public final String service, command, path;
    public Date date;

    public HistoryEntry(String service, String command, String path, Date date) {

        this.service = service;
        this.command = command;
        this.path = path;
        this.date = date;
    }
}
