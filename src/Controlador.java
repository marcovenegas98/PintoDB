import sun.jvm.hotspot.utilities.SystemDictionaryHelper;

import java.util.Iterator;
import java.util.PriorityQueue;
public class Controlador {
    private SistemaPintoDB sistema;
    private Interfaz interfaz;
    private Estadistico estadistico;
    private EstadisticoConsulta estadisticoConsulta;
    private int contadorCorridas;
    private double tiempoMax;
    private boolean modoLento;
    private double timeout;
    private int[] parametros; //k, n, p, m. En ese orden

    public Controlador(){
        estadistico = new Estadistico();
        estadisticoConsulta = new EstadisticoConsulta();
        contadorCorridas = 0;
    }

    public void crearSistema(){
        //this.parametros = interfaz.getParametros();
        //this.timeout = interfaz.getTimeout();

        //Para pruebas
        for(int i = 0; i < 4; ++i){
            parametros[i] = 20;
        }
        this.tiempoMax = 5000;
        this.timeout = 10;
        
        this.sistema = new SistemaPintoDB(this.estadistico, this.estadisticoConsulta, this.parametros, this.timeout, this.tiempoMax);
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
//        Controlador controlador = new Controlador();
//        controlador.ejecutar();

        PriorityQueue<Integer> cola = new PriorityQueue<>();
        System.out.println(cola.size());


    }
}
