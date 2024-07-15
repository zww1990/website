package io.online.videosite;

public class PostgresqlDriverTests {
    public static void main(String[] args) throws Exception {
        Class<?> aClass = Class.forName("org.postgresql.Driver");
        System.err.println(aClass);
    }
}
