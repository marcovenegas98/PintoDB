import java.util.PriorityQueue;
public class ModuloDeTransaccionesYAccesoADatos extends Modulo {

    public ModuloDeTransaccionesYAccesoADatos(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, PriorityQueue<Evento> listaDeEventos, CalculadorValoresAleatorios calculador, double reloj, int p, double timeout){
        super(estadistico, estadisticoConsulta, listaDeEventos, calculador, reloj, p, timeout);
        this.cola = new PriorityQueue<>();
    }

    void procesarLlegada(Consulta consulta){
        consulta.setTiempoIngresoModulo(this.reloj);
        estadisticoConsulta.incrementarConsultasRecibidas(2, consulta);
        if(numeroServidores == 0){
            consulta.setTiempoIngresoCola(this.reloj); //Ingresa a la cola
            cola.add(consulta);
        }else{
            this.generarSalidaTAD(consulta);
            numeroServidores--;
        }
    }

    void procesarSalida(Consulta consulta){

    }

    private void generarSalidaTAD(Consulta consulta){

    }

    private void generarEntradaEjecSentencias(Consulta consulta){

    }
}
