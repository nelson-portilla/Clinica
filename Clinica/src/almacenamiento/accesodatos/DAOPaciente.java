/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package almacenamiento.accesodatos;

/**
 *
 * @author Nelson
 */
import java.sql.*;
import proceso.Paciente;
public class DAOPaciente {
    private BaseDatos db;
    Connection conn ;
    /**
     * constructor, inicializa los atributos.
     */
    public DAOPaciente(Connection con){  db=new BaseDatos();conn=con;  }
    
    /**
     * Metodo que permite realizar la conexion a la base de datos
     */
    public void connectDB(){
        conn = db.getConnetion();
    }
    public Connection getConn(){
        return conn;
    }
    
    public int CrearPaciente(Paciente pac){
        String sql_save;
        int numRows=0;
        sql_save="INSERT INTO Paciente VALUES ('" + pac.getIdPaciente()+ 
                "' , '" + pac.getNumSeguridadSocial()+ 
                "', '" + pac.getActividadEconomica()+                   
                "', '"+ pac.getFechaNac()+"' , true)"; ///CONVERTIR DATE
        try{
            Statement sentencia = conn.createStatement();
            numRows = sentencia.executeUpdate(sql_save);           
            return numRows;            
        }
        catch(SQLException e){            
            System.out.println(e); 
            return -2;
        }
        catch(Exception e){ 
            System.out.println("exception dao crear persona");
            System.out.println(e);
        }
        return -1;
    }
    
    public Paciente LeerPaciente(String req){
        Paciente pac= new Paciente();
        String sql_select;
        sql_select="SELECT Paciente.id_paciente, Paciente.num_seguridad_social, Paciente.actividad_economica,Paciente.fecha_nac FROM  Paciente WHERE id_paciente='" + req +  "' and estado=true";
        try{
            System.out.println("consultando en la bd");
            Statement statement = conn.createStatement();
            ResultSet table = statement.executeQuery(sql_select);
            
            while(table.next()){
                //System.out.println("dentro del while");
                pac.setIdPaciente(table.getString(1));
               
                pac.setNumSeguridadSocial(table.getString(2));
                
                pac.setActividadEconomica(table.getString(3));
                
                pac.setFechaNac(table.getString(4));
                System.out.println("ok");
            return pac;
            }            
            
         }
         catch(SQLException e){ System.out.println(e); }
         catch(Exception e){ System.out.println("excepcion del dao"); System.out.println(e); }
        return null;
    }//fin readUser
    
    
    public int ActualizarPaciente(Paciente pac, String cedula){
        String sql_save;
	sql_save="UPDATE Paciente SET num_seguridad_social='"+pac.getNumSeguridadSocial()+
                "', Actividad_economica='"+pac.getActividadEconomica()+
                "', Fecha_nac='"+pac.getFechaNac()+"' WHERE id_paciente='" + pac.getIdPaciente()+ "'";
        try{
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql_save);
            
        }            
        catch(SQLException e){
            System.out.println(e); 
            return -2;
        }
        catch(Exception e){ 
            System.out.println(e);
            return -1;
        }
        return 1;
    }//fin updateUser
    
    public int EliminarPaciente(String cedula){	
        String sql_save;
        //sql_save="UPDATE usuario SET estado=false WHERE cedula='" + cedula + "'";
        sql_save="UPDATE Paciente SET estado=false WHERE id_paciente='"+cedula+"'";
        try{
            Statement statement = conn.createStatement();

            return statement.executeUpdate(sql_save);            
            
        }
        catch(SQLException e){
            System.out.println(e);
            return -1;
        }
        catch(Exception e){ 
            System.out.println(e);
            
        }
        return -2;
        
    }
    public void closeConectionDB(){
        db.closeConection(db.getConnetion());
    }
    
    public int CostoPromedioCita (String id, String mes,String ano, int param ){	
        String sql_consult;
        String fecha_ini, fecha_fin;
        fecha_ini = ano +"-01-01";
        fecha_fin = ano+ "-12-31";
        String dia_final;
        int mes_entero = Integer.parseInt(mes);
        int resultado=0;
        switch (mes_entero)
        {
            case 2 : dia_final = "28"; break;
            case 4 : dia_final = "30"; break;
            case 6 : dia_final = "30"; break;
            case 9 : dia_final = "30"; break;
            case 11 : dia_final = "30"; break;
            default : dia_final= "31"; break;
        }
        switch(param)
        {
            case 1 :    fecha_ini = ano +"-"+ mes + "-01";
                        fecha_fin = ano+ "-" + mes + "-" + dia_final; break;
            
            case 2:     fecha_ini = ano +"-01-01";
                        fecha_fin = ano+ "-12-31"; break;
        }
        
        sql_consult = "select avg(costo) from cita where id_paciente = '"+id+"' and  fecha between '"+fecha_ini+"' and '"+fecha_fin+"'";
        // System.out.println(sql_consult);
        
        try{
            
             Statement statement = conn.createStatement();
            ResultSet table = statement.executeQuery(sql_consult);
            int i = 0;            
            while (table.next ())
            {
                resultado = table.getInt(1);                              
            }
            
           
            System.out.println(resultado);
            
        
            
        }
        catch(SQLException e){
            System.out.println(e);
        }
        catch(Exception e){ 
            System.out.println(e);
        }
        return resultado;
        
    }
    
    
    public int CostoPromedioMedicamentos (String id, String mes,String ano, int param ){	
        String sql_consult;
        String fecha_ini, fecha_fin;
        fecha_ini = ano +"-01-01";
        fecha_fin = ano+ "-12-31";
        String dia_final;
        int mes_entero = Integer.parseInt(mes);
        int resultado=0;
        switch (mes_entero)
        {
            case 2 : dia_final = "28"; break;
            case 4 : dia_final = "30"; break;
            case 6 : dia_final = "30"; break;
            case 9 : dia_final = "30"; break;
            case 11 : dia_final = "30"; break;
            default : dia_final= "31"; break;
        }
        switch(param)
        {
            case 1 :    fecha_ini = ano +"-"+ mes + "-01";
                        fecha_fin = ano+ "-" + mes + "-" + dia_final; break;
            
            case 2:     fecha_ini = ano +"-01-01";
                        fecha_fin = ano+ "-12-31"; break;
        }
        
        sql_consult = "select avg(precio) from medicacion where cod_historia = (select cod_historia from historia where id_paciente='"+id+"') and  fecha between '"+fecha_ini+"' and '"+fecha_fin+"'";
        // System.out.println(sql_consult);
        
        try{
            
             Statement statement = conn.createStatement();
            ResultSet table = statement.executeQuery(sql_consult);
            int i = 0;            
            while (table.next ())
            {
                resultado = table.getInt(1);                              
            }
            
           
            System.out.println(resultado);
            
        
            
        }
        catch(SQLException e){
            System.out.println(e);
        }
        catch(Exception e){ 
            System.out.println(e);
        }
        return resultado;
        
    }
    
}
