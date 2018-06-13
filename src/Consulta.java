public class Consulta {
    private TipoConsulta tipoConsulta;
    private boolean readOnly;
    private double tiempoRestante;
    private double tiempoEnCola;
    private double tiempoEnServicio;
    private EstadisticoConsulta estadistico;
    private int B;

    public Consulta(){

    }

    public TipoConsulta getTipoConsulta() {
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

    public double getTiempoRestante() {
        return tiempoRestante;
    }

    public void setTiempoRestante(double tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }

    public double getTiempoEnCola() {
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
    }

    public int getB() {
        return B;
    }

    public void setB(int b) {
        B = b;
    }
}
