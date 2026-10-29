package org.example.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Oferta {

    public static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final int id;
    private final String titulo;
    private final String empresa;
    private final String estado;
    private final LocalDate fechaPostulacion;
    private final String enlace;
    private final String notas;

    public Oferta(int id, String titulo, String empresa, String estado,
                  LocalDate fechaPostulacion, String enlace, String notas) {
        this.id = id;
        this.titulo = titulo;
        this.empresa = empresa;
        this.estado = estado;
        this.fechaPostulacion = fechaPostulacion;
        this.enlace = enlace;
        this.notas = notas;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getEmpresa() { return empresa; }
    public String getEstado() { return estado; }
    public LocalDate getFechaPostulacion() { return fechaPostulacion; }
    public String getEnlace() { return enlace; }
    public String getNotas() { return notas; }

    public String toLineaCsv() {
        return String.join(";",
                String.valueOf(id), titulo, empresa, estado,
                fechaPostulacion.format(FORMATO_FECHA), enlace, notas);
    }
}

