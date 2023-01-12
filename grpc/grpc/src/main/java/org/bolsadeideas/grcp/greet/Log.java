package org.bolsadeideas.grcp.greet;

public class Log {

    private Class clazz;

    private Log(Class clazz) {
        this.clazz = clazz;
    }

    public static Log getInstance(Class clazz) {
        return new Log(clazz);
    }

    public void log(String log) {
        System.out.println("[" + this.clazz.getSimpleName() + "]: " + log);
    }
}
