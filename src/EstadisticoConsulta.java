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
    
    public EstadisticoConsulta(){
        totalConsultasRecibidas = new int[5][4];
        tiempoTotalConsultas = new double[5][4];
        tiempoConsultasPorModulo = new double[5][4];
        acumuladoTiemposPromedioConsultasPorModulo = new double[5][4];
        this.reset();
    }
    
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
    
    public void reset(){
        this.resetParcial();
        this.acumuladoDeTiemposDeVidaPromedio = 0;
        for(int i = 0; i < 5; ++i){
            for(int j = 0; j < 4; ++j){
                this.acumuladoTiemposPromedioConsultasPorModulo[i][j] = 0;
            }
        }
    }
    
    public double[][] getTiempoPromedioConsultasPorModuloTotalCorridas(int corridas){
        double[][] tiempoPromedioTotalConsultasPorModulo = new double[5][4];
        for(int i = 0; i < 5; ++i){
            for(int j = 0; j < 4; ++j){
                tiempoPromedioTotalConsultasPorModulo[i][j] = acumuladoTiemposPromedioConsultasPorModulo[i][j]/corridas;
            }
        }
        return tiempoPromedioTotalConsultasPorModulo;
    }
    
    public void incrementarAcumuladoDeTiemposDeVidaPromedio(double tiempo){
        acumuladoDeTiemposDeVidaPromedio += tiempo;
    }
    
    public void incrementarAcumuladoTiemposPromedioConsultasPorModulo(double[][] tiempos){
        for(int i = 0; i < 5; ++i){
            for(int j = 0; j < 4; ++j){
                acumuladoTiemposPromedioConsultasPorModulo[i][j] += tiempos[i][j];
            }
        }
    }
    
    public double getTiempoPromedioDeVidaTotalCorridas(int corridas){
        return acumuladoDeTiemposDeVidaPromedio/corridas;
    }

    public double[][] calcularTiempoPromedioDeSentenciaPorModulo(){
        for(int modulo = 0; modulo < 5; ++modulo){
            for(int consulta = 0; consulta < 4; ++consulta){
                double tiempoPromedio = tiempoTotalConsultas[modulo][consulta]/totalConsultasRecibidas[modulo][consulta];
                tiempoConsultasPorModulo[modulo][consulta] = tiempoPromedio;
            }
        }
        return tiempoConsultasPorModulo;
    }

    public double calcularTiempoPromedioDeVida(){
        double tiempoPromedioDeVida = tiempoAcumuladoDeVida / consultasProcesadasDelSistema;
        return tiempoPromedioDeVida;
    }

    public void incrementarConsultasProcesadasDelSistema(){
        ++consultasProcesadasDelSistema;
    }

    public void incrementarTiempoAcumuladoDeVida(double tiempo){
        tiempoAcumuladoDeVida += tiempo;
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
