public class ModuloAdministradorDeClientesYConexiones extends Modulo {
    private double timeout;
    private int conexionesDescartadas;
    private ModuloAdministradorDeProcesos moduloAdministradorDeProcesos;
    private TipoConsulta tipoConsulta;

    public ModuloAdministradorDeClientesYConexiones(){
        conexionesDescartadas = 0;
    }

    void procesarLlegada(){
        if(numeroServidores == 0){
            conexionesDescartadas++;
        }else{
            generarLlegadaAdmProcesos();
            numeroServidores--;
        }
        tipoConsulta = calculador.genMonteCarloConsulta();
        this.generarLlegadaAdmClientes();
    }

    void procesarSalida(){
        numeroServidores++;
    }

    private void cerrarConexion(){

    }

    private void generarLlegadaAdmClientes(){
        double llegada = calculador.genValorExponencial(2); // 30 conexiones por min = 1 conex cada 2 seg
        listaDeEventos.add(new Evento(TipoModulo.ClientesYConexiones,llegada + sistemaPintoDB.reloj, new Consulta(tipoConsulta, timeout) , true ));
    }

    private void generarLlegadaAdmProcesos(){
        listaDeEventos.add(new Evento(TipoModulo.ProcesadorDeConsultas, sistemaPintoDB.reloj, new Consulta(tipoConsulta, timeout),true));
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
