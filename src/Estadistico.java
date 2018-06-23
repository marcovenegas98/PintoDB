public class Estadistico {
    //Variables para estadisticas después de cada corrida.
    private int[] tamanosAcumuladosDeColasPorModulo; //Tamano de la cola en cada módulo.
    private int[] numeroEntradasPorModulo;
    private double[] LQs;
    //Variables para estadisticas después de todas las corridas.
    private double[] acumuladoTamanosPromediosColas;
    private int acumuladoDeConexionesDescartadas;

    public Estadistico(){
        this.tamanosAcumuladosDeColasPorModulo = new int[4];
        this.numeroEntradasPorModulo = new int[4];
        this.LQs = new double[4];
        this.acumuladoTamanosPromediosColas = new double[4];
        this.reset();
    }
    
    public void resetParcial(){
        //Todos los valores inician en cero.
        for(int i = 0; i < 4; ++i){
            tamanosAcumuladosDeColasPorModulo[i] = 0;
            numeroEntradasPorModulo[i] = 0;
            LQs[i] = 0;
        }
    }
    
    public void reset(){
        this.resetParcial();
        for(int i = 0; i < 4; ++i){
            acumuladoTamanosPromediosColas[i] = 0;
        }
        acumuladoDeConexionesDescartadas = 0;
    }

    public double[] calcularLQs(){
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

    public void incrementarEntradasPorModulo(int i){numeroEntradasPorModulo[i]++;}

    public void incrementarTamanosAcumuladosDeColasPorModulo(int i, int tamCola){
        tamanosAcumuladosDeColasPorModulo[i] += tamCola;
    }
    
    public void incrementarConexionesDescartadas(int conexiones){
        this.acumuladoDeConexionesDescartadas += conexiones;
    }
    
    public void incrementarAcumuladoTamanosPromediosColas(double[] tamanos){
        for(int i = 0; i < 4; ++i){
            acumuladoTamanosPromediosColas[i] += tamanos[i];
        }
    }

    public double[] getTamanosPromediosColasTotalCorridas(int corridas){
        double[] tamanosPromediosColasTotalCorridas = new double[4];
        for(int i = 0; i < 4; ++i){
            tamanosPromediosColasTotalCorridas[i] = acumuladoTamanosPromediosColas[i]/corridas;
        }
        return tamanosPromediosColasTotalCorridas;
    }
    
    public double getPromedioConexionesDescartadasTotalCorridas(int corridas){
        double conexionesDescartadas = (double)acumuladoDeConexionesDescartadas;
        double cantCorridas = (double)corridas;
        return conexionesDescartadas/cantCorridas;
    }
    
    public double[] getLQs(){
        return this.LQs;
    }

}
