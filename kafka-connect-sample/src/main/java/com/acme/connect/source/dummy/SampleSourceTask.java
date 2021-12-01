package com.acme.connect.source.dummy;
import java.io.*;
import java.net.*;
import java.util.Base64;

import com.acme.connect.source.dummy.models.*;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;

public class SampleSourceTask extends SourceTask {

    private static Logger log = LoggerFactory.getLogger(SampleSourceTask.class);

    private SampleSourceConnectorConfig config;

    public static final String LAST_API_OFFSET = "last_id";
    private long apiOffset;
    private String topic;
    private int pollInterval;
    private String url;
    private Long fromId;
    private Long startId;
    private Long currentId;

    @Override
    public String version() {
        return PropertiesUtil.getConnectorVersion();
    }

    @Override
    public void start(Map<String, String> properties) {
        config = new SampleSourceConnectorConfig(properties);
        startId = config.getLong(SampleSourceConnectorConfig.START_ID);
        topic = config.getString(SampleSourceConnectorConfig.DESTINATION_TOPIC_CONFIG);
        url =  config.getString(SampleSourceConnectorConfig.URL);
        pollInterval = config.getInt(SampleSourceConnectorConfig.POLL_INTERVAL_MILLISECONDS_CONFIG);

        Map<String, Object> persistedMap = null;
        if (context != null && context.offsetStorageReader() != null) {
            persistedMap = context.offsetStorageReader().offset(Collections.singletonMap("table", "dummy"));
        }
        log.info("The persistedMap is {}", persistedMap);


        if (persistedMap != null) {
            Object lastApiOffset = persistedMap.get(LAST_API_OFFSET);
            if (lastApiOffset != null) {
                apiOffset = (Long) lastApiOffset;
                fromId = apiOffset;
            }
        } else {
            fromId = startId;
        }
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        Thread.sleep(pollInterval);
        int numRecords;
        List<SourceRecord> records;
        String urlComplement;
        String fullUrl;
        String data;
        Long endTime;
        Boolean endOfStream;


        fullUrl = url + "/users?from_id=" + fromId;
        log.info("Calling "  + fullUrl);
        data = getFromRequest(fullUrl);
        if (data.equals("")) {
            return new ArrayList<>();
        }

        UserResponse userResponse = new Gson().fromJson(data, UserResponse.class);
        List<UserSchema>  userRecords = userResponse.getUserRecords();
        numRecords = userRecords.size();
        records = new ArrayList<>();

        log.info(numRecords + " records polled!");





        for (UserSchema user : userRecords) {

            try {
                currentId = user.getId();
                records.add(new SourceRecord(
                        Collections.singletonMap("table", "dummy"),
                        buildSourceOffset(currentId),
                        topic, null, null, null, UserSchema.UserSchemaDef(),
                        user.toStruct()));
                fromId = currentId;

            } catch (Exception e) {
                log.info(new Gson().toJson(user));
                e.printStackTrace();
            }
        }

        

        return records;


    }


    private String getFromRequest(String urlText ){

        URL url = null;//your url i.e fetch data from .
        try {
            url = new URL(urlText);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setRequestProperty("Accept", "application/json");
        try {
            if (conn.getResponseCode() != 200) {

                BufferedReader errorBr = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

                String outputError = null;
                StringBuilder sbError = new StringBuilder();
                while (true) {
                    try {
                        if ((outputError = errorBr.readLine()) == null) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sbError.append(outputError).append("\n");
                }
                log.info(sbError.toString());

                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(conn.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert in != null;
        BufferedReader br = new BufferedReader(in);
        StringBuilder sb = new StringBuilder();

        String output = null;
        while (true) {
            try {
                if ((output = br.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb.append(output).append("\n");
        }

        return sb.toString();

    }



    private Map<String, Object> buildSourceOffset(Long apiOffset) {
        Map<String, Object> sourceOffset = new HashMap<>();
        sourceOffset.put(LAST_API_OFFSET, apiOffset);
        return sourceOffset;
    }


    @Override
    public void stop() {
    }

}
