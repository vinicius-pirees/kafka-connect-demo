FROM cnfldemos/cp-server-connect-datagen:0.4.0-6.1.0

USER root

RUN wget https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.16.1/jmx_prometheus_javaagent-0.16.1.jar && \
    mkdir -p /opt/kafka/libs/ && \
    mv ./jmx_prometheus_javaagent-0.16.1.jar /opt/kafka/libs/jmx_prometheus_javaagent.jar

RUN confluent-hub install --no-prompt mongodb/kafka-connect-mongodb:1.6.1 && \
    confluent-hub install --no-prompt confluentinc/kafka-connect-datagen:latest


RUN wget https://repo1.maven.org/maven2/io/debezium/debezium-connector-postgres/1.4.0.Final/debezium-connector-postgres-1.4.0.Final-plugin.tar.gz && \
    tar -xzvf debezium-connector-postgres-1.4.0.Final-plugin.tar.gz -C /usr/share/java/ && \
    rm  debezium-connector-postgres-1.4.0.Final-plugin.tar.gz


