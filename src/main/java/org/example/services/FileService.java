package org.example.services;

import org.example.models.Oferta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class FileService {

    private static final Path ARCHIVO_DATOS = Path.of("datos.csv");
    private static final String CABECERA = "id;titulo;empresa;estado;fechaPostulacion;enlace;notas";
    private static final int NUM_CAMPOS = 7;


    public Oferta agregar(String titulo, String empresa, String estado, LocalDate fecha) throws IOException {
        List<Oferta> ofertas = cargar();

        int nuevoId = ofertas.stream()
                .mapToInt(Oferta::getId)
                .max()
                .orElse(0) + 1;

        Oferta nueva = new Oferta(nuevoId, titulo, empresa, estado, fecha, "", "");
        ofertas.add(nueva);
        guardar(ofertas);
        return nueva;
    }


    public List<Oferta> filtrar(List<Oferta> ofertas, String estado, String empresa){

       return ofertas.stream()
               .filter(oferta -> estado == null || oferta.getEstado().equals(estado))
               .filter(oferta -> empresa == null || oferta.getEmpresa().equals(empresa))
               .toList();

    }

    public void guardar(List<Oferta> ofertas) throws IOException {
        List<String> lineas = new ArrayList<>();
        lineas.add(CABECERA);
        for (Oferta oferta : ofertas) {
            lineas.add(oferta.toLineaCsv());
        }
        Files.write(ARCHIVO_DATOS, lineas);
    }

    public List<Oferta> cargar() throws IOException {
        if (Files.notExists(ARCHIVO_DATOS)) {
            return new ArrayList<>();
        }
        return leerOfertas(ARCHIVO_DATOS);
    }

    private List<Oferta> leerOfertas(Path ruta) throws IOException {
        List<String> lineas = Files.readAllLines(ruta);
        List<Oferta> ofertas = new ArrayList<>();

        for (int i = 1; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            if (linea.isBlank()) {
                continue;
            }
            ofertas.add(parsearLinea(linea, i + 1));
        }
        return ofertas;
    }

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


    public int importarCSV(String nombreFichero) throws IOException {
        List<Oferta> importadas = leerOfertas(Path.of(nombreFichero));
        List<Oferta> existentes = cargar();

        Set<Integer> idsExistentes = new HashSet<>();
        for (Oferta oferta : existentes) {
            idsExistentes.add(oferta.getId());
        }

        int nuevas = 0;
        for (Oferta oferta : importadas) {
            if (idsExistentes.add(oferta.getId())) {
                existentes.add(oferta);
                nuevas++;
            }
        }

        guardar(existentes);
        return nuevas;
    }

}
