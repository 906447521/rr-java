package com.telecom.rr.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.telecom.rr.FrameController;

/**
 *
 */
@Controller
@RequestMapping("")
public class HomeController extends FrameController {

    @RequestMapping("")
    public ModelAndView home() throws Exception {

        ModelAndView m = new ModelAndView("index");

        return m;
    }

}
