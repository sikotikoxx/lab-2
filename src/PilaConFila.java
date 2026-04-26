public class PilaConFila<T> implements Pila<T>
{
    private Queue<T> lolero; //lolero funcionara como una fila
    public PilaConFila()
    {
        this.lolero = new Queue<T>(); //inicializamos el objeto "lolero" para que luego funcione como una fila
    }
    public void push(T item)
    {
        int pesodellolero = lolero.size(); //pesodellolero funciona como el tamaño actual de la fila
        lolero.enqueue(item); //inserta un item al final de la cola
        // aquí rotamos la cola para que el nuevo item quede al frente
        for (int i = 0; i < pesodellolero; i++) {
            lolero.enqueue(lolero.dequeue()); //mientras enqueue agrega dequeue elimina
        }
    }
    public T pop()
    {
        return lolero.dequeue();
    }
    public T peek()
    {
        return lolero.peek();
    }
    public boolean isEmpty()
    {
        return lolero.isEmpty();
    }
    public int size()
    {
        return lolero.size();
    }
}
