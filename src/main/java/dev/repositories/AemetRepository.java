package dev.repositories;

import dev.models.Aemet;
/**
 * Esta interfaz define un repositorio para acceder y gestionar los datos de AEMET.
 * Proporciona métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en objetos Aemet.
 *
 */
public interface AemetRepository extends CrudRepository<Aemet, Long> {
}
