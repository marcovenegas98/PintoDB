public class ModuloAdministradorDeClientesYConexiones extends Modulo {
    private double timeout;
    private int conexionesDescartadas;
    private ModuloAdministradorDeProcesos moduloAdministradorDeProcesos;

    public ModuloAdministradorDeClientesYConexiones(){
        conexionesDescartadas = 0;
    }

    void procesarLlegada(){
        if(numeroServidores == 0){
            conexionesDescartadas++;
        }else{
            moduloAdministradorDeProcesos.generarEntradaProcConsultas();
            numeroServidores--;
        }
    }

    void procesarSalida(){

    }

    private void cerrarConexion(){

    }

    private void generarLlegadaAdmClientes(){

    }

    private void generarLlegadaAdmProcesos(){

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
