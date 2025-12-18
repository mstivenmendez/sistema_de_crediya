 📚 Colecciones en Java y uso de `filter`

Este documento explica de forma clara y sencilla el **Framework de Colecciones en Java** y el uso del método **`filter`** con **Streams**, incluyendo ejemplos prácticos.

---

## 🧩 ¿Qué son las colecciones en Java?

Las **colecciones** son estructuras que permiten **almacenar y manipular grupos de datos** de manera dinámica, eficiente y segura.

Java provee el **Framework de Colecciones**, que incluye:
- Interfaces
- Clases
- Métodos listos para usar

---

## ✅ Ventajas de usar colecciones

- Tamaño dinámico
- Métodos para agregar, eliminar y buscar
- Código más limpio y reutilizable
- Mejor rendimiento que arreglos manuales

---

## 🏗️ Jerarquía del Framework de Colecciones

Iterable
└── Collection
├── List
├── Set
└── Queue

Map (no hereda de Collection)

csharp
Copiar código

---

## 📌 Interface `List`

Una **List**:
- ✔️ Permite elementos duplicados
- ✔️ Mantiene el orden de inserción
- ✔️ Acceso por índice

### Implementaciones comunes
- `ArrayList`
- `LinkedList`

### 🧪 Ejemplo con `ArrayList`

```java
import java.util.ArrayList;
import java.util.List;

public class EjemploList {
    public static void main(String[] args) {

        List<String> nombres = new ArrayList<>();

        nombres.add("Juan");
        nombres.add("Ana");
        nombres.add("Juan"); // duplicado permitido

        System.out.println(nombres);
        System.out.println(nombres.get(0));
    }
}
📌 Interface Set
Un Set:

❌ NO permite duplicados

❌ No garantiza orden (depende de la implementación)

Implementaciones comunes
HashSet

LinkedHashSet

TreeSet

🧪 Ejemplo con HashSet
java
Copiar código
import java.util.HashSet;
import java.util.Set;

public class EjemploSet {
    public static void main(String[] args) {

        Set<String> colores = new HashSet<>();

        colores.add("Rojo");
        colores.add("Azul");
        colores.add("Rojo"); // duplicado, no se guarda

        System.out.println(colores);
    }
}
📌 Interface Queue
Una Queue:

Sigue el principio FIFO (First In, First Out)

Se usa para colas y procesos

Implementaciones comunes
LinkedList

PriorityQueue

🧪 Ejemplo con Queue
java
Copiar código
import java.util.LinkedList;
import java.util.Queue;

public class EjemploQueue {
    public static void main(String[] args) {

        Queue<String> cola = new LinkedList<>();

        cola.add("Cliente 1");
        cola.add("Cliente 2");
        cola.add("Cliente 3");

        System.out.println(cola.poll()); // elimina el primero
        System.out.println(cola);
    }
}
🗺️ Interface Map
Un Map:

Usa pares clave - valor

❌ No permite claves duplicadas

✔️ Permite valores duplicados

Implementaciones comunes
HashMap

LinkedHashMap

TreeMap

🧪 Ejemplo con HashMap
java
Copiar código
import java.util.HashMap;
import java.util.Map;

public class EjemploMap {
    public static void main(String[] args) {

        Map<Integer, String> personas = new HashMap<>();

        personas.put(1, "Juan");
        personas.put(2, "Ana");
        personas.put(3, "Carlos");

        System.out.println(personas.get(2)); // Ana
    }
}
🔁 Recorrer colecciones
For-each
java
Copiar código
for (String nombre : nombres) {
    System.out.println(nombre);
}
forEach (Java 8+)
java
Copiar código
nombres.forEach(nombre -> System.out.println(nombre));
🔍 Streams y método filter
🧠 ¿Qué es un Stream?
Un Stream permite procesar colecciones de forma funcional, sin modificar la colección original.

Para obtener un stream:

java
Copiar código
coleccion.stream()
📌 ¿Qué es filter?
El método filter se usa para filtrar elementos según una condición.

Si la condición devuelve true → el elemento se queda

Si devuelve false → se descarta

🧪 Ejemplo con números
java
Copiar código
import java.util.Arrays;
import java.util.List;

public class EjemploFilterNumeros {
    public static void main(String[] args) {

        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6);

        numeros.stream()
               .filter(n -> n % 2 == 0)
               .forEach(System.out::println);
    }
}
Salida:
Copiar código
2
4
6
🧪 Ejemplo con Strings
java
Copiar código
import java.util.List;

public class EjemploFilterString {
    public static void main(String[] args) {

        List<String> nombres = List.of("Juan", "Ana", "Pedro", "Luis");

        nombres.stream()
               .filter(n -> n.startsWith("A"))
               .forEach(System.out::println);
    }
}
🧪 Ejemplo con objetos
java
Copiar código
class Persona {
    String nombre;
    int edad;

    Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }
}
java
Copiar código
import java.util.List;

public class EjemploFilterObjeto {
    public static void main(String[] args) {

        List<Persona> personas = List.of(
                new Persona("Juan", 20),
                new Persona("Ana", 17),
                new Persona("Carlos", 25)
        );

        personas.stream()
                .filter(p -> p.edad >= 18)
                .forEach(p -> System.out.println(p.nombre));
    }
}
🔗 filter con collect
java
Copiar código
import java.util.List;
import java.util.stream.Collectors;

List<Integer> pares = numeros.stream()
        .filter(n -> n % 2 == 0)
        .collect(Collectors.toList());
⚠️ Características importantes de filter
No modifica la colección original

Usa expresiones lambda

Es una operación intermedia

Se ejecuta solo con una operación terminal

📊 Comparación rápida
Tipo	Duplicados	Orden	Acceso
List	Sí	Sí	Índice
Set	No	No	No
Queue	Sí	FIFO	Orden
Map	Claves no	No	Clave

✅ Conclusión
El Framework de Colecciones y el uso de filter permiten escribir código:

Más limpio

Más legible

Más mantenible

Dominar estos conceptos es fundamental en Java.