/**
 * Clase Controlador, es el centro del programa, aquí se encuentra el main.
 */

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

    /**
     * Constructor de la clase Contolador, inicializa los componenentes.
     */
    public Controlador() {
        estadistico = new Estadistico();
        estadisticoConsulta = new EstadisticoConsulta();
        this.parametros = new int[4];
        this.semEjecucion = new Semaphore(0);
        this.interfaz = new Interfaz(this, this.semEjecucion);
        this.exit = false;
    }

    /**
     * El "verdadero main"
     * Muestra una interfaz de usuario gráfica, donde el usuario ingresa los
     * parámetros para la simulación. Aquí se crea el sistema que realizará
     * la simulación y se asignan dichos parámetros. Se actualiza la interfaz
     * y al final se le da la opción al usuario de salir o de crear una nueva
     * simulación.
     * @throws InterruptedException
     */
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

    /**
     * Se comunica con ambos estadísticos para que calculen las estadísticas
     * al final de cada corrida y suman los resultados a sus respectivas
     * variables acumulativas que se usarán al final para calcular el promedio
     * de las estadísticas por cada corrida.
     */
    private void calcularEstadisticasCorrida() {
        //Calculo los promedios y los acumulo en los estadísticos.
        this.estadistico.incrementarConexionesDescartadas(this.sistema.getConexionesDescartadas());
        double[] tamanosPromedioColas = this.estadistico.calcularLQs();
        this.estadistico.incrementarAcumuladoTamanosPromediosColas(tamanosPromedioColas);

        double tiempoPromVida = this.estadisticoConsulta.calcularTiempoPromedioDeVida();
        this.estadisticoConsulta.incrementarAcumuladoDeTiemposDeVidaPromedio(tiempoPromVida);
        this.estadisticoConsulta.agregarTiempoPromedioDeVida(tiempoPromVida);

        double[][] tiempoPromedioConsultaPorModulo = this.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo();
        this.estadisticoConsulta.incrementarAcumuladoTiemposPromedioConsultasPorModulo(tiempoPromedioConsultaPorModulo);
    }

    /**
     * Se utiliza después de cada corrida.
     * Reinicia los valores de los estadísticos y el sistema que solamente tienen que ver
     * con una corrida, para que se pueda simular otra corrida sin afectar los resultados.
     */
    private void resetParcial() {
        //Reseteo todo excepto los valores acumulados para poder realizar otra corrida.
        this.estadistico.resetParcial();
        this.estadisticoConsulta.resetParcial();
        this.sistema.resetSistemaParcial(); //Reloj en cero, conexiones descartadas en 0, colas vacías.
        this.interfaz.actualizarInteractivo();
    }

    /**
     * Se utiliza al final de todas las corridas, en el caso de que el usuario quiera
     * volver a realizar otra simulación.
     */
    private void reset() {
        this.estadistico.reset();
        this.estadisticoConsulta.reset();
        this.sistema.resetSistema(this.parametros, this.timeout, this.tiempoMax, this.modoLento);
    }

    /**
     * Se utiliza cuando el usuario ya ingresó los parámetros por medio de la
     * interfaz gráfica. Se crea un sistema con dichos parámetros y se le asigna
     * la misma interfaz para que el sistema pueda comunicarse con ella.
     */
    private void crearSistema() {
        this.sistema = new SistemaPintoDB(this.interfaz, this.semEjecucion, this.estadistico, this.estadisticoConsulta, this.parametros, this.timeout, this.tiempoMax, this.modoLento);
        this.interfaz.setSistema(sistema);
    }

    /**
     * Se asigna el valor ingresado a la variable modoLento.
     *
     * @param modoLento el booleano a asignar.
     */
    public void setModoLento(boolean modoLento) {
        this.modoLento = modoLento;
    }

    /**
     * Se asigna el valor ingresado a la variable tiempoMax.
     * Representa la duración de cada corrida de la simulación.
     *
     * @param tiempoMax el double a asignar.
     */
    public void setTiempoMax(double tiempoMax) {
        this.tiempoMax = tiempoMax;
    }

    /**
     * Se asigna el valor ingresado a la variable cantidadCorridas.
     *
     * @param cantidadCorridas el entero a asignar.
     */
    public void setCantidadCorridas(int cantidadCorridas) {
        this.cantidadCorridas = cantidadCorridas;
    }

    /**
     * Se asigna el valor ingresado a la variable parametros.
     *
     * @param parametros un vector de enteros que contiene los parámetros.
     */
    public void setParametros(int[] parametros) {
        for (int i = 0; i < 4; ++i) {
            this.parametros[i] = parametros[i];
        }
    }

    /**
     * Se asigna el valor ingresado a la variable timeout.
     *
     * @param timeout el double a asignar.
     */
    public void setTimeout(double timeout) {
        this.timeout = timeout;
    }

    /**
     * Devuelve los tamaños promedios de las colas, se utiliza después de cada corrida.
     *
     * @return Los tamaños promedios de las colas en cada módulo.
     */
    public double[] getLQs() {
        return this.estadistico.calcularLQs();
    }

    /**
     * Devuelve los tamaños promedios de las colas como un promedio entre el total de corridas.
     *
     * @return Los tamaños promedios de las colas en cada módulo.
     */
    public double[] getLQsTotales() {
        return this.estadistico.getTamanosPromediosColasTotalCorridas(cantidadCorridas);
    }

    /**
     * Devuelve una matriz que contiene los tiempos promedios por los que pasó
     * cada tipo de consulta por cada módulo.
     *
     * @return Los tiempos promedio pasados por consulta por módulo.
     */
    public double[][] getTiemposPasadosPorConsultaPorModulo() {
        return this.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo();
    }

    /**
     * Devuelve una matriz que contiene los tiempos promedios por los que pasó
     * cada tipo de consulta por cada módulo como un promedio entre el total de
     * corridas.
     *
     * @return Los tiempos promedios pasados por consulta por módulo.
     */
    public double[][] getTiemposPasadosPorConsultaPorModuloTotales() {
        return this.estadisticoConsulta.getTiempoPromedioConsultasPorModuloTotalCorridas(cantidadCorridas);
    }

    /**
     * Devuelve el tiempo promedio que dura una conexión en el sistema después de
     * cada corrida.
     *
     * @return El tiempo promedio de vida de una conexión.
     */
    public double getTiempoPromedioVidaConexion() {
        return this.estadisticoConsulta.calcularTiempoPromedioDeVida();
    }

    /**
     * Devuelve el tiempo promedio que dura una conexión en el sistema
     * como un promedio entre el total de corridas.
     *
     * @return El tiempo promedio de vida de una conexión.
     */
    public double getTiempoPromedioVidaConexionTotal() {
        return this.estadisticoConsulta.getTiempoPromedioDeVidaTotalCorridas(cantidadCorridas);
    }

    /**
     * Devuelve un promedio de conexiones descartadas que se dieron
     * entre el total de corridas.
     *
     * @return El promedio de conexiones descartadas
     */
    public double getConexionesDescartadasPromedioTotal() {
        return this.estadistico.getPromedioConexionesDescartadasTotalCorridas(cantidadCorridas);
    }

    /**
     * Le pide al estadístico de consultas el intervalo de confianza y lo devuelve.
     *
     * @return el intervalo de confianza.
     */
    public double[] getIntervaloDeConfianza(){
        return this.estadisticoConsulta.getIntervaloDeConfianza();
    }

    /**
     * Asigna el valor ingresado a la variable exit, de esta forma, al final de todas las
     * corridas, podremos saber si hacer una nueva simulación o si cerrar el programa.
     *
     * @param exit El booleano a asignar.
     */
    public void setExit(boolean exit){
        this.exit = exit;
    }

    /**
     * Método main. Crea un nuevo controlador y lo pone a ejecutar.
     *
     * @param args no se utiliza
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Controlador controlador = new Controlador();
        controlador.ejecutar();
    }

}
