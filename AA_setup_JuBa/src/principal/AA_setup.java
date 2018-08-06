/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import bd.ConexionBD;
import Objetos.*;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author julian
 */
public class AA_setup {

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
                System.out.println("Bienvenido "+u.getNombres()+" "+u.getApellidos());
                //desplegar empresas
                System.out.println("Seleccione una empresa:");
                ArrayList<empresa> emps = c.cargarEmpresas(u.getId());
                int contador = 1;
                for (empresa emp:emps){
                    String item = emp.getNombre();
                    System.out.println(contador+". "+item);
                    contador++;
                }
                System.out.println("Seleccione el numero de la empresa:");
                int s = Integer.parseInt(in.next());
                empresa empresa_select = c.obtenerDatosEmpresa(emps.get(s-1).getNombre());
                System.out.println("Empresa seleccionada: "+empresa_select.getNombre());
                mostrarInfoEmpresa(empresa_select);
                //
                
            }else{
                System.out.println("Usuario Invalido");
            }
            c.desconectar();
        }catch(Exception e){
            System.out.println("No se pudo conectar a la base de datos. "+e);
        }
                
        
        //seleccionar empresa
        
        //seleccionar piscina
        
        //seleccionar operadores
        
        //seleccionar alimentador automatico
        
        //
        
    }
    
    
    public static void mostrarInfoEmpresa(empresa e){
        System.out.println("Informacion de la empresa seleccionada:");
        System.out.println("Id: "+e.getId_empresa());
        System.out.println("Nombre: "+e.getNombre());
        System.out.println("Ruc: "+e.getRuc());
        System.out.println("Dirección: "+e.getDireccion());
        System.out.println("Dirección de Planta: "+e.getDireccion_planta());
        System.out.println("Teléfono: "+e.getTelefono());
        System.out.println("Correo: "+e.getCorreo());
        System.out.println("Usuario id: "+e.getId_usuario());
    }
}
