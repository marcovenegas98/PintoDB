import java.util.Queue;
import java.util.PriorityQueue;
public abstract class Modulo {
    protected int numeroServidores;
    protected Queue<Consulta> cola;
    protected Estadistico estadistico;
    protected PriorityQueue<Evento> listaDeEventos;
    protected CalculadorValoresAleatorios calculador;
    protected SistemaPintoDB sistemaPintoDB;

    public void setNumeroServidores(int numeroServidores) {
        this.numeroServidores = numeroServidores;
    }

    abstract void procesarLlegada();
    abstract void procesarSalida();
}
