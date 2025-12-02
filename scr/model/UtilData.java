package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilData {

    private static final SimpleDateFormat FORMAT_USER = new SimpleDateFormat("dd/MM/yyyy");

    private static final SimpleDateFormat FORMAT_DB = new SimpleDateFormat("yyyy-MM-dd");

    static {
        FORMAT_USER.setLenient(false);
        FORMAT_DB.setLenient(false);
    }

    
    public static String formatarData(Date data) {
        if (data == null) return null;
        return FORMAT_DB.format(data);
    }

    
    public static Date parseDataUsuario(String dataString) {
        if (dataString == null || dataString.isEmpty()) return null;

        try {
            return FORMAT_USER.parse(dataString);
        } catch (ParseException e) {
            System.err.println("Erro ao converter data do usuário: " + e.getMessage());
            return null;
        }
    }

    public static Date parseDataBanco(String dataString) {
        if (dataString == null || dataString.isEmpty()) return null;

        try {
            return FORMAT_DB.parse(dataString);
        } catch (ParseException e) {
            System.err.println("Erro ao converter data do banco: " + e.getMessage());
            return null;
        }
    }
}