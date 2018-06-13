public class Evento {
    private TipoEvento tipoEvento;
    private double tiempoOcurrencia;
    private Consulta consulta;

    public Evento(){

    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
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
