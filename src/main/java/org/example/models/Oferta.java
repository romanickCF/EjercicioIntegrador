package org.example.models;

import java.time.LocalDate;

public class Oferta {

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
}
