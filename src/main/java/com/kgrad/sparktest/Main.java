package com.kgrad.sparktest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class Main {

    private static final String URL = "jdbc:postgresql:sparkdb";

    public static void main(String[] args) {

        get("/", (request, response) -> {
            List<String> names = getNames();
            StringBuilder html = new StringBuilder("");

            for (String name : names) {
                html.append(name);
                html.append("<br>");
            }

            return html;
        });

        get("/put/:name", (req, resp) -> {
           putName(req.params(":name"));
           return "Success!";
        });
    }

    // new java 8 try.
    private static Boolean putName(String name) {

        try (Connection connection = DriverManager.getConnection(URL, "kgrad5", "")) {
            PreparedStatement stmt = connection.prepareCall("insert into names (name) values (?)");
            stmt.setString(1, name);
            return (stmt.executeUpdate() > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static List<String> getNames() {
        try (Connection connection = DriverManager.getConnection(URL, "kgrad5", "")) {
            PreparedStatement stmt = connection.prepareCall("select * from names");
            ResultSet rs = stmt.executeQuery();

            List<String> names = new ArrayList<>();
            while(rs.next()){
                names.add(rs.getString("name"));
            }

            return names;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}