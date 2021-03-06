//julian cambio
//jerson junqui
package bd;

//import entidades.Universidad;
//import entidades.Usuario;
import Objetos.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ConexionBD {
    
       private Connection con;
       private static final String DRIVER = "com.mysql.jdbc.Driver";
       //private static final String DRIVER = "mysql-connector-java-5.1.23.jar";
       private static final String DBMS = "mysql";
       //private static final String HOST = "192.168.10.2";     //prueba juba       ok
       //private static final String HOST = "192.168.1.20";       //prueba home       ok
       private static final String HOST = "172.20.143.29";   //prueba espol      ok
       //private static final String HOST = "localhost";        //prueba local      ok
       //private static final String HOST = "127.0.0.1";        //prueba local      ok
       private static final String PORT = "3306";
       //base de datos
       private static final String DATABASE = "base_juba";      //cortejamiento: utf8_spanish_ci
       private static final String USER = "root2";
       private static final String PASSWORD = "proyecto";

    void Conexion(){}
    
    /*METODO CONECTAR*/
   
    public void conectar ()throws Exception{
        try{
            Class.forName(DRIVER);
            System.out.println("Cargando Driver MySQL...");
        }catch(ClassNotFoundException ce){System.out.println("error class " +ce);}
            try{
                this.con = DriverManager.getConnection("jdbc:" + DBMS + "://" + HOST + ":" + PORT + "/" + DATABASE, USER, PASSWORD);                
                //System.out.println("CONEXION EXITOSA CON LA BASE DE DATOS: ");
                System.out.println("Conectando con servidor "+HOST);
            }catch(SQLException exception){
                System.out.println("ERROR: NO SE PUDO CONECTAR CON LA BASE DE DATOS: "+exception);
            }             
    }
    
    public boolean desconectar(){
        try{
            this.con.close();
            return(true);
        }
        catch(SQLException e)
        {
            System.out.println("Error al Desconectar. "+e);
            return(false);
        }    
    }
    
    //FUNCIONES PARA PORYECTO CAMARONERA
    /*
    //coversion de fecha a date sql
    public class DatesConversion {

    public void main(String[] args) {
        java.util.Date uDate = new java.util.Date();
        System.out.println("Time in java.util.Date is : " + uDate);
        java.sql.Date sDate = convertUtilToSql(uDate);
        System.out.println("Time in java.sql.Date is : " + sDate);
        DateFormat df = new SimpleDateFormat("dd/MM/YYYY - hh:mm:ss");
        System.out.println("Using a dateFormat date is : " + df.format(uDate));
    }   
    }*/
    
    public alimentadorAuto obtenerDatosDispositivoAA(int id){
        alimentadorAuto aa = new alimentadorAuto();
        ResultSet rs = null;
        PreparedStatement st = null;
        try{
            st = con.prepareStatement("select * from alimentador_auto where id_aa = ?;");
            st.setInt(1,id);
            st.executeQuery();
            rs = st.executeQuery();
            if(rs.next()){
                aa.setId(id);
                aa.setDescripcion(rs.getString("descripcion"));
                aa.setNivel_bateria(rs.getFloat("nivel_bateria"));
                aa.setNivel_alimento(rs.getFloat("nivel_alimento"));
                aa.setCap_max_alimento(rs.getFloat("capacidad_max_alimento"));
                aa.setTipo(rs.getString("tipo"));
                aa.setDistancia_recorrida(rs.getFloat("distancia_recorrida"));
                aa.setEstado(rs.getString("estado"));
            }
            rs.close();
            st.close();
            System.out.println("Datos de dispositivo cargados correctamente...");
        }catch(Exception e){
            System.out.println("Error al obtener datos del dispositivo...\n"+e);
        }
        
        return aa;
    }
    
    public boolean encenderDispositivo(int id){
        System.out.println("Encendiendo Dispositivo...");
        String estado_actual = "Encendido";
        try{
            PreparedStatement st = null;
            st = con.prepareStatement("update alimentador_auto set estado = ? WHERE id_aa = ?;");
            st.setString(1,estado_actual);
            st.setInt(2,id);
            st.executeUpdate();
            st.close();
            System.out.println("Dispositivo encendido correctamente...");
            return true;
        }catch(SQLException e){
            System.out.println("Error al encender el dispositivo... "+e);
            return false;
        }
    }
    
    public boolean apagarDispositivo(int id){
        System.out.println("Apagando Dispositivo...");
        String estado_actual = "Apagado";
        try
        {
            PreparedStatement st = null;
            st = con.prepareStatement("update alimentador_auto set estado = ? WHERE id_aa = ?;");
            st.setString(1,estado_actual);
            st.setInt(2,id);
            st.executeUpdate();
            st.close();
            System.out.println("Dispositivo apagado correctamente...");
            return true;
        }catch(SQLException e)
        {
            System.out.println("Error al apagar el dispositivo... "+e);
            return false;
        }
    }
    
    public boolean ingresarEmpresa(empresa e){
        try{
            PreparedStatement st = null;
            st = con.prepareStatement("INSERT into empresa (nombre,ruc,direccion,direccion_planta,telefono,correo) VALUES(?,?,?,?,?,?);");
            st.setString(1,e.getNombre());
            st.setString(2,e.getRuc());
            st.setString(3,e.getDireccion());
            st.setString(4,e.getDireccion_planta());
            st.setString(5,e.getTelefono());
            st.setString(6,e.getCorreo());
            
            st.executeUpdate();
            st.close();
            
            System.out.println("Se ingreso la empresa exitosamente...");
            return true;
        }catch (SQLException ee){
            System.out.println("Error al ingresar la empresa\n"+ee);
            return false;
        }
    }
    
    public boolean ingresarUsuario(usuario u){
        try{
            PreparedStatement st = null;
            st = con.prepareStatement("INSERT INTO usuario (cuenta,clave,nombres,apellidos,cedula,edad,direccion,telefono,celular,correo,sexo,tipo,cargo,fecha_inicio,estado) VALUES(?,md5(?),?,?,?,?,?,?,?,?,?,?,?,?,?);");
            st.setString(1,u.getCuenta());
            st.setString(2,u.getClave());
            st.setString(3,u.getNombres());
            st.setString(4,u.getApellidos());
            st.setString(5,u.getCedula());
            st.setInt(6,u.getEdad());
            st.setString(7,u.getDireccion());
            st.setString(8,u.getTelefono());
            st.setString(9,u.getCelular());
            st.setString(10,u.getCorreo());
            st.setString(11,u.getSexo());
            st.setString(12,u.getTipo());
            st.setString(13,u.getCargo());
            st.setString(14,u.getEstado());
            st.setDate(15,u.getFecha_inicio()); //AAAA-MM-DD
            
            st.executeUpdate();
            st.close();
            
            System.out.println("Se ingreso el usuario exitosamente...");
            return true;
        }catch (SQLException e){
            System.out.println("Error al ingresar el usuario\n"+e);
            return false;
        }
    }
    
    public boolean esUsuarioValido(usuario u){        
        boolean resultado = false;
        ResultSet rs = null;
        PreparedStatement st = null;
        try{
            st = con.prepareStatement("SELECT * FROM usuario WHERE cuenta = ? AND clave = md5(?) AND estado = ?");
            //st = con.prepareStatement("SELECT * FROM usuario WHERE cuenta = ? AND clave = md5(?);");            
            st.setString(1,u.getCuenta());         
            st.setString(2,u.getClave());
            st.setString(3,"A");
            rs = st.executeQuery();            
            if(rs.next()){
                u.setTipo(rs.getString("estado"));
                resultado = true;
                System.out.println("Usuario Válido");
            }else{
                System.out.println("Usuario o Clave invalidos...");
            }
            rs.close();
            st.close();
        }
        catch(SQLException e){
            System.out.println("Error al consultar usuario. "+ e);
            resultado = false;
        }           
     return resultado; 
    }
    
    public boolean esEmpresaValida(String emp){        
        boolean resultado = false;
        ResultSet rs = null;
        PreparedStatement st = null;
        try{
            st = con.prepareStatement("SELECT nombre FROM empresa WHERE nombre = ?;");                
            st.setString(1,emp);
            rs = st.executeQuery();            
            if(rs.next()){
                resultado = true;
                System.out.println("Empresa valida:"+emp);
            }else{
                System.out.println("Empresa invalida...");
            }
            rs.close();
            st.close();
        }
        catch(SQLException e){
            System.out.println("Error al consultar empresa. "+ e);
            resultado = false;
        }           
     return resultado; 
    }
    
    //consultas
    //funcion para obtener obejtos usuarios desde cuenta
    public usuario obtenerDatosUsuario(String cuenta){
        usuario u = new usuario();
        ResultSet rs = null;                       
        PreparedStatement st = null;
        try{
            st = con.prepareStatement("SELECT * FROM usuario WHERE cuenta = ?;");            
            st.setString(1,cuenta);         
            rs = st.executeQuery();            
            if(rs.next()){
                u.setId(rs.getInt("id_usuario"));
                u.setCuenta(cuenta);
                u.setNombres(rs.getString("nombres"));
                u.setApellidos(rs.getString("apellidos"));
                u.setCedula(rs.getString("cedula"));
                u.setEdad(rs.getInt("edad"));
                u.setDireccion(rs.getString("direccion"));
                u.setTelefono(rs.getString("telefono"));
                u.setCelular(rs.getString("celular"));
                u.setCorreo(rs.getString("correo"));
                u.setSexo(rs.getString("sexo"));
                u.setTipo(rs.getString("tipo"));
                u.setCargo(rs.getString("cargo"));
                u.setEstado(rs.getString("estado"));
                u.setFecha_inicio(rs.getDate("fecha_inicio"));
                
                System.out.println("Datos de usuario obtenidos...");
            }
            rs.close();
            st.close();
        }catch(SQLException e){
            System.out.println(e);
        }           
        return u; 
    }
    
    //consulta de seleccion de empresa despues de autenticacion
    public ArrayList<empresa> cargarEmpresas(int id_usuario){        
        ArrayList<empresa> registroE = new ArrayList<>();
        try{
            Statement st = this.con.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery("SELECT * FROM empresa where id_usuario = "+id_usuario+";");
            while (rs.next()){
                int id = rs.getInt("id_empresa");
                String nombre = rs.getString("nombre");
                empresa emp = new empresa(id, nombre,id_usuario);
                registroE.add(emp);
            }
            System.out.println("Empresas Consultadas.");
        }catch (SQLException e){
            System.out.println("error en consulta de empresas."+e);
        }
        return registroE;
    }
    
     public ArrayList<piscina> cargarPiscinas(int id_empresa){
        ArrayList<piscina> registro = new ArrayList<>();
        try{
            Statement st = this.con.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery("SELECT * FROM piscina where id_empresa = "+id_empresa+";");
            while (rs.next()){
                int id = rs.getInt("id_piscina");
                String desc = rs.getString("descripcion");
                int emp = rs.getInt("id_empresa");
                int prod = rs.getInt("id_producto");
                piscina p = new piscina(id,desc,emp,prod);
                registro.add(p);
            }
            System.out.println("Piscinas Consultadas.");
        }catch (SQLException e){
            System.out.println("error en consulta de piscinas."+e);
        }
        return registro;
    }
     
     public ArrayList<evento> cargarEventosPendientes(int id_piscina, String fecha){
        ArrayList<evento> registro = new ArrayList<>();
        try{
            Statement st = this.con.createStatement();
            ResultSet rs = null;
            //SELECT * FROM evento where id_piscina = 1 AND fecha = 2018-08-11 AND estado = "Pendiente";
            rs = st.executeQuery("SELECT * FROM evento where id_piscina = "+id_piscina+" AND estado = \"Pendiente\";");
            while (rs.next()){
                int id = rs.getInt("id_evento");
                String nombre = rs.getString("nombre");
                String tipo = rs.getString("tipo");
                String desc = rs.getString("descripcion");
                Date fechaE = rs.getDate("fecha");
                int n_ops = rs.getInt("numero_operadores");
                int id_pisc = rs.getInt("id_piscina");
                String estado = rs.getString("estado");
                /*
                System.out.println("fecha consulta:     "+fecha);
                System.out.println("fecha evento base:  "+fechaE);
                */        
                if(fecha.equals(String.valueOf(fechaE))){
                    evento e = new evento(id,nombre, tipo, desc, fechaE, n_ops, id_pisc,estado);
                    registro.add(e);
                }
            }
            System.out.println("Eventos Consultados.");
        }catch (SQLException e){
            System.out.println("Error en consulta de eventos."+e);
        }
        return registro;
    }

    //obtener datos empresa
    public empresa obtenerDatosEmpresa(String nombre){
        empresa emp = new empresa();
        ResultSet rs = null;                       
        PreparedStatement st = null;
        try{
            st = con.prepareStatement("SELECT * FROM empresa WHERE nombre = ?;");            
            st.setString(1,nombre);         
            rs = st.executeQuery();            
            if(rs.next()){
                emp.setId_empresa(rs.getInt("id_empresa"));
                emp.setNombre(nombre);
                emp.setRuc(rs.getString("ruc"));
                emp.setDireccion(rs.getString("direccion"));
                emp.setDireccion_planta(rs.getString("direccion_planta"));
                emp.setTelefono(rs.getString("telefono"));
                emp.setCorreo(rs.getString("correo"));
                emp.setId_usuario(rs.getInt("id_usuario"));
                System.out.println("Datos de empresa obtenidos...");
            }
            rs.close();
            st.close();
        }catch(SQLException e){
            System.out.println(e);
        }           
        return emp; 
    }
    
    public piscina obtenerDatosPiscina(int empresa){
        piscina p = new piscina();
        ResultSet rs = null;                       
        PreparedStatement st = null;
        try{
            st = con.prepareStatement("SELECT * FROM piscina WHERE id_empresa = ?;");            
            st.setString(1,String.valueOf(empresa));
            rs = st.executeQuery();            
            if(rs.next()){
                p.setId_piscina(rs.getInt("id_piscina"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setUbicacion(rs.getString("ubicacion"));
                p.setLongitud_ancho(rs.getFloat("longitud_ancho"));
                p.setLongitud_largo(rs.getFloat("longitud_largo"));
                p.setArea(rs.getFloat("area"));
                p.setTipo(rs.getString("tipo"));
                p.setForma(rs.getString("forma"));
                p.setPoblacion(rs.getInt("poblacion"));
                p.setId_empresa(rs.getInt("id_empresa"));
                p.setId_producto(rs.getInt("id_producto"));
                
                System.out.println("Datos de la piscina obtenidos...");
            }
            rs.close();
            st.close();
        }catch(SQLException e){
            System.out.println(e);
        }           
        return p; 
    }
    
    public evento obtenerDatosEvento(int id){
        evento eve = new evento();
        ResultSet rs = null;
        PreparedStatement st = null;
        try{
            st = con.prepareStatement("select * from evento where id_evento = ?;");
            st.setInt(1,id);
            st.executeQuery();
            rs = st.executeQuery();
            if(rs.next()){
                eve.setId_evento(id);
                eve.setNombre(rs.getString("nombre"));
                eve.setTipo(rs.getString("tipo"));
                eve.setDescripcion(rs.getString("descripcion"));
                eve.setFecha(rs.getDate("fecha"));
                eve.setNumero_operadores(rs.getInt("numero_operadores"));
                eve.setId_piscina(rs.getInt("id_piscina"));
                eve.setEstado(rs.getString("estado"));
            }
            rs.close();
            st.close();
            System.out.println("Datos de evento cargados correctamente...");
        }catch(SQLException e){
            System.out.println("Error al obtener datos del evento...\n"+e);
        }
        return eve;
    }

    
    /*
    public evento obtenerDatosEvento(int evento){
        evento ev = new evento();
        ResultSet rs = null;                       
        PreparedStatement st = null;
        try{
            st = con.prepareStatement("SELECT * FROM evento WHERE id_evento = ?;");
            st.setString(1,String.valueOf(evento));
            rs = st.executeQuery();            
            if(rs.next()){
                ev.setId_evento(rs.getInt("id_evento"));
                ev.setNombre(rs.getString("nombre"));
                ev.setTipo(rs.getString("tipo"));
                ev.setDescripcion(rs.getString("descripcion"));
                ev.setFecha(Date.valueOf(rs.getString("fecha")));
                ev.setNumero_operadores(rs.getInt("numero_operadores"));
                ev.setId_piscina(rs.getInt("id_piscina"));
                ev.setEstado(rs.getString("estado"));
                System.out.println("Datos de evento obtenidos...");
            }
            rs.close();
            st.close();
        }catch(SQLException e){
            System.out.println("Error al obtener datos de evento... "+e);
        }           
        return ev; 
    }*/
    
    public ArrayList<empresa> consultarEmpresas(String busqueda, String tipo){
        ArrayList<empresa> registro = new ArrayList<>();
        try{
            Statement st = this.con.createStatement();            
            ResultSet rs = null;
            
            if(tipo.equalsIgnoreCase("empresa")){
                rs = st.executeQuery("SELECT * FROM empresa;");
            }else{
                rs = st.executeQuery("SELECT * FROM empresa WHERE "+tipo+" LIKE '%"+busqueda+"%';");
            }
            
            while (rs.next()){
                int id = rs.getInt("id_empresa");
                String nom = rs.getString("nombre");
                String ruc = rs.getString("ruc");
                String dir = rs.getString("direccion");
                String dir_p = rs.getString("direccion_planta");
                String tel = rs.getString("telefono");
                String cor = rs.getString("correo");
                int id_u = rs.getInt("id_usuario");
                
                empresa emp = new empresa(id, nom, ruc, dir, dir_p, tel, cor, id_u);
                registro.add(emp);
            }
            System.out.println("empresas consultadas...");
        }catch (SQLException e){
            System.out.println("error en consulta de empresas."+e);
        }
        return registro;
    } 
    
    public ArrayList<usuario> consultarUsuarios(String busqueda, String tipo){
	ArrayList<usuario> registroU = new ArrayList<>();
	try{
		Statement st = this.con.createStatement();		
		ResultSet rs = null;
		
		if(tipo.equalsIgnoreCase("usuario")){
			rs = st.executeQuery("SELECT * FROM usuario;");
		}else{
			rs = st.executeQuery("SELECT * FROM usuario WHERE "+tipo+" LIKE '%"+busqueda+"%';");
		}
		
		while (rs.next()){
                    int id           = rs.getInt("id_usuario");
                    String cuenta    = rs.getString("cuenta");
                    String nombres   = rs.getString("nombres");
                    String apellidos = rs.getString("apellidos");
                    String cedula    = rs.getString("cedula");
                    int edad         = rs.getInt("edad");
                    String direccion = rs.getString("direccion");
                    String telefono  = rs.getString("telefono");
                    String celular   = rs.getString("celular");
                    String correo    = rs.getString("correo");
                    String sexo      = rs.getString("sexo");
                    String tipoU     = rs.getString("tipo");
                    String cargo     = rs.getString("cargo");
                    String estado    = rs.getString("estado");
                    Date fecha_inicio = rs.getDate("fecha_inicio");
                    
                    usuario usr = new usuario(id, cuenta, cargo, nombres, apellidos, cedula, edad, direccion, telefono, celular, correo, sexo, tipoU, cargo, fecha_inicio, estado);
                    registroU.add(usr);
		}
		System.out.println("usuarios consultados.");
	}catch (SQLException e){
		System.out.println("error en consulta de usuarios."+e);
	}
	return registroU;
    }
    
    public ArrayList<operador> consultarPiscinas(String busqueda, String lista) {
        ArrayList<operador> registro = new ArrayList<>();
        try{
            Statement st = this.con.createStatement();            
            ResultSet rs = null;
            
            if(lista.equalsIgnoreCase("piscina")){
                rs = st.executeQuery("SELECT * FROM piscina;");
            }else{
                rs = st.executeQuery("SELECT * FROM piscina WHERE "+lista+" LIKE '%"+busqueda+"%';");
            }
            
            while (rs.next()){
                int id_operador  = rs.getInt("id_operador");
                String nombre    = rs.getString("nombre");
                String cedula    = rs.getString("cedula");
                String telefono  = rs.getString("telefono");
                String tipo      = rs.getString("tipo");
                
                operador emp = new operador(id_operador,nombre,cedula,telefono,tipo);
                registro.add(emp);
            }
            System.out.println("operadores consultados...");
        }catch (SQLException e){
            System.out.println("error en consulta de operadores"+e);
        }
        return registro;
    }
    
    public ArrayList<operador> consultarOperadores(String busqueda, String lista) {
        ArrayList<operador> registro = new ArrayList<>();
        try{
            Statement st = this.con.createStatement();            
            ResultSet rs = null;
            
            if(lista.equalsIgnoreCase("operador")){
                rs = st.executeQuery("SELECT * FROM operador;");
            }else{
                rs = st.executeQuery("SELECT * FROM operador WHERE "+lista+" LIKE '%"+busqueda+"%';");
            }
            
            while (rs.next()){
                int id_operador  = rs.getInt("id_operador");
                String nombre    = rs.getString("nombre");
                String cedula    = rs.getString("cedula");
                String telefono  = rs.getString("telefono");
                String tipo      = rs.getString("tipo");
                
                operador emp = new operador(id_operador,nombre,cedula,telefono,tipo);
                registro.add(emp);
            }
            System.out.println("operadores consultados...");
        }catch (SQLException e){
            System.out.println("error en consulta de operadores"+e);
        }
        return registro;
    }
    
    public boolean ingresarLog(log l){
        try{
            PreparedStatement st=null;
            st = con.prepareStatement("INSERT INTO logs (tipo,descripcion,valor,fecha_hora,prioridad,temperatura,id_evento) VALUES(?,?,?,?,?,?,?);");           
            st.setString(1,l.getTipo());          //notificacion,advertencia,error,critico. 
            st.setString(2,l.getDescripcion());   //nivel bat, nivel alimento, distancia, n act, estado_alimentador
            st.setFloat(3,l.getValor());          //valor del la variable
            st.setDate(4,l.getFecha_hora());      //fecha hora
            st.setString(5,l.getPrioridad());     //baja,media,alta
            st.setFloat(7,l.getTemperatura());    //random
            st.setInt(7,l.getId_evento());        //evento
            
            st.executeUpdate();
            st.close();
            System.out.println("Bitacora - Tipo: ["+l.getTipo()+"] Descripcion: ["+l.getDescripcion()+"] Valor: ["+l.getValor()+"] Fecha Hora: ["+l.getFecha_hora()+"] Prioridad: ["+l.getPrioridad()+"] Tempreatura ["+l.getTemperatura()+"°C] Evento: ["+l.getId_evento()+"]");
            //System.out.println(" Tipo: "+log.getTipo()+"Log: "+log.getId()+" Nombre: "+log.getNombre()+" FechaHora: "+log.getFecha_hora()+" Descripcion: "+log.getDescripcion()+"Valor: "+log.getValor()+" Prioridad: "+log.getPrioridad()+" Temperatura: "+log.getTemperatura()+"°C"+" Evento: "+log.getId_evento());
            return true;
        }catch (SQLException e){
            System.out.println("Error al generar el log\n"+e);
            return false;
        }
    }
    
    public boolean actualizarDispositivo(alimentadorAuto disp){
        try{
            System.out.println("Id del Dispositivo: "+disp.getId());
            PreparedStatement st2 = null;
            st2 = con.prepareStatement("update alimentador_auto set nivel_bateria = ?, nivel_alimento = ?, distancia_recorrida = ?, numero_activaciones = ? where id_aa = "+disp.getId()+";");
            st2.setFloat(1,disp.getNivel_bateria());
            st2.setFloat(2,disp.getNivel_alimento());
            st2.setFloat(3,disp.getDistancia_recorrida());
            st2.setInt(4,disp.getN_activaciones());
            
            st2.executeUpdate();
            st2.close();
            
            System.out.println("Actualizacion de datos de Dispositivo exitosa...");
            return true;
        }
        catch(SQLException e){
            System.out.println("Error al actalizar dispositivo..."+e);
            return false;
        }
    }
    
    public boolean finalizarEvento(evento ev){
        try{
            System.out.println("Terminando evento #"+ev.getId_evento());
            PreparedStatement st = null;
            st = con.prepareStatement("UPDATE evento set estado = \"Realizado\" WHERE id_evento = ?;");
            st.setInt(1,ev.getId_evento());
            
            st.executeUpdate();
            st.close();
            System.out.println("Evento Realizado Exitosamente...");
            
            return true;
        }catch (SQLException e){
            System.out.println("Error al terminar evento... "+ e);
            return false;
        }
    }
    
    /**
    //FUNCIONES DE PROYECTO RESTAURANTE
    //FUNCIONES PARA USUARIO
    //funcion ingreso de usuarios
    public boolean ingresarUsuario(usuario u){
        try{
            PreparedStatement st=null;
            st = con.prepareStatement("insert into usuario (cuenta,clave,rol,estado,fecha_registro) values (?,md5(?),?,?,?);");
            st.setString(1,u.getCuenta());
            st.setString(2,u.getClave());
            st.setString(3,u.getRol());
            st.setString(4,u.getEstado());
            st.setString(5,u.getFecha_registro());
            
            st.executeUpdate();
            st.close();
            
            System.out.println("Se ingreso el usuario exitosamente...");
            return true;
        }catch (SQLException e){
            System.out.println("Error al ingresar el usuario\n"+e);
            return false;
        }
    }
    
    public boolean esUsuarioValido(usuario u){        
        boolean resultado = false;
        ResultSet rs = null;
        PreparedStatement st = null;
        try{
            st = con.prepareStatement("SELECT * FROM usuario WHERE cuenta = ? AND clave = md5(?) AND estado = ?");            
            st.setString(1,u.getCuenta());         
            st.setString(2,u.getClave());
            st.setString(3,"A");
            rs = st.executeQuery();            
            if(rs.next()){
                u.setRol(rs.getString("rol"));
                resultado = true;
                System.out.println("usuario valido y activo...");
            }else{
                System.out.println("usuario despedido...");
            }
            rs.close();
            st.close();
        }
        catch(SQLException e){
            System.out.println("Error al consultar usuario. "+ e);
            resultado = false;
        }           
     return resultado; 
    }
    
    //funcion para obtener obejtos usuarios desde cuenta
    public usuario obtenerDatosUsuario(String cuenta){
        usuario u = new usuario();
        ResultSet rs = null;                       
        PreparedStatement st = null;
        try{
            st = con.prepareStatement("SELECT * FROM usuario WHERE cuenta = ?;");            
            st.setString(1,cuenta);         
            rs = st.executeQuery();            
            if(rs.next()){
                u.setId(rs.getInt("id"));
                u.setCuenta(rs.getString("cuenta"));
                u.setRol(rs.getString("rol"));
                u.setEstado(rs.getString("estado"));
                u.setFecha_registro(rs.getString("fecha_registro"));
                System.out.println("Datos de usuario obtenidos...");
            }
            rs.close();
            st.close();
        }catch(SQLException e){
            System.out.println(e);
        }           
        return u; 
    }
    
    //funcion para consulta de usuarios
    public ArrayList<usuario> consultarUsuarios(String busqueda, String tipo){
        ArrayList<usuario> registroU = new ArrayList<usuario>();
        try{
            Statement st = this.con.createStatement();
            //ResultSet rs = st.executeQuery("SELECT * FROM usuario;");
            ResultSet rs = null;
            
            if(tipo.equalsIgnoreCase("usuario")){
                rs = st.executeQuery("SELECT * FROM usuario;");
            }else{
                rs = st.executeQuery("SELECT * FROM cliente WHERE "+tipo+" LIKE '%"+busqueda+"%';");
            }
            
            while (rs.next()){
                int id = rs.getInt("id");
                String cuenta = rs.getString("cuenta");
                String clave = rs.getString("clave");
                String rol = rs.getString("rol");
                String estado = rs.getString("estado");
                String fecha_reg = rs.getString("fecha_registro");
                
                usuario usr = new usuario(id,cuenta,clave,rol,estado,fecha_reg);
                registroU.add(usr);
            }
            System.out.println("usuarios consultados.");
        }catch (Exception e){
            System.out.println("error en consulta de usuarios.");
        }
        return registroU;
    }

    //eliminacion de usuarios
    public boolean eliminarUsuario(int id)
    {
        try
        {
            PreparedStatement st = null;
            st = con.prepareStatement("DELETE FROM usuario WHERE id = ?");            
            st.setInt(1,id);         
            st.executeUpdate();
            st.close();                        
            return true;
        }
        catch(Exception e)
        {
            System.out.println(e);
            return false;
        }        
    }
    
    //despido de usuarios
    public boolean despidoUsuario(int id)
    {
        try
        {
            PreparedStatement st = null;
            st = con.prepareStatement("update usuario set estado='D' WHERE id = ?");            
            st.setInt(1,id);         
            st.executeUpdate();
            st.close();                        
            return true;
        }
        catch(Exception e)
        {
            System.out.println(e);
            return false;
        }        
    }
    
    //funcion para ingresar cliente a bd
    //funcion para consulta de usuarios
    public ArrayList<cliente> consultarClientes(String busqueda, String tipo){
        ArrayList<cliente> registroC = new ArrayList<cliente>();
        try{
            Statement st = this.con.createStatement();
            ResultSet rs = null;
            
            if(tipo.equalsIgnoreCase("cliente")){
                rs = st.executeQuery("SELECT * FROM cliente;");
            }else{
                rs = st.executeQuery("SELECT * FROM cliente WHERE "+tipo+" LIKE '%"+busqueda+"%';");
            }
            while (rs.next()){
                int id = rs.getInt("id");
                String ced = rs.getString("cedula");
                String noms = rs.getString("nombres");
                String apes = rs.getString("apellidos");
                String correo = rs.getString("correo");
                String dir = rs.getString("direccion");
                String tel = rs.getString("telefono");
                String tip = rs.getString("tipo");
                float sald = rs.getFloat("saldo");
                float desc = rs.getFloat("descuento");
                int edad = rs.getInt("edad");
                String sexo = rs.getString("sexo");
                int id_u = rs.getInt("id_usuario");
                
                cliente cli = new cliente(id, ced, noms, apes, correo, dir, tel, tip.charAt(0), sald, desc, edad, sexo.charAt(0), id_u);
                registroC.add(cli);
            }
            System.out.println("clientes consultados bd.");
        }catch (Exception e){
            System.out.println("error en consulta de clientes bd.");
        }
        return registroC;
    }
    
    
    public boolean ingresarCliente(cliente cli){
        try{
            //Ingreso de Datos de Cliente
            PreparedStatement st=null;
            st = con.prepareStatement("INSERT INTO cliente (cedula,nombres,apellidos,correo,direccion,telefono,tipo,saldo,descuento,edad,sexo,id_usuario) VALUES(?,?,?,?,?,?,?,?,?,?,?,?);");
            st.setString(1,cli.getCedula());
            st.setString(2,cli.getNombres());
            st.setString(3,cli.getApellidos());
            st.setString(4,cli.getCorreo());
            st.setString(5,cli.getDireccion());
            st.setString(6,cli.getTelefono());
            st.setString(7,String.valueOf(cli.getTipo()));
            st.setFloat(8,cli.getSaldo());
            st.setFloat(9,cli.getDescuento());
            st.setInt(10,cli.getEdad());
            st.setString(11,String.valueOf(cli.getSexo()));
            st.setInt(12,cli.getId_usuario());
            
            st.executeUpdate();
            st.close();
            
            System.out.println("Cliente Ingresado a DB...");
            return true;
        }catch (SQLException e){
            System.out.println("Error al ingrear cliente a DB... " +e);
            return false;
        }
    }
    
    //RESTAURANTE
    //consulta de restaurantes
    public ArrayList<restaurante> consultarRestaurante(String busqueda, String tipo){
        ArrayList<restaurante> registroR = new ArrayList<restaurante>();
        try{
            Statement st = this.con.createStatement();
            ResultSet rs = null;
            
            if(tipo.equalsIgnoreCase("restaurante")){
                rs = st.executeQuery("SELECT * FROM restaurante;");
            }else{
                rs = st.executeQuery("SELECT * FROM restaurante WHERE "+tipo+" LIKE '%"+busqueda+"%';");
            }
            
            while (rs.next()){
                int id = rs.getInt("id");
                String nom = rs.getString("nombre");
                String ubi = rs.getString("ubicacion");
                String desc = rs.getString("descripcion");
                int cap = rs.getShort("capacidad");
                String hora = rs.getString("horario");
                int punt = rs.getShort("puntuacion");
                
                restaurante rest = new restaurante(id, nom, ubi, desc, cap, hora, punt);
                registroR.add(rest);
            }
            System.out.println("restaurantes consultados.");
        }catch (SQLException e){
            System.out.println("error en consulta de restaurantes."+e);
        }
        return registroR;
    }
    
    //ingreso de restaurantes
    public boolean ingresarRestaurante(restaurante r){
        try{
            PreparedStatement st=null;
            st = con.prepareStatement("insert into restaurante (nombre,ubicacion,descripcion,capacidad,horario,puntuacion) values (?,?,?,?,?,?);");
            st.setString(1,r.getNombre());
            st.setString(2,r.getUbicacion());
            st.setString(3,r.getDescripcion());
            st.setInt(4,r.getCapacidad());
            st.setString(5,r.getHorario());
            st.setInt(6,r.getPuntuacion());
            
            st.executeUpdate();
            st.close();
            
            System.out.println("Se ingreso el restaurante exitosamente...");
            return true;
        }catch (SQLException e){
            System.out.println("Error al ingresar el restaurante\n"+e);
            return false;
        }
    }
    
    //funcion para obtener datos de restaurante por id
    public restaurante obtenerRestaurante(int id){
        PreparedStatement st = null;
        ResultSet rs = null;
        restaurante rest = new restaurante();
        try
        {            
            st = con.prepareStatement("SELECT * FROM restaurante WHERE id = ?;");
            st.setInt(1,id);         
            rs = st.executeQuery();            
            if(rs.next()){
                String nom = rs.getString("nombre");
                String ubi = rs.getString("ubicacion");
                String desc = rs.getString("descripcion");
                int cap = rs.getInt("cantidad");
                String hora = rs.getString("horario");
                int puntos = rs.getInt("puntuacion");
                rest = new restaurante(nom, ubi, desc, cap, hora, puntos);
            } 
            rs.close();
            st.close();
        }
        catch(SQLException e){
            System.out.println(e);
        }    
        return rest;
    }
    
    //funcion para modificar restaurantes
    public boolean modificarRestaurante(restaurante r1){
        try{
            int idc = r1.getId();
            System.out.println("id de restaurante: "+idc);
            PreparedStatement st2 = null;
            st2 = con.prepareStatement("update restaurante set nombre = ?, ubicacion = ?, descripcion = ?, capacidad = ?, horario = ?, puntuacion = ? where id = ?;");
            st2.setString(1,r1.getNombre());
            st2.setString(2,r1.getUbicacion());
            st2.setString(3,r1.getDescripcion());
            st2.setInt(4,r1.getCapacidad());
            st2.setString(5,r1.getHorario());
            st2.setInt(6,r1.getPuntuacion());
            st2.setInt(7,r1.getId());
            
            st2.executeUpdate();
            st2.close();
            System.out.println("modificacion de restaurante exitosa");
            return true;
        }
        catch(SQLException e){
            System.out.println(e);
            return false;
        }
    }

    public boolean eliminarRestaurante(int id_rest){
        try{
            PreparedStatement st = null;
            st = con.prepareStatement("DELETE FROM restaurante WHERE id = ?;");
            st.setInt(1,id_rest);
            st.executeUpdate();
            st.close();
            System.out.println("Se elimino el registro.-");
            return true;
        }catch(SQLException e){
            System.out.println("Error al eliminar el registro."+e);
            return false;
        }        
    }
    
    //PRODUCTOS
    
    public ArrayList<producto> consultarProducto(String busqueda, String tipo){
        ArrayList<producto> registroP = new ArrayList<producto>();
        try{
            Statement st = this.con.createStatement();
            ResultSet rs = null;
            if(tipo.equalsIgnoreCase("producto")){
                rs = st.executeQuery("SELECT * FROM producto;");
            }else{
                rs = st.executeQuery("SELECT * FROM producto WHERE "+tipo+"="+busqueda+";");
            }
            while (rs.next()){
                int id = rs.getInt("id");
                String nom = rs.getString("nombre");
                String desc = rs.getString("descripcion");
                String tip = rs.getString("tipo");
                float precio = rs.getFloat("precio");
                java.sql.Date cal = rs.getDate("calendario");
                int idr = rs.getInt("id_restaurante");
                
                producto p = new producto(id, nom, desc, tip, precio, cal, idr);
                registroP.add(p);
            }
            System.out.println("productos consultados.");
        }catch (SQLException e){
            System.out.println("error en consulta de productoss.\n"+e);
        }
        return registroP;
    }
    
    public boolean ingresarProducto(producto p){
        try{
            
            PreparedStatement st=null;
            st = con.prepareStatement("insert into producto (nombre,descripcion,tipo,precio,calendario,id_restaurante) VALUES (?,?,?,?,?,?);");
            st.setString(1,p.getNombre());
            st.setString(2,p.getDescripcion());
            st.setString(3,p.getTipo());
            st.setFloat(4,p.getPrecio());
            st.setDate(5, (Date) p.getCalendario());
            st.setInt(6,p.getId_restaurante());
            
            st.executeUpdate();
            st.close();
            
            System.out.println("Se ingreso el producto exitosamente...");
            
            return true;
        }catch (Exception e){
            System.out.println("Error al ingresar el producto BD\n"+e);
            return false;
        }
    }
    
    //DETALLE ORDEN
    public boolean ingresarDetalleOrden(detalleOrden d){
        try{
            PreparedStatement st=null;
            st = con.prepareStatement("INSERT INTO detalle_orden (cantidad,precio_unitario,precio_total,id_producto,id_orden) VALUES (?,?,?,?,?);");
            st.setInt(1,d.getCantidad());
            st.setFloat(2,d.getPrecio_unitario());
            st.setFloat(3,d.getPrecio_total());
            st.setInt(4,d.getId_prod());
            st.setInt(5,d.getId_orden());
            
            st.executeUpdate();
            st.close();
            
            System.out.println("Se ingreso de detalle orden exitos0...");
            
            return true;
        }catch (SQLException e){
            System.out.println("Error al ingresar el dettalle orden BD\n"+e);
            return false;
        }
    }
    
    public int numeroUltimaOrden(){
        int ultimo_registro = 0;
        try{
        //Statement st = this.con.createStatement();
        Statement st = this.con.createStatement();
        ResultSet rs = st.executeQuery("SELECT id FROM orden ORDER BY id DESC LIMIT 1;");
        if(rs.next()){ ultimo_registro = rs.getInt("id"); }
        System.out.println("ultimo orden id = "+ultimo_registro);
        }catch(SQLException e){
            System.out.println("error al obtener ultimo id de orden. "+e);
        }
        return ultimo_registro;
    }
    
    //ORDEN
    
    public boolean ingresarOrden(orden ord){
        try{
            PreparedStatement st=null;
            st = con.prepareStatement("INSERT INTO orden (numero,fecha,descripcion,subtotal,iva_cero,iva,total,id_cliente) VALUES(?,?,?,?,?,?,?,?);");
            st.setInt(1,ord.getNumero());
            st.setString(2,ord.getFecha());
            st.setString(3,ord.getDescripcion());
            st.setFloat(4,ord.getSubtotal());
            st.setFloat(5,ord.getIva_cero());
            st.setFloat(6,ord.getIva());
            st.setFloat(7,ord.getTotal());
            st.setInt(8,ord.getId_cliente());
            st.executeUpdate();
            st.close();
            System.out.println("Se ingreso orden con exito...");
            return true;
        }catch (Exception e){
            System.out.println("Error al ingresar la orden BD\n"+e);
            return false;
        }
    }
    
    public boolean cobroCliente(int id_cliente, float nuevo_saldo){
        try{
            PreparedStatement st = null;
            st = con.prepareStatement("update cliente set saldo = ? where id = ?;");
            st.setFloat(1,nuevo_saldo);
            st.setInt(2,id_cliente);
            st.executeUpdate();
            st.close();
            System.out.println("cobro correcto, nuevo saldo: "+nuevo_saldo+" cliente:"+id_cliente);
        }catch (SQLException e){
            System.out.println("Error al cobrar cliente - bd "+ e);
        }
        return false;
    }
    
    public String obtenerNombreCliente(int id_cliente){
        String nombre_cliente = "";
        try{
        //Statement st = this.con.createStatement();
        
        Statement st = this.con.createStatement();
        ResultSet rs = null;
            
        rs = st.executeQuery("SELECT * FROM cliente where id = "+id_cliente+";");
        if(rs.next()){
            String nom = rs.getString("nombres");
            String ape = rs.getString("apellidos");
            nombre_cliente.concat(nom+" "+ape);
            System.out.println("nombre obtenido: "+nombre_cliente);
        }
        }catch(SQLException e){
            System.out.println("error al obtener nombre cliente bd. "+e);
        }
        return nombre_cliente.toString();
    }
    
    public ArrayList<orden> consultarOrdenes(String busqueda, String tipo){
        ArrayList<orden> registro = new ArrayList<orden>();
        try{
            Statement st = this.con.createStatement();
            ResultSet rs = null;
            if(tipo.equalsIgnoreCase("orden")){
                rs = st.executeQuery("SELECT * FROM orden;");
            }else{
                rs = st.executeQuery("SELECT * FROM orden WHERE "+tipo+"="+busqueda+";");
            }
            while (rs.next()){
                int id = rs.getInt("id");
                String fecha = rs.getString("fecha");
                String desc = rs.getString("descripcion");
                float subt = rs.getFloat("subtotal");
                float iva0 = rs.getFloat("iva_cero");
                float iva12 = rs.getFloat("iva");
                float total = rs.getFloat("total");
                int cli = rs.getInt("id_cliente");
                
                orden ord = new orden(id, id, fecha, desc, subt, iva0, iva12, total, cli);
                registro.add(ord);
            }
            System.out.println("ordenes consultadas.");
        }catch (SQLException e){
            System.out.println("error en consulta de ordenes.\n"+e);
        }
        return registro;
    }
    
    //FUNCIONES DE PROYECTO ANTERIOR
    /*
    public boolean esUsuarioValido(Usuario u)
    {        
        boolean resultado = false;
        ResultSet rs = null;                       
        PreparedStatement st = null;
        try
        {            
            st = con.prepareStatement("SELECT * FROM usuario WHERE cuenta = ? AND clave = ? AND estado = ?");            
            st.setString(1,u.getCuenta());         
            st.setString(2,u.getClave());
            st.setString(3,"activo");
            rs = st.executeQuery();            
            if(rs.next()){
                u.setRol(rs.getString("rol"));
                resultado = true;
            } 
            rs.close();
            st.close();
        }
        catch(Exception e){
            System.out.println(e);
            resultado = false;
        }           
     return resultado; 
    }
    
    public String obtenerNombreUsuario(Usuario u){
        String nombre = "";
        PreparedStatement st = null;
        ResultSet rs = null;
        try
        {            
            st = con.prepareStatement("SELECT * FROM usuario WHERE cuenta = ?");            
            st.setString(1,u.getCuenta());         
            rs = st.executeQuery();            
            if(rs.next()){
                nombre = rs.getString("nombre");
                //u.getId(Integer.parseInt(rs.getString("id"));
                //u.setRol(rs.getString("nombre"));
            } 
            rs.close();
            st.close();
        }
        catch(Exception e){
            System.out.println(e);
        }    
        System.out.println("el nombre del usuario activo es: "+nombre);
        return nombre;
    }
    
    public Usuario obtenerDatosUsuario(String cuenta){
        PreparedStatement st = null;
        ResultSet rs = null;
        Usuario user = new Usuario();
        try
        {            
            st = con.prepareStatement("SELECT * FROM usuario WHERE cuenta = ?");            
            st.setString(1,cuenta);         
            rs = st.executeQuery();            
            if(rs.next()){
                int id = Integer.parseInt(rs.getString("id"));
                String nombre = rs.getString("nombre");
                String cuentau = rs.getString("cuenta");
                String pass = rs.getString("clave");
                user = new Usuario(id, nombre, cuentau, pass);
            } 
            rs.close();
            st.close();
        }
        catch(Exception e){
            System.out.println(e);
        }    
        //System.out.println("el nombre del usuario activo es: "+id);
        return user;
    }
    
    public boolean cambiarClaveUsuario(Usuario u){
        try{
            String id = String.valueOf(u.getId());
            System.out.println("id del usuario para cambio clave: "+id);
            PreparedStatement st = con.prepareStatement("update usuario set clave = ? where id = "+id);
            st.setString(1,u.getClave());
            
            return true;
        }catch (Exception e){
            return false;
        }
    }
    
    //funcion para ingresar cliente a bd
    public boolean ingresarCliente(Cliente cliente){
        try{
            //Ingreso de Datos de Cliente
            PreparedStatement st=null;
            st = con.prepareStatement("INSERT INTO cliente (cedula,nombre,apellido,correo,tipo,edad,fecha_nacimiento,sexo,direccion,telefono) VALUES(?,?,?,?,?,?,?,?,?,?)");
            st.setString(1,cliente.getCedula());
            st.setString(2,cliente.getNombres());
            st.setString(3,cliente.getApellidos());
            st.setString(4,cliente.getCorreo());
            st.setString(5,cliente.getTipo());
            st.setInt(6,cliente.getEdad());
            st.setString(7,cliente.getFecha_nacimiento());
            st.setString(8,cliente.getSexo());
            //st.setString(9,cliente.getEstado());
            st.setString(9,cliente.getDireccion());
            st.setString(10,cliente.getTelefono());
            
            st.executeUpdate();
            st.close();
            
            System.out.println("Cliente Ingresado a DB...");
            return true;
        }catch (Exception e){
            System.out.println("Error al ingrear cliente a DB...");
            return false;
        }
    }
    
    //funcion para modificar clientes
    public boolean modificarCliente(Cliente cl)
    {
        try
        {
            int idc = cl.getId();
            System.out.println("id de usuario: "+idc);
            PreparedStatement st2 = null;
            st2 = con.prepareStatement("update cliente set nombre = ?, apellido = ?, correo = ?, tipo = ?, edad = ?, fecha_nacimiento = ?, sexo = ?, estado = 1 , direccion = ?, telefono = ? where id = ?");
            st2.setString(1,cl.getNombres());
            st2.setString(2,cl.getApellidos());
            st2.setString(3,cl.getCorreo());
            st2.setString(4,cl.getTipo());
            st2.setInt(5,cl.getEdad());
            st2.setString(6,cl.getFecha_nacimiento());
            st2.setString(7,cl.getSexo());
            st2.setString(8,cl.getDireccion());
            st2.setString(9,cl.getTelefono());            
            st2.setInt(10,cl.getId());            
            
            st2.executeUpdate();
            st2.close();
            System.out.println("modificacion de cliente exitosa");
            return true;
        }
        catch(Exception e)
        {
            System.out.println(e);
            return false;
        }
        
    }
        
    //funcion para consultar clientes en bd
    public ArrayList<Cliente> consultarClientes(String busqueda, String tip){
        ArrayList<Cliente> registroC = new ArrayList<Cliente>();
        try{
            Statement st = this.con.createStatement();
            ResultSet rs = null;
            
            if (tip.equalsIgnoreCase("cliente")){
                rs = st.executeQuery("SELECT * FROM cliente;");
            }else if (tip.equalsIgnoreCase("huesped")){
                rs = st.executeQuery("select * from cliente where tipo = huesped;");
            }else if (tip.equalsIgnoreCase("cedula")){
                rs = st.executeQuery("select * from cliente where cedula = "+tip+";");
            }else{
                rs = st.executeQuery("SELECT * FROM cliente WHERE "+tip+" LIKE '%"+busqueda+"%';");
            }
            while(rs.next())
            {
                int id = rs.getInt("id");
                String cedula = rs.getString("cedula");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String correo = rs.getString("correo");
                String tipo = rs.getString("tipo");
                int edad = rs.getInt("edad");
                String fecha_nacimiento = rs.getString("fecha_nacimiento");
                String sexo = rs.getString("sexo");
                //int estado = rs.getInt("estado");
                String direccion = rs.getString("direccion");
                String telefono = rs.getString("telefono");
                //creacion de cliente desde consulta
                Cliente cli = new Cliente(id,cedula,nombre,apellido,correo,tipo,edad,fecha_nacimiento,sexo,direccion,telefono);
                registroC.add(cli);
            }
            System.out.println("cliente consultado");
        }catch(Exception e){
            System.out.println("error en consulta de cliente db");
        }
        return (registroC);
    }
    
    //funcion para eliminar clientes
    public boolean eliminarCliente(int id)
    {
        try
        {
            PreparedStatement st = null;
            st = con.prepareStatement("DELETE FROM cliente WHERE id = ?");            
            st.setInt(1,id);         
            st.executeUpdate();
            st.close();                        
            return true;
        }
        catch(Exception e)
        {
            System.out.println(e);
            return false;
        }        
    } 
    
    public ArrayList<Usuario> consultarUsuarios(){
        ArrayList<Usuario> registroU = new ArrayList<Usuario>();
        try{
            Statement st = this.con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM usuario");
            while (rs.next()){
                int id = rs.getInt("id");
                String cedula = rs.getString("cedula");
                String nombre = rs.getString("nombre");
                String rol = rs.getString("rol");
                String estado = rs.getString("estado");
                String cuenta = rs.getString("cuenta");
                String clave = rs.getString("clave");
                
                Usuario usr = new Usuario(id,cedula,nombre,rol,estado,cuenta,clave);
                registroU.add(usr);
            }
            System.out.println("usuarios consultados.");
        }catch (Exception e){
            System.out.println("error en consulta de usuarios.");
        }
        return registroU;
    }
    
    //funcion para ingresar ingredientes
    public boolean ingresarIngrediente(Ingrediente i){
        try{
            //Ingreso de Datos de Cliente
            PreparedStatement st=null;
            st = con.prepareStatement("insert into ingrediente (nombre,descripcion,tipo,costo_porcion,cantidad) values (?,?,?,?,?);");
            st.setString(1,i.getNombre());
            st.setString(2,i.getDescripcion());
            st.setString(3,i.getTipo());
            st.setFloat(4,i.getCosto_porcion());
            st.setInt(5,i.getCantidad());
            
            st.executeUpdate();
            st.close();
            
            System.out.println("Ingrediente Ingresado a DB...");
            return true;
        }catch (Exception e){
            System.out.println("Error al ingrear ingrediente a DB...");
            return false;
        }
    }

    //funcion para consultar ingredientes
    public ArrayList<Ingrediente> consultarIngredientes(String busqueda, String tip){
        ArrayList<Ingrediente> registroI = new ArrayList<Ingrediente>();
        try{
            Statement st = this.con.createStatement();
            ResultSet rs = null;
            
            if (tip.equalsIgnoreCase("ingrediente")){
                rs = st.executeQuery("SELECT * FROM ingrediente;");
            }else if (tip.equalsIgnoreCase("nombre")){
                rs = st.executeQuery("select * from ingrediente where nombre = "+tip+";");
            }else{
                rs = st.executeQuery("SELECT * FROM ingrediente WHERE "+tip+" LIKE '%"+busqueda+"%';");
            }
            while(rs.next())
            {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                String tipo = rs.getString("tipo");
                float costop = rs.getFloat("costo_porcion");
                int cantidad = rs.getInt("cantidad");
                
                Ingrediente ing = new Ingrediente(id, nombre, descripcion, tipo, costop, cantidad);
                registroI.add(ing);
            }
            System.out.println("ingredientes consultados");
        }catch(Exception e){
            System.out.println("error en consulta de ingredientes db");
        }
        return (registroI);
    }
    
    //funcion para modificar ingredientes
    public boolean modificarIngrediente(Ingrediente ing)
    {
        try
        {
            int id = ing.getId();
            System.out.println("id de ingrediente: "+id);
            PreparedStatement st2 = null;
            st2 = con.prepareStatement("update ingrediente set nombre = ?, descripcion = ?, tipo = ?, costo_porcion = ?, cantidad = ? where id = ?");
            st2.setString(1,ing.getNombre());
            st2.setString(2,ing.getDescripcion());
            st2.setString(3,ing.getTipo());
            st2.setFloat(4,ing.getCosto_porcion());
            st2.setInt(5,ing.getCantidad());        
            st2.setInt(6,ing.getId());
            
            st2.executeUpdate();
            st2.close();
            System.out.println("modificacion de ingrediente exitosa");
            return true;
        }
        catch(Exception e)
        {
            System.out.println(e);
            return false;
        }
    }
    
    //funcion para eliminar ingredientes
    public boolean eliminarIngrediente(int id)
    {
        try
        {
            PreparedStatement st = null;
            st = con.prepareStatement("DELETE FROM ingrediente WHERE id = ?");            
            st.setInt(1,id);         
            st.executeUpdate();
            st.close();                        
            return true;
        }
        catch(Exception e)
        {
            System.out.println(e);
            return false;
        }        
    } 
    
    //FUNCIONES DE INGRESO DE TRANSACCIONES: PRODUCTO Y PLATO
    public boolean ingresarProducto(Producto p){
        try{
            //Ingreso de Datos de producto
            PreparedStatement st=null;
            st = con.prepareStatement("insert into producto (nombre,descripcion,tamaño,precio,tipo,ids_ingredientes,ventas) values (?,?,?,?,?,?,?);");
            //query para ingresar manual
            //  insert into producto (id,nombre,descripcion,tamaño,precio,tipo) values (2,"plato2","platoss","peq",3,"caldo")
            st.setString(1,p.getNombre());
            st.setString(2,p.getDescripcion());
            st.setString(3,p.getTamaño());
            st.setFloat(4,p.getPrecio());
            st.setString(5,p.getTipo());
            st.setString(6,p.getIngredientes());
            st.setInt(7,p.getVentas());
            
            st.executeUpdate();
            st.close();
            
            System.out.println("Producto Ingresado a DB...");
            return true;
        }catch (Exception e){
            System.out.println("Error al ingrear producto a DB...");
            return false;
        }
    }
    
    public boolean ingresarPlato(int idpr,int iding){
        try{
            //Ingreso de Datos de producto
            PreparedStatement st=null;
            st = con.prepareStatement("insert into plato (id_producto,id_ingrediente) values (?,?);");
            //query para ingresar manual
            //  insert into producto (id,nombre,descripcion,tamaño,precio,tipo) values (2,"plato2","platoss","peq",3,"caldo")
            st.setString(1,String.valueOf(idpr));
            st.setString(2,String.valueOf(iding));
            
            st.executeUpdate();
            st.close();
            
            System.out.println("Plato Ingresado a DB...");
            return true;
        }catch (Exception e){
            System.out.println("Error al ingrear producto a DB...");
            return false;
        }
    }
    
    //  FUNCIONES PARA MANEJO DE PRODUCTOS
    public ArrayList<Ingrediente> cargarIngredientes_Producto(String busqueda, String tip){
        ArrayList<Ingrediente> registroI = new ArrayList<Ingrediente>();
        try{
            Statement st = this.con.createStatement();
            ResultSet rs = null;
            
            if (tip.equalsIgnoreCase("ingrediente")){
                rs = st.executeQuery("SELECT * FROM ingrediente;");
            }else if (tip.equalsIgnoreCase("nombre")){
                rs = st.executeQuery("select * from ingrediente where nombre = "+tip+";");
            }else{
                rs = st.executeQuery("SELECT * FROM ingrediente WHERE "+tip+" LIKE '%"+busqueda+"%';");
            }
            while(rs.next())
            {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                String tipo = rs.getString("tipo");
                float costop = rs.getFloat("costo_porcion");
                int cantidad = rs.getInt("cantidad");
                
                Ingrediente ing = new Ingrediente(id, nombre, descripcion, tipo, costop, cantidad);
                registroI.add(ing);
            }
            System.out.println("ingredientes consultados");
        }catch(Exception e){
            System.out.println("error en consulta de ingredientes db");
        }
        return (registroI);
    }
    
    public ArrayList<Producto> consultarProductos(String busqueda, String tip){
        ArrayList<Producto> registroP = new ArrayList<Producto>();
        try{
            Statement st = this.con.createStatement();
            ResultSet rs = null;
            
            if (tip.equalsIgnoreCase("producto")){
                rs = st.executeQuery("SELECT * FROM producto;");
            }else if (tip.equalsIgnoreCase("nombre")){
                rs = st.executeQuery("select * from producto where nombre = "+tip+";");
            }else{
                rs = st.executeQuery("SELECT * FROM producto WHERE "+tip+" LIKE '%"+busqueda+"%';");
            }
            while(rs.next())
            {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                String tamaño = rs.getString("tamaño");
                float precio = rs.getFloat("precio");
                String tipo = rs.getString("tipo");
                String ingredientes = rs.getString("ids_ingredientes");
                int ventas = rs.getInt("ventas");
                
                Producto prod = new Producto(id, nombre, descripcion, tamaño, precio, tipo, ingredientes, ventas);
                registroP.add(prod);
            }
            System.out.println("productos consultados");
        }catch(Exception e){
            System.out.println("error en consulta de productos db");
        }
        return (registroP);
    }
    
    public boolean eliminarProducto(int id)
    {
        try
        {
            PreparedStatement st = null;
            st = con.prepareStatement("DELETE FROM producto WHERE id = ?");            
            st.setInt(1,id);         
            st.executeUpdate();
            st.close();                        
            return true;
        }
        catch(Exception e)
        {
            System.out.println(e);
            return false;
        }        
    } 
    
    // FUNCIONES PARA GENERAR ORDENES
    
    public Cliente cargarClienteOrden(String cedula){
        Cliente cli = new Cliente();
        try {
            Statement st = this.con.createStatement();
            ResultSet rs = null;
            
            rs = st.executeQuery("select * from cliente where cedula = "+cedula);
            while(rs.next())
            {
                //int id = rs.getInt("id");
                //String ced = rs.getString("cedula");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String correo = rs.getString("correo");
                //String tipo = rs.getString("tipo");
                //int edad = rs.getInt("edad");
                //String fecha_nacimiento = rs.getString("fecha_nacimiento");
                //String sexo = rs.getString("sexo");
                //int estado = rs.getInt("estado");
                String direccion = rs.getString("direccion");
                String telefono = rs.getString("telefono");
                //creacion de cliente desde consulta
                cli = new Cliente(nombre,apellido,correo,direccion,telefono);
                //cliente.add(cli);
                
                System.out.println("cliente obtenido");
                System.out.println(cli);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return cli;
    }
    
    public Producto cargarProductoOrden(String id){
        Producto prod = new Producto();
        int idpr = Integer.parseInt(id);
        try {
            Statement st = this.con.createStatement();
            ResultSet rs = null;
            
            rs = st.executeQuery("select * from producto where id = "+id);
            while(rs.next())
            {
                //String ced = rs.getString("cedula");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String correo = rs.getString("correo");
                //String tipo = rs.getString("tipo");
                //int edad = rs.getInt("edad");
                //String fecha_nacimiento = rs.getString("fecha_nacimiento");
                //String sexo = rs.getString("sexo");
                //int estado = rs.getInt("estado");
                String direccion = rs.getString("direccion");
                String telefono = rs.getString("telefono");
                float precio = rs.getFloat("");
                //creacion de cliente desde consulta
                prod = new Producto(idpr,nombre,precio);
                //cliente.add(cli);
                
                System.out.println("cliente obtenido");
                System.out.println(prod);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return prod;
    }
    
    public boolean ingresarOrden(Orden ord){
        try{
            //Ingreso de Datos de orden
            PreparedStatement st=null;
            st = con.prepareStatement("insert into orden (fecha,usuario,cliente,estado,cantidad,productos,subtotal,descuento,subtotal_cero,iva,total) values (?,?,?,?,?,?,?,?,?,?,?);");
            
            st.setString(1,ord.getFecha());
            st.setString(2,ord.getUsuario());
            st.setString(3,ord.getCliente());
            st.setString(4,ord.getEstado());
            st.setString(5,ord.getCantidad());
            st.setString(6,ord.getProductos());
            st.setFloat(7,ord.getSub_total());
            st.setFloat(8,ord.getDescuento());
            st.setFloat(9,ord.getSubcero());
            st.setFloat(10,ord.getIva());
            st.setFloat(11,ord.getTotal());
            
            st.executeUpdate();
            st.close();
            
            System.out.println("Orden Ingresada a DB...");
            return true;
        }catch (Exception e){
            System.out.println("Error al ingresar orden a DB...");
            return false;
        }
    }
    
    /*
    public boolean ingresarUniversidad(Universidad u)
    {
        try
        {
                PreparedStatement st = null;
                st = con.prepareStatement("INSERT INTO universidad(nombre,direccion,ciudad,telefono) VALUES(?,?,?,?) ");            
                st.setString(1,u.getNombre());
                st.setString(2,u.getDireccion());
                st.setString(3,u.getCiudad());
                st.setString(4,u.getTelefono());
                st.executeUpdate();
                st.close();                        
            return true;
        }
        catch(Exception e)
        {
            System.out.println(e);
            return false;
        }        
    }
    
    public ArrayList<Universidad> consultarUniversidades(String descripcion, String tipo)
    {
        ArrayList<Universidad> universidades = new ArrayList<Universidad>();

        try
        {
            Statement st = this.con.createStatement();
            ResultSet rs = null;  
            
            if(tipo.equalsIgnoreCase("TODOS")){
                rs = st.executeQuery("SELECT * FROM universidad;");
            }else if(tipo.equalsIgnoreCase("ID")){
                rs = st.executeQuery("SELECT * FROM universidad WHERE id = "+descripcion+";");
            }else{
                rs = st.executeQuery("SELECT * FROM universidad WHERE "+tipo+" LIKE '%"+descripcion+"%';");
            }            
                        
            while(rs.next())
            {
                int id              = rs.getInt("id");
                String nombre       = rs.getString("nombre");
                String direccion    = rs.getString("direccion");
                String ciudad       = rs.getString("ciudad");
                String telefono     = rs.getString("telefono");
                Universidad u = new Universidad(id, nombre, direccion, ciudad, telefono);                
                universidades.add(u);
            }
        }
        catch(Exception e){System.out.println(e);}
     return(universidades);
    } 
    
    
    public boolean modificarUniversidad(Universidad u)
    {
        try
        {
            PreparedStatement st = null;
            st = con.prepareStatement("UPDATE universidad SET nombre = ?, ciudad = ?, direccion = ?, telefono = ? WHERE id = ?");                        
            st.setString(1,u.getNombre());
            st.setString(2,u.getCiudad());
            st.setString(3,u.getDireccion());
            st.setString(4,u.getTelefono());
            st.setInt(5,u.getId());                       
            st.executeUpdate();
            st.close();                        
            return true;
        }
        catch(Exception e)
        {
            System.out.println(e);
            return false;
        }
        
    }   
    
    public boolean eliminarUniversidad(int id)
    {
        try
        {
            PreparedStatement st = null;
            st = con.prepareStatement("DELETE FROM universidad WHERE id = ?");            
            st.setInt(1,id);         
            st.executeUpdate();
            st.close();                        
            return true;
        }
        catch(Exception e)
        {
            System.out.println(e);
            return false;
        }        
    } 
   
    
    public boolean esUsuarioValido(Usuario u)
    {        
        boolean resultado = false;
        ResultSet rs = null;                       
        PreparedStatement st = null;
        try
        {            
            st = con.prepareStatement("SELECT * FROM usuario WHERE id = ? AND clave = md5(?) AND estado = ?");            
            st.setString(1,u.getId());         
            st.setString(2,u.getClave());
            st.setString(3,"A");
            rs = st.executeQuery();            
            if(rs.next()){
                u.setRol(rs.getString("rol").charAt(0));
                resultado = true;
            } 
            
            rs.close();
            st.close();
        }
        catch(Exception e){
            System.out.println(e);
            resultado = false;
        }           
     return resultado; 
    }
    */
    
    
    /*
    //funcion para cargar al sistema datos de los clientes
    public ArrayList<Cliente> cargarClientes() throws Exception , SQLException{
        //recoleccion de datos de clientes en base de datos a arraylist
        Statement st = this.con.createStatement();
        ResultSet rst = st.executeQuery("SELECT * FROM cliente");
        ArrayList<Cliente> listaClientes = new ArrayList<Cliente>();
        while (rst.next()){
            Cliente c = new Cliente(Integer.getInteger(rst.getString("id")),rst.getString("cedula"),rst.getString("nombre"),rst.getString("apellido"),rst.getString("correo"),rst.getString("tipo"),Integer.getInteger(rst.getString("edad")),rst.getString("fecha_nacimiento"),rst.getString("sexo"),rst.getString("estado"),rst.getString("direccion"),rst.getString("telefono"));
            listaClientes.add(c);
        }
        return listaClientes;
    }
    */
    
}
