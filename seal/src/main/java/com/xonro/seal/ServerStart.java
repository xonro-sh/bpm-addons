package com.xonro.seal;

import com.actionsoft.bpms.server.AWSServer;
import com.actionsoft.exception.AWSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * aws服务入口类
 * Created by louie on 2017-12-18.
 */
public class ServerStart {
    private static Logger logger = LoggerFactory.getLogger(ServerStart.class);
    public static void main(String[] args) {
        try {
            AWSServer.getInstance().startup(args);
        } catch (AWSException e) {
            logger.error(e.getMessage(),e);
        }
    }
}
