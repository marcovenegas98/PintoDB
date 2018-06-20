public class Estadistico {

    private int[] tamanosAcumuladosDeColasPorModulo = new int[4]; //Tamano de la cola en cada módulo.
    private int conexionesDescartadas;
    private int numeroCorridas;

    public Estadistico(){
        for(int i = 0; i < 4; ++i){
            tamanosAcumuladosDeColasPorModulo[i] = 0;
        }
        numeroCorridas = 0;
    }

    public int[] calcularLQs(){
        int[] tamanoPromedioDeColaPorModulo = new int[4];
        for(int i = 0; i < 4; ++i){
            tamanoPromedioDeColaPorModulo[i] = tamanosAcumuladosDeColasPorModulo[i] / numeroCorridas;
        }
        return tamanoPromedioDeColaPorModulo;
    }

    public void incrementarNumeroCorridas(){
        ++numeroCorridas;
    }

    public int getConexionesDescartadas(){
        return conexionesDescartadas;
    }

    public void setConexionesDescartadas(int conexionesDescartadas) {
        this.conexionesDescartadas = conexionesDescartadas;
    }

    public void incrementarTamanosAcumuladosDeColasPorModulo(int[] tamanos){
        for(int i = 0; i < 4 ; ++i){
            tamanosAcumuladosDeColasPorModulo[i] += tamanos[i];
        }

    }

}
