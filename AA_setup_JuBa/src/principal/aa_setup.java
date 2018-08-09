/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import bd.ConexionBD;
import Objetos.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;

/**
 *
 * @author julian
 */
public class aa_setup {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Proyecto JuBa\nPrograma de Configuracion de Alimentador Automatico");
        System.out.println("INGRESO AL SISTEMA - AUTENTICACION");
        //ingresao de usuario
        Scanner in = new Scanner(System.in);
        System.out.println("Ingrese su cuenta de usuario:");
        String cuenta = in.next();
        System.out.println("Ingrese su contraseña:");
        String clave = in.next();
        //validacion de usuario y contraseña
        usuario u = new usuario(cuenta, clave);
        ConexionBD c = new ConexionBD();
        try{
            c.conectar();
            if(c.esUsuarioValido(u)){
                //ingreso al sistema
                u = c.obtenerDatosUsuario(cuenta);
                System.out.println("Bienvenido "+u.getNombres()+" "+u.getApellidos()+" - "+u.getTipo());
                //desplegar empresas
                System.out.println("Seleccione una empresa:");
                ArrayList<empresa> emps = c.cargarEmpresas(u.getId());
                mostrarListaEmpresas(emps);
                //seleccionar empresa
                System.out.println("Seleccione el numero de la empresa:");
                int emp = Integer.parseInt(in.next());
                empresa empresa_select = c.obtenerDatosEmpresa(emps.get(emp-1).getNombre());
                System.out.println("\nEmpresa seleccionada: "+empresa_select.getNombre());
                mostrarInfoEmpresa(empresa_select);
                //listar piscinas
                ArrayList<piscina> pisc = c.cargarPiscinas(empresa_select.getId_empresa());
                mostrarListaPiscinas(pisc);
                //seleccionar piscina
                System.out.println("Seleccione la Piscina:");
                int pi = Integer.parseInt(in.next());
                piscina piscina_select = c.obtenerDatosPiscina(pi);
                System.out.println("\nPiscina Seleccionada: "+piscina_select.getId_piscina()+" "+piscina_select.getDescripcion());
                mostrarInfoPiscina(piscina_select);
                //seleccionar evento
                System.out.println("Seleccione el Evento que desea ejecutar:");
                
            }else{
                System.out.println("Usuario Invalido");
            }
            c.desconectar();
        }catch(Exception e){
            System.out.println("No se pudo conectar a la base de datos. "+e);
        }
    }
    
    //funciones para mostrar datos
    
    public static void mostrarInfoEmpresa(empresa e){
        System.out.println("Informacion de la empresa seleccionada");
        System.out.println("Id Empresa:             "+e.getId_empresa());
        System.out.println("Nombre:                 "+e.getNombre());
        System.out.println("Ruc:                    "+e.getRuc());
        System.out.println("Dirección:              "+e.getDireccion());
        System.out.println("Dirección de Planta:    "+e.getDireccion_planta());
        System.out.println("Teléfono:               "+e.getTelefono());
        System.out.println("Correo:                 "+e.getCorreo());
        System.out.println("Usuario id:             "+e.getId_usuario()+"\n");
    }
    
    public static void mostrarInfoPiscina(piscina p){
        System.out.println("Informacion de la Piscina Seleccionada");
        System.out.println("Id Piscina:     "+p.getId_piscina());
        System.out.println("Descripción:    "+p.getDescripcion());
        System.out.println("Ubicación:      "+p.getUbicacion());
        System.out.println("Longitud Ancho: "+p.getLogitud_ancho()+" [m]");
        System.out.println("Longitud Largo: "+p.getLongitud_largo()+" [m]");
        System.out.println("Area:           "+p.getArea()+" [m2]");
        System.out.println("Tipo:           "+p.getTipo());
        System.out.println("Forma:          "+p.getForma());
        System.out.println("Poblacion Actual: "+p.getPoblacion()+" Camarónes.");
        System.out.println("Id Empresa:     "+p.getId_empresa());
        System.out.println("Id Producto:    "+p.getId_producto()+"\n");
    }
    
    public static void mostrarListaEmpresas(ArrayList<empresa> e){
        int i = 1;
            //presentando las empresas en linea de comando
            for (empresa emp:e){
                String nom_e = emp.getNombre();
                System.out.println(i+". "+nom_e);
                i++;
            }
    }
    
    public static void mostrarListaPiscinas(ArrayList<piscina> p){
        int i = 1;
            //presentando las empresas en linea de comando
            for (piscina pi:p){
                String desc_p = pi.getDescripcion();
                System.out.println(i+". Piscina - "+desc_p);
                i++;
            }
    }
    
    //funciones para validaciones
    
    public static boolean esNumero(){
        return false;
    }
    
    public static boolean estadoAlimentacion(){
        return false;
    }
    
    //funciones de procesos
    public static void alimentar(){
        
    }
}
