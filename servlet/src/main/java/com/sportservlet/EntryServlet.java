package com.sportservlet;

import com.sports.logic.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class EntryServlet extends HttpServlet {
    private static final String servletsLocation = Util.concatStrings(new String[] { "", "WEB-INF", "servlets.txt" },
            Util.fileSeparator);

    private static final Map<String, HttpServlet> servletMap = new HashMap<>();

    @Override
    public void init() {
        try {
            InputStream is = getServletContext().getResourceAsStream(servletsLocation);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while (br.ready()) {
                String line = br.readLine();

                HttpServlet servlet = (HttpServlet) Class.forName(line).getDeclaredConstructor().newInstance();
                servlet.init(getServletConfig());

                String[] parts = line.split("\\.");
                servletMap.put(parts[parts.length - 1], servlet);
            }

            br.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();

        String servletPath;

        if (uri.equals(req.getContextPath() + "/"))
            servletPath = getServletContext().getInitParameter("welcome-file");
        else
            servletPath = uri.substring((req.getContextPath() + "/servlet/").length());

        servletMap.get(servletPath).service(req, resp);
    }
}
