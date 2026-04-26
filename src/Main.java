public class Main
{
    public static void main(String[] args)
    {
        Pila<String> miPila = new PilaConFila<>();
        Fila<String> miFila = new FilaConPilas<>();

        WebCrawler crawler = new WebCrawler(miPila, miFila);

        System.out.println("--------------------------------------------------");
        System.out.println("Iniciando Web Crawler en EIT UDP (Límite: 100)");
        System.out.println("--------------------------------------------------");
        boolean resultadoEIT = crawler.chequearURLs("https://eit.udp.cl/", 100);
        System.out.println("¿Todas las páginas revisadas tienen XHTML válido?: " + resultadoEIT);
        System.out.println("\n--------------------------------------------------");
        System.out.println("Iniciando Web Crawler en Test (Límite: 200)");
        System.out.println("--------------------------------------------------");
        boolean resultadoTest = crawler.chequearURLs("https://crawler-test.com/", 200);
        System.out.println("¿Todas las páginas revisadas tienen XHTML válido?: " + resultadoTest);
    }
}