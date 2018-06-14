import java.util.PriorityQueue;
public class SistemaPintoDB {
    private ModuloAdministradorDeClientesYConexiones adminClientes;
    private ModuloAdministradorDeProcesos adminProcesos;
    private ModuloProcesadorDeConsultas procesadorConsultas;
    private ModuloDeTransaccionesYAccesoADatos adminDatos;
    private ModuloEjecutorDeSentencias ejecutorSentencias;

    private CalculadorValoresAleatorios calculador;
    private Estadistico estadistico;
    private EstadisticoConsulta estadisticoConsulta;

    public double reloj;
    public PriorityQueue<Evento> listaDeEventos;

    private double duracionSimulacion; //en segundos


    public SistemaPintoDB(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta){
        this.estadistico = estadistico;
        this.estadisticoConsulta = estadisticoConsulta;
    }

    public void setDuracionSimulacion(double duracionSimulacion){
        this.duracionSimulacion = duracionSimulacion;
    }

    public double getDuracionSimulacion(){
        return duracionSimulacion;
    }

    public void simular(){
        reloj = 0.0; // segundos
        double llegada = calculador.genValorExponencial(2); // 30 conexiones por min = 1 conex cada 2 seg
        while(reloj < duracionSimulacion){
            


            
        }
    }
}
