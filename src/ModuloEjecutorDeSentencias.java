import java.util.LinkedList;
import java.util.PriorityQueue;

public class ModuloEjecutorDeSentencias extends Modulo {

    public ModuloEjecutorDeSentencias(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, PriorityQueue<Evento> listaDeEventos, CalculadorValoresAleatorios calculador, double reloj, int m, double timeout){
        super(estadistico, estadisticoConsulta, listaDeEventos, calculador, reloj, m, timeout);
        this.cola = new LinkedList<>();
    }

    void procesarLlegada(Consulta consulta){
        consulta.setTiempoIngresoModulo(this.reloj);
        estadisticoConsulta.incrementarConsultasRecibidas(3, consulta);
        if(numeroServidores == 0){
            consulta.setTiempoIngresoCola(this.reloj); //Ingresa a la cola
            cola.add(consulta);
        }else{
            this.generarSalidaEjecSentencias(consulta);
            numeroServidores--;
        }
    }

    void procesarSalida(Consulta consulta){

    }

    private void generarSalidaEjecSentencias(Consulta consulta){
        int bloques = consulta.getB();
        int milisEjec = bloques * bloques;
        double segEjec = milisEjec / 1000; //Convierto de mili segundos a segundos.
        double tiempoActualización = 0.0;
        if(consulta.getTipoConsulta() == TipoConsulta.DDL){
            tiempoActualización = 0.5;
        }else if(consulta.getTipoConsulta() == TipoConsulta.UPDATE){
            tiempoActualización = 1;
        }
        double tiempoTotal = segEjec + tiempoActualización;
    }

    private void generarSalidaAdmClientes(Consulta consulta){

    }
}
