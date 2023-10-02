package dev.repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Esta interfaz define un conjunto de operaciones CRUD (Crear, Leer, Actualizar, Eliminar).
 *
 * @param <T>  El tipo de objeto sobre el que se aplicaran las operaciones CRUD.
 * @param <ID> El tipo de identificador unico utilizado para buscar y eliminar objetos.
 */
public interface CrudRepository<T, ID> {

    /**
     * Guarda un objeto en el repositorio.
     *
     * @param t El objeto que se desea guardar.
     * @return El objeto guardado, que puede contener informacion actualizada (como un identificador unico).
     * @throws SQLException Si ocurre un error durante la operacion de guardar.
     */
    T save(T t) throws SQLException;

    /**
     * Actualiza un objeto en el repositorio.
     *
     * @param t El objeto que se desea actualizar.
     * @return El objeto actualizado.
     * @throws SQLException Si ocurre un error durante la operacion de actualizar.
     */
    T update(T t) throws SQLException;

    /**
     * Busca un objeto por su identificador unico.
     *
     * @param id El identificador unico del objeto que se desea buscar.
     * @return Un objeto Optional que puede contener el objeto encontrado o estar vacio si no se encuentra.
     * @throws SQLException Si ocurre un error durante la operacion.
     */
    Optional<T> findById(ID id) throws SQLException;

    /**
     * Recupera todos los objetos del repositorio.
     *
     * @return Una lista de objetos que representan todos los elementos del repositorio.
     * @throws SQLException Si ocurre un error durante la operacion.
     */
    List<T> findAll() throws SQLException;

    /**
     * Elimina un objeto por su identificador unico.
     *
     * @param id El identificador unico del objeto que se desea eliminar.
     * @return `true` si el objeto se elimino con exito, `false` si no se encontro o si ocurrio un error.
     * @throws SQLException Si ocurre un error durante la eliminancion.
     */
    boolean deleteById(ID id) throws SQLException;

    /**
     * Elimina todos los objetos del repositorio.
     *
     * @throws SQLException Si ocurre un error durante la operacion.
     */
    void deleteAll() throws SQLException;
}
