import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Clase ModuloEjecutorDeSentencias.
 * Simula el comportamiento de este módulo en el DBMS.
 */
public class ModuloEjecutorDeSentencias extends Modulo {

    /**
     * Constructor de la clase.
     * Inicializa sus atributos.
     *
     * @param estadistico
     * @param estadisticoConsulta
     * @param listaDeEventos
     * @param calculador
     * @param reloj
     * @param m
     */
    public ModuloEjecutorDeSentencias(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, PriorityQueue<Evento> listaDeEventos, CalculadorValoresAleatorios calculador, double reloj, int m){
        super(estadistico, estadisticoConsulta, listaDeEventos, calculador, reloj, m);
        this.cola = new LinkedList<>();
    }

    /**
     * Procesa la llegada de una consulta a este módulo.
     *
     * @param consulta la consulta entrante.
     */
    void procesarLlegada(Consulta consulta){
        consulta.setTipoModulo(TipoModulo.EjecutorDeSentencias);
        consulta.setTiempoIngresoModulo(this.reloj);

        if(numeroServidores == 0){
            //consulta.setTiempoIngresoCola(this.reloj); //Ingresa a la cola
            consulta.setEnCola(true);
            cola.add(consulta);
        }else{
            this.generarSalidaEjecSentencias(consulta);
            numeroServidores--;
        }
    }

    /**
     * Procesa la salida de una consulta de este módulo.
     *
     * @param consulta la consulta saliente.
     */
    void procesarSalida(Consulta consulta){
        estadisticoConsulta.incrementarConsultasRecibidas(3, consulta);
        double tiempoTranscurrido = this.reloj-consulta.getTiempoIngresoModulo();
        estadisticoConsulta.incrementarTiempoConsulta(3, consulta, tiempoTranscurrido);
        //double tiempoRestante = consulta.getTiempoRestante() - tiempoTranscurrido;
        //consulta.setTiempoRestante(tiempoRestante);

        if(cola.size() > 0){ //Si hay gente en la cola.
            Consulta otraConsulta = cola.poll(); //Saco a otra consulta de la cola
            otraConsulta.setEnCola(false); //Cuando se saca de la cola, se desmarca el booleano.
            //otraConsulta.setTiempoEnCola(this.reloj-otraConsulta.getTiempoIngresoCola());
            generarSalidaEjecSentencias(otraConsulta);
        }else{
            numeroServidores++;
        }

        generarSalidaAdmClientes(consulta);

    }

    /**
     * Calcula el tiempo en el que saldrá la consulta del módulo y genera la salida.
     *
     * @param consulta la consulta.
     */
    private void generarSalidaEjecSentencias(Consulta consulta) {
        int bloques = consulta.getB();
        double milisEjec = (double)(bloques * bloques); // cast
        double tiempoEjec = (milisEjec / 1000.00); //Convierto de mili segundos a segundos.
        double tiempoActualizacion = 0.0;
        if (consulta.getTipoConsulta() == TipoConsulta.DDL) {
            tiempoActualizacion = 0.5;
        } else if (consulta.getTipoConsulta() == TipoConsulta.UPDATE) {
            tiempoActualizacion = 1;
        }
        double tiempoEnServicio = tiempoEjec + tiempoActualizacion;
        //consulta.setTiempoEnServicio(tiempoEnServicio);
        double tiempoOcurrencia = tiempoEnServicio + this.reloj;

        this.listaDeEventos.add(new Evento(TipoEvento.SALIDA, TipoModulo.EjecutorDeSentencias, tiempoOcurrencia, consulta));
    }

    /**
     * Genera la entrada al siguente módulo.
     *
     * @param consulta la consulta
     */
    private void generarSalidaAdmClientes(Consulta consulta){
        this.listaDeEventos.add(new Evento(TipoEvento.SALIDA, TipoModulo.ClientesYConexiones, this.reloj, consulta));
    }
}
