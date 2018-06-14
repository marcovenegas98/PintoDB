public class Evento implements Comparable <Evento> {
    private TipoModulo tipoModulo;
    private double tiempoOcurrencia;
    private Consulta consulta;
    private Boolean llegada;

    public Evento(){

    }

    public int compareTo(Evento ev){
        if(this.tiempoOcurrencia == ev.tiempoOcurrencia)
            return 0;
        else if (this.tiempoOcurrencia > ev.tiempoOcurrencia)
            return 1;
        else
            return -1;
    }

    public TipoModulo getTipoEvento() {
        return tipoModulo;
    }

    public void setTipoModulo(TipoModulo tipoModulo) {
        this.tipoModulo = tipoModulo;
    }

    public double getTiempoOcurrencia() {
        return tiempoOcurrencia;
    }

    public void setTiempoOcurrencia(double tiempoOcurrencia) {
        this.tiempoOcurrencia = tiempoOcurrencia;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }
}
