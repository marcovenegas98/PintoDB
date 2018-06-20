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
        reloj = 0.0; // segundos
        while(reloj < duracionSimulacion){
            Evento eventoActual = listaDeEventos.poll(); //Saca el siguiente evento de la lista de eventos.
            eventoActual.getConsulta().setTiempoIngreso(this.reloj); //Se le da a la consulta el tiempo en el que ingresó al sistema.
            this.reloj = eventoActual.getTiempoOcurrencia(); //Se actualiza el reloj.
            this.mandarAProcesar(eventoActual);
        }
        this.estadistico.setConexionesDescartadas(adminClientes.getConexionesDescartadas()); //Actualizo las conexiones descartadas.
        this.estadistico.incrementarTamanosAcumuladosDeColasPorModulo(this.getTamanoColaPorModulo());
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
                        adminClientes.procesarLlegada(evento.getConsulta());
                        break;
                    }
                    case AdministradorDeProcesos:{
                        adminProcesos.procesarLlegada(evento.getConsulta());
                        break;
                    }
                    case ProcesadorDeConsultas:{
                        procesadorConsultas.procesarLlegada(evento.getConsulta());
                        break;
                    }
                    case TransaccionYAccesoADatos:{
                        adminDatos.procesarLlegada(evento.getConsulta());
                        break;
                    }
                    case EjecutorDeSentencias:{
                        ejecutorSentencias.procesarLlegada(evento.getConsulta());
                        break;
                    }
                }
                break;
            }
            case SALIDA:{
                switch(evento.getTipoModulo()){
                    case ClientesYConexiones:{
                        adminClientes.procesarSalida(evento.getConsulta());
                        break;
                    }
                    case AdministradorDeProcesos:{
                        adminProcesos.procesarSalida(evento.getConsulta());
                        break;
                    }
                    case ProcesadorDeConsultas:{
                        procesadorConsultas.procesarSalida(evento.getConsulta());
                        break;
                    }
                    case TransaccionYAccesoADatos:{
                        adminDatos.procesarSalida(evento.getConsulta());
                        break;
                    }
                    case EjecutorDeSentencias:{
                        ejecutorSentencias.procesarSalida(evento.getConsulta());
                        break;
                    }
                }
                break;
            }
            case TIMEOUT:{
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
