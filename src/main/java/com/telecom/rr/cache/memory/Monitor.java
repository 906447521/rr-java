package com.telecom.rr.cache.memory;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 *
 */
public class Monitor {

    public static final LinkedHashMap  list = Maps.newLinkedHashMap();
    public static final Lock           lock = new ReentrantLock();

    public static List getList() {
        List a = Lists.newArrayList();
        for (Object b : list.values())
            a.add(b);
        Collections.reverse(a);
        return a;
    }

    public static void put(Monitor info) {
        lock.lock();
        try {
            if(info.Id == null) {
                return;
            }
            if (list.size() >= 300) {
                list.remove(list.keySet().iterator().next());
            }
            info.setLastestDate(new Date());
            list.put(info.Id, info);
        } finally {
            lock.unlock();
        }
    }

    /**
     *
     type Monitor struct {
         Id                    string
         Version               string
         Date                  string
         Os                    string
         Arch                  string
         Mac                   string
         All                   uint64 `json:"all"`
         Used                  uint64 `json:"used"`
         Free                  uint64 `json:"free"`
         Self                  uint64 `json:"self"`
         HeapAlloc             uint64
         HeapIdle              uint64
         HeapSys               uint64
     }
     */
    private Date   lastestDate;
    private String Id;
    private String Version;
    private String Date;
    private String Mac;

    public Monitor() {}

    public Date getLastestDate() {
        return lastestDate;
    }

    public void setLastestDate(Date lastestDate) {
        this.lastestDate = lastestDate;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getMac() {
        return Mac;
    }

    public void setMac(String mac) {
        Mac = mac;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Monitor{");
        sb.append("Id='").append(Id).append('\'');
        sb.append(", Date='").append(Date).append('\'');
        sb.append(", Mac='").append(Mac).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
