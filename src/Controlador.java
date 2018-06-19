public class Controlador {
    private SistemaPintoDB sistema;
    private Interfaz interfaz;
    private Estadistico estadistico;
    private EstadisticoConsulta estadisticoConsulta;
    private int contadorCorridas;
    private double tiempoMax;
    private boolean modoLento;
    private int[] servidoresPorModulo; //k, n, p, m, t

    public Controlador(){
        estadistico = new Estadistico();
        estadisticoConsulta = new EstadisticoConsulta();
        contadorCorridas = 0;
    }

    public void crearSistema(){
        //this.servidoresPorModulo = interfaz.getServidoresPorModulo();
        this.sistema = new SistemaPintoDB(estadistico, estadisticoConsulta, servidoresPorModulo);
    }

    public void setTiempoMax(double tiempoMax) {
        this.tiempoMax = tiempoMax;
    }

    public double getTiempoMax() {
        return tiempoMax;
    }

    public boolean isModoLento() {
        return modoLento;
    }

    public void setModoLento(boolean modoLento) {
        this.modoLento = modoLento;
    }

    public void ejecutar(){

    }

    public static void main(String[] args){
        System.out.println("Hello World\n");
    }
}
