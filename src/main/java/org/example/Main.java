package org.example;

import org.example.models.Oferta;
import org.example.services.FileService;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class Main {

    static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("No hay argumentos");
            return;
        }
        FileService fileService = new FileService();
        String comando = args[0];

        switch (comando) {
            case "-importar" -> {
                if (args.length < 2) {
                    System.out.println("Falta el fichero. Uso: -importar <fichero.csv>");
                    return;
                }
                try {
                    int nuevas = fileService.importarCSV(args[1]);
                    System.out.println("Importación correcta: " + nuevas + " candidaturas añadidas.");
                } catch (NoSuchFileException e) {
                    System.out.println("Archivo no encontrado: " + args[1]);
                } catch (IOException e) {
                    System.out.println("Error al leer o escribir el archivo: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("CSV con formato incorrecto. " + e.getMessage());
                }
            }
            case "-listar" -> {
                String estado = obtenerValor(args, "--estado");
                String empresa = obtenerValor(args, "--empresa");

                try {
                    List<Oferta> ofertas = fileService.cargar();
                    if (ofertas.isEmpty()) {
                        System.out.println("No hay candidaturas. Usa -importar para cargar datos.");
                        return;
                    }

                    List<Oferta> filtradas = fileService.filtrar(ofertas, estado, empresa);

                    if (filtradas.isEmpty()) {
                        System.out.println("No hay candidaturas que coincidan con los filtros.");
                        return;
                    }

                    for (Oferta oferta : filtradas) {
                        System.out.println(oferta.toResumen());
                    }
                } catch (IOException e) {
                    System.out.println("Error al leer o escribir el archivo: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("Datos con formato incorreto. " + e.getMessage());
                }

            }

            case "-añadir" -> {

                String titulo = obtenerValor(args, "--titulo");
                String empresa = obtenerValor(args, "--empresa");
                String estado = obtenerValor(args, "--estado");
                String fechaTexto = obtenerValor(args, "--fecha");

                boolean faltaValor = titulo == null || empresa == null || estado == null || fechaTexto == null;


                boolean valido = "pendiente".equalsIgnoreCase(estado)
                        || "entrevista".equalsIgnoreCase(estado)
                        || "rechazado".equalsIgnoreCase(estado)
                        || "aceptado".equalsIgnoreCase(estado);


                if (faltaValor) {
                    System.out.println("Uso: -añadir --titulo X --empresa Y --estado Z --fecha dd/MM/yyyy");
                    return;
                }

                if (!valido) {
                    System.out.println("Estado no válido. Usa: pendiente, entrevista, rechazado o aceptado.");
                    return;
                }

                try {
                    LocalDate fecha = LocalDate.parse(fechaTexto, Oferta.FORMATO_FECHA);
                    Oferta nueva = fileService.anadir(titulo, empresa, estado.toLowerCase(), fecha);
                    System.out.println("Candidatura añadida con ID " + nueva.getId());

                } catch (DateTimeParseException e) {
                    System.out.println("Fecha no válida. Usa el formato dd/MM/yyyy.");
                } catch (IOException e) {
                    System.out.println("Error al leer o escribir el archivo: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("Datos con formato incorrecto. " + e.getMessage());
                }
            }

            case "-ver" -> {
                if (args.length < 2) {
                    System.out.println("Falta ID . Uso: -ver <id>");
                    return;
                }
                try {
                    int id = Integer.parseInt(args[1]);
                    Optional<Oferta> oferta = fileService.buscarPorId(id);

                    if (oferta.isPresent()) {
                        System.out.println(oferta.get().toDetalle());
                    } else {
                        System.out.println("No existe ninguna candidatura con el ID " + id);
                    }

                } catch (NumberFormatException e) {
                    System.out.println("El ID debe ser un número. Recibido: " + args[1]);
                } catch (IOException e) {
                    System.out.println("Error al leer el archivo: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("Datos con formato incorrecto. " + e.getMessage());
                }
            }

            case "-borrar" -> {
                if (args.length < 2) {
                    System.out.println("Falta ID. USO -borrar 13");
                    return;
                }


                try {
                    int id = Integer.parseInt(args[1]);

                    if (fileService.borrar(id)) {

                        System.out.println("Oferta " + id + " eliminada.");
                    } else {
                        System.out.println("No existe nunguna candidatura con Id " + id);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("El ID debe ser un número. Recibido: " + args[1]);
                } catch (IOException e) {
                    System.out.println("Error al leer o escribir el archivo: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("Datos con formato incorrecto. " + e.getMessage());
                }

            }

            default -> {
                System.out.println("Comando no reconocido: " + comando);
                mostrarAyuda();
            }

        }
    }

    //Metodos Auxiliares

    private static void mostrarAyuda() {
        System.out.println("Comandos disponibles:");
        System.out.println("  -listar [--estado X] [--empresa Y]");
        System.out.println("  -añadir --titulo X --empresa Y --estado Z --fecha dd/MM/yyyy");
        System.out.println("  -ver <id>");
        System.out.println("  -borrar <id>");
        System.out.println("  -importar <fichero.csv>");
    }

    private static String obtenerValor(String[] args, String clave) {
        for (int i = 1; i < args.length - 1; i++) {
            if (args[i].equals(clave) && !args[i + 1].startsWith("--")) {
                return args[i + 1];
            }
        }
        return null;
    }

}