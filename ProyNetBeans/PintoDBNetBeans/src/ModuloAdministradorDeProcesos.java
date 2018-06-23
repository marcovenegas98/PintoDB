import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Clase ModuloAdministradorDeProcesos.
 * Simula el comportamiento de este módulo en el DBMS.
 */
public class ModuloAdministradorDeProcesos extends Modulo {

    /**
     * Constructor de la clase.
     * Inicializa sus atributos.
     *
     * @param estadistico
     * @param estadisticoConsulta
     * @param listaDeEventos
     * @param calculador
     * @param reloj
     */
    public ModuloAdministradorDeProcesos(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, PriorityQueue<Evento> listaDeEventos, CalculadorValoresAleatorios calculador, double reloj){
        super(estadistico, estadisticoConsulta, listaDeEventos, calculador, reloj,1);//Solo tiene un servidor
        this.cola = new LinkedList<>();
    }

    /**
     * Procesa la llegada de una consulta a este módulo.
     *
     * @param consulta la consulta entrante.
     */
    void procesarLlegada(Consulta consulta){
        consulta.setTipoModulo(TipoModulo.AdministradorDeProcesos); //Consulta en el módulo admin de procesos.
        consulta.setTiempoIngresoModulo(this.reloj); //Ingresó al módulo en este momento.
        if(numeroServidores == 0){
            consulta.setEnCola(true); //Cuando se mete a la cola, se marca el booleano
            this.cola.add(consulta);
        }else{
            this.generarSalidaAdmProcesos(consulta);
            this.numeroServidores--;
        }
    }

    /**
     * Procesa la salida de una consulta de este módulo.
     *
     * @param consulta la consulta saliente.
     */
    void procesarSalida(Consulta consulta){
        estadisticoConsulta.incrementarConsultasRecibidas(0, consulta); //Una consulta más recibida en este módulo.
        double tiempoTranscurrido = this.reloj-consulta.getTiempoIngresoModulo();
        estadisticoConsulta.incrementarTiempoConsulta(0, consulta, tiempoTranscurrido);

        if(cola.size() > 0){ //Si hay gente en la cola.
            Consulta otraConsulta = cola.poll(); //Saco a otra consulta de la cola
            otraConsulta.setEnCola(false); //Cuando se saca de la cola, se desmarca el booleano.
            generarSalidaAdmProcesos(otraConsulta);
        }else{
            numeroServidores++;
        }
        generarEntradaProcConsultas(consulta); //Si hay timeout después de esta salida, la entrada recién generada se borra de la lista de eventos.
    }

    /**
     * Calcula el tiempo en el que saldrá la consulta del módulo y genera la salida.
     *
     * @param consulta la consulta.
     */
    private void generarSalidaAdmProcesos(Consulta consulta){
        double tiempoEnServicio = calculador.genValorRandomNormal(); //Valor normal
        double tiempoOcurrencia = tiempoEnServicio + this.reloj; //La salida se da en el tiempo actual + el que duren en atenderme

        this.listaDeEventos.add(new Evento(TipoEvento.SALIDA, TipoModulo.AdministradorDeProcesos, tiempoOcurrencia, consulta));
    }

    /**
     * Genera la entrada al siguente módulo.
     *
     * @param consulta la consulta
     */
    private void generarEntradaProcConsultas(Consulta consulta){
        //Se genera la entrada en el mismo tiempo de reloj pues este método solo se llama cuando se procesa una salida y el tiempo
        //de paso de una consulta de un módulo a otro es cero.
        listaDeEventos.add(new Evento(TipoEvento.ENTRADA, TipoModulo.ProcesadorDeConsultas, this.reloj, consulta));
    }
}
