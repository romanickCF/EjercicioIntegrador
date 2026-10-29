package org.example;

import org.example.models.Oferta;
import org.example.services.FileService;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

public class Main {
    static void main(String[] args) {

        if (args.length == 0){
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
                try {
                    List<Oferta> ofertas = fileService.cargar();
                    if (ofertas.isEmpty()){
                        System.out.println("No hay candidaturas. Usa -importar para cargar datos.");
                        return;
                    }

                    for (Oferta oferta: ofertas){
                        System.out.println(oferta.toResumen());
                    }
                } catch (IOException e) {
                    System.out.println("Error al leer o escribir el archivo: " + e.getMessage());
                }
                catch (IllegalArgumentException e){
                    System.out.println("Datos con formato incorreto. "+ e.getMessage());
                }

            }

            default -> {
                System.out.println("Comando no reconocido: "+ comando );
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
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals(clave)&& (i+1<args.length)){
                return args[i+1];
            }
        }
      return null;
    }
}