import java.util.PriorityQueue;
public class ModuloDeTransaccionesYAccesoADatos extends Modulo {

    public ModuloDeTransaccionesYAccesoADatos(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, PriorityQueue<Evento> listaDeEventos, CalculadorValoresAleatorios calculador, double reloj, int p, double timeout){
        super(estadistico, estadisticoConsulta, listaDeEventos, calculador, reloj, p, timeout);
        this.cola = new PriorityQueue<>();
    }

    void procesarLlegada(Consulta consulta){
        
    }

    void procesarSalida(Consulta consulta){

    }

    private void generarSalidaTAD(){

    }

    private void generarEntradaEjecSentencias(){

    }
}
