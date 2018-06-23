import java.util.PriorityQueue;

/**
 * Clase ModuloAdministradorDeClientesYConexiones.
 * Simula el funcionamiento de este módulo en el DBMS PintoDB.
 */
public class ModuloAdministradorDeClientesYConexiones extends Modulo {
    private int conexionesDescartadas;
    private double timeout;

    /**
     * Constructor de la clase.
     * Inicializa sus variable.
     *
     * @param estadistico
     * @param estadisticoConsulta
     * @param listaDeEventos
     * @param calculador
     * @param reloj
     * @param k
     * @param timeout
     */
    public ModuloAdministradorDeClientesYConexiones(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, PriorityQueue<Evento> listaDeEventos, CalculadorValoresAleatorios calculador, double reloj, int k, double timeout){
        super(estadistico, estadisticoConsulta, listaDeEventos, calculador, reloj, k);
        this.timeout = timeout;
        this.conexionesDescartadas = 0;
    }

    /**
     * Procesa la llegada de una consulta.
     *
     * @param consulta la consulta entrante.
     */
    @Override
    public void procesarLlegada(Consulta consulta){
        consulta.setTipoModulo(TipoModulo.ClientesYConexiones); //La consulta se encuentra en este módulo.
        if(numeroServidores == 0){
            conexionesDescartadas++;
            listaDeEventos.removeIf((Evento ev) -> ev.getConsulta() == consulta); //Quito el timeout si la conexion fue descartada
        }else{
            numeroServidores--;
            generarLlegadaAdmProcesos(consulta);
        }
        this.generarLlegadaAdmClientes();
    }

    /**
     * Procesa una salida de este módulo, que es equivalente a una salida del sistema.
     *
     * @param consulta la consulta saliente.
     */
    void procesarSalida(Consulta consulta){
        double tiempoSubida = (double)consulta.getB()/64.00; //Calcula el tiempo que dura en subirse el resultado.

        this.estadisticoConsulta.incrementarConsultasRecibidas(4, consulta);

        this.estadisticoConsulta.incrementarConsultasProcesadasDelSistema();

        this.estadisticoConsulta.incrementarTiempoConsulta(4, consulta, tiempoSubida); //Actualiza las estadísticas de la consulta.

        double tiempoDeVida = this.reloj-consulta.getTiempoIngreso();

        tiempoDeVida += tiempoSubida; //Se le suma lo que durará en subirse el resultado.

        this.estadisticoConsulta.incrementarTiempoAcumuladoDeVida(tiempoDeVida);

        this.numeroServidores++;
        this.listaDeEventos.removeIf((Evento ev) -> ev.getConsulta() == consulta); //Saca de la lista de eventos el timeout.
        //La consulta salió del sistema.
    }

    /**
     * Se encarga de manejar un evento timeout.
     * Saca de la lista de eventos cualquier otro evento con la misma consulta y
     * actualiza las estadísticas.
     *
     * @param consulta la consulta que hizo timeout.
     * @param modulo el módulo en el que se encontraba la consulta.
     */
    public void manejarTimeout(Consulta consulta, Modulo modulo){
        this.numeroServidores++;
        if(consulta.isEnCola()){ //Si la consulta esta en cola, la saco de la cola.
           modulo.getCola().remove(consulta);
        }else{
            //Si no estaba en cola, la salida del módulo ocurrió primero que este timeout, entonces ya se actualizaron las estadísticas
        }
        double tiempoDeVida = this.reloj-consulta.getTiempoIngreso();
        this.estadisticoConsulta.incrementarConsultasProcesadasDelSistema();
        this.estadisticoConsulta.incrementarTiempoAcumuladoDeVida(tiempoDeVida);

        listaDeEventos.removeIf((Evento ev) -> ev.getConsulta() == consulta); //Saca de la lista de eventos cualquier otro evento con esta misma consulta.
    }

    /**
     * Genera una nueva llegada a este mismo módulo.
     */
    private void generarLlegadaAdmClientes(){
        double tiempo = calculador.genValorExponencial(2); // 30 conexiones por min = 1 conex cada 2 seg
        TipoConsulta tipo = calculador.genMonteCarloConsulta();
        Consulta nuevaConsulta = new Consulta(tipo);
        double tiempoOcurrencia = this.reloj + tiempo;
        listaDeEventos.add(new Evento(TipoEvento.ENTRADA, TipoModulo.ClientesYConexiones, tiempoOcurrencia, nuevaConsulta));

        //Genero el timeout para esta misma consulta.
        listaDeEventos.add(new Evento(TipoEvento.TIMEOUT, TipoModulo.ClientesYConexiones, tiempoOcurrencia + this.timeout, nuevaConsulta));
    }

    /**
     * Genera una llegada al siguiente módulo
     *
     * @param consulta la consulta que pasa al siguiente módulo.
     */
    private void generarLlegadaAdmProcesos(Consulta consulta){
        //Se genera la entrada en el mismo tiempo de reloj pues este método solo se llama cuando se procesa una salida y el tiempo
        //de paso de una consulta de un módulo a otro es cero.
        listaDeEventos.add(new Evento(TipoEvento.ENTRADA, TipoModulo.AdministradorDeProcesos, this.reloj, consulta));
    }

    /**
     * Devuelve la cantidad de conexiones que han sido descartadas.
     *
     * @return conexiones Descartadas.
     */
    public int getConexionesDescartadas(){
        return conexionesDescartadas;
    }

    /**
     * Asigna la cantidad de conexiones que han sido descartadas.
     *
     * @param conexionesDescartadas cantidad de conexiones a asignar.
     */
    public void setConexionesDescartadas(int conexionesDescartadas){
        this.conexionesDescartadas = conexionesDescartadas;
    }

    /**
     * Asigna el valor que tiene la variable timeout.
     *
     * @param timeout double timeout a asignar.
     */
    public void setTimeout(double timeout){
        this.timeout = timeout;
    }
}

