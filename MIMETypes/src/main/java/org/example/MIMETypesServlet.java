package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@MultipartConfig
@WebServlet("/")
public class MIMETypesServlet extends HttpServlet {
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
////        Read Text/plain data from http request body
//        String body = new BufferedReader(new InputStreamReader(req.getInputStream())).lines().collect(Collectors.joining("\n"));
//        resp.setContentType("text/plain");
//        resp.getWriter().println(body);
//    }


//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String name = req.getParameter("name");
//        String address = req.getParameter("address");
//        resp.setContentType("text/plain");
//        resp.getWriter().print("Hello " + name + " " + address);
//    }

//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String name = req.getParameter("name");
//        Part img = req.getPart("img");
//        Part file = req.getPart("file");
//        String fileName = file.getSubmittedFileName();
//        String submittedFileName = img.getSubmittedFileName();
//        resp.setContentType("text/plain");
//        resp.getWriter().write(submittedFileName + " has been submitted, " + fileName + " has been submitted");
//    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(req.getReader());
        String name = jsonNode.get("name").asText();
        String address = jsonNode.get("address").asText();

        resp.setContentType("text/plain");
        resp.getWriter().write(name + " " + address);
    }
}