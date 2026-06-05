package modelo;

/**
 * Representa al personal de sala encargado de atender las mesas y tomar las comandas.
 * Al usar 'extends Usuario', hereda automáticamente la estructura básica de cualquier empleado
 * (id, nombre, password) sin necesidad de repetir esas variables aquí.
 */
public class Camarero extends Usuario {

    /**
     * Constructor para instanciar un nuevo Camarero extraído de la base de datos.
     * @param id Identificador único gestionado por MariaDB.
     * @param nombre El nombre del camarero (es el que luego se imprimirá en el Ticket).
     * @param password La clave de acceso.
     */
    public Camarero(int id, String nombre, String password) {
        // La instrucción super() llama directamente al constructor de la clase padre (Usuario).
        // Le pasamos los datos para que sea el padre quien inicialice las variables encapsuladas.
        super(id, nombre, password);
    }
}