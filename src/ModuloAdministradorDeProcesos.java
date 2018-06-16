public class ModuloAdministradorDeProcesos extends Modulo {

    public ModuloAdministradorDeProcesos(){
        numeroServidores = 1; //
    }

    void procesarLlegada(){
        if(numeroServidores == 0){
            listaDeEventos.peek().getConsulta().setTiempoEnCola(sistemaPintoDB.reloj); // tiempo en el que ingresó en la cola
            cola.add(listaDeEventos.peek().getConsulta());
        }else{
            this.generarSalidaAdmProcesos( listaDeEventos.peek().getConsulta() );

            numeroServidores--;
        }
    }

    void procesarSalida(){
        if(cola.size() > 0){
            Consulta consulta = cola.poll();
            consulta.setTiempoEnCola( sistemaPintoDB.reloj -  consulta.getTiempoEnCola()  ); // tiempo en el que salió - tiempo en el que ingresó = tiempo en cola
            consulta.setTiempoRestante( consulta.getTiempoEnCola() - consulta.getTiempoRestante()) ; // tiempo que dure en cola - el tiempo que me queda de conexion
            // If de que si el tiempo restante es negativo, se cierra la conexion
            generarSalidaAdmProcesos(consulta);
        }else{
            numeroServidores++;
        }
    }

    private void generarSalidaAdmProcesos(Consulta consulta){
        double tiempo = calculador.genValorRandomNormal(); //
        double tiempoOcurrencia = tiempo + sistemaPintoDB.reloj;
        double tiempoRestante = (tiempo + sistemaPintoDB.reloj) - listaDeEventos.peek().getConsulta().getTiempoRestante();
        TipoConsulta tc = consulta.getTipoConsulta();
        listaDeEventos.add(new Evento(TipoModulo.AdministradorDeProcesos, tiempoOcurrencia, new Consulta(tc, tiempoRestante), false));
        this.generarEntradaProcConsultas(tiempo);
    }

    private void generarEntradaProcConsultas(double tiemp){
        double tiempoOcurrencia = tiemp + sistemaPintoDB.reloj;
        double tiempoRestante = (tiemp + sistemaPintoDB.reloj) - listaDeEventos.peek().getConsulta().getTiempoRestante();
        TipoConsulta tc = listaDeEventos.peek().getConsulta().getTipoConsulta();
        listaDeEventos.add(new Evento(TipoModulo.ProcesadorDeConsultas, tiempoOcurrencia, new Consulta(tc,tiempoRestante) , true));
    }
}
