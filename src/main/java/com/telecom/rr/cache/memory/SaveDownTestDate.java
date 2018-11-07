package com.telecom.rr.cache.memory;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.collect.Lists;

/**
 *
 */
public class SaveDownTestDate {

    public static final LinkedList list = Lists.newLinkedList();
    public static final Lock       lock = new ReentrantLock();

    public static List getList() {
        List a = Lists.newArrayList();
        for (Object b : list)
            a.add(b);
        Collections.reverse(a);
        return a;
    }

    public static void put(SaveDownTestDate info) {
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
    private String avgSpeed;
    private String maxSpeed;
    private String allAvgSpeeds;
    private String showSpeed;
    private String isSuccess;
    private String testsource;
    private String location;
    private String target;
    private String totalSpeed;
    private String softwarecount;
    private String backspeed;
    private String cpuinfo;
    private String cupuse;
    private String memory;
    private String memoryuse;
    private String mark;

    public SaveDownTestDate() {}

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(String avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getAllAvgSpeeds() {
        return allAvgSpeeds;
    }

    public void setAllAvgSpeeds(String allAvgSpeeds) {
        this.allAvgSpeeds = allAvgSpeeds;
    }

    public String getShowSpeed() {
        return showSpeed;
    }

    public void setShowSpeed(String showSpeed) {
        this.showSpeed = showSpeed;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getTestsource() {
        return testsource;
    }

    public void setTestsource(String testsource) {
        this.testsource = testsource;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTotalSpeed() {
        return totalSpeed;
    }

    public void setTotalSpeed(String totalSpeed) {
        this.totalSpeed = totalSpeed;
    }

    public String getSoftwarecount() {
        return softwarecount;
    }

    public void setSoftwarecount(String softwarecount) {
        this.softwarecount = softwarecount;
    }

    public String getBackspeed() {
        return backspeed;
    }

    public void setBackspeed(String backspeed) {
        this.backspeed = backspeed;
    }

    public String getCpuinfo() {
        return cpuinfo;
    }

    public void setCpuinfo(String cpuinfo) {
        this.cpuinfo = cpuinfo;
    }

    public String getCupuse() {
        return cupuse;
    }

    public void setCupuse(String cupuse) {
        this.cupuse = cupuse;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getMemoryuse() {
        return memoryuse;
    }

    public void setMemoryuse(String memoryuse) {
        this.memoryuse = memoryuse;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SaveDownTestDate{");
        sb.append("avgSpeed='").append(avgSpeed).append('\'');
        sb.append(", maxSpeed='").append(maxSpeed).append('\'');
        sb.append(", allAvgSpeeds='").append(allAvgSpeeds).append('\'');
        sb.append(", showSpeed='").append(showSpeed).append('\'');
        sb.append(", isSuccess='").append(isSuccess).append('\'');
        sb.append(", testsource='").append(testsource).append('\'');
        sb.append(", location='").append(location).append('\'');
        sb.append(", target='").append(target).append('\'');
        sb.append(", totalSpeed='").append(totalSpeed).append('\'');
        sb.append(", softwarecount='").append(softwarecount).append('\'');
        sb.append(", backspeed='").append(backspeed).append('\'');
        sb.append(", cpuinfo='").append(cpuinfo).append('\'');
        sb.append(", cupuse='").append(cupuse).append('\'');
        sb.append(", memory='").append(memory).append('\'');
        sb.append(", memoryuse='").append(memoryuse).append('\'');
        sb.append(", mark='").append(mark).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
