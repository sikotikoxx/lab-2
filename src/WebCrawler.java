import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler{
    private Pila<String> pila;
    private Fila<String> fila;
    public WebCrawler(Pila<String> pila, Fila<String> fila)
    {
        this.pila = pila;
        this.fila = fila;
    }
    public boolean esXHTMLValido(String xhtml)
    {
        // Regla 1: <!DOCTYPE> es mandatorio
        if (!xhtml.toUpperCase().contains("<!DOCTYPE")) return false;
        // Regla 2: El atributo xmlns en <html> es obligatorio
        int inicioHtml = xhtml.indexOf("<html");
        if (inicioHtml == -1) return false;
        int finHtml = xhtml.indexOf(">", inicioHtml);
        String etiquetaHtml = xhtml.substring(inicioHtml, finHtml);
        if (!etiquetaHtml.contains("xmlns")) return false;
        // Limpiamos la pila por si quedaron restos de una validación anterior
        while (!pila.isEmpty())
        {
            pila.pop();
        }
        // Reglas 3 y 4: LIFO y Minúsculas
        Pattern patron = Pattern.compile("<(/?)(\\w+)([^>]*)>");
        Matcher matcher = patron.matcher(xhtml);
        while (matcher.find())
        {
            String esCierre = matcher.group(1);     // Devuelve "/" si es de cierre, vacío si es apertura
            String nombreEtiqueta = matcher.group(2); // Devuelve el nombre (p, div, a, etc)
            String atributos = matcher.group(3);      // Devuelve lo que sigue al nombre
            if (!nombreEtiqueta.equals(nombreEtiqueta.toLowerCase())) return false;
            if (atributos.trim().endsWith("/")) continue;
            if (esCierre.equals(""))
            {
                pila.push(nombreEtiqueta);
            }
            else
            {
                if (pila.isEmpty()) return false;
                String tope = pila.pop();
                if (!tope.equals(nombreEtiqueta)) return false;
            }
        }
        return pila.isEmpty();
    }
    public boolean chequearURL(String url)
    {
        try{ // intenta ejecutar sentencias que pueden lanzar un error en tiempo de ejecución
            In in = new In(url);
            if (!in.exists()) return false;

            String contenidoHTML = in.readAll();
            return esXHTMLValido(contenidoHTML);
        }
        catch (Exception e) {return false;} //captura y maneja los errores del bloque de try
    }
    public boolean chequearURLs(String urlInicial, int limite)
    {
        SET<String> visitados = new SET<String>();
        boolean todasSonValidas = true;
        int paginasRevisadas = 0;
        while (!fila.isEmpty()) fila.dequeue();
        fila.enqueue(urlInicial);
        visitados.add(urlInicial);
        Pattern patronLinks = Pattern.compile("href=\"(http[^\"]+)\"");
        while (!fila.isEmpty() && paginasRevisadas < limite)
        {
            String urlActual = fila.dequeue();
            boolean esValida = chequearURL(urlActual);
            if (!esValida)
            {
                todasSonValidas = false;
            }
            paginasRevisadas++;
            if (paginasRevisadas >= limite)
            {
                break;
            }
            try{
                In in = new In(urlActual);
                if (in.exists())
                {
                    String contenidoHTML = in.readAll();
                    Matcher matcher = patronLinks.matcher(contenidoHTML);
                    while (matcher.find())
                    {
                        String nuevaUrl = matcher.group(1);
                        if (!visitados.contains(nuevaUrl))
                        {
                            visitados.add(nuevaUrl);
                            fila.enqueue(nuevaUrl);
                        }
                    }
                }
            }
            catch (Exception e) {}
        }
        return todasSonValidas;
    }
    public static void simularCrawlerOffline(int N, Fila<String> fila)
    {
        fila.enqueue("url_raiz");
        for (int i = 0; i < N; i++)
        {
            if (!fila.isEmpty())
            {
                fila.dequeue();
            }
            // simulacion que se encuentran 2 url por pagina
            fila.enqueue("link1_" + i);
            fila.enqueue("link2_" + i);
        }
    }
}