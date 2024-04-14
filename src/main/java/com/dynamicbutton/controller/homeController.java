package com.dynamicbutton.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class homeController {


    @RequestMapping("/home")
    public String home(){
        return "home";
    }


    @RequestMapping("/view")
    public String view(){
        return "view";
    }


}
