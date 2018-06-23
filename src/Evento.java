public class Evento implements Comparable <Evento> {
    private TipoModulo tipoModulo;
    private TipoEvento tipoEvento;
    private double tiempoOcurrencia;
    private Consulta consulta;

    public Evento(TipoEvento tipoEvento, TipoModulo tipoModulo, double tiempoOcurrencia, Consulta consulta){
        this.tipoEvento = tipoEvento;
        this.tipoModulo = tipoModulo;
        this.tiempoOcurrencia = tiempoOcurrencia;
        this.consulta = consulta;
    }

    public int compareTo(Evento ev){
        //Si son eventos con la misma consulta, compara en base al tipo de evento, no en base al tiempo de ocurrencia.
        if(this.tiempoOcurrencia == ev.tiempoOcurrencia) { //Si hay dos eventos con el mismo tiempo de reloj
            if(this.consulta == ev.consulta) { //Si son dos eventos diferentes de la misma consulta E-T, E-S, S-T           
                if (this.tipoEvento == TipoEvento.TIMEOUT) { //Si yo soy un timeout
                    if (ev.tipoEvento == TipoEvento.SALIDA) { //Si el otro es salida
                        return 1; //La salida va primero.
                    } else if (ev.tipoEvento == TipoEvento.ENTRADA) { //Si el otro es entrada
                        return -1; //El timeout va primero que la entrada.
                    } //No hay else porque no pueden haber 2 timeouts de la misma consulta.
                }else if (ev.tipoEvento == TipoEvento.TIMEOUT) { //Si el otro es un timeout
                    if (this.tipoEvento == TipoEvento.SALIDA) { //Si yo soy salida.
                        return -1; //Salida va primero.
                    } else if (this.tipoEvento == TipoEvento.ENTRADA) { //Si yo soy entrada
                        return 1; //El timeout va primero que la entrada.
                    } //No hay else porque no pudeden haber 2 timeouts de la misma consulta.
                }
            }else{
                return 0; //Si son dos eventos diferentes y de diferentes consultas, entonces son iguales.
            }
        }
        else if (this.tiempoOcurrencia > ev.tiempoOcurrencia) {
            return 1;
        }
        else {
            return -1;
        }
        return 0;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public TipoModulo getTipoModulo() {
        return tipoModulo;
    }

    public void setTipoModulo(TipoModulo tipoModulo) {
        this.tipoModulo = tipoModulo;
    }

    public double getTiempoOcurrencia() {
        return tiempoOcurrencia;
    }

    public void setTiempoOcurrencia(double tiempoOcurrencia) {
        this.tiempoOcurrencia = tiempoOcurrencia;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }


}
