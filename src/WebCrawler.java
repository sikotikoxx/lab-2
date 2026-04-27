// Ariel Olea y Santiago González
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler{
    private Pila<String> pila;
    private Fila<String> fila;
    // mediciones regla 3: Compilamos los patrones una sola vez al inicio para que no ensucien la medición de tiempo
    private static final Pattern PATRON_ETIQUETAS = Pattern.compile("<(/?)(\\w+)([^>]*)>");
    private static final Pattern PATRON_LINKS = Pattern.compile("href=\"(http[^\"]+)\"");
    public WebCrawler(Pila<String> pila, Fila<String> fila)
    {
        this.pila = pila;
        this.fila = fila;
    }
    public boolean esXHTMLValido(String xhtml)
    {
        // regla 1: <!DOCTYPE> es mandatorio
        if (!xhtml.toUpperCase().contains("<!DOCTYPE")) return false;
        // regla 2: El atributo xmlns en <html> es obligatorio
        int inicioHtml = xhtml.indexOf("<html");
        if (inicioHtml == -1) return false;
        int finHtml = xhtml.indexOf(">", inicioHtml);
        String etiquetaHtml = xhtml.substring(inicioHtml, finHtml);
        if (!etiquetaHtml.contains("xmlns")) return false;
        // limpiamos la pila por si quedaron restos
        while (!pila.isEmpty())
        {
            pila.pop();
        }
        // reglas 3 y 4: LIFO y Minúsculas usando el patrón pre-compilado
        Matcher matcher = PATRON_ETIQUETAS.matcher(xhtml);
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
            else{
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
        } catch (Exception e) {
            return false;
        } //captura y maneja los errores del bloque de try
    }
    public boolean chequearURLs(String urlInicial, int limite)
    {
        SET<String> visitados = new SET<String>();
        boolean todasSonValidas = true;
        int paginasRevisadas = 0;
        while (!fila.isEmpty()) fila.dequeue();
        fila.enqueue(urlInicial);
        visitados.add(urlInicial);
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
                if (in.exists()) {
                    String contenidoHTML = in.readAll();
                    // usamos el patrón de links pre-compilado
                    Matcher matcher = PATRON_LINKS.matcher(contenidoHTML);
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
            } catch (Exception e) {}
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
            // Simulamos que encontramos 2 links por pagina
            fila.enqueue("link1_" + i);
            fila.enqueue("link2_" + i);
        }
    }
}