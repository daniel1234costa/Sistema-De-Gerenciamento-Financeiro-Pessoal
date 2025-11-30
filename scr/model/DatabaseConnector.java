package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {

    // CAMINHO: Na raiz do projeto (mais seguro)
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

            // 2. MÁGICA AQUI: Chama a criação das tabelas toda vez que conecta
            criarTabelas(conexao);

        } catch (SQLException e) {
            System.err.println("Erro conexão: " + e.getMessage());
        }
        return conexao;
    }

    private static void criarTabelas(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            
            // USUARIO
            stmt.execute("CREATE TABLE IF NOT EXISTS Usuario ("
                    + "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "nome VARCHAR(255), "
                    + "email VARCHAR(255) UNIQUE, "
                    + "senha VARCHAR(255), "
                    + "data_nascimento DATE)");

            // CATEGORIA
            stmt.execute("CREATE TABLE IF NOT EXISTS Categoria ("
                    + "idCategoria INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "nomeCategoria VARCHAR(255), "
                    + "status BOOLEAN)");

            // RENDA (Tabela corrigida para o seu código Java)
            stmt.execute("CREATE TABLE IF NOT EXISTS Renda ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "nome VARCHAR(255) NOT NULL, "
                    + "valor NUMERIC(10, 2) NOT NULL, "
                    + "data TEXT NOT NULL, "
                    + "tipo BOOLEAN, "
                    + "id_usuario INTEGER NOT NULL, " // Link obrigatório
                    + "FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE)");

            // DESPESA
            stmt.execute("CREATE TABLE IF NOT EXISTS Despesa ("
                    + "idDespesa INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "nomeDespesa VARCHAR(255), "
                    + "valor NUMERIC(10, 2), "
                    + "data TEXT, "
                    + "idUsuario INTEGER, "
                    + "idCategoria INTEGER, "
                    + "FOREIGN KEY (idUsuario) REFERENCES Usuario(id_usuario), "
                    + "FOREIGN KEY (idCategoria) REFERENCES Categoria(idCategoria))");

            // --- CRIA USUÁRIO DE TESTE (ID 1) PARA NÃO TRAVAR A RENDA ---
            stmt.execute("INSERT OR IGNORE INTO Usuario (id_usuario, nome, email, senha) VALUES (1, 'Admin', 'admin', '123')");

        } catch (SQLException e) {
            System.err.println("Erro tabelas: " + e.getMessage());
        }
    }

    public static void fecharConexao(Connection conn) {
        try { if (conn != null) conn.close(); } catch (SQLException e) {}
    }
}