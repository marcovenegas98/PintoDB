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
        TipoConsulta tipoConsulta = calculador.genMonteCarloConsulta();
        listaDeEventos.add(new Evento(TipoModulo.ClientesYConexiones, 0, new Consulta(tipoConsulta, adminClientes.getTimeout()) , true ));
    }

    public SistemaPintoDB(){
        TipoConsulta tipoConsulta = calculador.genMonteCarloConsulta();
        listaDeEventos.add(new Evento(TipoModulo.ClientesYConexiones, 0, new Consulta(tipoConsulta, adminClientes.getTimeout()) , true ));
    }

    public void setDuracionSimulacion(double duracionSimulacion){
        this.duracionSimulacion = duracionSimulacion;
    }

    public double getDuracionSimulacion(){
        return duracionSimulacion;
    }

    public void simular(){
        reloj = 0.0; // segundos
        while(reloj < duracionSimulacion){
            reloj = listaDeEventos.peek().getTiempoOcurrencia();
            boolean esLlegada = listaDeEventos.peek().isLlegada();
            TipoModulo tipoModulo = listaDeEventos.peek().getTipoModulo();

            if(tipoModulo == TipoModulo.ClientesYConexiones && esLlegada)
                adminClientes.procesarLlegada();
            else if (tipoModulo ==  TipoModulo.AdministradorDeProcesos && esLlegada)
                adminProcesos.procesarLlegada();
            else if (tipoModulo == TipoModulo.AdministradorDeProcesos && !esLlegada)
                adminProcesos.procesarSalida();
        }
    }
}
