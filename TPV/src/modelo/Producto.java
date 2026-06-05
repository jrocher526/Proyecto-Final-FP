package modelo;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Representa un artículo del menú del restaurante (ej: un refresco, una hamburguesa).
 * Esta clase viaja entre MariaDB (el catálogo) y ObjectDB (cuando se añade a un Ticket).
 */
@Entity // Le indica a JPA que esta clase es una entidad persistente en la base de datos de objetos
public class Producto implements Serializable {

    // Al implementar Serializable, esto evite que la aplicación pete si guardamos un producto hoy y mañana le añadimos un nuevo atributo a la clase.
    private static final long serialVersionUID = 1L;

    // atributos

    @Id
    private int id;

    private String nombre;
    private Categoria categoria;
    private float precio;

    /**
     * Constructor vacío.
     * JPA obliga a tener un constructor sin parámetros. Cuando ObjectDB lee
     * un producto de la base de datos, primero crea el objeto vacío con este constructor y luego
     * le va metiendo los datos con los setters "por detrás".
     */
    public Producto() {}

    /**
     * Constructor principal para crear productos a mano en nuestro código.
     */
    public Producto(int id, String nombre, Categoria categoria, float precio) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
    }

    // GETTERS Y SETTERS

    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public Categoria getCategoria() {
        return categoria;
    }
    public float getPrecio() {
        return precio;
    }
    public void setPrecio(float precio) {
        this.precio = precio;
    }

    //MÉTODOS DE COMPARACIÓN

    /**
     * Sobrescribimos el equals para enseñar a Java cuándo dos productos son "el mismo".
     * Si no hacemos esto, Java compararía si ocupan el mismo espacio en la memoria RAM,
     * y nosotros queremos saber si tienen el mismo ID y el mismo Nombre.
     */
    @Override
    public boolean equals(Object o) {
        // 1. Verificamos si es exactamente el mismo objeto en memoria
        if (this == o) return true;

        // 2. Si el objeto es nulo o ni siquiera es de la clase Producto, devolvemos falso
        if (o == null || getClass() != o.getClass()) return false;

        // 3. Moldeamos (Casteamos) el objeto genérico a Producto para poder leer sus atributos
        Producto producto = (Producto) o;

        // 4. Son iguales SI coinciden en su número de ID y en su nombre
        return id == producto.id && Objects.equals(nombre, producto.nombre);
    }

    /**
     * Sobrescribimos hashCode para generar una "huella digital" numérica del producto.
     * Es vital para que las colecciones avanzadas (como HashMaps o HashSets) funcionen rápido
     * y no guarden productos duplicados.
     */
    @Override
    public int hashCode() {
        // Genera el código basándose en el ID y el nombre (los mismos campos que usamos en el equals)
        return Objects.hash(id, nombre);
    }
}