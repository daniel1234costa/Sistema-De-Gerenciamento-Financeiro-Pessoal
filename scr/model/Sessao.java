package model;

public class Sessao {
    
    private static String idUsuarioLogado = null;

    private static String nomeUsuarioLogado = null; 

    private Sessao() {}

    
     
    public static void logar(String idUsuario) {
        idUsuarioLogado = idUsuario;
    }
    
    public static void logar(String idUsuario, String nome) {
        idUsuarioLogado = idUsuario;
        nomeUsuarioLogado = nome;
    }

    
    public static void deslogar() {
        idUsuarioLogado = null;
        nomeUsuarioLogado = null;
    }

    public static String getIdUsuarioLogado() {
        return idUsuarioLogado;
    }

    
    public static boolean isLogado() {
        return idUsuarioLogado != null;
    }
}