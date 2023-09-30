package dev.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalTime;

public class LocalTimeAdapter extends TypeAdapter<LocalTime> {

    @Override
    public LocalTime read(final JsonReader jsonReader) throws IOException {
        return LocalTime.parse(jsonReader.nextString());
    }

    @Override
    public void write(JsonWriter jsonWriter, LocalTime localTime) throws IOException {
        jsonWriter.value(localTime.toString());
    }




}
