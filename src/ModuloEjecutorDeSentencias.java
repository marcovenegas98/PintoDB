import java.util.PriorityQueue;

public class ModuloEjecutorDeSentencias extends Modulo {

    public ModuloEjecutorDeSentencias(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, PriorityQueue<Evento> listaDeEventos, CalculadorValoresAleatorios calculador, double reloj, int m, double timeout){
        super(estadistico, estadisticoConsulta, listaDeEventos, calculador, reloj, m, timeout);
    }

    void procesarLlegada(Consulta consulta){

    }

    void procesarSalida(Consulta consulta){

    }

    private void generarSalidaEjecSentencias(){

    }

    private void generarSalidaAdmClientes(){

    }
}
