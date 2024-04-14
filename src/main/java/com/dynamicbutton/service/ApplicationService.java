package com.dynamicbutton.service;


import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;

import java.util.Map;

public interface ApplicationService {
    public Map<String,Object> getApplication(Map<String,Object> map, HttpServletRequest request);
    public Map<String,Object> getApplicationMenu(Map<String,Object> map, HttpServletRequest request);
}
