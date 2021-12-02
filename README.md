# kafka-connect-demo

Set up:
```bash
$ docker-compose up -d
```


## Connector deployments

Postgres Source connector:
```bash
curl -X POST -H "Content-Type: application/json"  --data '
{
  "name": "shipments-connector",  
  "config": {
    "connector.class": "io.debezium.connector.postgresql.PostgresConnector", 
    "plugin.name": "pgoutput",
    "database.hostname": "postgres", 
    "database.port": "5432", 
    "database.user": "postgresuser", 
    "database.password": "postgrespw", 
    "database.dbname" : "shipment_db", 
    "database.server.name": "postgres", 
    "table.include.list": "public.shipments",
    "slot.name" : "myslotname"    
  }
}' http://localhost:8083/connectors 

```


MongoDB Sink connector:
```bash
curl -X POST -H "Content-Type: application/json" --data '
  {"name": "mongo-sink",
   "config": {
     "connector.class":"com.mongodb.kafka.connect.MongoSinkConnector",
     "tasks.max":"1",
     "topics":"postgres.public.shipments",
     "connection.uri":"mongodb://mongodb1:27017",
     "database":"test",
     "collection":"shipments"
  }
}' http://localhost:8083/connectors

```


Sample Source connector (API):
```bash
curl -X POST -H "Content-Type: application/json"  --data '
{
  "name" : "dummy-api-source",
  "config" : {
    "connector.class": "com.acme.connect.source.dummy.SampleSourceConnector",
    "tasks.max": "1",
    "url": "http://dummy-api:8080",
    "topic": "my-users",
    "poll.interval.ms": 10000
  }
}' http://localhost:8083/connectors 

```