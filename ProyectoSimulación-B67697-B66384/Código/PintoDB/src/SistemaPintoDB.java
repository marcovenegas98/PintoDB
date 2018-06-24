import java.util.PriorityQueue;
import java.util.concurrent.Semaphore;

/**
 * Clase SistemaPintoDB.
 * Esta clase se encarga de realiza la simulación.
 * Simula el comportamiento de el DBMS Pinto DB.
 */
public class SistemaPintoDB {
    private Interfaz interfaz;
    
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

    private double timeout;
    private double duracionSimulacion; //en segundos
    private boolean modoLento;

    private Semaphore semEjecucion;

    /**
     * Constructor de la clase SistemaPintoDB.
     * Inicializa sus componentes.
     *
     * @param interfaz
     * @param semEjecucion
     * @param estadistico
     * @param estadisticoConsulta
     * @param parametros
     * @param timeout
     * @param duracion
     * @param modoLento
     */
    public SistemaPintoDB(Interfaz interfaz, Semaphore semEjecucion, Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, int[] parametros, double timeout, double duracion, boolean modoLento){
        this.interfaz = interfaz;
        this.semEjecucion = semEjecucion;
        this.reloj = 0;
        this.estadistico = estadistico;
        this.estadisticoConsulta = estadisticoConsulta;
        this.listaDeEventos = new PriorityQueue<>();
        this.calculador = new CalculadorValoresAleatorios();
        this.timeout = timeout;
        this.duracionSimulacion = duracion;
        this.modoLento = modoLento;

        //Módulos
        this.adminClientes = new ModuloAdministradorDeClientesYConexiones(this.estadistico, this.estadisticoConsulta, this.listaDeEventos,  this.calculador, this.reloj, parametros[0], this.timeout);
        this.adminProcesos = new ModuloAdministradorDeProcesos(this.estadistico, this.estadisticoConsulta, this.listaDeEventos,  this.calculador, this.reloj); //Solo 1 servidor
        this.procesadorConsultas = new ModuloProcesadorDeConsultas(this.estadistico, this.estadisticoConsulta, this.listaDeEventos,  this.calculador, this.reloj, parametros[1]);
        this.adminDatos = new ModuloDeTransaccionesYAccesoADatos(this.estadistico, this.estadisticoConsulta, this.listaDeEventos,  this.calculador, this.reloj, parametros[2]);
        this.ejecutorSentencias = new ModuloEjecutorDeSentencias(this.estadistico, this.estadisticoConsulta, this.listaDeEventos,  this.calculador, this.reloj, parametros[3]);
        
        this.insertarEventoInicial();
    }

    /**
     * Inserta un evento a la lista de eventos para que pueda empezar la simulación.
     */
    private void insertarEventoInicial(){
        //Se guarda en la lista de eventos el evento inicial.
        TipoConsulta tipoConsulta = calculador.genMonteCarloConsulta();
        Consulta consultaInicial = new Consulta(tipoConsulta);
        listaDeEventos.add(new Evento(TipoEvento.ENTRADA, TipoModulo.ClientesYConexiones, this.reloj, consultaInicial));
        //Se genera el timeout para esta consulta.
        listaDeEventos.add(new Evento(TipoEvento.TIMEOUT, TipoModulo.ClientesYConexiones, this.reloj + this.timeout, consultaInicial));
    }

    /**
     * Mientras que el tiempo no se haya acabado, saca un evento
     * de la lista de eventos y lo envía a procesar al módulo correspondiente.
     * También actualiza la interfaz cada vez que se saca un evento.
     *
     * @throws InterruptedException
     */
    public void simular() throws InterruptedException{
        Evento eventoActual = listaDeEventos.poll(); //Saca el siguiente evento de la lista de eventos.
        eventoActual.getConsulta().setTiempoIngreso(this.reloj); //Se le da a la consulta el tiempo en el que ingresó al sistema.
        this.reloj = eventoActual.getTiempoOcurrencia(); //Se actualiza el reloj.
        double relojAnterior = this.reloj;

        while(reloj < duracionSimulacion){
            this.mandarAProcesar(eventoActual);
            eventoActual = listaDeEventos.poll(); //Saca el siguiente evento de la lista de eventos.
            this.reloj = eventoActual.getTiempoOcurrencia(); //Se actualiza el reloj.
            this.interfaz.actualizarInteractivo();
            if(this.reloj != relojAnterior){ //Hubo un cambio de reloj
                relojAnterior = this.reloj;
                if(this.modoLento){ //Si estamos en modo lento.
                    Thread.sleep(1000); //Duerma 1 segundo.
                }
            }
        }
    }

    /**
     * Dependiendo del tipo de evento, y el tipo de módulo del evento, envía a procesar al
     * módulo correspondiente, el evento.
     *
     * @param evento el evento a procesar.
     */
    private void mandarAProcesar(Evento evento){
        switch(evento.getTipoEvento()){
            case ENTRADA:{
                switch(evento.getTipoModulo()){
                    case ClientesYConexiones:{
                        adminClientes.setReloj(reloj);
                        adminClientes.procesarLlegada(evento.getConsulta());
                        break;
                    }
                    case AdministradorDeProcesos:{
                        this.estadistico.incrementarEntradasPorModulo(0);
                        this.estadistico.incrementarTamanosAcumuladosDeColasPorModulo(0,adminProcesos.getTamanoCola());
                        adminProcesos.setReloj(reloj);
                        adminProcesos.procesarLlegada(evento.getConsulta());
                        break;
                    }
                    case ProcesadorDeConsultas:{
                        this.estadistico.incrementarEntradasPorModulo(1);
                        this.estadistico.incrementarTamanosAcumuladosDeColasPorModulo(1,procesadorConsultas.getTamanoCola());
                        procesadorConsultas.setReloj(reloj);
                        procesadorConsultas.procesarLlegada(evento.getConsulta());
                        break;
                    }
                    case TransaccionYAccesoADatos:{
                        this.estadistico.incrementarEntradasPorModulo(2);
                        this.estadistico.incrementarTamanosAcumuladosDeColasPorModulo(2,adminDatos.getTamanoCola());
                        adminDatos.setReloj(reloj);
                        adminDatos.procesarLlegada(evento.getConsulta());
                        break;
                    }
                    case EjecutorDeSentencias:{
                        this.estadistico.incrementarEntradasPorModulo(3);
                        this.estadistico.incrementarTamanosAcumuladosDeColasPorModulo(3,ejecutorSentencias.getTamanoCola());
                        ejecutorSentencias.setReloj(reloj);
                        ejecutorSentencias.procesarLlegada(evento.getConsulta());
                        break;
                    }
                }
                break;
            }
            case SALIDA:{
                switch(evento.getTipoModulo()){
                    case ClientesYConexiones:{
                        adminClientes.setReloj(reloj);
                        adminClientes.procesarSalida(evento.getConsulta());
                        break;
                    }
                    case AdministradorDeProcesos:{
                        adminProcesos.setReloj(reloj);
                        adminProcesos.procesarSalida(evento.getConsulta());
                        break;
                    }
                    case ProcesadorDeConsultas:{
                        procesadorConsultas.setReloj(reloj);
                        procesadorConsultas.procesarSalida(evento.getConsulta());
                        break;
                    }
                    case TransaccionYAccesoADatos:{
                        adminDatos.setReloj(reloj);
                        adminDatos.procesarSalida(evento.getConsulta());
                        break;
                    }
                    case EjecutorDeSentencias:{
                        ejecutorSentencias.setReloj(reloj);
                        ejecutorSentencias.procesarSalida(evento.getConsulta());
                        break;
                    }
                }
                break;
            }
            case TIMEOUT:{
                adminClientes.setReloj(reloj);
                adminClientes.manejarTimeout(evento.getConsulta(), this.determinarModulo(evento.getConsulta()));
                break;
            }
        }
    }

    /**
     * Dada una consulta, devuelve el módulo en el que se
     * encuentra dicha consulta.
     *
     * @param consulta la consulta.
     * @return Modulo donde se encuentra la consulta.
     */
    private Modulo determinarModulo(Consulta consulta){
        TipoModulo tipoModulo = consulta.getTipoModulo();
        Modulo modulo = null;
        switch(tipoModulo){
            case ClientesYConexiones:{
                modulo = this.adminClientes;
                break;
            }
            case AdministradorDeProcesos:{
                modulo = this.adminProcesos;
                break;
            }
            case ProcesadorDeConsultas:{
                modulo = this.procesadorConsultas;
                break;
            }
            case TransaccionYAccesoADatos:{
                modulo = this.adminDatos;
                break;
            }
            case EjecutorDeSentencias:{
                modulo = this.ejecutorSentencias;
                break;
            }
        }
        return modulo;
    }

    /**
     * Devuelve el valor actual del reloj.
     *
     * @return el valor del reloj.
     */
    public double getReloj(){
        return this.reloj;
    }

    /**
     * Asigna el valor del reloj.
     *
     * @param reloj valor de reloj a ser asignado.
     */
    public void setReloj(double reloj){
        this.reloj = reloj;
    }

    /**
     * Reinicia el reloj, la lista de eventos, y los módulos sin
     * modificar los parámetros de cada módulo.
     */
    public void resetSistemaParcial(){
        this.reloj = 0;
        this.adminClientes.setConexionesDescartadas(0);
        this.adminClientes.reset();
        this.adminProcesos.reset();
        this.procesadorConsultas.reset();
        this.adminDatos.reset();
        this.adminDatos.resetDDLsEsperando();
        this.ejecutorSentencias.reset();
        this.listaDeEventos.clear();
        this.insertarEventoInicial();
    }

    /**
     * Asigna a los módulos nuevos parámetros para correr una nueva simulación.
     * Reinicia los módulos, el reloj y la lista de eventos.
     *
     * @param parametros los nuevos parámetros.
     * @param timeout timeout nuevo.
     * @param duracionSimulacion duracion de simulación nueva.
     * @param modoLento modoLento nuevo.
     */
    public void resetSistema(int[] parametros, double timeout, double duracionSimulacion, boolean modoLento){
        this.adminClientes.setNumeroServidores(parametros[0]);
        this.adminClientes.setTimeout(timeout);

        this.procesadorConsultas.setNumeroServidores(parametros[1]); 
        
        this.adminDatos.setNumeroServidores(parametros[2]);
        this.adminDatos.setTiempoCoordinacion();
        
        this.ejecutorSentencias.setNumeroServidores(parametros[3]);
        
        this.resetSistemaParcial();
        
        this.duracionSimulacion = duracionSimulacion;
        
        this.modoLento = modoLento;
    }

    /**
     * Devuelve la cantidad de conexiones descartadas por el administrador de clientes.
     *
     * @return la cantidad de conexiones descartadas.
     */
    public int getConexionesDescartadas(){
        return this.adminClientes.getConexionesDescartadas();
    }

    /**
     * Devuelve los tamaños de las colas que hay en cada módulo.
     *
     * @return los tamaños de las colas en cada módulo.
     */
    public int[] getTamanosColas(){
        int[] tamanosColas = new int[4];
        tamanosColas[0] = this.adminProcesos.getTamanoCola();
        tamanosColas[1] = this.procesadorConsultas.getTamanoCola();
        tamanosColas[2] = this.adminDatos.getTamanoCola();
        tamanosColas[3] = this.ejecutorSentencias.getTamanoCola();
        return tamanosColas;
    }
}
