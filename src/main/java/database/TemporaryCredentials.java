package database;

public class TemporaryCredentials {
    // TODO: вынести USER и PASS в properties.txt, чтобы не палить их здесь.
    static final String DB_URL = "jdbc:postgresql://hillmine.ru:5432/cma";
    static final String USER = "ojzdwcrlqj0msaemqz5b";
    static final String PASS = "ummSe6bb2OU1ZW3xccSCJ1hSqmPYQKoVYnsnFCSK";

    public static String DB_URL(){
        return DB_URL;
    }
    public static String getUSER() {
        return USER;
    }
    public static String getPASS() {
        return PASS;
    }
}
