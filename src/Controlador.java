//import sun.jvm.hotspot.utilities.SystemDictionaryHelper;
import java.util.Scanner;
import java.util.Iterator;
import java.util.PriorityQueue;
public class Controlador {
    private SistemaPintoDB sistema;
    //private Interfaz interfaz;
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

    public void crearSistemaAutomático(){
        //this.parametros = interfaz.getParametros();
        //this.timeout = interfaz.getTimeout();
        parametros = new int[4];
        //Para pruebas
        for(int i = 0; i < 4; ++i){
            parametros[i] = 20;
        }
        this.tiempoMax = 5000;
        this.timeout = 30;
        
        this.sistema = new SistemaPintoDB(this.estadistico, this.estadisticoConsulta, this.parametros, this.timeout, this.tiempoMax);
    }

    private void crearSistemaManual(int vecesCorridas,  double maxTiempo ,int [] par, double timeout){
        this.contadorCorridas = vecesCorridas;
        this.tiempoMax = maxTiempo;
        parametros = new int[4];
        for(int i = 0; i < 4 ; ++i){
            parametros[i] = par[i];
        }
        this.timeout = timeout;
        this.sistema = new SistemaPintoDB(this.estadistico, this.estadisticoConsulta, this.parametros, this.timeout, this.tiempoMax);
    }

    public void pedirInformacion(){
        int vecesCorridas;
        double maxTiempo;
        int [] param = new int[4];
        double timeOut;
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el numero de veces que se correrá la simulación: ");
        vecesCorridas = sc.nextInt();
        System.out.println("Ingrese el tiempo maximo para correr cada simulación: ");
        maxTiempo = sc.nextDouble();
        System.out.println("Ingrese el numero k de conexiones concurrentes que puede manejar el sistema: ");
        param[0] = sc.nextInt();
        System.out.println("Ingrese el numero n de procesos disponibles para el procesamiento de consultas: ");
        param[1] = sc.nextInt();
        System.out.println("Ingrese el numero p de procesos disponibles para la ejecuci\u0013on de transacciones.: ");
        param[2] = sc.nextInt();
        System.out.println("Ingrese el numero m de procesos disponibles para ejecutar consultas.: ");
        param[3] = sc.nextInt();
        System.out.println("Ingrese la cantidad t de segundos de timeout de las conexiones.: ");
        timeOut = sc.nextDouble();
        this.crearSistemaManual(vecesCorridas,maxTiempo,param,timeOut);
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
        this.sistema.simular();
    }

    public static void main(String[] args){
        Controlador controlador = new Controlador();
        //controlador.crearSistemaAutomático();
        controlador.pedirInformacion();
        // CREAR WHILE DE NUMERO DE CORRIDAS
        controlador.ejecutar();
        System.out.println("Se acabo la simulacion\n\t\t\t ESTADISTICAS:");
        //PriorityQueue<Integer> cola = new PriorityQueue<>();
        //System.out.println(cola.size());
        int[] tamanoPromedioColaPorModulo = controlador.estadistico.calcularLQs();
        System.out.println("\n");
        System.out.println("Longitud cola promedio modulo Administrador de Procesos :" + tamanoPromedioColaPorModulo[0]);
        System.out.println("Longitud cola promedio modulo Procesador de Consultas :" + tamanoPromedioColaPorModulo[1]);
        System.out.println("Longitud cola promedio modulo Transaccion y Acesso a Datos :" + tamanoPromedioColaPorModulo[2]);
        System.out.println("Longitud cola promedio modulo Ejecutor de Sentencias :" + tamanoPromedioColaPorModulo[3]);
        System.out.println("\n");
        System.out.println("Tiempo promedio de vida de una conexion: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeVida());
        System.out.println("\n");
        System.out.println("Conexiones Descartadas: " + controlador.estadistico.getConexionesDescartadas());
        System.out.println("\n");
        System.out.println("Tiempo promedio de sentencia DDL en modulo Administrador de Clientes y Conexiones: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(4,0));
        System.out.println("Tiempo promedio de sentencia SELECT en modulo Administrador de Clientes y Conexiones: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(4,1));
        System.out.println("Tiempo promedio de sentencia JOIN en modulo Administrador de Clientes y Conexiones: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(4,2));
        System.out.println("Tiempo promedio de sentencia UPDATE en modulo Administrador de Clientes y Conexiones: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(4,3));
        System.out.println("Tiempo promedio de sentencia DDL en modulo Administrador de Procesos: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(0,0));
        System.out.println("Tiempo promedio de sentencia SELECT en modulo Administrador de Procesos: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(0,1));
        System.out.println("Tiempo promedio de sentencia JOIN en modulo Administrador de Procesos: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(0,2));
        System.out.println("Tiempo promedio de sentencia UPDATE en modulo Administrador de Procesos: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(0,3));
        System.out.println("Tiempo promedio de sentencia DDL en modulo Procesador de Consultas: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(1,0));
        System.out.println("Tiempo promedio de sentencia SELECT en modulo Procesador de Consultas: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(1,1));
        System.out.println("Tiempo promedio de sentencia JOIN en modulo Procesador de Consultas: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(1,2));
        System.out.println("Tiempo promedio de sentencia UPDATE en modulo Procesador de Consultas: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(1,3));
        System.out.println("Tiempo promedio de sentencia DDL en modulo Transaccion y Acceso a Datos: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(2,0));
        System.out.println("Tiempo promedio de sentencia SELECT en modulo Transaccion y Acceso a Datos: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(2,1));
        System.out.println("Tiempo promedio de sentencia JOIN en modulo Transaccion y Acceso a Datos: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(2,2));
        System.out.println("Tiempo promedio de sentencia UPDATE en modulo Transaccion y Acceso a Datos: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(2,3));
        System.out.println("Tiempo promedio de sentencia DDL en modulo Ejecutor de Sentencias: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(3,0));
        System.out.println("Tiempo promedio de sentencia SELECT en modulo Ejecutor de Sentencias: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(3,1));
        System.out.println("Tiempo promedio de sentencia JOIN en modulo Ejecutor de Sentencias: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(3,2));
        System.out.println("Tiempo promedio de sentencia UPDATE en modulo Ejecutor de Sentencias: " + controlador.estadisticoConsulta.calcularTiempoPromedioDeSentenciaPorModulo(3,3));
    }
}
