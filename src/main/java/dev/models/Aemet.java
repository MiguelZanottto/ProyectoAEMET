package dev.models;

import lombok.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
/**
 * Esta clase representa los datos meteorologicos de AEMET.
 */
@Data
@Getter
@Setter
@Builder
@ToString
public class Aemet {
    /**
     * El nombre de la localidad.
     */
    private String localidad;
    /**
     * El nombre de la provincia.
     */
    private String provincia;
    /**
     * La temperatura Maxima registrada.
     */
    private double temperaturaMax;
    /**
     * La hora donde se registro la temperatura Maxima.
     */
    private LocalTime horaTemperaturaMax;

    /**
     * La temperatura Minima registrada.
     */
    private double temperaturaMin;
    /**
     * La hora donde se registro la temperatura Minima.
     */
    private LocalTime horaTemperaturaMin;
    /**
     * La cantidad de precipitacion que se registro.
     */
    private double precipitacion;
    /**
     * El dia en especifico registrado en el CSV.
     */
    private LocalDate dia;

    /**
     * Identificador unico para el registro.
     */
    private Long id;
}
