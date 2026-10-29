package org.example.services;

import org.example.models.Oferta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


public class FileService {

    private static final Path ARCHIVO_DATOS = Path.of("datos.csv");
    private static final String CABECERA = "id;titulo;empresa;estado;fechaPostulacion;enlace;notas";
    private static final int NUM_CAMPOS = 7;

    private Oferta parsearLinea(String linea, int numeroLinea) {
        String[] campos = linea.split(";", -1);
        if (campos.length != NUM_CAMPOS) {
            throw new IllegalArgumentException(
                    "Línea " + numeroLinea + ": se esperaban " + NUM_CAMPOS
                            + " campos y hay " + campos.length);
        }
        try {
            return new Oferta(
                    Integer.parseInt(campos[0].trim()),
                    campos[1], campos[2], campos[3],
                    LocalDate.parse(campos[4].trim(), Oferta.FORMATO_FECHA),
                    campos[5], campos[6]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Línea " + numeroLinea + ": el id no es un número");
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Línea " + numeroLinea + ": fecha inválida (usa dd/MM/yyyy)");
        }
    }


    public boolean importarCSV (String nombreFichero){
        String rutaCompleta = "C:\\Users\\romanickraga\\IdeaProjects\\EjercicioIntegrador\\src\\main\\resources\\" + nombreFichero;
        try {
            List<String> lineas = Files.readAllLines(Path.of(rutaCompleta));
            for (String linea : lineas){
                String[] campos = linea.split(";");
            }
            return true;

        } catch (IOException e) {
            System.err.println("Error al leer el fichero " + e.getMessage());
            return false;
        }


    }

}
