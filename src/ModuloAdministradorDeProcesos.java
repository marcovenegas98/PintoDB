public class ModuloAdministradorDeProcesos extends Modulo {

    public ModuloAdministradorDeProcesos(){
        numeroServidores = 1; //
    }

    void procesarLlegada(){
        if(numeroServidores == 0){
            cola.add(listaDeEventos.peek().getConsulta());
        }else{
            this.generarSalidaAdmProcesos( listaDeEventos.peek().getConsulta() );

            numeroServidores--;
        }
    }

    void procesarSalida(){
        if(cola.size() > 0){
            Consulta consulta = cola.poll();
            generarSalidaAdmProcesos(consulta);
        }else{
            numeroServidores++;
        }
    }

    private void generarSalidaAdmProcesos(Consulta consulta){
        double tiempo = calculador.genValorRandomNormal(); // 3
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
        listaDeEventos.add(new Evento(TipoModulo.TransaccionYAccesoADatos, tiempoOcurrencia, new Consulta(tc,tiempoRestante) , true));
    }
}
