public class Experimento{
    public static String generarXHTMLSintetico(int N)// metodo para hacer la pagina falsa
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html xmlns='http://...'>\n");
        for (int i = 0; i < N - 1; i++) sb.append("<div>\n");
        for (int i = 0; i < N - 1; i++) sb.append("</div>\n");
        sb.append("</html>");
        return sb.toString();
    }
}