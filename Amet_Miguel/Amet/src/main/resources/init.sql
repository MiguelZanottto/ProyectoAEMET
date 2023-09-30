DROP TABLE IF EXISTS Aemet;
CREATE TABLE IF NOT EXISTS Aemet (
                                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                                       localidad TEXT,
                                       provincia TEXT,
                                       temperaturaMax REAL,
                                       horaTemperaturaMax TEXT,
                                       temperaturaMin REAL,
                                       horaTemperaturaMin TEXT,
                                       precipitacion REAL,
                                       dia TEXT
);
