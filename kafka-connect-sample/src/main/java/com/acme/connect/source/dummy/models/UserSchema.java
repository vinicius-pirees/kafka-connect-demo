package com.acme.connect.source.dummy.models;

import com.google.gson.annotations.SerializedName;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;


public class UserSchema {

    public static final String ID = "id";
    public static final String NAME = "name";

    @SerializedName(value = "id")
    private Long Id;
    @SerializedName(value = "name")
    private String Name;

    UserSchema( Long id, String name) {
        Id = id;
        Name = name;
    }

    public Long getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public static Schema UserSchemaDef(){
        return SchemaBuilder.struct()
                .name(UserSchema.class.getSimpleName())

                .field(ID, Schema.INT64_SCHEMA)
                .field(NAME, Schema.OPTIONAL_STRING_SCHEMA)

                .build();
    }



    public Struct toStruct() {
        Struct schemaStruct = new Struct(UserSchemaDef())

                .put(ID, getId())
                .put(NAME, getName());

        return schemaStruct;
    }
}
