package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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
}