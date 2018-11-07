package com.telecom.rr.service;

import java.util.Map;

import com.google.common.collect.Maps;
import com.telecom.rr.FrameService;
import com.telecom.rr.domain.User;

/**
 *
 */
//@Service
public class TestService extends FrameService {

    public void get() {

        User result = null;

        User query = new User();
        query.setName("111");

        result = getSqlSession().selectOne("scrr.Test.list", query);
        System.out.println(result);

        Map queryMap = Maps.newHashMap();
        queryMap.put("name", "111");

        result = getSqlSession().selectOne("scrr.Test.list", query);
        System.out.println(result);
    }
}
