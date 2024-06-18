package org.github.mbmll.example.flink;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.utils.ParameterTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author xlc
 * @Description
 * @Date 2024/1/20 21:21:19
 */

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.warn("start process!");

        // Checking input parameters
        final ParameterTool params = ParameterTool.fromArgs(args);

        // set up execution environment
        ExecutionEnvironment env = ExecutionEnvironment.createLocalEnvironment();
        env.getConfig()
            .setGlobalJobParameters(params); // make parameters available in the web interface
    }

}