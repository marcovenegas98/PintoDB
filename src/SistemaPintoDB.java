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

    public SistemaPintoDB(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta){
        this.estadistico = estadistico;
        this.estadisticoConsulta = estadisticoConsulta;
    }

    public void simular(){

    }
}
