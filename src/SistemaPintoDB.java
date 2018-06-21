import java.util.PriorityQueue;
public class SistemaPintoDB {
    private ModuloAdministradorDeClientesYConexiones adminClientes;
    private ModuloAdministradorDeProcesos adminProcesos;
    private ModuloProcesadorDeConsultas procesadorConsultas;
    private ModuloDeTransaccionesYAccesoADatos adminDatos;
    private ModuloEjecutorDeSentencias ejecutorSentencias;

    private CalculadorValoresAleatorios calculador;
    private Estadistico estadistico;
    private EstadisticoConsulta estadisticoConsulta;

    public double reloj;
    public PriorityQueue<Evento> listaDeEventos;

    //private int[] parametros; //k, n, p, m. En ese orden.
    private double timeout;
    private double duracionSimulacion; //en segundos


    public SistemaPintoDB(Estadistico estadistico, EstadisticoConsulta estadisticoConsulta, int[] parametros, double timeout, double duracion){
        //this.parametros = parametros;
        this.reloj = 0;
        this.estadistico = estadistico;
        this.estadisticoConsulta = estadisticoConsulta;
        this.listaDeEventos = new PriorityQueue<>();
        this.calculador = new CalculadorValoresAleatorios();
        this.timeout = timeout;
        this.duracionSimulacion = duracion;

        //Módulos
        this.adminClientes = new ModuloAdministradorDeClientesYConexiones(this.estadistico, this.estadisticoConsulta, this.listaDeEventos,  this.calculador, this.reloj, parametros[0], this.timeout);
        this.adminProcesos = new ModuloAdministradorDeProcesos(this.estadistico, this.estadisticoConsulta, this.listaDeEventos,  this.calculador, this.reloj); //Solo 1 servidor
        this.procesadorConsultas = new ModuloProcesadorDeConsultas(this.estadistico, this.estadisticoConsulta, this.listaDeEventos,  this.calculador, this.reloj, parametros[1]);
        this.adminDatos = new ModuloDeTransaccionesYAccesoADatos(this.estadistico, this.estadisticoConsulta, this.listaDeEventos,  this.calculador, this.reloj, parametros[2]);
        this.ejecutorSentencias = new ModuloEjecutorDeSentencias(this.estadistico, this.estadisticoConsulta, this.listaDeEventos,  this.calculador, this.reloj, parametros[3]);

        //Se guarda en la lista de eventos el evento inicial.
        TipoConsulta tipoConsulta = calculador.genMonteCarloConsulta();
        Consulta consultaInicial = new Consulta(tipoConsulta);
        listaDeEventos.add(new Evento(TipoEvento.ENTRADA, TipoModulo.ClientesYConexiones, this.reloj, consultaInicial));
        //Se genera el timeout para esta consulta.
        listaDeEventos.add(new Evento(TipoEvento.TIMEOUT, TipoModulo.ClientesYConexiones, this.reloj + this.timeout, consultaInicial));

    }

    public void setDuracionSimulacion(double duracionSimulacion){
        this.duracionSimulacion = duracionSimulacion;
    }

    public double getDuracionSimulacion(){
        return duracionSimulacion;
    }

    public void simular(){
        Evento eventoActual = listaDeEventos.poll(); //Saca el siguiente evento de la lista de eventos.
        eventoActual.getConsulta().setTiempoIngreso(this.reloj); //Se le da a la consulta el tiempo en el que ingresó al sistema.
        this.reloj = eventoActual.getTiempoOcurrencia(); //Se actualiza el reloj.
        while(reloj < duracionSimulacion){
            System.out.println("Reloj del sistema: " + this.reloj + " ---- Procesando una " + eventoActual.getTipoEvento() + " en el modulo " + eventoActual.getTipoModulo());
            this.mandarAProcesar(eventoActual);
            System.out.println("Longitud cola Administrador de Procesos: " + adminProcesos.getCola().size());
            System.out.println("Longitud cola Procesador de Consultas: " + procesadorConsultas.getCola().size());
            System.out.println("Longitud cola Transaccion Y Acceso a Datos: " + adminDatos.getCola().size());
            System.out.println("Longitud cola Ejecutor de Sentencias: " + ejecutorSentencias.getCola().size());
            System.out.println("ConexionesDescartadas :" + adminClientes.getConexionesDescartadas());
            eventoActual = listaDeEventos.poll(); //Saca el siguiente evento de la lista de eventos.
            eventoActual.getConsulta().setTiempoIngreso(this.reloj); //Se le da a la consulta el tiempo en el que ingresó al sistema.
            this.reloj = eventoActual.getTiempoOcurrencia(); //Se actualiza el reloj.
            System.out.println("\n");
        }
        System.out.println("Se acabo la simulacion");
        this.estadistico.setConexionesDescartadas(adminClientes.getConexionesDescartadas()); //Actualizo las conexiones descartadas.
       //this.estadistico.incrementarTamanosAcumuladosDeColasPorModulo(this.getTamanoColaPorModulo());
    }

    private int[] getTamanoColaPorModulo(){
        int[] tamanoColaPorModulo = new int[4];
        tamanoColaPorModulo[0] = adminProcesos.getCola().size();
        tamanoColaPorModulo[1] = procesadorConsultas.getCola().size();
        tamanoColaPorModulo[2] = adminDatos.getCola().size();
        tamanoColaPorModulo[3] = ejecutorSentencias.getCola().size();
        return tamanoColaPorModulo;
    }

    private void mandarAProcesar(Evento evento){
        switch(evento.getTipoEvento()){
            case ENTRADA:{
                switch(evento.getTipoModulo()){
                    case ClientesYConexiones:{

                        adminClientes.setReloj(reloj);
                        adminClientes.procesarLlegada(evento.getConsulta());
                        break;
                    }
                    case AdministradorDeProcesos:{
                        this.estadistico.incrementarEntradasPorModulo(0);
                        this.estadistico.incrementarTamanosAcumuladosDeColasPorModulo(0,adminProcesos.getCola().size());
                        adminProcesos.setReloj(reloj);
                        adminProcesos.procesarLlegada(evento.getConsulta());
                        break;
                    }
                    case ProcesadorDeConsultas:{
                        this.estadistico.incrementarEntradasPorModulo(1);
                        this.estadistico.incrementarTamanosAcumuladosDeColasPorModulo(1,procesadorConsultas.getCola().size());
                        procesadorConsultas.setReloj(reloj);
                        procesadorConsultas.procesarLlegada(evento.getConsulta());
                        break;
                    }
                    case TransaccionYAccesoADatos:{
                        this.estadistico.incrementarEntradasPorModulo(2);
                        this.estadistico.incrementarTamanosAcumuladosDeColasPorModulo(2,adminDatos.getCola().size());
                        adminDatos.setReloj(reloj);
                        adminDatos.procesarLlegada(evento.getConsulta());
                        break;
                    }
                    case EjecutorDeSentencias:{
                        this.estadistico.incrementarEntradasPorModulo(3);
                        this.estadistico.incrementarTamanosAcumuladosDeColasPorModulo(3,ejecutorSentencias.getCola().size());
                        ejecutorSentencias.setReloj(reloj);
                        ejecutorSentencias.procesarLlegada(evento.getConsulta());
                        break;
                    }
                }
                break;
            }
            case SALIDA:{
                switch(evento.getTipoModulo()){
                    case ClientesYConexiones:{
                        adminClientes.setReloj(reloj);
                        adminClientes.procesarSalida(evento.getConsulta());
                        break;
                    }
                    case AdministradorDeProcesos:{
                        adminProcesos.setReloj(reloj);
                        adminProcesos.procesarSalida(evento.getConsulta());
                        break;
                    }
                    case ProcesadorDeConsultas:{
                        procesadorConsultas.setReloj(reloj);
                        procesadorConsultas.procesarSalida(evento.getConsulta());
                        break;
                    }
                    case TransaccionYAccesoADatos:{
                        adminDatos.setReloj(reloj);
                        adminDatos.procesarSalida(evento.getConsulta());
                        break;
                    }
                    case EjecutorDeSentencias:{
                        ejecutorSentencias.setReloj(reloj);
                        ejecutorSentencias.procesarSalida(evento.getConsulta());
                        break;
                    }
                }
                break;
            }
            case TIMEOUT:{
                adminClientes.setReloj(reloj);
                adminClientes.manejarTimeout(evento.getConsulta(), this.determinarModulo(evento.getConsulta()));
                break;
            }
        }
    }

    private Modulo determinarModulo(Consulta consulta){
        TipoModulo tipoModulo = consulta.getTipoModulo();
        Modulo modulo = null;
        switch(tipoModulo){
            case ClientesYConexiones:{
                modulo = this.adminClientes;
                break;
            }
            case AdministradorDeProcesos:{
                modulo = this.adminProcesos;
                break;
            }
            case ProcesadorDeConsultas:{
                modulo = this.procesadorConsultas;
                break;
            }
            case TransaccionYAccesoADatos:{
                modulo = this.adminDatos;
                break;
            }
            case EjecutorDeSentencias:{
                modulo = this.ejecutorSentencias;
                break;
            }
        }
        return modulo;
    }
}
