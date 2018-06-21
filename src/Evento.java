public class Evento implements Comparable <Evento> {
    private TipoModulo tipoModulo;
    private TipoEvento tipoEvento;
    private double tiempoOcurrencia;
    private Consulta consulta;

    public Evento(TipoEvento tipoEvento, TipoModulo tipoModulo, double tiempoOcurrencia, Consulta consulta){
        this.tipoEvento = tipoEvento;
        this.tipoModulo = tipoModulo;
        this.tiempoOcurrencia = tiempoOcurrencia;
        this.consulta = consulta;
    }

    public int compareTo(Evento ev){
        //Si son eventos con la misma consulta, compara en base al tipo de evento, no en base al tiempo de ocurrencia.
        if(this.tiempoOcurrencia == ev.tiempoOcurrencia) { //Si hay dos eventos con el mismo tiempo de reloj
            return 0;
        }
        else if (this.tiempoOcurrencia > ev.tiempoOcurrencia) {
            return 1;
        }
        else {
            return -1;
        }
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
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


}
