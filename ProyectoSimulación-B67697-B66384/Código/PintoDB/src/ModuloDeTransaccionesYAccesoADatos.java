import java.util.PriorityQueue;

/**
 * Clase ModuloDeTransaccionesYAccesoADatos.
 * Simula el comportamiento de este módulo en el DBMS.
 */
public class ModuloDeTransaccionesYAccesoADatos extends Modulo {

    private double tiempoCoordinacion;

    //Variables para controlar que el DDL se debe ejecutar solo
    private int DDLsEsperando;
    private double tiempoUltimaSalida;

    /**
     * Constructor de la clase.
     * Inicializa sus atributos.
     *
     * @param estadistico
     * @param estadisticoConsulta
     * @param listaDeEventos
     * @param calculador
     * @param reloj
     * @param p
     */
    public ModuloDeTransaccionesYAccesoADatos(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, PriorityQueue<Evento> listaDeEventos, CalculadorValoresAleatorios calculador, double reloj, int p){
        super(estadistico, estadisticoConsulta, listaDeEventos, calculador, reloj, p);
        this.cola = new PriorityQueue<>();
        this.DDLsEsperando = 0;
        this.tiempoUltimaSalida = this.reloj;
        this.setTiempoCoordinacion();
    }

    /**
     * Procesa la llegada de una consulta a este módulo.
     *
     * @param consulta la consulta entrante.
     */
    void procesarLlegada(Consulta consulta){
        consulta.setTipoModulo(TipoModulo.TransaccionYAccesoADatos);
        consulta.setTiempoIngresoModulo(this.reloj);
        if(numeroServidores == 0 || DDLsEsperando > 0){ //Si llega un proceso mientras se esta procesando un DDL se mete a la cola
            consulta.setEnCola(true);
            cola.add(consulta);
        }else{
            this.generarSalidaTAD(consulta);
            numeroServidores--;
        }
    }

    /**
     * Procesa la salida de una consulta de este módulo.
     *
     * @param consulta la consulta saliente.
     */
    void procesarSalida(Consulta consulta){
        estadisticoConsulta.incrementarConsultasRecibidas(2, consulta);
        double tiempoTranscurrido = this.reloj-consulta.getTiempoIngresoModulo();
        estadisticoConsulta.incrementarTiempoConsulta(2, consulta, tiempoTranscurrido);

        if(consulta.getTipoConsulta() == TipoConsulta.DDL){
            DDLsEsperando--;
        }
        if(cola.size() > 0){ //Si hay gente en la cola.
            Consulta otraConsulta = cola.poll(); //Saco a otra consulta de la cola
            otraConsulta.setEnCola(false); //Cuando se saca de la cola, se desmarca el booleano.
            generarSalidaTAD(otraConsulta);
        }else{
            numeroServidores++;
        }

        generarEntradaEjecSentencias(consulta);
    }

    /**
     * Calcula el tiempo en el que saldrá la consulta del módulo y genera la salida.
     *
     * @param consulta la consulta.
     */
    private void generarSalidaTAD(Consulta consulta){
        double tiempoCargaBloques = 0;
        if(consulta.getTipoConsulta() == TipoConsulta.JOIN){
            int bloques = this.calculador.genValorUniformeDiscreto(); //Valor uniforme discreto entre 1 y 64
            consulta.setB(bloques);
            tiempoCargaBloques = bloques * (1/10);
        }else if(consulta.getTipoConsulta() == TipoConsulta.SELECT){
            tiempoCargaBloques = 1/10; //Solo carga 1 bloque.
            consulta.setB(1);
        }

        double tiempoEnServicio = tiempoCoordinacion + tiempoCargaBloques;
        //consulta.setTiempoEnServicio(tiempoEnServicio);
        double tiempoOcurrencia = tiempoEnServicio + this.reloj;
        if(consulta.getTipoConsulta() == TipoConsulta.DDL){ //Si la consulta que va a salir es DDL
            DDLsEsperando++; //Una DDL más esperando para salir.
            if(tiempoUltimaSalida >= this.reloj){
                tiempoOcurrencia = tiempoEnServicio + tiempoUltimaSalida; //El DDL sale hasta que se procese la ultima salida de este modulo antes de esta.
            }
        }
        this.tiempoUltimaSalida = tiempoOcurrencia; //Se actualiza el tiempo en el que sale la última consulta de este módulo.
        this.listaDeEventos.add(new Evento(TipoEvento.SALIDA, TipoModulo.TransaccionYAccesoADatos, tiempoOcurrencia, consulta));
    }

    /**
     * Genera la entrada al siguente módulo.
     *
     * @param consulta la consulta
     */

    private void generarEntradaEjecSentencias(Consulta consulta){
        this.listaDeEventos.add(new Evento(TipoEvento.ENTRADA, TipoModulo.EjecutorDeSentencias, this.reloj, consulta));
    }

    /**
     * Asignar el tiempo de coordinación de este módulo.
     */
    public void setTiempoCoordinacion(){
        this.tiempoCoordinacion = this.NUMSERVIDORES * 0.03;
    }

    /**
     * Pone la cantidad de DDL esperando en 0.
     */
    public void resetDDLsEsperando(){
        this.DDLsEsperando = 0;
    }
}
