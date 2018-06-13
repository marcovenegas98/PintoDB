public class ModuloAdministradorDeClientesYConexiones extends Modulo {
    private double timeout;
    private int conexionesDescartadas;

    public ModuloAdministradorDeClientesYConexiones(){

    }

    void procesarLlegada(){

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
