package com.acme.connect.source.dummy;

import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

public class SampleSourceConnectorConfig extends AbstractConfig {

    public SampleSourceConnectorConfig(final Map<?, ?> originalProps) {
        super(CONFIG_DEF, originalProps);
    }


    public static final String DESTINATION_TOPIC_CONFIG = "topic";
    private static final String DESTINATION_TOPIC_DOC = "Destination topic";


    public static final String START_ID = "start_id";
    private static final String START_TIME_DOC = "Starting ID";
    private static final Long START_TIME_DEFAULT = 0L;

    public static final String POLL_INTERVAL_MILLISECONDS_CONFIG = "poll.interval.ms";
    private static final String POLL_INTERVAL_MILLISECONDS_DOC = "Poll interval";
    public static final int POLL_INTERVAL_MILLISECONDS_DEFAULT = 2000;

    public static final String URL = "url";
    private static final String URL_DOC = "Url address";

    public static final ConfigDef CONFIG_DEF = createConfigDef();

    private static ConfigDef createConfigDef() {
        ConfigDef configDef = new ConfigDef();
        addParams(configDef);
        return configDef;
    }

    private static void addParams(final ConfigDef configDef) {
        configDef.define(START_ID, Type.LONG, START_TIME_DEFAULT,  Importance.HIGH, START_TIME_DOC)
                .define(DESTINATION_TOPIC_CONFIG, Type.STRING, Importance.HIGH, DESTINATION_TOPIC_DOC)
                .define(URL, Type.STRING, Importance.HIGH, URL_DOC)
                .define(POLL_INTERVAL_MILLISECONDS_CONFIG, Type.INT, POLL_INTERVAL_MILLISECONDS_DEFAULT, Importance.HIGH, POLL_INTERVAL_MILLISECONDS_DOC);
    }

}
