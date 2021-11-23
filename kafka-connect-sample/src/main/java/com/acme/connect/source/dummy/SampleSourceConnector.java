package com.acme.connect.source.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.kafka.common.config.Config;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigValue;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceConnector;

public class SampleSourceConnector extends SourceConnector {

    private final Logger log = LoggerFactory.getLogger(SampleSourceConnector.class);

    private Map<String, String> originalProps;
    private SampleSourceConnectorConfig config;

    @Override
    public String version() {
        return PropertiesUtil.getConnectorVersion();
    }

    @Override
    public ConfigDef config() {
        return SampleSourceConnectorConfig.CONFIG_DEF;
    }

    @Override
    public Class<? extends Task> taskClass() {
        return SampleSourceTask.class;
    }

    @Override
    public Config validate(Map<String, String> connectorConfigs) {
        Config config = super.validate(connectorConfigs);
        List<ConfigValue> configValues = config.configValues();

        //You can raise an exception if a config is different from the expected
        return config;
    }

    @Override
    public void start(Map<String, String> originalProps) {
        this.originalProps = originalProps;
        config = new SampleSourceConnectorConfig(originalProps);
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        if (maxTasks != 1) {
            log.info("Ignoring maxTasks as there can only be one.");
        }

        List<Map<String, String>> taskConfigs = new ArrayList<>();
        Map<String, String> taskConfig = new HashMap<>(originalProps);
        taskConfigs.add(taskConfig);

        return taskConfigs;
    }

    @Override
    public void stop() {

    }

}
