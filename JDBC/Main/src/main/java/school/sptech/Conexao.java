package school.sptech;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
   private String url = "jdbc:mysql://localhost:3306/roadvizor";
   private String usuario_db = "roadvizor_user";
   private String senha_db = "sptech";

   private Connection connection;

    public Connection getConnection() throws SQLException {
        try {
            if(connection == null){
                connection = DriverManager.getConnection(url,
                        usuario_db,
                        senha_db);
            };

            return connection;

        } catch (SQLException exception){
            throw new RuntimeException("Erro ao conectar com o banco", exception);
        }
    }
}
