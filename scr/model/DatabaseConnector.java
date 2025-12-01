package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {

    // CAMINHO: Na raiz do projeto
    private static final String URL = "jdbc:sqlite:database/financas.db"; 
    
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Erro driver: " + e.getMessage());
        }
    }

    public static Connection conectar() {
        Connection conexao = null;
        try {
            conexao = DriverManager.getConnection(URL);
            
            // 1. Ativa Chaves Estrangeiras
            try (Statement stmt = conexao.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }

            // 2. Cria as tabelas se não existirem
            criarTabelas(conexao);

        } catch (SQLException e) {
            System.err.println("Erro conexão: " + e.getMessage());
        }
        return conexao;
    }

    private static void criarTabelas(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            
          
            stmt.execute("CREATE TABLE IF NOT EXISTS Usuario ("
                    + "id_usuario TEXT PRIMARY KEY, " 
                    + "nome VARCHAR(255), "
                    + "email VARCHAR(255) UNIQUE, "
                    + "senha VARCHAR(255), "
                    + "data_nascimento TEXT)"); 

            // --- CATEGORIA ---
            stmt.execute("CREATE TABLE IF NOT EXISTS Categoria ("
                    + "idCategoria TEXT PRIMARY KEY, " 
                    + "idUsuario TEXT, "               // FK deve ser TEXT para bater com Usuario
                    + "nomeCategoria VARCHAR(255), "
                    + "status BOOLEAN, "
                    + "FOREIGN KEY (idUsuario) REFERENCES Usuario(id_usuario))");

            // --- RENDA ---
            stmt.execute("CREATE TABLE IF NOT EXISTS Renda ("
                    + "id TEXT PRIMARY KEY, "
                    + "nome VARCHAR(255) NOT NULL, "
                    + "valor NUMERIC(10, 2) NOT NULL, "
                    + "data TEXT NOT NULL, "
                    + "tipo BOOLEAN, "
                    + "id_usuario TEXT NOT NULL, " // FK alterada para TEXT
                    + "FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE)");

            // --- DESPESA ---
            stmt.execute("CREATE TABLE IF NOT EXISTS Despesa ("
                    + "idDespesa TEXT PRIMARY KEY, " // Sugiro TEXT para padronizar UUID
                    + "nomeDespesa VARCHAR(255), "
                    + "valor NUMERIC(10, 2), "
                    + "data TEXT, "
                    + "idUsuario TEXT, " // FK alterada para TEXT
                    + "idCategoria TEXT, "
                    + "FOREIGN KEY (idUsuario) REFERENCES Usuario(id_usuario), "
                    + "FOREIGN KEY (idCategoria) REFERENCES Categoria(idCategoria))");

            // --- CRIA USUÁRIO DE TESTE (Adapte se necessário) ---
            // Como agora é TEXT, o '1' será salvo como string "1"
            stmt.execute("INSERT OR IGNORE INTO Usuario (id_usuario, nome, email, senha) VALUES ('1', 'Admin', 'admin', '123')");

        } catch (SQLException e) {
            System.err.println("Erro tabelas: " + e.getMessage());
        }
    }

    public static void fecharConexao(Connection conn) {
        try { if (conn != null) conn.close(); } catch (SQLException e) {}
    }
}