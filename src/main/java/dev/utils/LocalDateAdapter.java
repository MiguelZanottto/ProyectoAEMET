package dev.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Un adaptador personalizado de Gson para serializar y deserializar objetos de tipo LocalDate.
 */
public class LocalDateAdapter extends TypeAdapter<LocalDate> {
    /**
     * Escribe una instancia de LocalDate en formato de cadena en el JSON.
     *
     * @param out   El escritor JSON.
     * @param value El valor de tipo LocalDate que se va a escribir.
     * @throws IOException Si ocurre un error durante la escritura.
     */
    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        out.value(value.toString());
    }

    /**
     * Lee una instancia de LocalDate desde una cadena en el lector JSON y la devuelve.
     *
     * @param jsonReader El lector JSON.
     * @return La instancia de Localdate leida.
     * @throws IOException Si ocurre un error durante la lectura o el formato de la cadena no es valido.
     */
    @Override
    public LocalDate read(final JsonReader jsonReader) throws IOException {
        return LocalDate.parse(jsonReader.nextString());
    }
}

