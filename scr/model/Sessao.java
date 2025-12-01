package model;

/**
 * Gerencia o estado da sessão do usuário logado na aplicação (Memória).
 * Funciona como uma variável global para saber quem está usando o sistema.
 */
public class Sessao {
    
    // Armazena o ID do usuário logado. É static para ser único em todo o app.
    private static String idUsuarioLogado = null;
    // Opcional: Se quiser guardar o nome para exibir sem ir no banco
    private static String nomeUsuarioLogado = null; 

    // Construtor privado para impedir que alguém faça "new Sessao()"
    private Sessao() {}

    /**
     * Define o usuário logado no sistema.
     * @param idUsuario O ID do usuário que acabou de logar.
     */
    public static void logar(String idUsuario) {
        idUsuarioLogado = idUsuario;
    }
    
    // Sobrecarga opcional se quiser guardar o nome também
    public static void logar(String idUsuario, String nome) {
        idUsuarioLogado = idUsuario;
        nomeUsuarioLogado = nome;
    }

    /**
     * Remove o usuário da sessão (Logout).
     */
    public static void deslogar() {
        idUsuarioLogado = null;
        nomeUsuarioLogado = null;
    }

    /**
     * Retorna o ID do usuário logado para ser usado em cadastros (FK).
     * @return String ID ou null se não houver login.
     */
    public static String getIdUsuarioLogado() {
        return idUsuarioLogado;
    }

    /**
     * Verifica se existe alguém logado.
     * @return true se logado, false se não.
     */
    public static boolean isLogado() {
        return idUsuarioLogado != null;
    }
}