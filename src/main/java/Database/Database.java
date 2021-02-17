package Database;

import Risorse.Persona;
import java.sql.*;
import java.util.ArrayList;

public class Database {
    
    static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DATABASE = "jdbc:mysql://localhost:3306/Database_TPSIT?user=root&password=Lazzarelli&serverTimezone=Europe/Rome";
    static final String QUERY = "SELECT Alunni.* FROM Alunni";

    ArrayList<Persona> alunni = new ArrayList<>();
    
    public Database() {
        init();
    }
    
    // Getter
    public ArrayList<Persona> getAlunni() {
        return alunni;
    }
        
    // method to connect to database
    public void init (){
        
        try{         
            // connect the database to java
            Class.forName(DRIVER);
            Connection conn = DriverManager.getConnection(DATABASE);
            Statement statement = conn.createStatement();
            
            // recovery all the information from table 'Alunni'
            ResultSet rs = statement.executeQuery(QUERY);
            
            // adding all the object in the array
            while(rs.next()){
                alunni.add(new Persona(rs.getString("Nome"), rs.getString("Cognome")));
            }
        }catch(ClassNotFoundException | SQLException e){
            System.out.println(e);
        }
    }    
}
