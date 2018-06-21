import java.util.Queue;
import java.util.PriorityQueue;
public abstract class Modulo {
    protected int numeroServidores;
    protected Queue<Consulta> cola;
    protected Estadistico estadistico;
    protected EstadisticoConsulta estadisticoConsulta;
    protected PriorityQueue<Evento> listaDeEventos;
    protected CalculadorValoresAleatorios calculador;
    protected double reloj;
    //protected SistemaPintoDB sistemaPintoDB;

    public Modulo(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, PriorityQueue<Evento> listaDeEventos, CalculadorValoresAleatorios calculador, double reloj, int numeroServidores){
        this.estadistico = estadistico;
        this.estadisticoConsulta = estadisticoConsulta;
        this.listaDeEventos = listaDeEventos;
        this.calculador = calculador;
        this.numeroServidores = numeroServidores;
        this.reloj = reloj;
    }

    public Queue<Consulta> getCola(){
        return this.cola;
    }
    public void setReloj(double rel){ this.reloj = rel;}

    abstract void procesarLlegada(Consulta consulta);
    abstract void procesarSalida(Consulta consulta);


}
