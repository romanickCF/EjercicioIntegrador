package org.example;

import org.example.services.FileService;

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
                if(args.length<2){
                    System.out.println("Falta el fichero. Uso: -importar <fichero.csv>");
                    return;
                }
                String mensaje = fileService.importarCSV(args[1])
                        ?"Las candidaturas han sido importadas correctamente."
                        :"Error al importar CSV";

                System.out.println(mensaje);
            }
            case "-listar" -> {

            }

            default -> {
                System.out.println("Comando no reconocido: "+ comando );
                mostrarAyuda();
            }

        }
    }


    private static void mostrarAyuda() {
        System.out.println("Comandos disponibles:");
        System.out.println("  -listar [--estado X] [--empresa Y]");
        System.out.println("  -añadir --titulo X --empresa Y --estado Z --fecha dd/MM/yyyy");
        System.out.println("  -ver <id>");
        System.out.println("  -borrar <id>");
        System.out.println("  -importar <fichero.csv>");
    }
}