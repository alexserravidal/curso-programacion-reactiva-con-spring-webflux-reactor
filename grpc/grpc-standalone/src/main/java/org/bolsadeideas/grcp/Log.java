package org.bolsadeideas.grcp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    private Class clazz;

    private Log(Class clazz) {
        this.clazz = clazz;
    }

    private String TIME_PATTERN = "hh:mm:ss";
    private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(TIME_PATTERN);

    public static Log getInstance(Class clazz) {
        return new Log(clazz);
    }

    public void log(String log) {
        final String TIME = DATE_FORMAT.format(new Date());
        System.out.println("[" + this.clazz.getSimpleName() + "][" + TIME + "]: " + log);
    }
}
