package com.xonro.ehr;

import com.actionsoft.bpms.server.AWSServer;


/**
 * aws服务入口类
 * Created by louie on 2017-12-18.
 */
public class ServerStart {
    public static void main(String[] args) {
        try {
            AWSServer.getInstance().startup(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
