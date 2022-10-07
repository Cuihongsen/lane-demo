package com.example.gateway.ribbon;

/**
 * 有个bug，第一次启动的时候不生效的
 *
 * @author Cui
 */
public class LaneThreadLocalEnvironment {
    private static final ThreadLocal<String> laneThreadLocal = new ThreadLocal<String>();

    public static String getCurrentEnvironment() {
        return laneThreadLocal.get();
    }

    public static void setCurrentEnvironment(String currentLane) {
        System.out.println("lane = " + currentLane);
        laneThreadLocal.set(currentLane);
    }
}