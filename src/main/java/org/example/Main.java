package org.example;

import org.example.services.FileService;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
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
                String fichero = args[1];
                String mensaje = fileService.importarCSV(fichero)?"Las candidaturas han sido importadas correctamente."
                       :"Error al importar CSV";

                System.out.println(mensaje);
            }

            default -> throw new IllegalStateException("Unexpected value: " + comando);
        }
        }

    }



