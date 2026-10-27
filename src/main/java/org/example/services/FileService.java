package org.example.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileService {


    public boolean importarCSV (String nombreFichero){
        String rutaCompleta = "src/main/resources/" + nombreFichero;
        try {
            List<String> lineas = Files.readAllLines(Path.of(rutaCompleta));
            for (String linea : lineas){
                String[] campos = linea.split(";");
            }
            return true;

        } catch (IOException e) {
           System.err.println("Error al leer el fichero" + e.getMessage());
            return false;
        }


}

}
