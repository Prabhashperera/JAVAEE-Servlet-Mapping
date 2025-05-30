package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/event")
public class EventServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventDB", "root", "1234");
            ResultSet resultSet = connection.prepareStatement("select * from event").executeQuery();

            List<Map<String, String>> elist = new ArrayList<>();

            while (resultSet.next()) {
                Map<String, String> events = new HashMap<String, String>();
                events.put("id", resultSet.getString("id"));
                events.put("name", resultSet.getString("name"));
                events.put("date", resultSet.getString("date"));
                events.put("time", resultSet.getString("time"));
                elist.add(events);
            }
            resp.setContentType("application/json");

//            Cors Access
            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
            resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
            resp.addHeader("Access-Control-Max-Age", "1728000");

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(resp.getWriter(), elist);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Allow CORS (optional but helpful if calling from frontend)
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:63342");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventDB", "root", "1234");

            // Read JSON body
            BufferedReader reader = req.getReader();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> eventData = mapper.readValue(reader, Map.class);

            String id = eventData.get("id");
            String name = eventData.get("name");
            String date = eventData.get("date");
            String time = eventData.get("time");

            PreparedStatement stm = connection.prepareStatement("INSERT INTO event (id, name, date, time) VALUES (?, ?, ?, ?)");
            stm.setString(1, id);
            stm.setString(2, name);
            stm.setString(3, date);
            stm.setString(4, time);
            int i = stm.executeUpdate();
            if (i > 0) {
                System.out.println("Event added");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:63342");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK); // Very important!
    }

}