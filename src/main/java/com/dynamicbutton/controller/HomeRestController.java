package com.dynamicbutton.controller;

import com.dynamicbutton.service.ApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class HomeRestController {

    private final ApplicationService applicationService;

    @Autowired
    public HomeRestController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }


    @GetMapping("/getData")
    public ResponseEntity<?> getData(HttpServletRequest request, @RequestParam  Map<String,Object> map){
        Map<String,Object> application = applicationService.getApplication(map,request);
        return new ResponseEntity<>(application, HttpStatus.OK);
    }


    @GetMapping("/getapplicationmenu")
    public ResponseEntity<?> getapplicationmenu(HttpServletRequest request, @RequestParam  Map<String,Object> map){
        Map<String,Object> applicationmenu = applicationService.getApplicationMenu(map,request);
        return new ResponseEntity<>(applicationmenu, HttpStatus.OK);
    }
}
