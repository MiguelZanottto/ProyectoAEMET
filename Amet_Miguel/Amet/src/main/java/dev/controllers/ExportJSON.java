package dev.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.models.Aemet;
import dev.utils.LocalDateAdapter;
import dev.utils.LocalTimeAdapter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ExportJSON {
    private final String APP_PATH = System.getProperty("user.dir");
    private final String DATA_DIR = APP_PATH + File.separator + "data";
    private final String BACKUP_FILE = DATA_DIR + File.separator + "aemet.json";


    public void exportar(List<Aemet> aemet) throws IOException {
         Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .setPrettyPrinting()
                .create();
        String json = gson.toJson(aemet);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(BACKUP_FILE), StandardCharsets.UTF_8))) {
            writer.write(json);
        }
    }
}

