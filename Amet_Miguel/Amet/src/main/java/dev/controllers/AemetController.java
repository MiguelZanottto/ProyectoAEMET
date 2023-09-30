package dev.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import dev.models.Aemet;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AemetController {
    private static AemetController instance;
    ArrayList<Aemet> lista = new ArrayList<>();

    public AemetController() throws IOException {
        loadData();
    }

    public ArrayList<Aemet> getLista() {
        return lista;
    }

    public static AemetController getInstance() throws IOException {
        if (instance == null){
            instance = new AemetController();
        }
        return instance;
    }

    private void loadData() throws IOException {
        cargarCSV();
    }

    public void mostrar(){
        lista.forEach(aemet -> System.out.println(aemet));
    }

    private void cargarCSV() throws IOException {
        leerCSV("Aemet20171029");
        leerCSV("Aemet20171030");
        leerCSV("Aemet20171031");
    }

    private void leerCSV(String nombreFichero) throws IOException {
        Path relativePath = Paths.get("");
        String rutaRelativa = relativePath.toAbsolutePath().toString();
        String directorio = rutaRelativa + File.separator + "data";
        String CSVFichero = directorio + File.separator + nombreFichero + ".csv";
        String CSVFicheroConvertido = directorio + File.separator + nombreFichero + "_convertido.csv";

        convertFileToUtf8(CSVFichero, CSVFicheroConvertido );

        try (CSVReader reader = new CSVReader(new FileReader(CSVFicheroConvertido))) {
            String[] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                String[] datos = line[0].split(";");
                if (datos.length >= 7) {
                    Aemet aemet = Aemet.builder()
                            .localidad(datos[0])
                            .provincia(datos[1])
                            .temperaturaMax(Double.parseDouble(datos[2]))
                            .horaTemperaturaMax(getHora(datos[3]))
                            .temperaturaMin(Double.parseDouble(datos[4]))
                            .horaTemperaturaMin(getHora(datos[5]))
                            .precipitacion(Double.parseDouble(datos[6]))
                            .dia(getDia(nombreFichero))
                            .build();
                    lista.add(aemet);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void convertFileToUtf8(String inputFile, String outputFile) throws IOException {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), Charset.forName("Windows-1252")));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private LocalTime getHora(String hora) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return hora.length() == 4 ? LocalTime.parse("0" + hora, formatter) : LocalTime.parse(hora, formatter);
    }

    private LocalDate getDia(String dia) {
        dia = dia.substring(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(dia, formatter);
    }
}
