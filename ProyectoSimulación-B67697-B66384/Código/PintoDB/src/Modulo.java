import java.util.Queue;
import java.util.PriorityQueue;

/**
 * Clase abstracta Modulo.
 * Todos los módulos heredan los atributos de esta clase.
 */
public abstract class Modulo {
    protected int numeroServidores;
    protected int NUMSERVIDORES; //Este no debe cambiar, se utiliza para el reset.
    protected Queue<Consulta> cola;
    protected Estadistico estadistico;
    protected EstadisticoConsulta estadisticoConsulta;
    protected PriorityQueue<Evento> listaDeEventos;
    protected CalculadorValoresAleatorios calculador;
    protected double reloj;

    /**
     * Constructor abstracto de un módulo.
     * Todos los módulos tienen un Estadistico, un EstadisticoConsulta,
     * una Lista de eventos, un CalculadorValoresAleatorios, el reloj y el número de servidores.
     *
     * @param estadistico
     * @param estadisticoConsulta
     * @param listaDeEventos
     * @param calculador
     * @param reloj
     * @param numeroServidores
     */
    public Modulo(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, PriorityQueue<Evento> listaDeEventos, CalculadorValoresAleatorios calculador, double reloj, int numeroServidores){
        this.estadistico = estadistico;
        this.estadisticoConsulta = estadisticoConsulta;
        this.listaDeEventos = listaDeEventos;
        this.calculador = calculador;
        this.numeroServidores = numeroServidores;
        this.NUMSERVIDORES = numeroServidores;
        this.reloj = reloj;
    }

    /**
     * Resetea los valores del módulo al default.
     */
    public void reset(){
        this.reloj = 0;
        this.numeroServidores = NUMSERVIDORES;
        try{
            this.cola.clear(); //Dentro de un try porque Admin Clientes no tiene cola.
        }catch(Exception e){
            
        }
    }

    /**
     * Asigna un nuevo número de servidores al módulo.
     *
     * @param numeroServidores
     */
    public void setNumeroServidores(int numeroServidores){
        this.numeroServidores = numeroServidores;
        this.NUMSERVIDORES = numeroServidores;
    }

    /**
     * Devuelve la cola de este módulo.
     *
     * @return la cola.
     */
    public Queue<Consulta> getCola(){
        return this.cola;
    }

    /**
     * Devuelve el tamaño de la cola de este módulo.
     *
     * @return el tamaño de la cola.
     */
    public int getTamanoCola(){
        return this.cola.size();
    }

    /**
     * Asigna el valor del reloj a este módulo.
     *
     * @param reloj el valor del reloj.
     */
    public void setReloj(double reloj){ this.reloj = reloj;}

    /**
     * Procesa la llegada de una consulta al módulo.
     *
     * @param consulta
     */
    abstract void procesarLlegada(Consulta consulta);

    /**
     * Procesa la salida de una consulta del módulo.
     *
     * @param consulta
     */
    abstract void procesarSalida(Consulta consulta);


}
