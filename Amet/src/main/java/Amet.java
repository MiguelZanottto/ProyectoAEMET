import java.time.LocalTime;

public class Amet {
    private String Localidad;
    private String Provincia;
    private double TemperaturaMaxima;
    private double TemperaturaMinima;
    private LocalTime HoraTemperaturaMinima;
    private LocalTime HoraTemperaturaMaxima;
    private double Precipitacion;

    public Amet() {
    }

    // Getter y setters
    public String getLocalidad() {
        return Localidad;
    }
    public void setLocalidad(String localidad) {
        this.Localidad = localidad;
    }
    public String getProvincia() {
        return Provincia;
    }
    public void setProvincia(String provincia) {
        this.Provincia = provincia;
    }
    public double getTemperaturaMaxima() {
        return TemperaturaMaxima;
    }
    public void setTemperaturaMaxima(double temperaturaMaxima) {
        this.TemperaturaMaxima = temperaturaMaxima;
    }
    public double getTemperaturaMinima() {
        return TemperaturaMinima;
    }
    public void setTemperaturaMinima(double temperaturaMinima) {
        this.TemperaturaMinima = temperaturaMinima;
    }
    public LocalTime getHoraTemperaturaMinima() {
        return HoraTemperaturaMinima;
    }
    public void setHoraTemperaturaMinima(LocalTime horaTemperaturaMinima) {
        this.HoraTemperaturaMinima = horaTemperaturaMinima;
    }
    public LocalTime getHoraTemperaturaMaxima() {
        return HoraTemperaturaMinima;
    }
    public void setHoraTemperaturaMaxima(LocalTime horaTemperaturaMinima) {
        this.HoraTemperaturaMinima = horaTemperaturaMinima;
    }
    public double getPrecipitacion() {
        return Precipitacion;
    }
    public void setPrecipitacion(double precipitacion) {
        this.Precipitacion = precipitacion;
    }
}
