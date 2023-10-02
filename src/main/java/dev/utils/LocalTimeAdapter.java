package dev.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalTime;

/**
 * Un adaptador personalizado de Gson para serializar y deserializar objetos de tipo LocalTime.
 */
public class LocalTimeAdapter extends TypeAdapter<LocalTime> {
    /**
     * Lee una instancia de LocalTime desde una cadena en el lector JSON y la devuelve.
     *
     * @param jsonReader El lector JSON.
     * @return La instancia de LocalTime.
     * @throws IOException Si ocurre un error durante la lectura o el formato de la cadena no es valido.
     */
    @Override
    public LocalTime read(final JsonReader jsonReader) throws IOException {
        return LocalTime.parse(jsonReader.nextString());
    }

    /**
     * Escribe una instancia de LocalTime en formato de cadena en el escritor de JSON.
     *
     * @param jsonWriter El escritor JSON.
     * @param localTime  El valor de tipo LocalTime que se va a escribir.
     * @throws IOException Si ocurre un error durante la escritura.
     */
    @Override
    public void write(JsonWriter jsonWriter, LocalTime localTime) throws IOException {
        jsonWriter.value(localTime.toString());
    }
}

