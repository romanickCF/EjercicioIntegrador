# Tracker de Candidaturas Laborales

Aplicación Java para llevar el seguimiento de candidaturas de empleo o prácticas: a qué empresa se ha enviado cada CV, en qué estado está cada proceso y cuándo se postuló.

Proyecto integrador del primer trimestre de **Acceso a Datos** y **Desarrollo de Interfaces**. Evoluciona en tres entregas:

| Versión | Contenido | Estado |
|---|---|---|
| **v1.0** | Persistencia en archivos de texto + interfaz CLI por argumentos + prototipo Figma y manual de estilo | Esta entrega |
| v2.0 | JDBC + JavaFX con Scene Builder | Pendiente |
| v3.0 | Hibernate + JavaFX avanzado + asistente IA con Ollama4j | Pendiente |

## Equipo

- Romanick Raga Rodriguez([@romanickCF](https://github.com/romanickCF))
- Diego Miguel Benitez Muñoz ([@Diego-Code-Killah](https://github.com/Diego-Code-Killah))

## Tecnologías (v1.0)

- **Java 27** (versión configurada en el `pom.xml`)
- **Maven** como gestor de dependencias y construcción
- **Archivos de texto CSV** (separador `;`) como sistema de persistencia, sin base de datos
- **Git / GitHub** para el control de versiones
- **Figma** para el prototipo interactivo y el manual de estilo

Tecnologías previstas para las siguientes entregas: JDBC con SQLite/MySQL/PostgreSQL (v2.0), JavaFX y Scene Builder (v2.0), Hibernate/JPA (v3.0) y Ollama4j (v3.0).

## Estructura del proyecto

```
EjercicioIntegrador/
├── pom.xml
├── candidaturas.csv        # CSV de datos de prueba (origen para -importar)
├── datos.csv               # Archivo de datos de la aplicación (se genera solo)
└── src/main/java/org/example/
    ├── Main.java           # Interpreta los argumentos y llama al servicio
    ├── models/
    │   └── Oferta.java     # Modelo de una candidatura
    └── services/
        └── FileService.java  # Lectura, escritura, filtrado, alta, baja e importación
```

## Compilar y ejecutar (v1.0)

### Requisitos

- JDK 27 instalado
- Maven (o el Maven integrado en IntelliJ IDEA)

### Desde la terminal

Situarse en la raíz del proyecto (donde está el `pom.xml`) y compilar:

```bash
mvn compile
```

Ejecutar un comando (cada ejecución realiza **una** operación y termina):

```bash
java -cp target/classes org.example.Main -listar
```

> Los archivos `candidaturas.csv` y `datos.csv` se buscan en la **carpeta desde la que se ejecuta el comando**, por lo que hay que lanzarlo desde la raíz del proyecto.

### Desde IntelliJ IDEA

1. *Run → Edit Configurations...* y seleccionar la clase `Main`.
2. Escribir el comando en **Program arguments**, por ejemplo `-listar --estado pendiente`.
3. Comprobar que **Working directory** apunta a la raíz del proyecto.
4. Pulsar *Run*.

## Comandos disponibles

| Comando | Descripción |
|---|---|
| `-importar <fichero.csv>` | Importa candidaturas desde un CSV externo a `datos.csv` |
| `-listar` | Lista todas las candidaturas |
| `-listar --estado X --empresa Y` | Lista filtrando por estado y/o empresa (ambos filtros son opcionales y no distinguen mayúsculas) |
| `-añadir --titulo X --empresa Y --estado Z --fecha dd/MM/yyyy` | Añade una candidatura nueva |
| `-ver <id>` | Muestra todos los detalles de una candidatura |
| `-borrar <id>` | Elimina una candidatura |

### Ejemplos

```bash
# Primer uso: cargar los datos de prueba
java -cp target/classes org.example.Main -importar candidaturas.csv

# Listados
java -cp target/classes org.example.Main -listar
java -cp target/classes org.example.Main -listar --estado pendiente
java -cp target/classes org.example.Main -listar --estado entrevista --empresa Indra

# Alta (los valores con espacios van entre comillas dobles)
java -cp target/classes org.example.Main -añadir --titulo "Desarrollo de Software" --empresa Indra --estado pendiente --fecha 30/10/2026

# Consulta y borrado
java -cp target/classes org.example.Main -ver 13
java -cp target/classes org.example.Main -borrar 13
```

### Estados válidos

`pendiente`, `entrevista`, `rechazado`, `aceptado`

### Mensajes de error

Todos los errores se muestran por consola con un mensaje claro, sin volcar trazas de excepciones:

- Comando desconocido o ausencia de argumentos: se muestra la ayuda.
- Argumentos obligatorios que faltan (`-ver` sin ID, `-añadir` incompleto...).
- Archivo no encontrado al importar.
- ID que no es un número o que no existe.
- Estado o fecha con formato no válido.
- Línea de CSV mal formada: se indica el número de línea.

## Funcionamiento de los datos

La aplicación usa **dos archivos distintos**:

- `candidaturas.csv`: archivo de **origen** con los datos de prueba. Solo se lee al ejecutar `-importar`.
- `datos.csv`: archivo de **trabajo** de la aplicación. Lo usan `-listar`, `-ver`, `-añadir` y `-borrar`. Se crea automáticamente la primera vez que se importa o se añade una candidatura.

Formato de ambos (una candidatura por línea, campos separados por `;`):

```
id;titulo;empresa;estado;fechaPostulacion;enlace;notas
1;Desarrollador Java Junior;Indra;pendiente;26/10/2026;https://empleo.example.com/indra/1001;Enviado por el portal de empleo
```

### Decisiones de diseño

- **Patrón cargar → modificar → guardar:** cada operación lee `datos.csv` completo en memoria, modifica la lista y, si hay cambios, reescribe el archivo.
- **Importar dos veces no duplica datos:** las candidaturas cuyo ID ya existe en `datos.csv` se omiten y el programa informa de cuántas se han añadido realmente.
- **Importación atómica:** el archivo de origen se lee y valida entero antes de escribir, de modo que un CSV con una línea errónea no deja `datos.csv` a medias.
- **ID nuevo:** al añadir, el ID es el mayor existente más uno, de modo que borrar candidaturas nunca provoca IDs repetidos.
- **Servicio sin impresión:** `FileService` no imprime nada; las excepciones suben hasta `Main`, que decide el mensaje al usuario.

### Limitaciones conocidas

- Los campos no pueden contener `;`, porque es el separador del archivo.
- Los valores con espacios deben escribirse entre comillas dobles; sin ellas, la terminal los trata como argumentos distintos.

## Datos de prueba

El archivo [`candidaturas.csv`](candidaturas.csv) contiene 15 candidaturas de 6 empresas (Indra, Accenture, Capgemini, NTT Data, Telefónica y BBVA), repartidas entre los cuatro estados. Fue generado con una herramienta de IA a partir del prompt de la plantilla del enunciado y consensuado por el equipo.

## Diseño (Figma y manual de estilo)

- **Prototipo interactivo en Figma (solo lectura):** [COMPLETAR: enlace]
- **Manual de estilo (PDF):** [COMPLETAR: enlace o ruta, p. ej. `docs/manual-de-estilo.pdf`]
- **Pantallas del prototipo (PDF):** [COMPLETAR: enlace o ruta, p. ej. `docs/pantallas-prototipo.pdf`]
- **Assets de diseño (logo, iconos):** [COMPLETAR: carpeta, p. ej. `docs/assets/`]

## Vídeo demostrativo

https://drive.google.com/file/d/1DjLe-TaB6591QVQEdA1E8l-AOrQQqXSL/view?usp=sharing

## Contribuciones por miembro

| Miembro | Entrega | Tareas realizadas |
|---|---|---|
| @romanickCF | v1.0 | Aplicación CLI en Java: modelo `Oferta`, `FileService`, comandos `-importar`, `-listar` (con filtros), `-añadir`, `-ver` y `-borrar`, tratamiento de errores, README |
| @Diego-Code-Killah | v1.0 | Prototipo interactivo en Figma, manual de estilo (paleta, tipografía, logo, guía de estilo), Aplicación CLI en java: modelo `Oferta`, `FileService`, metodos de servicio `parsearLinea`, `leerOfertas`, `cargar`.|

## Uso de herramientas de IA generativa

- **Datos de prueba:** el CSV `candidaturas.csv` se generó con una herramienta de IA a partir del prompt de la plantilla del enunciado.
- **Desarrollo del código Java (@romanickCF):** se ha utilizado un asistente de IA conversacional (Claude, de Anthropic) en un chat, como apoyo para entender los conceptos (argumentos de `main`, lectura de CSV, flujo cargar → modificar → guardar) y para revisar el código. No se han utilizado agentes de código ni herramientas que modifiquen el repositorio de forma automática.
- **Diseño en Figma y manual de estilo (@Diego-Code-Killah):**Se ha utilizado asistende de IA Claude para generar el manual de estilo a raiz del prototipo Figma.
