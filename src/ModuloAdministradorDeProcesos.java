import java.util.LinkedList;
import java.util.PriorityQueue;

public class ModuloAdministradorDeProcesos extends Modulo {

    public ModuloAdministradorDeProcesos(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, PriorityQueue<Evento> listaDeEventos, CalculadorValoresAleatorios calculador, double reloj, double timeout){
        super(estadistico, estadisticoConsulta, listaDeEventos, calculador, reloj,1, timeout);//Solo tiene un servidor
        this.cola = new LinkedList<>();
    }

    void procesarLlegada(Consulta consulta){
        consulta.setTiempoIngresoModulo(this.reloj);
        estadisticoConsulta.incrementarConsultasRecibidas(0, getCasillaParaEstadistico(consulta));
        if(numeroServidores == 0){
            consulta.setTiempoIngresoCola(this.reloj); //Ingresa a la cola
            cola.add(consulta);
        }else{
            this.generarSalidaAdmProcesos(consulta);
            numeroServidores--;
        }
    }

    void procesarSalida(Consulta consulta){
        double tiempoTranscurrido = this.reloj-consulta.getTiempoIngresoModulo();
        estadisticoConsulta.incrementarTiempoConsulta(0, getCasillaParaEstadistico(consulta), tiempoTranscurrido);
        double tiempoRestante = consulta.getTiempoRestante() - tiempoTranscurrido;
        consulta.setTiempoRestante(tiempoRestante);

        if(cola.size() > 0){ //Si hay gente en la cola.
            Consulta otraConsulta = cola.poll(); //Saco a otra consulta de la cola
            otraConsulta.setTiempoEnCola(this.reloj-otraConsulta.getTiempoIngresoCola());
            generarSalidaAdmProcesos(otraConsulta);
        }else{
            numeroServidores++;
        }
        if(tiempoRestante <= 0){ //timeout
            this.listaDeEventos.add(new Evento(TipoEvento.TIMEOUT, TipoModulo.ClientesYConexiones, this.reloj, consulta));
        }else {
            generarEntradaProcConsultas(consulta);
        }
    }

    private void generarSalidaAdmProcesos(Consulta consulta){
        double tiempoEnServicio = calculador.genValorRandomNormal(); //Valor normal
        consulta.setTiempoEnServicio(tiempoEnServicio); //Este es el tiempo que duran en atenderme
        double tiempoOcurrencia = tiempoEnServicio + this.reloj; //La salida se da en el tiempo actual + el que duren en atenderme

        this.listaDeEventos.add(new Evento(TipoEvento.SALIDA, TipoModulo.AdministradorDeProcesos, tiempoOcurrencia, consulta));
    }

    private void generarEntradaProcConsultas(Consulta consulta){
        listaDeEventos.add(new Evento(TipoEvento.ENTRADA, TipoModulo.ProcesadorDeConsultas, this.reloj, consulta));
    }
}
