import java.util.Queue;
import java.util.PriorityQueue;
public abstract class Modulo {
    protected int numeroServidores;
    protected int NUMSERVIDORES; //Este no debe cambiar, se utiliza para el reset.
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
        this.NUMSERVIDORES = numeroServidores;
        this.reloj = reloj;
    }
    
    public void reset(){
        this.reloj = 0;
        this.numeroServidores = NUMSERVIDORES;
        try{
            this.cola.clear(); //Dentro de un try porque Admin Clientes no tiene cola.
        }catch(Exception e){
            
        }
    }
    
    public void setNumeroServidores(int numeroServidores){
        this.numeroServidores = numeroServidores;
        this.NUMSERVIDORES = numeroServidores;
    }

    public Queue<Consulta> getCola(){
        return this.cola;
    }
    
    public int getTamanoCola(){
        return this.cola.size();
    }
    
    public void setReloj(double rel){ this.reloj = rel;}

    abstract void procesarLlegada(Consulta consulta);
    abstract void procesarSalida(Consulta consulta);


}
