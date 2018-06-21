import java.util.Iterator;
import java.util.PriorityQueue;

public class ModuloAdministradorDeClientesYConexiones extends Modulo {
    private int conexionesDescartadas;
    private double timeout;

    public ModuloAdministradorDeClientesYConexiones(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, PriorityQueue<Evento> listaDeEventos, CalculadorValoresAleatorios calculador, double reloj, int k, double timeout){
        super(estadistico, estadisticoConsulta, listaDeEventos, calculador, reloj, k);
        this.timeout = timeout;
        this.conexionesDescartadas = 0;
    }

    public void procesarLlegada(Consulta consulta){
        consulta.setTipoModulo(TipoModulo.ClientesYConexiones); //La consulta se encuentra en este módulo.
        if(numeroServidores == 0){
            conexionesDescartadas++;
        }else{
            numeroServidores--;

            generarLlegadaAdmProcesos(consulta);
        }
        this.generarLlegadaAdmClientes();
    }

    //Cuando sale de aquí, ya pasó por el resto de módulos.
    void procesarSalida(Consulta consulta){
        double tiempoSubida = (double)consulta.getB()/64.00; //Calcula el tiempo que dura en subirse el resultado.

        this.estadisticoConsulta.incrementarConsultasRecibidas(4, consulta);

        this.estadisticoConsulta.incrementarConsultasProcesadasDelSistema();

        this.estadisticoConsulta.incrementarTiempoConsulta(4, consulta, tiempoSubida); //Actualiza las estadísticas de la consulta.

        double tiempoDeVida = this.reloj-consulta.getTiempoIngreso();

        tiempoDeVida += tiempoSubida; //Se le suma lo que durará en subirse el resultado.

        this.estadisticoConsulta.incrementarTiempoAcumuladoDeVida(tiempoDeVida);

        numeroServidores++;
        listaDeEventos.removeIf((Evento ev) -> ev.getConsulta() == consulta); //Saca de la lista de eventos el timeout.
        //La consulta salió del sistema.
    }


    public void manejarTimeout(Consulta consulta, Modulo modulo){
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

    private void generarLlegadaAdmClientes(){
        double tiempo = calculador.genValorExponencial(2); // 30 conexiones por min = 1 conex cada 2 seg
        TipoConsulta tipo = calculador.genMonteCarloConsulta();
        Consulta nuevaConsulta = new Consulta(tipo);
        double tiempoOcurrencia = this.reloj + tiempo;
        listaDeEventos.add(new Evento(TipoEvento.ENTRADA, TipoModulo.ClientesYConexiones, tiempoOcurrencia, nuevaConsulta));

        //Genero el timeout para esta misma consulta.
        listaDeEventos.add(new Evento(TipoEvento.TIMEOUT, TipoModulo.ClientesYConexiones, tiempoOcurrencia + this.timeout, nuevaConsulta));
    }

    private void generarLlegadaAdmProcesos(Consulta c){
        //Se genera la entrada en el mismo tiempo de reloj pues este método solo se llama cuando se procesa una salida y el tiempo
        //de paso de una consulta de un módulo a otro es cero.
        listaDeEventos.add(new Evento(TipoEvento.ENTRADA, TipoModulo.AdministradorDeProcesos, this.reloj, c));
    }

    public int getConexionesDescartadas(){
        return conexionesDescartadas;
    }
}
