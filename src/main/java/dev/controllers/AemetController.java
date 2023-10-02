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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Esta clase es el controlador principal para gestionar los datos meteorologicos obtenidos de los CSV.
 * Este se encarga de cargar los datos, leer los csv, leer bien los datos de las fechas del csv, y las Api Stream.
 */
public class AemetController {
    private static AemetController instance;
    ArrayList<Aemet> lista = new ArrayList<>();

    // Constructor privado para implementar Singleton y cargar datos.
    private AemetController() throws IOException {
        loadData();
    }

    /**
     * Obtiene la lista de objetos Aemet.
     *
     * @return ArrayList de objetos Aemet.
     */
    public ArrayList<Aemet> getLista() {
        return lista;
    }

    /**
     * Obtiene una instancia unica de la clase AemetController.
     *
     * @return La instancia unica de AemetController.
     * @throws IOException Si hay un error al cargar los datos.
     */
    public static AemetController getInstance() throws IOException {
        if (instance == null) {
            instance = new AemetController();
        }
        return instance;
    }

    /**
     * Carga los datos de los archivos CSV.
     *
     * @throws IOException Si se produce un error durante la lectura o carga de los archivos CSV.
     */
    private void loadData() throws IOException {
        cargarCSV();
    }

    /**
     * Muestra todas las mediciones meteorologicas en la lista.
     */
    public void mostrar() {
        lista.forEach(aemet -> System.out.println(aemet));
    }

    /**
     * Carga datos desde los archivos CSV y los convierte a objetos Aemet.
     *
     * @throws IOException Si se produce un error durante la lectura o carga de los archivos CSV.
     */
    private void cargarCSV() throws IOException {
        leerCSV("Aemet20171029");
        leerCSV("Aemet20171030");
        leerCSV("Aemet20171031");
    }

    /**
     * Lee los archivos CSV y los convierte en objetos de Aemet.
     *
     * @param nombreFichero El nombre del archivo CSV a leer.
     * @throws IOException Si se produce un error durante la lectura del archivo o la conversion de datos.
     */
    private void leerCSV(String nombreFichero) throws IOException {
        // Obtiene la ruta relativa al directorio de datos.
        Path relativePath = Paths.get("");
        String rutaRelativa = relativePath.toAbsolutePath().toString();
        String directorio = rutaRelativa + File.separator + "data";
        String CSVFichero = directorio + File.separator + nombreFichero + ".csv";
        String CSVFicheroConvertido = directorio + File.separator + nombreFichero + "_convertido.csv";

        // Convierte el archivo CSV al formato UTF-8 para manejar caracteres especiales.
        convertFileToUtf8(CSVFichero, CSVFicheroConvertido);

        try (CSVReader reader = new CSVReader(new FileReader(CSVFicheroConvertido))) {
            String[] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                String[] datos = line[0].split(";");
                if (datos.length >= 7) {
                    // Crea un objeto Aemet a partir de los datos y lo agrega a la lista.
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

    /**
     * Convierte un archivo de texto a formato UTF-8 para manejar caracteres especiales.
     *
     * @param inputFile  El nombre del archivo de entrada en formato Windows-1252.
     * @param outputFile El nombre del archivo de salida en formato UTF-8.
     * @throws IOException Si se produce un error durante la conversion de archivos.
     */
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

    /**
     * Convierte una cadena de hora en formato "HH:mm" en un objeto LocalTime.
     *
     * @param hora La cadena de hora en formato "HH:mm".
     * @return Un objeto LocalTime que representa la hora.
     */
    private LocalTime getHora(String hora) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return hora.length() == 4 ? LocalTime.parse("0" + hora, formatter) : LocalTime.parse(hora, formatter);
    }

    /**
     * Convierte una cadena de fecha en formato "yyyyMMdd" en un objeto LocalDate.
     *
     * @param dia La cadena de fecha en formato "yyyyMMdd".
     * @return Un objeto LocalDate que representa la fecha.
     */
    private LocalDate getDia(String dia) {
        dia = dia.substring(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(dia, formatter);
    }

    /**
     *Esta parte se crean los metodos para hacer las Api Stream para realizar las busquedas dentro de la base de datos.
     */

    /**
     * Calcula y almacena las localidades con la temperatura maxima y minima por dia.
     */
    private void MaximaYMinimaTemperaturaporDia() {
        System.out.println("Maximas y minimas temperaturas en cada uno de los dias");

        Predicate<Aemet> temperaturaValida = aemet -> true; // Todos los valores de double son válidos

        Map<String, Double[]> result = new HashMap<>();

        lista.stream()
                .filter(temperaturaValida)
                .forEach(aemet -> {
                    String dia = aemet.getDia();

                    if (!result.containsKey(dia)) {
                        Double[] temperatures = new Double[2];
                        temperatures[0] = aemet.getTemperaturaMax();
                        temperatures[1] = aemet.getTemperaturaMin();
                        result.put(dia, temperatures);
                    } else {
                        Double[] existingTemperatures = result.get(dia);
                        if (aemet.getTemperaturaMax() > existingTemperatures[0]) {
                            existingTemperatures[0] = aemet.getTemperaturaMax();
                        }
                        if (aemet.getTemperaturaMin() < existingTemperatures[1]) {
                            existingTemperatures[1] = aemet.getTemperaturaMin();
                        }
                    }
                });

        result.forEach((dia, temperatures) -> {
            double maxTemp = temperatures[0];
            double minTemp = temperatures[1];
            String localidad = lista.stream()
                    .filter(aemet -> aemet.getDia().equals(dia) && aemet.getTemperaturaMax() == maxTemp && aemet.getTemperaturaMin() == minTemp)
                    .findFirst()
                    .map(Aemet::getLocalidad)
                    .orElse("");

            System.out.println("Dia: " + dia + ", Temperatura Maxima: " + maxTemp + ", Temperatura Minima: " + minTemp + ", Localidad: " + localidad);
        });

        System.out.println(result);
    }


    /**
     * Calcula y almacena la temperatura maxima por provincia y dia.
     */
    private void MaximaTemperaturaProvinciasydia() {
        Map<String, Map<String, Double>> maxTemperaturaPorProvinciaYDia = new HashMap<>();

        lista.stream()
                .filter(aemet -> aemet.getTemperaturaMax() >= 0) // Verifica que la temperatura máxima sea válida (no negativa)
                .forEach(aemet -> {
                    String provincia = aemet.getProvincia();
                    String dia = aemet.getDia();
                    double temperaturaMax = aemet.getTemperaturaMax();

                    if (!maxTemperaturaPorProvinciaYDia.containsKey(provincia)) {
                        maxTemperaturaPorProvinciaYDia.put(provincia, new HashMap<>());
                    }

                    Map<String, Double> provinciaMap = maxTemperaturaPorProvinciaYDia.get(provincia);
                    if (!provinciaMap.containsKey(dia) || temperaturaMax > provinciaMap.get(dia)) {
                        provinciaMap.put(dia, temperaturaMax); // Actualiza la temperatura máxima si es mayor
                    }
                });

        System.out.println("Resultados de temperaturas maximas por provincia y dia:");
        maxTemperaturaPorProvinciaYDia.forEach((provincia, diaTempMap) -> {
            System.out.println("Provincia: " + provincia);
            diaTempMap.forEach((dia, temperaturaMax) -> {
                System.out.println("Día: " + dia + ", Temperatura Máxima: " + temperaturaMax);
            });
        });
    }



    /**
     * Calcula y almacena la temperatura minima por provincia y dia.
     */
    private void MinimaTemperaturaProvinciasydia() {
        Map<String, Map<String, Double>> minTemperaturaPorProvinciaYDia = new HashMap<>();
        lista.stream()
                .filter(aemet -> aemet.getTemperaturaMin() >= 0)
                .forEach(aemet -> {
                    String provincia = aemet.getProvincia();
                    String dia = aemet.getDia();
                    double temperaturaMin = aemet.getTemperaturaMin();

                    if (!minTemperaturaPorProvinciaYDia.containsKey(provincia)) {
                        minTemperaturaPorProvinciaYDia.put(provincia, new HashMap<>());
                    }

                    Map<String, Double> provinciaMap = minTemperaturaPorProvinciaYDia.get(provincia);
                    if (!provinciaMap.containsKey(dia) || temperaturaMin > provinciaMap.get(dia)) {
                        provinciaMap.put(dia, temperaturaMin);
                    }
                });

        System.out.println("Resultados de temperaturas minimas por provincia y dia:");
        minTemperaturaPorProvinciaYDia.forEach((provincia, diaTempMap) -> {
            System.out.println("Provincia: " + provincia);
            diaTempMap.forEach((dia, temperaturaMin) -> {
                System.out.println("Dia: " + dia + ", Temperatura Minima: " + temperaturaMin);
            });
        });

    }

    /**
     * Calcula y almacena la media de la temperatura por provincia y dia.
     */
    private void MediaTemperaturaProvinciasyDia() {
        Map<String, Map<String, Double>> mediatemperatura = new HashMap<>();

        lista.stream()
                .filter(aemet -> aemet.getTemperaturaMax() >= 0 && aemet.getTemperaturaMin() >= 0) // Verifica ambas temperaturas válidas
                .forEach(aemet -> {
                    String provincia = aemet.getProvincia();
                    String dia = aemet.getDia();
                    double temperaturaMax = aemet.getTemperaturaMax();
                    double temperaturaMin = aemet.getTemperaturaMin();

                    if (!mediatemperatura.containsKey(provincia)) {
                        mediatemperatura.put(provincia, new HashMap<>());
                    }

                    Map<String, Double> provinciaMap = mediatemperatura.get(provincia);
                    if (!provinciaMap.containsKey(dia)) {
                        provinciaMap.put(dia, (temperaturaMax + temperaturaMin) / 2); // Calcula la media
                    } else {
                        double mediaActual = provinciaMap.get(dia);
                        provinciaMap.put(dia, (mediaActual + (temperaturaMax + temperaturaMin) / 2) / 2); // Actualiza la media
                    }
                });

        mediatemperatura.forEach((provincia, diaTempMap) -> {
            System.out.println("Provincia: " + provincia);
            diaTempMap.forEach((dia, temperaturaMedia) -> {
                System.out.println("Dia: " + dia + ", Temperatura Media: " + temperaturaMedia);
            });
        });
    }



    /**
     * Calcula y almacena la precipitacion maxima por dia y localidad.
     */
    private void PrecipitacionMaximaDiasLocalidad() {
        Map<String, Map<String, Double>> maxPrecipitacionPorDiaYLocalidad = new HashMap<>();

        lista.stream()
                .filter(aemet -> aemet.getPrecipitacion() >=0)
                .forEach(aemet -> {
                    String dia = aemet.getDia();
                    String localidad = aemet.getLocalidad();
                    double precipitacion = aemet.getPrecipitacion();

                    if (!maxPrecipitacionPorDiaYLocalidad.containsKey(dia)) {
                        maxPrecipitacionPorDiaYLocalidad.put(dia, new HashMap<>());
                    }

                    Map<String, Double> localidadMap = maxPrecipitacionPorDiaYLocalidad.get(dia);
                    if (!localidadMap.containsKey(localidad) || precipitacion > localidadMap.get(localidad)) {
                        localidadMap.put(localidad, precipitacion);
                    }
                });
        maxPrecipitacionPorDiaYLocalidad.forEach((dia, localidadPrecipitacionMap) -> {
            System.out.println("Dia: " + dia);
            localidadPrecipitacionMap.forEach((localidad, maxPrecipitacion) -> {
                System.out.println("Localidad: " + localidad + ", Precipitacion Maxima: " + maxPrecipitacion);
            });
        });
    }


    /**
     * Calcula y almacena la precipitacion media por provincia y dia.
     */
    private void PrecipitacionMediaProvinciasyDias() {
        Map<String, Map<String, Double>> mediaPrecipitacion = new HashMap<>();
        lista.stream()
                .filter(aemet -> aemet.getPrecipitacion() >=0)
                .forEach(aemet -> {
                    String provincia = aemet.getProvincia();
                    String dia = aemet.getDia();
                    double precipitacion = aemet.getPrecipitacion();

                    if (!mediaPrecipitacion.containsKey(provincia)) {
                        mediaPrecipitacion.put(provincia, new HashMap<>());
                    }

                    Map<String, Double> provinciaMap = mediaPrecipitacion.get(provincia);
                    if (!provinciaMap.containsKey(dia)) {
                        provinciaMap.put(dia, precipitacion);
                    } else {
                        double precipitacionMedia = provinciaMap.get(dia);
                        int count = provinciaMap.size() + 1;
                        double nuevaMedia = (precipitacionMedia + precipitacion) / count;
                        provinciaMap.put(dia, nuevaMedia);
                    }
                });

        mediaPrecipitacion.forEach((provincia, diaPrecipitacionMap) -> {
            System.out.println("Provincia: " + provincia);
            diaPrecipitacionMap.forEach((dia, precipitacionMedia) -> {
                System.out.println("Dia: " + dia + ", Precipitacion Media: " + precipitacionMedia);
            });
        });
    }



    /**
     * Calcula y almacena las localidades donde ha llovido por provincia y dia.
     */
    private void LugaresLlovidoProvinciasYDia() {
        Map<String, Map<String, List<String>>> lugaresLluviosos = new HashMap<>();

        lista.stream()
                .filter(aemet -> aemet.getPrecipitacion() >= 0)
                .forEach(aemet -> {
                    String provincia = aemet.getProvincia();
                    String dia = aemet.getDia();
                    String localidad = aemet.getLocalidad();

                    if (!lugaresLluviosos.containsKey(provincia)) {
                        lugaresLluviosos.put(provincia, new HashMap<>());
                    }

                    Map<String, List<String>> provinciaMap = lugaresLluviosos.get(provincia);
                    if (!provinciaMap.containsKey(dia)) {
                        provinciaMap.put(dia, new ArrayList<>());
                    }
                    provinciaMap.get(dia).add(localidad);
                });
        lugaresLluviososPorProvinciaYDia.forEach((provincia, diaLocalidadesMap) -> {
            System.out.println("Provincia: " + provincia);
            diaLocalidadesMap.forEach((dia, localidades) -> {
                System.out.println("Dia: " + dia);
                System.out.println("Localidades con precipitacion: " + String.join(", ", localidades));
            });
        });
    }


    /**
     * Calcula y almacena la localidad con mayor precipitacion acumulada.
     */
    private void LugaresLLovidoMas() {
        Map<String, Double> lugarPrecipitacionPromedio = new HashMap<>();

        lista.stream()
                .filter(aemet -> aemet.getPrecipitacion() >=0) //
                .forEach(aemet -> {
                    String localidad = aemet.getLocalidad();
                    double precipitacion = aemet.getPrecipitacion();

                    if (!lugarPrecipitacionPromedio.containsKey(localidad)) {
                        lugarPrecipitacionPromedio.put(localidad, precipitacion);
                    } else {
                        double cola = lugarPrecipitacionPromedio.get(localidad);
                        int count = lugarPrecipitacionPromedio.size() + 1;
                        double nuevoPromedio = (cola + precipitacion) / count;
                        lugarPrecipitacionPromedio.put(localidad, nuevoPromedio);
                    }
                });

        String localidadMasLluviosa = null;
        double precipitacionMasAlta = Double.MIN_VALUE;

        for (String localidad : lugarPrecipitacionPromedio.keySet()) {
            double precipitacionPromedio = lugarPrecipitacionPromedio.get(localidad);
            if (localidadMasLluviosa == null || precipitacionPromedio > precipitacionMasAlta) {
                localidadMasLluviosa = localidad;
                precipitacionMasAlta = precipitacionPromedio;
            }
        }

        if (localidadMasLluviosa != null) {
            System.out.println("Localidad mas lluviosa: " + localidadMasLluviosa);
            System.out.println("Precipitacion promedio: " + precipitacionMasAlta);
        }

    }


    /**
     * Realiza un analisis de datos especifico para la provincia de Madrid y almacena los resultados.
     * Calcula estadisticas como la temperatura maxima, temperatura minima, temperatura media,
     * precipitacion maxima, precipitacion media, y las localidades correspondientes a dichos valores
     * por dia y localidad dentro de la provincia de Madrid.
     * @return Un mapa que contiene estadisticas por dia y localidad para la provincia de Madrid, como en el ejemplo de
     * clase que se dio en la practica.
     */
    private void ProvinciaMadrid() {
        Map<String, Map<String, Double>> datosMadridPorDia = new HashMap<>();

        lista.stream()
                .filter(aemet -> "Madrid".equals(aemet.getProvincia()))
                .forEach(aemet -> {
                    String dia = aemet.getDia();
                    String localidad = aemet.getLocalidad();

                    if (!datosMadridPorDia.containsKey(dia)) {
                        datosMadridPorDia.put(dia, new HashMap<>());
                    }

                    Map<String, Double> datosPorDia = datosMadridPorDia.get(dia);

                    // Temperatura Maxima
                    double temperaturaMaxima = aemet.getTemperaturaMax();
                    double maxTemperaturaMaxima = datosPorDia.getOrDefault("TemperaturaMaxima", Double.MIN_VALUE);
                    if (temperaturaMaxima >= maxTemperaturaMaxima) {
                        datosPorDia.put("TemperaturaMaxima", temperaturaMaxima);
                        datosPorDia.put("LocalidadTemperaturaMaxima", localidad);
                    }

                    // Temperatura Minima
                    double temperaturaMinima = aemet.getTemperaturaMin();
                    double minTemperaturaMinima = datosPorDia.getOrDefault("TemperaturaMinima", Double.MAX_VALUE);
                    if (temperaturaMinima <= minTemperaturaMinima) {
                        datosPorDia.put("TemperaturaMinima", temperaturaMinima);
                        datosPorDia.put("LocalidadTemperaturaMinima", localidad);
                    }

                    // Temperatura Media Maxima
                    double temperaturaMediaMaxima = datosPorDia.getOrDefault("TemperaturaMediaMaxima", 0.0);
                    datosPorDia.put("TemperaturaMediaMaxima", (temperaturaMediaMaxima + temperaturaMaxima) / 2.0);

                    // Temperatura Media Minima
                    double temperaturaMediaMinima = datosPorDia.getOrDefault("TemperaturaMediaMinima", 0.0);
                    datosPorDia.put("TemperaturaMediaMinima", (temperaturaMediaMinima + temperaturaMinima) / 2.0);

                    // Precipitacion Maxima
                    double precipitacion = aemet.getPrecipitacion();
                    double maxPrecipitacion = datosPorDia.getOrDefault("PrecipitacionMaxima", Double.MIN_VALUE);
                    if (precipitacion >= maxPrecipitacion) {
                        datosPorDia.put("PrecipitacionMaxima", precipitacion);
                        datosPorDia.put("LocalidadPrecipitacionMaxima", localidad);
                    }

                    // Precipitacion Media
                    double precipitacionMedia = datosPorDia.getOrDefault("PrecipitacionMedia", 0.0);
                    datosPorDia.put("PrecipitacionMedia", (precipitacionMedia + precipitacion) / 2.0);
                });

        // Mostrar resultados
        datosMadridPorDia.forEach((dia, datosPorDia) -> {
            System.out.println("Dia: " + dia);
            datosPorDia.forEach((dato, valor) -> {
                System.out.println(dato + ": " + valor);
            });
        });
    }

}