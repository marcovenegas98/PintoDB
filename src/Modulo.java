import java.util.Queue;
import java.util.PriorityQueue;
public abstract class Modulo {
    private int numeroServidores;
    private Queue<Consulta> cola;
    private Estadistico estadistico;
    private PriorityQueue<Evento> listaDeEventos;
    private CalculadorValoresAleatorios calculador;

    abstract void procesarLlegada();
    abstract void procesarSalida();
}
