package model.Entities;

import javax.annotation.Generated;
import javax.persistence.*;

@Entity(name = "LOGS")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String ip;
    private String log;

    public Log() {
    }

    public Log(String ip, String log) {
        this.ip = ip;
        this.log = log;
    }

    public String getIp() {
        return ip;
    }

    public String getLog() {
        return log;
    }
}
