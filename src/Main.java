// Ariel Olea y Santiago González
public class Main
{
    public static void main(String[] args)
    {
        int[] tamanosN = {1024, 2048, 4096, 8192, 16384, 32768}; //{2^10,2^11,2^12,2^13,2^14,2^15}
        int repeticiones = 100;

        for (int N : tamanosN) {
            System.out.println("Corriendo experimentos para N = " + N);

            // generamos el string gigante una sola vez por tamaño (llamando a la clase Experimento)
            String xhtmlFalso = Experimento.generarXHTMLSintetico(N);
            // arreglo para almacenar los resultados en RAM sin molestar el disco duro durante el loop (asi para no afectar la medicion)
            String[] lineasCSV = new String[repeticiones];

            for (int r = 1; r <= repeticiones; r++) {
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

                // guardamos la medición en el arreglo en lugar de escribir al archivo de inmediato
                lineasCSV[r - 1] = r + "," + tiempoPilaPrinceton + "," + tiempoPilaConFila + "," + tiempoFilaPrinceton + "," + tiempoFilaConPilas;
            }
            Out archivoCSV = new Out("resultados_N_" + N + ".csv");
            archivoCSV.println("reps_Nashe,PilaPrinceton,PilaConFila,FilaPrinceton,FilaConPilas");
            for (String linea : lineasCSV) // escribimos las filas de golpe ahora
            {
                archivoCSV.println(linea);
            }
            archivoCSV.close(); // cerrar el archivo al terminar el tamaño
        }
        System.out.println("eu se termino el experimento, revisa los csv");
    }
}