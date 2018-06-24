import org.apache.commons.math3.distribution.TDistribution;
import java.util.ArrayList;
/**
 * Clase EstadisticoConsulta.
 * Se encarga de calcular todas aquellas estadísticas que tienen que ver
 * con las consultas.
 */
public class EstadisticoConsulta {

    /*
     *  Modulo Administrador de Procesos -> 0
     *  Modulo Procesador de Consultas -> 1
     *  Modulo de Transacciones y acceso a Datos -> 2
     *  Modulo Ejecutor de Sentencias -> 3
     *  Modulo Administrador de Clientes y Conexiones -> 4
     *
     *  DDL -> 0
     *  SELECT -> 1
     *  JOIN -> 2
     *  UPDATE -> 3
     */

    //Variables para estadísticas después de cada corrida.
    private int consultasProcesadasDelSistema;
    private double tiempoAcumuladoDeVida;
    private int[][] totalConsultasRecibidas;
    private double[][] tiempoTotalConsultas;
    private double[][] tiempoConsultasPorModulo; //Tiempo promedio de cada consulta en cada módulo.
    
    //Variables para estadísticas al final de todas las corridas.
    private double[][] acumuladoTiemposPromedioConsultasPorModulo; //Se acumulan los promedio de todas las corridas aquí.
    private double acumuladoDeTiemposDeVidaPromedio;

    private ArrayList<Double> promediosDeVida;
    private double tiempoPromedioDeVidaTotal;
    /**
     * Constructor de la clase EstadisticoConsulta.
     * Inicializa sus atributos.
     */
    public EstadisticoConsulta(){
        totalConsultasRecibidas = new int[5][4];
        tiempoTotalConsultas = new double[5][4];
        tiempoConsultasPorModulo = new double[5][4];
        acumuladoTiemposPromedioConsultasPorModulo = new double[5][4];
        promediosDeVida = new ArrayList<>();
        this.reset();
    }

    /**
     * Reinicia a cero los valores de las variables que solo
     * se utilizan para el cálculo de estadísticas después de cada corrida.
     */
    public void resetParcial(){
        this.consultasProcesadasDelSistema = 0;
        this.tiempoAcumuladoDeVida = 0;
        
        for(int i = 0; i < 5; ++i){
            for(int j = 0; j < 4; ++j){
                this.totalConsultasRecibidas[i][j] = 0;
            }
        }

        for(int i = 0; i < 5; ++i){
            for(int j = 0; j < 4; ++j){
                this.tiempoTotalConsultas[i][j] = 0;
            }
        }

        for(int i = 0; i < 5; ++i){
            for(int j = 0; j < 4; ++j){
                this.tiempoConsultasPorModulo[i][j] = 0;
            }
        }
    }

    /**
     * Reinicia los valores de todas las variables a 0
     * de modo que se pueda correr una nueva simulación.
     */
    public void reset(){
        this.resetParcial();
        this.acumuladoDeTiemposDeVidaPromedio = 0;
        for(int i = 0; i < 5; ++i){
            for(int j = 0; j < 4; ++j){
                this.acumuladoTiemposPromedioConsultasPorModulo[i][j] = 0;
            }
        }
        this.promediosDeVida.clear();
        this.tiempoPromedioDeVidaTotal = 0;
    }

    /**
     * Calcula el tiempo promedio que pasó cada consulta por cada módulo
     * como un promedio entre el total de corridas.
     *
     * @param corridas la cantidad de corridas.
     * @return el tiempo promedio pasado por cada consulta por cada módulo.
     */
    public double[][] getTiempoPromedioConsultasPorModuloTotalCorridas(int corridas){
        double[][] tiempoPromedioTotalConsultasPorModulo = new double[5][4];
        for(int i = 0; i < 5; ++i){
            for(int j = 0; j < 4; ++j){
                tiempoPromedioTotalConsultasPorModulo[i][j] = acumuladoTiemposPromedioConsultasPorModulo[i][j]/corridas;
            }
        }
        return tiempoPromedioTotalConsultasPorModulo;
    }

    /**
     * Acumula los promedios de tiempos de vida después de cada corrida.
     *
     * @param tiempo el tiempo promedio de vida a acumular.
     */
    public void incrementarAcumuladoDeTiemposDeVidaPromedio(double tiempo){
        acumuladoDeTiemposDeVidaPromedio += tiempo;
    }

    /**
     * Acumula los tiempos promedios pasados por cada consulta por cada
     * módulo después de cada corrida.
     *
     * @param tiempos los tiempo a acumular.
     */
    public void incrementarAcumuladoTiemposPromedioConsultasPorModulo(double[][] tiempos){
        for(int i = 0; i < 5; ++i){
            for(int j = 0; j < 4; ++j){
                acumuladoTiemposPromedioConsultasPorModulo[i][j] += tiempos[i][j];
            }
        }
    }

    /**
     * Calcula el tiempo promedio de vida que pasó una consulta como un
     * promedio entre la cantidad de corridas.
     *
     * @param corridas la cantidad de corridas.
     * @return el tiempo promedio de vida.
     */
    public double getTiempoPromedioDeVidaTotalCorridas(int corridas){
        this.tiempoPromedioDeVidaTotal = acumuladoDeTiemposDeVidaPromedio/corridas;
        return this.tiempoPromedioDeVidaTotal;
    }

    /**
     * Calcula un intervalo de confianza para el promedio de tiempo de vida
     * de una conexión en el sistema, con un 80% de precisión.
     *
     * @return intervalo de confianza.
     */
    public double[] getIntervaloDeConfianza(){
        double[] intervaloConfianza = new double[2];
        int N = promediosDeVida.size();
        double gradosDeLibertad = (double)(N-1);
        TDistribution distribucion = new TDistribution(gradosDeLibertad);
        double sumatoria = 0.0;

        for(int i = 0; i < N; ++i){
            double resta = promediosDeVida.get(i) - tiempoPromedioDeVidaTotal;
            double restaAlCuadrado = resta * resta;
            sumatoria += restaAlCuadrado;
        }
        double sumatoriaEntreN = sumatoria / (double)N;
        double desviacionEstandard = Math.sqrt(sumatoriaEntreN);
        double probabilidadAcumuladaInversa = distribucion.inverseCumulativeProbability(0.04); //(1-0.8)^2 para un 80% de precisión.
        double desviacion = desviacionEstandard * probabilidadAcumuladaInversa;
        intervaloConfianza[0] = tiempoPromedioDeVidaTotal + desviacion;
        intervaloConfianza[1] = tiempoPromedioDeVidaTotal - desviacion;
        return intervaloConfianza;
    }

    /**
     * Calcula el tiempo promedio que pasa cada tipo de sentencia por
     * cada módulo.
     *
     * @return el tiempo promedio pasado por tipo de sentencia por módulo.
     */
    public double[][] calcularTiempoPromedioDeSentenciaPorModulo(){
        for(int modulo = 0; modulo < 5; ++modulo){
            for(int consulta = 0; consulta < 4; ++consulta){
                double totalConsultasEnModuloActual = (double)totalConsultasRecibidas[modulo][consulta];
                double tiempoPromedio = tiempoTotalConsultas[modulo][consulta]/totalConsultasEnModuloActual;
                tiempoConsultasPorModulo[modulo][consulta] = tiempoPromedio;
            }
        }
        return tiempoConsultasPorModulo;
    }

    /**
     * Calcula el tiempo promedio de vida que tiene una consulta dentro del sistema.
     *
     * @return El tiempo promedio de vida.
     */
    public double calcularTiempoPromedioDeVida(){
        double tiempoPromedioDeVida = tiempoAcumuladoDeVida / consultasProcesadasDelSistema;
        return tiempoPromedioDeVida;
    }

    /**
     * Incrementa la cantidad de consultas procesadas por el sistema en esta corrida.
     */
    public void incrementarConsultasProcesadasDelSistema(){
        ++consultasProcesadasDelSistema;
    }

    /**
     * Acumula los tiempos de vida de las conexioens en esta corrida.
     *
     * @param tiempo el tiempo a acumular.
     */
    public void incrementarTiempoAcumuladoDeVida(double tiempo){
        tiempoAcumuladoDeVida += tiempo;
    }

    /**
     * Incrementa la cantidad de consultas que ha recibido un módulo.
     *
     * @param modulo número que representa al módulo.
     * @param consulta la consulta.
     */
    public void incrementarConsultasRecibidas(int modulo, Consulta consulta){
        ++totalConsultasRecibidas[modulo][getTipoConsulta(consulta)];
    }

    /**
     * Incrementa el tiempo que pasa una consulta en un módulo.
     *
     * @param modulo número que representa al módulo.
     * @param consulta la consulta
     * @param tiempo la cantidad de tiempo a incrementar
     */
    public void incrementarTiempoConsulta(int modulo, Consulta consulta, double tiempo){
        tiempoTotalConsultas[modulo][getTipoConsulta(consulta)] += tiempo;
    }

    public void agregarTiempoPromedioDeVida(double tiempoPromedioDeVida){
        this.promediosDeVida.add(tiempoPromedioDeVida);
    }

    /**
     * Determina cuál índice reprensenta a este tipo de consulta.
     *
     * @param consulta la consulta
     * @return el número que representa a esta consulta.
     */
    private int getTipoConsulta(Consulta consulta){
        int tipo = -1;
        switch (consulta.getTipoConsulta()){
            case DDL:{
                tipo = 0;
                break;
            }
            case SELECT:{
                tipo = 1;
                break;
            }

            case JOIN:{
                tipo = 2;
                break;
            }
            case UPDATE:{
                tipo = 3;
                break;
            }
        }
        return tipo;
    }

}
