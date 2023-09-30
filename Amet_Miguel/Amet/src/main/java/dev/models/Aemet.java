package dev.models;

import lombok.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Getter
@Setter
@Builder
@ToString
public class Aemet {
    private String localidad;
    private String provincia;
    private double temperaturaMax;
    private LocalTime horaTemperaturaMax;
    private double temperaturaMin;
    private LocalTime horaTemperaturaMin;
    private double precipitacion;
    private LocalDate dia;
    private Long id;
}
