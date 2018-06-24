/**
 * Clase Estadistico.
 * Se encarga de calcular las estadísticas relacionadas con los
 * tamaños de las colas y las conexiones descartadas.
 */
public class Estadistico {
    /*
     *  Modulo Administrador de Procesos         -> 0
     *  Modulo Procesador de Consultas           -> 1
     *  Modulo de Transacciones y acceso a Datos -> 2
     *  Modulo Ejecutor de Sentencias            -> 3
     */

    //Variables para estadisticas después de cada corrida.
    private int[] tamanosAcumuladosDeColasPorModulo; //Tamano de la cola en cada módulo.
    private int[] numeroEntradasPorModulo;

    //Variables para estadisticas después de todas las corridas.
    private double[] acumuladoTamanosPromediosColas;
    private int acumuladoDeConexionesDescartadas;

    /**
     * Constructor de la clase Estadistico.
     * Inicializa sus atributos.
     */
    public Estadistico(){
        this.tamanosAcumuladosDeColasPorModulo = new int[4];
        this.numeroEntradasPorModulo = new int[4];
        this.acumuladoTamanosPromediosColas = new double[4];
        this.reset();
    }

    /**
     * Reinicia a 0 los valores de los atributos que
     * solo se utilizan para el cálculo de estadísticas
     * después de cada corrida.
     */
    public void resetParcial(){
        for(int i = 0; i < 4; ++i){
            tamanosAcumuladosDeColasPorModulo[i] = 0;
            numeroEntradasPorModulo[i] = 0;
        }
    }

    /**
     * Reinicia a 0 todos los valores de los
     * atributos de modo que se pueda correr
     * una nueva simulación.
     */
    public void reset(){
        this.resetParcial();
        for(int i = 0; i < 4; ++i){
            acumuladoTamanosPromediosColas[i] = 0;
        }
        acumuladoDeConexionesDescartadas = 0;
    }

    /**
     * Calcula los tamaños promedios de las colas
     * por módulo dividiendo el acumulado de los tamaños
     * las colas entre número de entradas al módulo.
     *
     * @return
     */
    public double[] calcularLQs(){
        double[] LQs = new double[4];
        for(int i = 0; i < 4; ++i){
            try{ //Try porque si no hay entradas al módulo, intenta dividir entre 0.
                double tamanoAcumuladoDeColaActual = (double)tamanosAcumuladosDeColasPorModulo[i];
                double numeroDeEntradasEnModuloActual = (double)numeroEntradasPorModulo[i];
                LQs[i] = tamanoAcumuladoDeColaActual / numeroDeEntradasEnModuloActual;
            }catch(Exception e){
                LQs[i] = 0;
            }
        }
        return LQs;
    }

    /**
     * Incrementa la cantidad de entradas que se dan en el módulo.
     *
     * @param modulo número que representa a un módulo.
     */
    public void incrementarEntradasPorModulo(int modulo){numeroEntradasPorModulo[modulo]++;}

    /**
     * Acumula el tamaño de la cola en el módulo.
     *
     * @param modulo número que representa a un módulo.
     * @param tamCola tamaño de la cola a acumular.
     */
    public void incrementarTamanosAcumuladosDeColasPorModulo(int modulo, int tamCola){
        tamanosAcumuladosDeColasPorModulo[modulo] += tamCola;
    }

    /**
     * Acumula después de cada corrida cuántas conexiones fueron descartadas.
     *
     * @param conexiones cantidad de conexiones a acumular.
     */
    public void incrementarConexionesDescartadas(int conexiones){
        this.acumuladoDeConexionesDescartadas += conexiones;
    }

    /**
     * Acumula los tamaños promedios de las colas al final de cada corrida.
     *
     * @param tamanos tamaños de las colas promedio a acumular.
     */
    public void incrementarAcumuladoTamanosPromediosColas(double[] tamanos){
        for(int i = 0; i < 4; ++i){
            acumuladoTamanosPromediosColas[i] += tamanos[i];
        }
    }

    /**
     * Obtiene un promedio de los tamaños de las colas entre la cantidad de corridas.
     *
     * @param corridas la cantidad de corridas.
     * @return el tamaño de la cola promedio en cada módulo entre el total de corridas.
     */
    public double[] getTamanosPromediosColasTotalCorridas(int corridas){
        double[] tamanosPromediosColasTotalCorridas = new double[4];
        for(int i = 0; i < 4; ++i){
            tamanosPromediosColasTotalCorridas[i] = acumuladoTamanosPromediosColas[i]/corridas;
        }
        return tamanosPromediosColasTotalCorridas;
    }

    /**
     * Obtiene un promedio de las conexiones descartadas entre la cantidad de corridas.
     *
     * @param corridas la cantidad de corridas.
     * @return la cantidad  promedio de conexiones descartadas entre el total de corridas.
     */
    public double getPromedioConexionesDescartadasTotalCorridas(int corridas){
        double conexionesDescartadas = (double)acumuladoDeConexionesDescartadas;
        double cantCorridas = (double)corridas;
        return conexionesDescartadas/cantCorridas;
    }

}
