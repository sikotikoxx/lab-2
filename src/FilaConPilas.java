// Ariel Olea y Santiago González
public class FilaConPilas<T> implements Fila<T> {
    private Stack<T> nashe;
    private Stack<T> lol;
    public FilaConPilas() {
        this.nashe = new Stack <T> ();
        this.lol = new Stack <T> ();
    }
    public void enqueue(T item) {
        nashe.push(item);
    }
    public T dequeue()
    {
        if (lol.isEmpty())
        {
            while (!nashe.isEmpty())
            {
                lol.push(nashe.pop());
            }
        }
        return lol.pop();
    }
    public T peek()
    {
        if (lol.isEmpty()) {
            while (!nashe.isEmpty()) {
                lol.push(nashe.pop());
            }
        }
        return lol.peek();
    }
    public int size()
    {
        return lol.size() + nashe.size();
    }
    public boolean isEmpty()
    {
        return lol.isEmpty() && nashe.isEmpty();
    }
}