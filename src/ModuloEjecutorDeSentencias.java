import java.util.LinkedList;
import java.util.PriorityQueue;

public class ModuloEjecutorDeSentencias extends Modulo {

    public ModuloEjecutorDeSentencias(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, PriorityQueue<Evento> listaDeEventos, CalculadorValoresAleatorios calculador, double reloj, int m){
        super(estadistico, estadisticoConsulta, listaDeEventos, calculador, reloj, m);
        this.cola = new LinkedList<>();
    }

    void procesarLlegada(Consulta consulta){
        consulta.setTipoModulo(TipoModulo.EjecutorDeSentencias);
        consulta.setTiempoIngresoModulo(this.reloj);
        estadisticoConsulta.incrementarConsultasRecibidas(3, consulta);
        if(numeroServidores == 0){
            //consulta.setTiempoIngresoCola(this.reloj); //Ingresa a la cola
            consulta.setEnCola(true);
            cola.add(consulta);
        }else{
            this.generarSalidaEjecSentencias(consulta);
            numeroServidores--;
        }
    }

    void procesarSalida(Consulta consulta){
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

    private void generarSalidaEjecSentencias(Consulta consulta) {
        int bloques = consulta.getB();
        int milisEjec = bloques * bloques;
        double tiempoEjec = (milisEjec / 1000); //Convierto de mili segundos a segundos.
        double tiempoActualización = 0.0;
        if (consulta.getTipoConsulta() == TipoConsulta.DDL) {
            tiempoActualización = 0.5;
        } else if (consulta.getTipoConsulta() == TipoConsulta.UPDATE) {
            tiempoActualización = 1;
        }
        double tiempoEnServicio = tiempoEjec + tiempoActualización;
        //consulta.setTiempoEnServicio(tiempoEnServicio);
        double tiempoOcurrencia = tiempoEnServicio + this.reloj;

        this.listaDeEventos.add(new Evento(TipoEvento.SALIDA, TipoModulo.EjecutorDeSentencias, tiempoOcurrencia, consulta));
    }

    private void generarSalidaAdmClientes(Consulta consulta){
        this.listaDeEventos.add(new Evento(TipoEvento.SALIDA, TipoModulo.ClientesYConexiones, this.reloj, consulta));
    }
}
