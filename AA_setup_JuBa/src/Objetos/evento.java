/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;


import java.io.Serializable;
import java.sql.Date;

/**
 *
 * @author julian
 */
public class evento implements Serializable{
    
    private int id_evento;
    private String nombre;
    private String tipo;
    private String descripcion;
    private Date fecha;
    private int numero_operadores;
    private int id_piscina;
    private String estado;
    
    //constructor

    public evento(int id_evento, String nombre, String tipo, String descripcion, Date fecha, int numero_operadores, int id_piscina, String estado) {
        this.id_evento = id_evento;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.numero_operadores = numero_operadores;
        this.id_piscina = id_piscina;
        this.estado = estado;
    }
    
    //constructor para ingresar en Piscina
    public evento(String nombre, String tipo, String descripcion, Date fecha, int numero_operadores, int id_piscina, String estado) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.numero_operadores = numero_operadores;
        this.id_piscina = id_piscina;
        this.estado = estado;
    }
    
    //constructor vacio

    public evento(){
    }
    
    //set

    public void setId_evento(int id_evento) {
        this.id_evento = id_evento;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setNumero_operadores(int numero_operadores) {
        this.numero_operadores = numero_operadores;
    }

    public void setId_piscina(int id_piscina) {
        this.id_piscina = id_piscina;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    //get

    public int getId_evento() {
        return id_evento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public int getNumero_operadores() {
        return numero_operadores;
    }

    public int getId_piscina() {
        return id_piscina;
    }

    public String getEstado() {
        return estado;
    }
    
}
