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
    public static void main(String[] args) {
        int[] tamanosN = {1024, 2048, 4096, 8192, 16384, 32768}; //{2^10,2^11,2^12,2^13,2^14,2^15}
        int repeticiones = 100;
        for (int N : tamanosN)
        {
            System.out.println("Corriendo experimentos para N = " + N);
            // 1. crear el archivo CSV para este tamaño N usando Out de Princeton
            Out archivoCSV = new Out("resultados_N_" + N + ".csv");
            archivoCSV.println("reps_Nashe,PilaPrinceton,PilaConFila,FilaPrinceton,FilaConPilas");
            // generamos el string gigante una sola vez por tamaño
            String xhtmlFalso = generarXHTMLSintetico(N);
            for (int r = 1; r <= repeticiones; r++)
            {
                // experimento en pilas
                // medir PilaPrinceton
                Pila<String> pila1 = new PilaPrinceton<>();
                WebCrawler crawlerPila1 = new WebCrawler(pila1, new FilaPrinceton<>()); // Fila dummy
                StopwatchCPU timer1 = new StopwatchCPU();
                crawlerPila1.esXHTMLValido(xhtmlFalso);
                double tiempoPilaPrinceton = timer1.elapsedTime();
                // medir PilaConFila
                Pila<String> pila2 = new PilaConFila<>();
                WebCrawler crawlerPila2 = new WebCrawler(pila2, new FilaPrinceton<>());
                StopwatchCPU timer2 = new StopwatchCPU();
                crawlerPila2.esXHTMLValido(xhtmlFalso);
                double tiempoPilaConFila = timer2.elapsedTime();
                // experimento en filas ahora
                // medir FilaPrinceton
                Fila<String> fila1 = new FilaPrinceton<>();
                StopwatchCPU timer3 = new StopwatchCPU();
                WebCrawler.simularCrawlerOffline(N, fila1);
                double tiempoFilaPrinceton = timer3.elapsedTime();
                // medir FilaConPilas
                Fila<String> fila2 = new FilaConPilas<>();
                StopwatchCPU timer4 = new StopwatchCPU();
                WebCrawler.simularCrawlerOffline(N, fila2);
                double tiempoFilaConPilas = timer4.elapsedTime();
                // se escribe la fila en el CSV
                archivoCSV.println(r + "," + tiempoPilaPrinceton + "," + tiempoPilaConFila + "," + tiempoFilaPrinceton + "," + tiempoFilaConPilas);
            }
            archivoCSV.close(); // cerrar el archivo al terminar el tamaño
        }
        System.out.println("eu se termino el experimento, revisa los csv");
    }
}