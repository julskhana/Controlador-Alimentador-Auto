/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

/**
 *
 * @author julian
 */
public class alimentadorAuto {
    
    private int id;
    private String descripcion;
    private float nivel_bateria;
    private float nivel_alimento;
    private float cap_max_alimento;
    private String tipo;
    private float distancia_recorrida;
    private int n_activaciones;
    private String estado;
    
    //constructor general

    public alimentadorAuto(int id, String descripcion, float nivel_bateria, float nivel_alimento, float cap_max_alimento, String tipo, float distancia_recorrida, int n_activaciones, String estado) {
        this.id = id;
        this.descripcion = descripcion;
        this.nivel_bateria = nivel_bateria;
        this.nivel_alimento = nivel_alimento;
        this.cap_max_alimento = cap_max_alimento;
        this.tipo = tipo;
        this.distancia_recorrida = distancia_recorrida;
        this.n_activaciones = n_activaciones;
        this.estado = estado;
    }
    
    //constructor vacio

    public alimentadorAuto() {
    }
    
    //set

    public void setId(int id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setNivel_bateria(float nivel_bateria) {
        this.nivel_bateria = nivel_bateria;
    }

    public void setNivel_alimento(float nivel_alimento) {
        this.nivel_alimento = nivel_alimento;
    }

    public void setCap_max_alimento(float cap_max_alimento) {
        this.cap_max_alimento = cap_max_alimento;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDistancia_recorrida(float distancia_recorrida) {
        this.distancia_recorrida = distancia_recorrida;
    }

    public void setN_activaciones(int n_activaciones) {
        this.n_activaciones = n_activaciones;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    //get

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public float getNivel_bateria() {
        return nivel_bateria;
    }

    public float getNivel_alimento() {
        return nivel_alimento;
    }

    public float getCap_max_alimento() {
        return cap_max_alimento;
    }

    public String getTipo() {
        return tipo;
    }

    public float getDistancia_recorrida() {
        return distancia_recorrida;
    }

    public int getN_activaciones() {
        return n_activaciones;
    }

    public String getEstado() {
        return estado;
    }
    
}
