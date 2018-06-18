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

    private int[] parametros; //k, n, p, m. En ese orden.
    private double timeout;
    private double duracionSimulacion; //en segundos


    public SistemaPintoDB(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, int[] parametros){
        this.parametros = parametros;
        this.estadistico = estadistico;
        this.estadisticoConsulta = estadisticoConsulta;
        this.listaDeEventos = new PriorityQueue<>();

        //Módulos
        this.adminClientes = new ModuloAdministradorDeClientesYConexiones(this.estadistico, this.estadisticoConsulta, this.listaDeEventos,  this.calculador, this.reloj, parametros[0], this.timeout);
        this.adminProcesos = new ModuloAdministradorDeProcesos(this.estadistico, this.estadisticoConsulta, this.listaDeEventos,  this.calculador, this.reloj, this.timeout); //Solo 1 servidor
        this.procesadorConsultas = new ModuloProcesadorDeConsultas(this.estadistico, this.estadisticoConsulta, this.listaDeEventos,  this.calculador, this.reloj, parametros[1], this.timeout);
        this.adminDatos = new ModuloDeTransaccionesYAccesoADatos(this.estadistico, this.estadisticoConsulta, this.listaDeEventos,  this.calculador, this.reloj, parametros[2], this.timeout);
        this.ejecutorSentencias = new ModuloEjecutorDeSentencias(this.estadistico, this.estadisticoConsulta, this.listaDeEventos,  this.calculador, this.reloj, parametros[3], this.timeout);

        TipoConsulta tipoConsulta = calculador.genMonteCarloConsulta();
        listaDeEventos.add(new Evento(TipoEvento.ENTRADA, TipoModulo.ClientesYConexiones, 0, new Consulta(tipoConsulta, this.timeout, this.estadisticoConsulta)));
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
            Evento eventoActual = listaDeEventos.poll();
            eventoActual.getConsulta().setTiempoIngreso(this.reloj);
            this.reloj = eventoActual.getTiempoOcurrencia();
            switch(eventoActual.getTipoEvento()){
                case ENTRADA:{
                    switch(eventoActual.getTipoModulo()){
                        case ClientesYConexiones:{
                            adminClientes.procesarLlegada(eventoActual.getConsulta());
                            break;
                        }
                        case AdministradorDeProcesos:{
                            adminProcesos.procesarLlegada(eventoActual.getConsulta());
                            break;
                        }
                        case ProcesadorDeConsultas:{
                            procesadorConsultas.procesarLlegada(eventoActual.getConsulta());
                            break;
                        }
                        case TransaccionYAccesoADatos:{
                            adminDatos.procesarLlegada(eventoActual.getConsulta());
                            break;
                        }
                        case EjecutorDeSentencias:{
                            ejecutorSentencias.procesarLlegada(eventoActual.getConsulta());
                            break;
                        }
                    }
                    break;
                }
                case SALIDA:{
                    switch(eventoActual.getTipoModulo()){
                        case ClientesYConexiones:{
                            adminClientes.procesarSalida(eventoActual.getConsulta());
                            break;
                        }
                        case AdministradorDeProcesos:{
                            adminProcesos.procesarSalida(eventoActual.getConsulta());
                            break;
                        }
                        case ProcesadorDeConsultas:{
                            procesadorConsultas.procesarSalida(eventoActual.getConsulta());
                            break;
                        }
                        case TransaccionYAccesoADatos:{
                            adminDatos.procesarSalida(eventoActual.getConsulta());
                            break;
                        }
                        case EjecutorDeSentencias:{
                            ejecutorSentencias.procesarSalida(eventoActual.getConsulta());
                            break;
                        }
                    }
                    break;
                }
                case TIMEOUT:{
                    adminClientes.cerrarConexion(eventoActual.getConsulta());
                    break;
                }
            }
        }
    }
}
