package com.telecom.rr.cache.memory;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.collect.Lists;

/**
 *
 */
public class PrinTestInfo {

    public static final LinkedList list = Lists.newLinkedList();
    public static final Lock       lock = new ReentrantLock();

    public static List getList() {
        List a = Lists.newArrayList();
        for (Object b : list)
            a.add(b);
        Collections.reverse(a);
        return a;
    }

    public static void put(PrinTestInfo info) {
        lock.lock();
        try {
            if (list.size() >= 100) {
                list.remove();
            }
            info.setDate(new Date());
            list.add(info);
        } finally {
            lock.unlock();
        }
    }

    private Date   date;
    private String testinfo;

    public PrinTestInfo() {}

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTestInfo() {
        return testinfo;
    }

    public void setTestInfo(String testinfo) {
        this.testinfo = testinfo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PrinTestInfo{");
        sb.append("testinfo='").append(testinfo).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
