public class Evento implements Comparable <Evento> {
    private TipoModulo tipoModulo;
    private double tiempoOcurrencia;
    private Consulta consulta;
    private boolean llegada;

    public Evento(){

    }

    public Evento(TipoModulo tipoModulo, double tiempoOcurrencia, Consulta consulta, boolean llegada){
        this.tipoModulo = tipoModulo;
        this.tiempoOcurrencia = tiempoOcurrencia;
        this.consulta = consulta;
        this.llegada = llegada;
    }

    public int compareTo(Evento ev){
        if(this.tiempoOcurrencia == ev.tiempoOcurrencia)
            return 0;
        else if (this.tiempoOcurrencia > ev.tiempoOcurrencia)
            return 1;
        else
            return -1;
    }

    public TipoModulo getTipoModulo() {
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

    public boolean isLlegada() {
        return llegada;
    }
}
