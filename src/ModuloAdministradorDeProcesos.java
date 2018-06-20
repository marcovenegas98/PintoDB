import java.util.LinkedList;
import java.util.PriorityQueue;

public class ModuloAdministradorDeProcesos extends Modulo {

    public ModuloAdministradorDeProcesos(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, PriorityQueue<Evento> listaDeEventos, CalculadorValoresAleatorios calculador, double reloj){
        super(estadistico, estadisticoConsulta, listaDeEventos, calculador, reloj,1);//Solo tiene un servidor
        this.cola = new LinkedList<>();
    }

    void procesarLlegada(Consulta consulta){
        consulta.setTipoModulo(TipoModulo.AdministradorDeProcesos); //Consulta en el módulo admin de procesos.
        consulta.setTiempoIngresoModulo(this.reloj); //Ingresó al módulo en este momento.
        estadisticoConsulta.incrementarConsultasRecibidas(0, consulta); //Una consulta más recibida en este módulo.
        if(numeroServidores == 0){
            //consulta.setTiempoIngresoCola(this.reloj); //Ingresa a la cola
            consulta.setEnCola(true); //Cuando se mete a la cola, se marca el booleano
            this.cola.add(consulta);
        }else{
            this.generarSalidaAdmProcesos(consulta);
            this.numeroServidores--;
        }
    }

    void procesarSalida(Consulta consulta){
        double tiempoTranscurrido = this.reloj-consulta.getTiempoIngresoModulo();
        estadisticoConsulta.incrementarTiempoConsulta(0, consulta, tiempoTranscurrido);
        //double tiempoRestante = consulta.getTiempoRestante() - tiempoTranscurrido;
        //consulta.setTiempoRestante(tiempoRestante);

        if(cola.size() > 0){ //Si hay gente en la cola.
            Consulta otraConsulta = cola.poll(); //Saco a otra consulta de la cola
            otraConsulta.setEnCola(false); //Cuando se saca de la cola, se desmarca el booleano.
            //otraConsulta.setTiempoEnCola(this.reloj-otraConsulta.getTiempoIngresoCola());
            generarSalidaAdmProcesos(otraConsulta);
        }else{
            numeroServidores++;
        }
        generarEntradaProcConsultas(consulta); //Si hay timeout después de esta salida, la entrada recién generada se borra de la lista de eventos.
    }

    private void generarSalidaAdmProcesos(Consulta consulta){
        double tiempoEnServicio = calculador.genValorRandomNormal(); //Valor normal
        //consulta.setTiempoEnServicio(tiempoEnServicio); //Este es el tiempo que duran en atenderme
        double tiempoOcurrencia = tiempoEnServicio + this.reloj; //La salida se da en el tiempo actual + el que duren en atenderme

        this.listaDeEventos.add(new Evento(TipoEvento.SALIDA, TipoModulo.AdministradorDeProcesos, tiempoOcurrencia, consulta));
    }

    private void generarEntradaProcConsultas(Consulta consulta){
        //Se genera la entrada en el mismo tiempo de reloj pues este método solo se llama cuando se procesa una salida y el tiempo
        //de paso de una consulta de un módulo a otro es cero.
        listaDeEventos.add(new Evento(TipoEvento.ENTRADA, TipoModulo.ProcesadorDeConsultas, this.reloj, consulta));
    }
}
