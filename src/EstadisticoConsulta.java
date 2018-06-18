public class EstadisticoConsulta {

    /*
     *  Modulo Administrador de Procesos -> 0
     *  Modulo Procesador de Consultas -> 1
     *  Modulo de Transacciones y acceso a Datos -> 2
     *  Modulo Ejecutor de Sentencias -> 3
     *
     *  DDL
     */


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

    public void incrementarConsultasRecibidas(int modulo, Consulta consulta){
        ++totalConsultasRecibidas[modulo][getTipoConsulta(consulta)];
    }

    public void incrementarTiempoConsulta(int modulo, Consulta consulta, double tiempo){
        tiempoTotalConsultas[modulo][getTipoConsulta(consulta)] += tiempo;
    }

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
