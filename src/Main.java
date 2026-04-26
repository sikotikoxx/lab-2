public class Main
{
    public static void main(String[] args)
    {
        PilaConFila<String> stackUrls = new PilaConFila<>();

        System.out.println("--- PRUEBA DE PILACONFILA (Stack usando una Fila) ---");

        // 1. Verificación inicial
        System.out.println("¿La pila está vacía?: " + stackUrls.isEmpty());

        // 2. Operación Push (Aquí es donde ocurre la rotación en tu código)
        System.out.println("\n-> Agregando URLs (Push):");
        System.out.println("1. https://udp.cl");
        stackUrls.push("https://udp.cl");

        System.out.println("2. https://canvas.udp.cl");
        stackUrls.push("https://canvas.udp.cl");

        System.out.println("3. https://github.com");
        stackUrls.push("https://github.com");

        // 3. Verificación de tamaño y tope
        System.out.println("\nTamaño actual: " + stackUrls.size());
        System.out.println("Elemento en el tope (peek): " + stackUrls.peek());
        // Debería ser github.com porque fue el último en entrar

        // 4. Operación Pop (Extracción)
        System.out.println("\n-> Extrayendo elementos (Pop) - Orden LIFO:");
        while (!stackUrls.isEmpty()) {
            System.out.println("Sacando: " + stackUrls.pop());
        }

        // 5. Verificación final
        System.out.println("\n¿Está vacía después de los pops?: " + stackUrls.isEmpty());
        System.out.println("Tamaño final: " + stackUrls.size());
    }
}