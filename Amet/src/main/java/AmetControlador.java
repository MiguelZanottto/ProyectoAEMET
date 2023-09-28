import com.opencsv.CSVReader;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;

//import java.util.List;
public class AmetControlador {
    String path;

    public ArrayList<Amet> read() {
        ArrayList<Amet> mediciones = new ArrayList<Amet>();
        String[] archivos = {"Aemet20171029.csv", "Aemet20171030.csv", "Aemet20171031.csv"};
        for (String archivo : archivos) {
            path = Paths.get("").toAbsolutePath().toString() + File.separator + "src" + File.separator + "csv" + File.separator + archivo;

            try (CSVReader reader = new CSVReader(new FileReader(path))) {
                String[] line;
                reader.readNext();
                while ((line = reader.readNext()) != null) {
                    Amet amet = new Amet();
                    String[] datos = line[0].split(";");
                    if (datos.length >= 7) {
                        amet.setLocalidad(datos[0]);
                        amet.setProvincia(datos[1]);
                        amet.setTemperaturaMaxima(Double.parseDouble(datos[2]));
                        amet.setHoraTemperaturaMaxima(LocalTime.parse(datos[3], DateTimeFormatter.ofPattern("HH:mm")));
                        amet.setTemperaturaMinima(Double.parseDouble(datos[4]));
                        amet.setHoraTemperaturaMinima(LocalTime.parse(datos[5], DateTimeFormatter.ofPattern("HH:mm")));
                        amet.setPrecipitacion(Double.parseDouble(datos[6]));
                        mediciones.add(amet);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!mediciones.isEmpty()) {
            for (Amet amet : mediciones) {
                System.out.println("Localidad: " + amet.getLocalidad());
                System.out.println("Provincia: " + amet.getProvincia());
                System.out.println("Temperatura Máxima: " + amet.getTemperaturaMaxima());
                System.out.println("Hora Temperatura Máxima: " + amet.getHoraTemperaturaMaxima());
                System.out.println("Temperatura Mínima: " + amet.getTemperaturaMinima());
                System.out.println("Hora Temperatura Mínima: " + amet.getHoraTemperaturaMinima());
                System.out.println("Precipitación: " + amet.getPrecipitacion());
                System.out.println("-------------------------------------------");
            }
        } else {
            // System.out.println("No se han encontrado mediciones.");
        }

        return mediciones;
    }

    public void create(){

    }
    public void delete(){

    }

    public void update(){

    }
    public static void main(String[] args) throws FileNotFoundException {
        AmetControlador Amet= new AmetControlador();
        Amet.read();
    }
}


