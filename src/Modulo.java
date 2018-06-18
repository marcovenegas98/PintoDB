import java.util.Queue;
import java.util.PriorityQueue;
public abstract class Modulo {
    protected int numeroServidores;
    protected double timeout;
    protected Queue<Consulta> cola;
    protected Estadistico estadistico;
    protected EstadisticoConsulta estadisticoConsulta;
    protected PriorityQueue<Evento> listaDeEventos;
    protected CalculadorValoresAleatorios calculador;
    protected double reloj;
    //protected SistemaPintoDB sistemaPintoDB;

    public Modulo(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, PriorityQueue<Evento> listaDeEventos, CalculadorValoresAleatorios calculador, double reloj, int numeroServidores, double timeout){
        this.estadistico = estadistico;
        this.estadisticoConsulta = estadisticoConsulta;
        this.listaDeEventos = listaDeEventos;
        this.calculador = calculador;
        this.numeroServidores = numeroServidores;
        this.reloj = reloj;
        this.timeout = timeout;
    }

    abstract void procesarLlegada(Consulta consulta);
    abstract void procesarSalida(Consulta consulta);
    protected int getCasillaParaEstadistico(Consulta consulta){
        int tipo = -1;
        switch (consulta.getTipoConsulta()){
            case DDL:{
                tipo = 0;
                break;
            }
            case SELECT:{
                tipo = 1;
                break;
            }

            case JOIN:{
                tipo = 2;
                break;
            }
            case UPDATE:{
                tipo = 3;
                break;
            }
        }
        return tipo;
    }
}
