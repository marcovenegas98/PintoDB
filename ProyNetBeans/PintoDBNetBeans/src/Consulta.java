public class Consulta implements Comparable<Consulta>{
    private TipoConsulta tipoConsulta; //El tipo de sentencia de esta consulta
    private TipoModulo tipoModulo; //El módulo en el que se encuentra esta consulta.
    private boolean enCola;
    private boolean readOnly;
    private double tiempoIngreso;
    //private double tiempoSalida;
    //private double tiempoIngresoCola;
    private double tiempoIngresoModulo;
    //private double tiempoEnCola;
    //private double tiempoEnServicio;
    //private EstadisticoConsulta estadistico;
    private int B;

    public Consulta(TipoConsulta tipoConsulta){
        this.tipoConsulta = tipoConsulta;
        this.obtenerReadOnly();
        //this.estadistico = estadistico;
        this.enCola = false;
        this.B = 0;
       // this.tiempoEnCola = 0;
       // this.tiempoEnServicio = 0;
    }

    public int compareTo(Consulta otraConsulta){
        if(this.tipoConsulta == otraConsulta.tipoConsulta){
            return 0;
        }else if(this.tipoConsulta == TipoConsulta.DDL && (otraConsulta.tipoConsulta == TipoConsulta.JOIN || otraConsulta.tipoConsulta == TipoConsulta.SELECT || otraConsulta.tipoConsulta == TipoConsulta.UPDATE)){
            return 1;
        }else if(this.tipoConsulta == TipoConsulta.UPDATE && (otraConsulta.tipoConsulta == TipoConsulta.JOIN || otraConsulta.tipoConsulta == TipoConsulta.SELECT)){
            return 1;
        }else if(this.tipoConsulta == TipoConsulta.JOIN && (otraConsulta.tipoConsulta == TipoConsulta.SELECT)){
            return 1;
        }else{
            return -1;
        }
    }

    private void obtenerReadOnly(){
        if(tipoConsulta == TipoConsulta.SELECT || tipoConsulta == TipoConsulta.JOIN)
            readOnly = true;
        else
            readOnly = false;
    }

    public TipoConsulta getTipoConsulta(){
        return tipoConsulta;
    }

    public void setTipoConsulta(TipoConsulta tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }


    /*public double getTiempoEnCola() {
        return tiempoEnCola;
    }

    public void setTiempoEnCola(double tiempoEnCola) {
        this.tiempoEnCola = tiempoEnCola;
    }

    public double getTiempoEnServicio() {
        return tiempoEnServicio;
    }

    public void setTiempoEnServicio(double tiempoEnServicio) {
        this.tiempoEnServicio = tiempoEnServicio;
    }*/

    public int getB() {
        return B;
    }

    public void setB(int b) {
        B = b;
    }

    public double getTiempoIngreso() {
        return tiempoIngreso;
    }

    public void setTiempoIngreso(double tiempoIngreso) {
        this.tiempoIngreso = tiempoIngreso;
    }

    /*public double getTiempoSalida() {
        return tiempoSalida;
    }

    public void setTiempoSalida(double tiempoSalida) {
        this.tiempoSalida = tiempoSalida;
    }

    public double getTiempoIngresoCola() {
        return tiempoIngresoCola;
    }

    public void setTiempoIngresoCola(double tiempoIngresoCola) {
        this.tiempoIngresoCola = tiempoIngresoCola;
    }
    */

    public double getTiempoIngresoModulo() {
        return tiempoIngresoModulo;
    }

    public void setTiempoIngresoModulo(double tiempoIngresoModulo) {
        this.tiempoIngresoModulo = tiempoIngresoModulo;
    }

    public TipoModulo getTipoModulo() {
        return tipoModulo;
    }

    public void setTipoModulo(TipoModulo tipoModulo) {
        this.tipoModulo = tipoModulo;
    }

    public boolean isEnCola() {
        return enCola;
    }

    public void setEnCola(boolean enCola) {
        this.enCola = enCola;
    }
}
