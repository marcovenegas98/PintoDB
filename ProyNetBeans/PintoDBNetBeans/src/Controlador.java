//import sun.jvm.hotspot.utilities.SystemDictionaryHelper;

import java.util.Scanner;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.concurrent.Semaphore;

public class Controlador {

    private SistemaPintoDB sistema;
    private Interfaz interfaz;
    private Estadistico estadistico;
    private EstadisticoConsulta estadisticoConsulta;
    private int cantidadCorridas;
    private double tiempoMax;
    private boolean modoLento;
    private double timeout;
    private int[] parametros; //k, n, p, m. En ese orden
    public Semaphore semEjecucion;

    private boolean exit;

    public Controlador() {
        estadistico = new Estadistico();
        estadisticoConsulta = new EstadisticoConsulta();
        this.parametros = new int[4];
        this.semEjecucion = new Semaphore(0);
        this.interfaz = new Interfaz(this, this.semEjecucion);
        this.exit = false;
    }

    public void ejecutar() throws InterruptedException {
        try {
            semEjecucion.acquire();
        } catch (InterruptedException e) {}
        this.crearSistema();
        while (!exit) {
            for (int i = 0; i < cantidadCorridas; ++i) {
                this.sistema.simular();
                this.semEjecucion.release();
                //Después de cada corrida:
                this.calcularEstadisticasCorrida();
                this.interfaz.corridaTerminada();
                this.resetParcial();
                try { //Espero a que se presione el botón continuar
                    semEjecucion.acquire();
                } catch (InterruptedException e) {}
            } //Simulo de nuevo si quedan corridas
            this.semEjecucion.release(); //Si ya acabó y no hay más corridas, le hago signal a la interfaz.
            //Cuando ya finalizaron todas las corridas.
            this.interfaz.finSimulacion();
            //Espero a ver si el usuario decide salir o simular de nuevo.
            try {
                semEjecucion.acquire();
            } catch (InterruptedException e) {}
            if(!exit){
                //Si la interfaz me hizo signal y mi exit sigue en false, es porque simulo de nuevo
                this.reset(); //Reinicio todo.
            }
        }

    }

    private void calcularEstadisticasCorrida() {
        //Calculo los promedios y los acumulo en los estadísticos.
        this.estadistico.incrementarConexionesDescartadas(this.sistema.getConexionesDescartadas());
        double[] tamanosPromedioColas = this.estadistico.calcularLQs();
        this.estadistico.incrementarAcumuladoTamanosPromediosColas(tamanosPromedioColas);

        double tiempoPromVida = this.estadisticoConsulta.calcularTiempoPromedioDeVida();
        this.estadisticoConsulta.incrementarAcumuladoDeTiemposDeVidaPromedio(tiempoPromVida);

        double[][] tiempoPromedioConsultaPorModulo = this.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo();
        this.estadisticoConsulta.incrementarAcumuladoTiemposPromedioConsultasPorModulo(tiempoPromedioConsultaPorModulo);
    }

    private void resetParcial() {
        //Reseteo todo excepto los valores acumulados para poder realizar otra corrida.
        this.estadistico.resetParcial();
        this.estadisticoConsulta.resetParcial();
        this.sistema.resetSistemaParcial(); //Reloj en cero, conexiones descartadas en 0, colas vacías.
        this.interfaz.actualizarInteractivo();
    }

    private void reset() {
        this.estadistico.reset();
        this.estadisticoConsulta.reset();
        this.sistema.resetSistema(this.parametros, this.timeout, this.tiempoMax, this.modoLento);
    }

    private void crearSistema() {
        this.sistema = new SistemaPintoDB(this.interfaz, this.semEjecucion, this.estadistico, this.estadisticoConsulta, this.parametros, this.timeout, this.tiempoMax, this.modoLento);
        this.interfaz.setSistema(sistema);
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

    public void setTiempoMax(double tiempoMax) {
        this.tiempoMax = tiempoMax;
    }

    public void setCantidadCorridas(int cantidadCorridas) {
        this.cantidadCorridas = cantidadCorridas;
    }

    public void setParametros(int[] parametros) {
        for (int i = 0; i < 4; ++i) {
            this.parametros[i] = parametros[i];
        }
    }

    public void setTimeout(double timeout) {
        this.timeout = timeout;
    }

    public double[] getLQs() {
        return this.estadistico.calcularLQs();
    }

    public double[] getLQsTotales() {
        return this.estadistico.getTamanosPromediosColasTotalCorridas(cantidadCorridas);
    }

    public double[][] getTiemposPasadosPorConsultaPorModulo() {
        return this.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo();
    }

    public double[][] getTiemposPasadosPorConsultaPorModuloTotales() {
        return this.estadisticoConsulta.getTiempoPromedioConsultasPorModuloTotalCorridas(cantidadCorridas);
    }

    public double getTiempoPromedioVidaConexion() {
        return this.estadisticoConsulta.calcularTiempoPromedioDeVida();
    }

    public double getTiempoPromedioVidaConexionTotal() {
        return this.estadisticoConsulta.getTiempoPromedioDeVidaTotalCorridas(cantidadCorridas);
    }

    public double getConexionesDescartadasPromedioTotal() {
        return this.estadistico.getPromedioConexionesDescartadasTotalCorridas(cantidadCorridas);
    }

    public int getCorridas() {
        return this.cantidadCorridas;
    }
    
    public void setExit(boolean exit){
        this.exit = exit;
    }

    public static void main(String[] args) throws InterruptedException {
        Controlador controlador = new Controlador();
        controlador.ejecutar();
    }

}
