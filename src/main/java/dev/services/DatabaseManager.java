package dev.services;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Esta Clase gestiona la conexion y configuracion de la base de datos, incluida la inicializacion de tablas si es necesario.
 * Tambien proporciona metodos para ejecutar scripts SQL en la base de datos.
 */
public class DatabaseManager implements AutoCloseable {
    private static DatabaseManager instance;
    private boolean databaseInitTables;
    private String databaseUrl;
    private String databaseInitScript;
    private Connection conn;

    /**
     * Constructor privado que carga la configuracion de la base de datos y establece la conexion.
     */
    private DatabaseManager() {
        loadProperties();
        try {
            openConnection();
            if (databaseInitTables) {
                initTables();
            }
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    /**
     * Obtiene una instancia unica de la clase DatabaseManager.
     *
     * @return instancia unica de DatabaseManager.
     */

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Carga la configuracion de la base de datos desde el archivo "config.properties".
     */
    private void loadProperties() {
        try {
            var file = ClassLoader.getSystemResource("config.properties").getFile();
            var props = new Properties();
            props.load(new FileReader(file));
            databaseUrl = props.getProperty("database.connectionUrl", "jdbc:sqlite:Aemet.db");
            databaseInitTables = Boolean.parseBoolean(props.getProperty("database.initTables", "false"));
            databaseInitScript = props.getProperty("database.initScript", "init.sql");
        } catch (IOException e) {
            e.getMessage();
        }
    }

    /**
     * Abre una conexion con la base de datos.
     *
     * @throws SQLException Si ocurre un error al abrir la conexion.
     */
    private void openConnection() throws SQLException {
        conn = DriverManager.getConnection(databaseUrl);
    }

    /**
     * Cierra la conexion con la base de datos.
     *
     * @throws SQLException Si ocurre un error al cerrar la conexion.
     */

    private void closeConnection() throws SQLException {
        conn.close();
    }

    /**
     * Inicializa las tablas de la base de datos ejecutando el script de inicializacion.
     */
    private void initTables() {
        try {
            executeScript(databaseInitScript, true);
        } catch (FileNotFoundException e) {
            e.getMessage();
        }
    }


    /**
     * Ejecuta un script SQL en la base de datos.
     *
     * @param scriptSqlFile El nombre del archivo de script SQL a ejecutar.
     * @param logWriter     Indica si se debe registrar la salida del script.
     * @throws FileNotFoundException Si el archivo de script SQL no se encuentra.
     */
    public void executeScript(String scriptSqlFile, boolean logWriter) throws FileNotFoundException {
        ScriptRunner sr = new ScriptRunner(conn);
        var file = ClassLoader.getSystemResource(scriptSqlFile).getFile();
        Reader reader = new BufferedReader(new FileReader(file));
        sr.runScript(reader);
    }

    /**
     * Obtiene una conexion a la base de datos.
     *
     * @return La conexion a la base de datos.
     * @throws SQLException Si ocurre un error al obtener la conexion.
     */
    public Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                openConnection();
            } catch (SQLException e) {
                e.getMessage();
                throw e;
            }
        }
        return conn;
    }

    /**
     * Implementacion del metodo close de la interfaz AutoCloseable para cerrar la conexion.
     *
     * @throws Exception Si ocurre un error al cerrar la conexion.
     */
    @Override
    public void close() throws Exception {
        closeConnection();
    }
}