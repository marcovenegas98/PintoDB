import java.util.PriorityQueue;

public class ModuloAdministradorDeClientesYConexiones extends Modulo {
    private int conexionesDescartadas;

    public ModuloAdministradorDeClientesYConexiones(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, PriorityQueue<Evento> listaDeEventos, CalculadorValoresAleatorios calculador, double reloj, int k, double timeout){
        super(estadistico, estadisticoConsulta, listaDeEventos, calculador, reloj, k, timeout);
        conexionesDescartadas = 0;
    }

    public void procesarLlegada(Consulta consulta){
        if(numeroServidores == 0){
            conexionesDescartadas++;
        }else{
            numeroServidores--;
            generarLlegadaAdmProcesos(consulta);
        }
        this.generarLlegadaAdmClientes();
    }

    //Cuando sale de aquí, ya pasó por el resto de módulos.
    void procesarSalida(Consulta consulta){
        //Estadísticas.
        numeroServidores++;
    }


    public void cerrarConexion(Consulta consulta){

    }

    private void generarLlegadaAdmClientes(){
        double tiempo = calculador.genValorExponencial(2); // 30 conexiones por min = 1 conex cada 2 seg
        TipoConsulta tipo = calculador.genMonteCarloConsulta();
        listaDeEventos.add(new Evento(TipoEvento.ENTRADA, TipoModulo.ClientesYConexiones, this.reloj + tiempo, new Consulta(tipo, timeout, this.estadisticoConsulta)));
    }

    private void generarLlegadaAdmProcesos(Consulta c){
        listaDeEventos.add(new Evento(TipoEvento.ENTRADA, TipoModulo.ProcesadorDeConsultas, this.reloj, c));
    }

    public double getTimeout(){
        return this.timeout;
    }

    public void setTimeout(double timeout){
        this.timeout = timeout;
    }

    public int getConexionesDescartadas(){
        return conexionesDescartadas;
    }
}
