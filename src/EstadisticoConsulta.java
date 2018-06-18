public class EstadisticoConsulta {

    private int[][] totalConsultasRecibidas;
    private double[][] tiempoTotalConsultas;
    private double[][] tiempoConsultas; //Tiempo promedio de cada consulta en cada módulo.

    public EstadisticoConsulta(){
        totalConsultasRecibidas = new int[4][4];
        for(int i = 0; i < 4; ++i){
            for(int j = 0; j < 4; ++j){
                totalConsultasRecibidas[i][j] = 0;
            }
        }

        tiempoTotalConsultas = new double[4][4];
        for(int i = 0; i < 4; ++i){
            for(int j = 0; j < 4; ++j){
                tiempoTotalConsultas[i][j] = 0;
            }
        }

        tiempoConsultas = new double[4][4];
        for(int i = 0; i < 4; ++i){
            for(int j = 0; j < 4; ++j){
                tiempoConsultas[i][j] = 0;
            }
        }
    }

    public double calcularTiempoPromedioConexion(int modulo, int consulta){
        double tiempoPromedio = tiempoTotalConsultas[modulo][consulta]/totalConsultasRecibidas[modulo][consulta];
        tiempoConsultas[modulo][consulta] = tiempoPromedio;
        return tiempoPromedio;
    }

    public void incrementarConsultasRecibidas(int modulo, int consulta){
        ++totalConsultasRecibidas[modulo][consulta];
    }

    public void incrementarTiempoConsulta(int modulo, int consulta, double tiempo){
        tiempoTotalConsultas[modulo][consulta] += tiempo;
    }
}
