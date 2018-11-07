package com.telecom.rr.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.telecom.rr.cache.memory.Monitor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.telecom.rr.FrameController;
import com.telecom.rr.cache.memory.PrinTestInfo;
import com.telecom.rr.cache.memory.SaveDownTestDate;
import com.telecom.rr.commons.json.Result;

/**
 *
 */
@Controller
@RequestMapping("client")
public class ClientController extends FrameController {

    @RequestMapping("")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String authorization = request.getHeader("authorization");

        if (authorization == null || !"Basic YWRtaW46a29icmE7".equals(authorization)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("WWW-Authenticate", "Basic realm=\"Authenticate\"");
            return null;
        }

        ModelAndView mv = new ModelAndView("client/index");

        mv.addObject("ptinfo", PrinTestInfo.getList());
        mv.addObject("sdinfo", SaveDownTestDate.getList());
        return mv;
    }

    @RequestMapping("/send")
    @ResponseBody
    public Result send(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String method, //
            @ModelAttribute PrinTestInfo prinTestInfo,//
            @ModelAttribute SaveDownTestDate saveDownTestDate) throws Exception {

        if (method != null) {
            if (method.equalsIgnoreCase("PrinTestInfo")) {
                PrinTestInfo.put(prinTestInfo);
            } else if (method.equalsIgnoreCase("SaveDownTestDate")) {
                SaveDownTestDate.put(saveDownTestDate);
            }
        }

        return new Result("ok");

    }

    @RequestMapping("/monitor")
    public ModelAndView monitor(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String authorization = request.getHeader("authorization");

        if (authorization == null || !"Basic YWRtaW46a29icmE7".equals(authorization)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("WWW-Authenticate", "Basic realm=\"Authenticate\"");
            return null;
        }

        ModelAndView mv = new ModelAndView("client/monitor");

        mv.addObject("mtinfo", Monitor.getList());
        return mv;
    }

    @RequestMapping("/monitor/send")
    @ResponseBody
    public Result monitorSend(HttpServletRequest request, HttpServletResponse response, @ModelAttribute Monitor monitor)
            throws Exception {
        Monitor.put(monitor);
        return new Result("ok");

    }

    @RequestMapping("/ip")
    @ResponseBody
    public String ip(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;

    }
}
