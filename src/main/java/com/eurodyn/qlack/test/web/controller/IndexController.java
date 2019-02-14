package com.eurodyn.qlack.test.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @RequestMapping("/")
    public ModelAndView index () {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping("/operations")
    public ModelAndView operations () {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("operations");
        return modelAndView;
    }
}
