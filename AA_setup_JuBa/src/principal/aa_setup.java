/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import bd.ConexionBD;
import Objetos.*;
import java.sql.Date;
//import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author julian
 */
public class aa_setup {
    /**
     * @param args the command line arguments
     */
    private static final int IDAA = 1;
    
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Proyecto JuBa\nPrograma de Configuracion de Alimentador Automatico");
        System.out.println("INGRESO AL SISTEMA - AUTENTICACION");
        //generar fecha hora
        System.out.println("Fecha y Hora Actual: "+generarFechaHora());
        //System.out.println("Fecha: "+generarFecha());
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
                //ALIMENTADOR AUTOMATICO
                System.out.println("Datos de Alimetador Automático");
                alimentadorAuto dispositivo = c.obtenerDatosDispositivoAA(IDAA);
                mostrarInfoDisp(dispositivo);
                if(c.encenderDispositivo(IDAA)){
                    //EMPRESAS
                    //desplegar empresas
                    System.out.println("Seleccione una empresa:");
                    ArrayList<empresa> empresas = c.cargarEmpresas(u.getId());
                    mostrarListaEmpresas(empresas);
                    //seleccionar empresa
                    System.out.println("Seleccione el numero de la empresa:");
                    int emp = Integer.parseInt(in.next());
                    empresa empresa_select = c.obtenerDatosEmpresa(empresas.get(emp-1).getNombre());
                    System.out.println("\nEmpresa seleccionada: "+empresa_select.getNombre());
                    mostrarInfoEmpresa(empresa_select);
                    //PISCINAS
                    //listar piscinas
                    ArrayList<piscina> piscinas = c.cargarPiscinas(empresa_select.getId_empresa());
                    mostrarListaPiscinas(piscinas);
                    //seleccionar piscina
                    System.out.println("Seleccione la Piscina:");
                    int pi = Integer.parseInt(in.next());
                    piscina piscina_select = c.obtenerDatosPiscina(pi);
                    System.out.println("\nPiscina Seleccionada: "+piscina_select.getId_piscina()+" "+piscina_select.getDescripcion());
                    mostrarInfoPiscina(piscina_select);
                    //EVENTOS
                    //listar evento
                    System.out.println("Seleccione el Evento programado que desea ejecutar:");
                    ArrayList<evento> eventos = c.cargarEventosPendientes(piscina_select.getId_piscina(),generarFecha());
                    System.out.println("Eventos programados: "+eventos.size());
                    if(eventos.size()>0){
                        mostrarListaEventos(eventos);
                        //seleccionar evento
                        System.out.println("Seleccione el *ID* Evento:");
                        int ev1 = Integer.parseInt(in.next());
                        evento evento_selec = c.obtenerDatosEvento(ev1);
                        mostrarInfoEvento(evento_selec);
                        //proceso de alimentacion
                        System.out.println("Desea Iniciar el Evento Seleccionado?[S/N]:");
                        String op = String.valueOf(in.next());
                        if(op.equalsIgnoreCase("s")){
                            //Iniciar Evento
                            System.out.println("\n*** Inicializacion de Evento ***");
                            //Cargar datos inicio
                            System.out.println("Datos Iniciales:");
                            mostrarInfoDisp(dispositivo);
                            if(c.encenderDispositivo(dispositivo.getId())){
                                while(dispositivo.getNivel_bateria()>20){
                                System.out.println("Estado del Dispositivo AA - Nivel Bateria: "+dispositivo.getNivel_bateria()+
                                        "% - Nivel Alimento: "+dispositivo.getNivel_alimento()+
                                        "% - Distancia Recorrida: "+dispositivo.getDistancia_recorrida()+
                                        "[m] - Número Activaciones: "+dispositivo.getN_activaciones()+
                                        " - Temperatura: "+generarTemperatura()+"°C"+
                                        " - Estado: "+dispositivo.getEstado());
                                //proceso de consumibles
                                procesoAlimentacion(dispositivo);
                                c.actualizarDispositivo(dispositivo);
                                /*
                                //generacion de logs
                                Date fh = Date.valueOf(generarFechaHora());
                                //log de bateria
                                log bitacora = generarBitacora(evento_selec.getId_evento(),dispositivo,fh);
                                c.ingresarLog(bitacora);
                                
                                //log de alimento
                                bitacora = generarBitacora(evento_selec.getId_evento(),dispositivo,fh,"ali");
                                c.ingresarLog(bitacora);
                                //log de movimiento
                                bitacora = generarBitacora(evento_selec.getId_evento(),dispositivo,fh,"mov");
                                c.ingresarLog(bitacora);
                                */
                                }
                                c.finalizarEvento(evento_selec);
                            }else{
                                System.out.println("Error en el proceso de alimentacion...");
                            }
                            System.out.println("Bateria Baja: "+dispositivo.getNivel_bateria()+"% - Regreso a Base *Muelle*");
                        }else if(op.equalsIgnoreCase("n")){
                            System.out.println("Evento cancelado por usuario...\n");
                        }else{
                            System.out.println("Opcion Invalida...");
                        }
                        //apagando dispositivo
                        if(c.apagarDispositivo(IDAA)){
                            System.out.println("Dispositivo "+IDAA+" Apagado...");
                        }
                    }else{
                        System.out.println("No hay eventos disponibles el dia de hoy: "+generarFecha());
                    }
                }else{
                    System.out.println("Error al conectar el dispositivo...");
                }
            }else{
                System.out.println("Usuario Invalido");
            }
        }catch(Exception e){
            System.out.println("No se pudo conectar a la base de datos. "+e);
            try{
                System.out.println("Apagado de emergencia...");
                c.apagarDispositivo(IDAA);
                c.desconectar();
            }catch(Exception ex){
                System.out.println("Error al apagar de emergencia el dispositivo. "+ex);
            }
                    
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
    
    public static void mostrarInfoDisp(alimentadorAuto aa){
        System.out.println("Informacion del Dispositivo");
        System.out.println("Id:                     "+aa.getId());
        System.out.println("Descripción:            "+aa.getDescripcion());
        System.out.println("Nivel Bateria:          "+aa.getNivel_bateria()+"%");
        System.out.println("Nivel Alimento:         "+aa.getNivel_alimento()+"%");
        System.out.println("Capacidad Max Alimento: "+aa.getCap_max_alimento()+"[KG]");
        System.out.println("Tipo:                   "+aa.getTipo());
        System.out.println("Distancia Recorrida:    "+aa.getDistancia_recorrida());
        System.out.println("Numero de Activaciones: "+aa.getN_activaciones());
        System.out.println("Estado: "+aa.getEstado()+"\n");   
    }
    
    public static void mostrarInfoEvento(evento e){
        System.out.println("Informacion del Evento");
        System.out.println("ID:                *"+e.getId_evento()+"*");
        System.out.println("Nombre:            "+e.getNombre());
        System.out.println("Tipo:              "+e.getTipo());
        System.out.println("Descripción:       "+e.getDescripcion());
        System.out.println("Fecha:             "+e.getFecha());
        System.out.println("Numero Operadores: "+e.getNumero_operadores());
        System.out.println("Piscina:           "+e.getId_piscina());        
        System.out.println("Estado:            "+e.getEstado()+"\n");    
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
    
    public static void mostrarListaEventos(ArrayList<evento> e){
        int i = 1;
            //presentando las empresas en linea de comando
            for (evento eve:e){
                int id = eve.getId_evento();
                String nom = eve.getNombre();
                String fecha = eve.getFecha().toString();
                String fecha_act = generarFecha();
                String estado = eve.getEstado();
                if(fecha.equals(fecha_act)){
                    System.out.println(i+". Evento Id: *"+id+"* "+nom+" - "+estado);
                    i++;
                }
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
    
    public static String generarFechaHora(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now(); 
        return dtf.format(now);
    }
    
    public static String generarFecha(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
        LocalDateTime now = LocalDateTime.now(); 
        return dtf.format(now);
    }
    
    public static boolean iniciarEvento(evento e, alimentadorAuto aa){
        float n_bat = aa.getNivel_bateria();
        float n_al = aa.getNivel_alimento();
        return (n_bat==100.00)&&(n_al==100.00);
    }
    
    public static float generarTemperatura(){   
        //generador de temperatura - 50 is the maximum and the 1 is our minimum 
        Random rand = new Random();
        float temp = rand.nextInt(20) + 19;
        return temp;
    }
    
    public static alimentadorAuto procesoAlimentacion(alimentadorAuto d){
        //actualizacion de consumibles en alimentador automatico
        //disminuir bateria
        float bat_act = d.getNivel_bateria()-1;
        //disminuir alimento
        float al_act = d.getNivel_alimento()-1;
        //aumentar distancia
        float dist_act = (float) (d.getDistancia_recorrida()+0.2);
        //activaciones
        int n_activaciones = d.getN_activaciones()+1;
        //insertar valores actualizados
        d.setNivel_bateria(bat_act);
        d.setNivel_alimento(al_act);
        d.setDistancia_recorrida(dist_act);
        d.setN_activaciones(n_activaciones);
        
        try{
            TimeUnit.SECONDS.sleep(1);
        }catch(InterruptedException e){
            System.out.println("Error en el proceso de alimentacion...\n"+e);
        }
        return d;
    }
    
    public static log generarBitacora(int id_evento, alimentadorAuto d,Date fh){
        //tipos bat, ali, mov
        log bit = new log();
        /*
        private int id;
        private String tipo;
        private String descripcion;
        private String valor;
        private Date fecha_hora;
        private String prioridad;
        private float temperatura;
        private int id_evento;
        */
        //descripcion
        //valores
        /*
        float n_bat = d.getNivel_bateria();
        float n_al = d.getNivel_alimento();
        String est = d.getEstado();
        */
        //tipos
        String tipo[] = {"Notificacion","Advertencia","Error","Critico"};
        //prioridades
        String pri[] = {"Baja","Media","Alta"};        
        //seleccion de tipos de bitacora
        //energia
        if (d.getNivel_bateria()>40){
            bit.setTipo("Notificacion");
            bit.setDescripcion("Bateria %");
            bit.setValor(d.getNivel_bateria());
            bit.setPrioridad("Baja");
        }else if(d.getNivel_bateria()>20 && d.getNivel_bateria()<=40){
            bit.setTipo("Advertencia");
            bit.setDescripcion("Bateria %");
            bit.setValor(d.getNivel_bateria());
            bit.setPrioridad("Media");
        }else if(d.getNivel_bateria()<=20){
            bit.setTipo("Critico");
            bit.setDescripcion("Bateria %");
            bit.setValor(d.getNivel_bateria());
            bit.setPrioridad("Alta");
        }
        /*
        if(tip.equalsIgnoreCase("bat")){
            //noveles de bateria
            if(n_bat>50){
                bit.setNombre(nombre[1]);
                bit.setDescripcion("Nivel de Bateria %");
                bit.setValor(n_al+"%");
                bit.setFecha_hora(fh);
                bit.setTipo(tipo[0]);
                bit.setPrioridad(pri[0]);
            }else if(n_bat<=50 && n_bat>=25){
                bit.setNombre(nombre[1]);
                bit.setDescripcion("Nivel de Bateria %");
                bit.setValor(n_al+"%");
                bit.setFecha_hora(fh);
                bit.setTipo(tipo[1]);
                bit.setPrioridad(pri[1]);
            }else if(n_bat<25){
                bit.setNombre(nombre[1]);
                bit.setDescripcion("Nivel de Bateria %");
                bit.setValor(n_al+"%");
                bit.setFecha_hora(fh);
                bit.setTipo(tipo[3]);
                bit.setPrioridad(pri[2]);
            }
        }else if(tip.equalsIgnoreCase("ali")){
            //nivelers de alimento
            if(n_al>0){
                bit.setNombre(nombre[0]);
                bit.setDescripcion("Nivel de Alimento %");
                bit.setValor(n_al+"%");
                bit.setFecha_hora(fh);
                bit.setTipo(tipo[0]);
                bit.setPrioridad(pri[0]);            
            }else{
                bit.setNombre(nombre[0]);
                bit.setDescripcion("Nivel de Alimento %");
                bit.setValor(n_al+"%");
                bit.setFecha_hora(fh);
                bit.setTipo(tipo[2]);
                bit.setPrioridad(pri[2]);            
            }
        }else if(tip.equalsIgnoreCase("mov")){
            //distancia recorrida
            bit.setNombre(nombre[2]);
            bit.setDescripcion("Distancia Recorrida [m]");
            bit.setValor(n_al+"%");
            bit.setFecha_hora(fh);
            bit.setTipo(tipo[0]);
            bit.setPrioridad(pri[0]);            
        }else{
            System.out.println("Error al generar log...");
        }*/
        System.out.println("");
        bit.setFecha_hora(fh);
        bit.setTemperatura(generarTemperatura());
        bit.setId_evento(id_evento);
        return bit;
    }
}
