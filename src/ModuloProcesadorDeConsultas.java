import java.util.LinkedList;
import java.util.PriorityQueue;

public class ModuloProcesadorDeConsultas extends Modulo {

    public ModuloProcesadorDeConsultas(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, PriorityQueue<Evento> listaDeEventos, CalculadorValoresAleatorios calculador, double reloj, int n, double timeout){
        super(estadistico, estadisticoConsulta, listaDeEventos, calculador, reloj, n, timeout);
        this.cola = new LinkedList<>();
    }

    void procesarLlegada(Consulta consulta){
        consulta.setTiempoIngresoModulo(this.reloj);
        estadisticoConsulta.incrementarConsultasRecibidas(1, getCasillaParaEstadistico(consulta));
        if(numeroServidores == 0){
            consulta.setTiempoIngresoCola(this.reloj); //Ingresa a la cola
            cola.add(consulta);
        }else{
            this.generarSalidaProcConsultas(consulta);
            numeroServidores--;
        }
    }

    void procesarSalida(Consulta consulta){
        double tiempoTranscurrido = this.reloj-consulta.getTiempoIngresoModulo();
        estadisticoConsulta.incrementarTiempoConsulta(1, getCasillaParaEstadistico(consulta), tiempoTranscurrido);
        double tiempoRestante = consulta.getTiempoRestante() - tiempoTranscurrido;
        consulta.setTiempoRestante(tiempoRestante);

        if(cola.size() > 0){ //Si hay gente en la cola.
            Consulta otraConsulta = cola.poll(); //Saco a otra consulta de la cola
            otraConsulta.setTiempoEnCola(this.reloj-otraConsulta.getTiempoIngresoCola());
            generarSalidaProcConsultas(otraConsulta);
        }else{
            numeroServidores++;
        }
        if(tiempoRestante <= 0){ //timeout
            this.listaDeEventos.add(new Evento(TipoEvento.TIMEOUT, TipoModulo.ClientesYConexiones, this.reloj, consulta));
        }else {
            generarEntradaTAD(consulta);
        }
    }

    private void generarSalidaProcConsultas(Consulta consulta){
        // Validación Lexica
        double tiempoValLexica = 0.1; //
        // Validación Sintactica
        double tiempoValSintactica = calculador.genValorUniformeContinuo(0,1);
        // Validacion Semantica
        double tiempoValSemantica = calculador.genValorUniformeContinuo(0,2);
        // Verifiacion de Permisos
        double tiempoVerifPermisos = calculador.genValorExponencial(0.7);
        // Optimización de Consultas
        double tiempoOptimizacionConsultas;
        if(consulta.isReadOnly())
            tiempoOptimizacionConsultas = 0.1;
        else
            tiempoOptimizacionConsultas = 0.25;
        // TIEMPO TOTAL
        double tiempoEnServicio = tiempoValLexica + tiempoValSintactica + tiempoValSemantica + tiempoVerifPermisos + tiempoOptimizacionConsultas;
        consulta.setTiempoEnServicio(tiempoEnServicio);
        double tiempoOcurrencia = tiempoEnServicio + this.reloj;

        this.listaDeEventos.add(new Evento(TipoEvento.SALIDA, TipoModulo.ProcesadorDeConsultas, tiempoOcurrencia, consulta));
    }

    private void generarEntradaTAD(Consulta consulta){
        listaDeEventos.add(new Evento(TipoEvento.ENTRADA, TipoModulo.TransaccionYAccesoADatos, this.reloj, consulta));
    }

}
