package dev.repositories;

import dev.controllers.AemetController;
import dev.controllers.ExportJSON;
import dev.models.Aemet;
import dev.services.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Esta clase implementa la interfaz AemetRepository.
 * Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en Amet.
 */
public class AemetRepositoryImpl implements AemetRepository {

    private static AemetRepositoryImpl instance;
    private final Logger logger = LoggerFactory.getLogger(AemetRepositoryImpl.class);

    private final DatabaseManager db;

    private AemetRepositoryImpl(DatabaseManager db) {
        this.db = db;
    }


    public static AemetRepositoryImpl getInstance(DatabaseManager db) {
        if (instance == null) {
            instance = new AemetRepositoryImpl(db);
        }
        return instance;
    }

    /**
     * Obtiene todas las mediciones meteorologicas almacenadas en la base de datos.
     *
     * @return Una lista de objetos Aemet.
     * @throws SQLException Si se produce un error al acceder a la base de datos.
     */
    @Override
    public List<Aemet> findAll() throws SQLException {
        logger.debug("Obteniendo todas las mediciones");
        var query = "SELECT * FROM Aemet";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            var rs = stmt.executeQuery();
            var lista = new ArrayList<Aemet>();
            while (rs.next()) {
                Aemet aemet = Aemet.builder()
                        .id(rs.getLong("id"))
                        .localidad(rs.getString("localidad"))
                        .provincia(rs.getString("provincia"))
                        .temperaturaMax(rs.getDouble("temperaturaMax"))
                        .horaTemperaturaMax(rs.getObject("horaTemperaturaMax", LocalTime.class))
                        .temperaturaMin(rs.getDouble("temperaturaMin"))
                        .horaTemperaturaMin(rs.getObject("horaTemperaturaMin", LocalTime.class))
                        .precipitacion(rs.getDouble("precipitacion"))
                        .dia(rs.getObject("dia", LocalDate.class))
                        .build();
                lista.add(aemet);
            }
            return lista;
        }
    }

    /**
     * Busca una medicion meteorologica por su identificador unico.
     *
     * @param id El identificador unico de la medicion que se desea buscar.
     * @return Un objeto Optional que puede contener la medicion encontrada o estar vacío si no se encuentra.
     * @throws SQLException Si se produce un error al acceder a la base de datos.
     */
    @Override
    public Optional<Aemet> findById(Long id) throws SQLException {
        logger.debug("Obteniendo la medicion con id: " + id);
        String query = "SELECT * FROM Aemet WHERE id = ?";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            stmt.setLong(1, id);
            var rs = stmt.executeQuery();
            Optional<Aemet> aemet = Optional.empty();
            while (rs.next()) {
                aemet = Optional.of(Aemet.builder()
                        .id(rs.getLong("id"))
                        .localidad(rs.getString("localidad"))
                        .provincia(rs.getString("provincia"))
                        .temperaturaMax(rs.getDouble("temperaturaMax"))
                        .horaTemperaturaMax(rs.getObject("horaTemperaturaMax", LocalTime.class))
                        .temperaturaMin(rs.getDouble("temperaturaMin"))
                        .horaTemperaturaMin(rs.getObject("horaTemperaturaMin", LocalTime.class))
                        .precipitacion(rs.getDouble("precipitacion"))
                        .dia(rs.getObject("dia", LocalDate.class))
                        .build()
                );
            }
            return aemet;
        }
    }

    /**
     * Guarda una nueva medicion meteorologica en la base de datos.
     *
     * @param aemet La medicion meteorologica que se desea guardar.
     * @return La medicion meteorologica guardada, que puede contener el identificador unico asignados.
     * @throws SQLException Si se produce un error al acceder a la base de datos.
     */
    @Override
    public Aemet save(Aemet aemet) throws SQLException  {
        logger.debug("Guardando la medicion: " + aemet);
        String query = "INSERT INTO Aemet (localidad, provincia, temperaturaMax, horaTemperaturaMax, temperaturaMin, horaTemperaturaMin, precipitacion, dia) VALUES (?, ?, ?, ?, ?, ?, ? ,?)";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, aemet.getLocalidad());
            stmt.setString(2, aemet.getProvincia());
            stmt.setDouble(3, aemet.getTemperaturaMax());
            stmt.setString(4, aemet.getHoraTemperaturaMax().toString());
            stmt.setDouble(5, aemet.getTemperaturaMin());
            stmt.setString(6, aemet.getHoraTemperaturaMin().toString());
            stmt.setDouble(7, aemet.getPrecipitacion());
            stmt.setString(8, aemet.getDia().toString());
            var res = stmt.executeUpdate();
            // connection.commit();
            if (res > 0) {
                var stmt2 = connection.prepareStatement("SELECT LAST_INSERT_ROWID()");
                ResultSet rs = stmt2.executeQuery();
                while (rs.next()) {
                    aemet.setId(rs.getLong(1));
                }
                rs.close();
            } else {
                logger.error("Medicion no guardada");
            }
        }
        return aemet;
    }

    /**
     * Actualiza una medicion meteorologica existente en la base de datos.
     *
     * @param aemet La medicion meteorologica actualizada que se desea guardar.
     * @return La medicion meteorologica actualizada.
     * @throws SQLException Si se produce un error al acceder a la base de datos.
     */
    @Override
    public Aemet update(Aemet aemet) throws SQLException  {
        logger.debug("Actualizando la medicion: " + aemet);
        String query = "UPDATE Aemet SET localidad =?, provincia =?, temperaturaMax =?, horaTemperaturaMax =?, temperaturaMin =?, horaTemperaturaMin =?, precipitacion =?, dia =? WHERE id =?";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            stmt.setString(1, aemet.getLocalidad());
            stmt.setString(2, aemet.getProvincia());
            stmt.setDouble(3, aemet.getTemperaturaMax());
            stmt.setString(4, aemet.getHoraTemperaturaMax().toString());
            stmt.setDouble(5, aemet.getTemperaturaMin());
            stmt.setString(6, aemet.getHoraTemperaturaMin().toString());
            stmt.setDouble(7, aemet.getPrecipitacion());
            stmt.setString(8, aemet.getDia().toString());
            stmt.setLong(9, aemet.getId());
            var res = stmt.executeUpdate();
            if (res > 0) {
                logger.debug("Medicion actualizada");
            } else {
                logger.error("La medicion no pudo ser actualizada al no encontrarse en la base de datos con id: " + aemet.getId());
            }
        }
        return aemet;
    }

    /**
     * Elimina una medicion meteorologica de la base de datos por su identificador unico.
     *
     * @param id El identificador unico de la medicion que se desea eliminar.
     * @return `true` si la medicion se elimino con exito, `false` si no se encontro o si ocurrio un error.
     * @throws SQLException Si se produce un error al acceder a la base de datos.
     */
    @Override
    public boolean deleteById(Long id) throws SQLException {
        logger.debug("Borrando la medicion con id: " + id);
        String query = "DELETE FROM Aemet WHERE id =?";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            stmt.setLong(1, id);
            var res = stmt.executeUpdate();
            return res > 0;
        }
    }

    /**
     * Elimina todas las mediciones meteorologicas de la base de datos.
     *
     * @throws SQLException Si se produce un error al acceder a la base de datos.
     */
    @Override
    public void deleteAll() throws SQLException {
        logger.debug("Borrando todos las mediciones");
        String query = "DELETE FROM Aemet";
        try (var connection = db.getConnection();
             var stmt = connection.prepareStatement(query)
        ) {
            stmt.executeUpdate();
        }
    }

    /**
     * Metodo principal utilizado para realizar pruebas y exportar datos a JSON.
     *
     * @throws SQLException Si se produce un error al acceder a la base de datos.
     * @throws IOException  Si se produce un error al exportar datos a JSON.
     */
    public static void main(String[] args) throws SQLException, IOException {
        AemetController ac = AemetController.getInstance();

        ExportJSON export = new ExportJSON();
        export.exportar(ac.getLista());
    }
}

