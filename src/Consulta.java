/**
 * Clase Consulta.
 * Objeto que contiene atributos concernientes a una consulta
 * ingresada a la base de datos.
 */
public class Consulta implements Comparable<Consulta>{
    private TipoConsulta tipoConsulta; //El tipo de sentencia de esta consulta
    private TipoModulo tipoModulo; //El módulo en el que se encuentra esta consulta.
    private boolean enCola;
    private boolean readOnly;
    private double tiempoIngreso;
    private double tiempoIngresoModulo;
    private int B;

    /**
     * Constructor de la clase Consulta.
     * Inicializa sus atributos.
     *
     * @param tipoConsulta el tipo de sentencia.
     */
    public Consulta(TipoConsulta tipoConsulta){
        this.tipoConsulta = tipoConsulta;
        this.obtenerReadOnly();
        this.enCola = false;
        this.B = 0;
    }

    /**
     * Ordenamiento natural para objetos de tipo consulta.
     *
     * @param otraConsulta
     * @return Si esta consutla es mayor o menor que la otra.
     *
     */
    @Override
    public int compareTo(Consulta otraConsulta){
        if(this.tipoConsulta == otraConsulta.tipoConsulta){
            return 0;
        }else if(this.tipoConsulta == TipoConsulta.DDL && (otraConsulta.tipoConsulta == TipoConsulta.JOIN || otraConsulta.tipoConsulta == TipoConsulta.SELECT || otraConsulta.tipoConsulta == TipoConsulta.UPDATE)){
            return -1;
        }else if(this.tipoConsulta == TipoConsulta.UPDATE && (otraConsulta.tipoConsulta == TipoConsulta.JOIN || otraConsulta.tipoConsulta == TipoConsulta.SELECT)){
            return -1;
        }else if(this.tipoConsulta == TipoConsulta.JOIN && (otraConsulta.tipoConsulta == TipoConsulta.SELECT)){
            return -1;
        }else{
            return 1;
        }
    }

    /**
     * Asigna el booleano respectivo a la variable readOnly.
     * Las consultas de tipo readOnly son SELECT y JOIN.
     */
    private void obtenerReadOnly(){
        if(tipoConsulta == TipoConsulta.SELECT || tipoConsulta == TipoConsulta.JOIN)
            readOnly = true;
        else
            readOnly = false;
    }

    /**
     * Devuelve el tipo de sentencia que tiene esta consulta.
     *
     * @return El tipo de sentencia de esta consulta.
     */
    public TipoConsulta getTipoConsulta(){
        return tipoConsulta;
    }

    /**
     * Devuelve true si es readOnly y false si no lo es.
     *
     * @return el booleano readOnly.
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Devuelve la cantidad de bloques B leídos de disco para
     * ejecutar esta consulta.
     *
     * @return el entero B.
     */
    public int getB() {
        return B;
    }

    /**
     * Asigna la cantidad de bloques que fueron leídos
     * para ejecutar esta consulta.
     *
     * @param b
     */
    public void setB(int b) {
        B = b;
    }

    /**
     * Devuelve el tiempo en el que ingresó esta consulta al sistema.
     *
     * @return el double tiempoIngreso.
     */
    public double getTiempoIngreso() {
        return tiempoIngreso;
    }

    /**
     * Asigna el tiempo en el que ingresó esta consulta al sistema.
     *
     * @param tiempoIngreso el tiempo de ingreso a asignar.
     */
    public void setTiempoIngreso(double tiempoIngreso) {
        this.tiempoIngreso = tiempoIngreso;
    }

    /**
     * Devuelve el tiempo en el que ingresó al último módulo.
     *
     * @return el double tiempoIngresoModulo.
     */
    public double getTiempoIngresoModulo() {
        return tiempoIngresoModulo;
    }

    /**
     * Asigna el tiempo en el que ingresó la consulta al último módulo.
     *
     * @param tiempoIngresoModulo El tiempo de ingreso al módulo a ser asignado.
     */
    public void setTiempoIngresoModulo(double tiempoIngresoModulo) {
        this.tiempoIngresoModulo = tiempoIngresoModulo;
    }

    /**
     * Devuelve el tipo de módulo de esta consulta, es decir, en
     * qué módulo se encuentra.
     *
     * @return
     */
    public TipoModulo getTipoModulo() {
        return tipoModulo;
    }

    /**
     * Se asigna el tipo de módulo en el que acaba de entrar esta consulta.
     *
     * @param tipoModulo El tipo del módulo al que se ingresó.
     */
    public void setTipoModulo(TipoModulo tipoModulo) {
        this.tipoModulo = tipoModulo;
    }

    /**
     * Devuelve true si la consulta está en cola en el módulo
     * y false si no está en cola.
     *
     * @return booleano enCola.
     */
    public boolean isEnCola() {
        return enCola;
    }

    /**
     * Asigna a la consulta el booleano que indica si está en cola o no.
     *
     * @param enCola el booleano a ser asignado.
     */
    public void setEnCola(boolean enCola) {
        this.enCola = enCola;
    }
}
