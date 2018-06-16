public class ModuloProcesadorDeConsultas extends Modulo {

    public ModuloProcesadorDeConsultas(){

    }

    void procesarLlegada(){
        if(numeroServidores == 0){
            listaDeEventos.peek().getConsulta().setTiempoEnCola(sistemaPintoDB.reloj); // tiempo en el que ingresó en la cola
            cola.add(listaDeEventos.peek().getConsulta());
        }else{
            this.generarSalidaProcConsultas( listaDeEventos.peek().getConsulta() );
            numeroServidores--;
        }
    }

    void procesarSalida(){
        if(cola.size() > 0){
            Consulta consulta = cola.poll();
            consulta.setTiempoEnCola( sistemaPintoDB.reloj -  consulta.getTiempoEnCola()  ); // tiempo en el que salió - tiempo en el que ingresó = tiempo en cola
            consulta.setTiempoRestante( consulta.getTiempoEnCola() - consulta.getTiempoRestante()) ; // tiempo que dure en cola - el tiempo que me queda de conexion
            // If de que si el tiempo restante es negativo, se cierra la conexion
            generarSalidaProcConsultas(consulta);
        }else{
            numeroServidores++;
        }
    }

    private void generarSalidaProcConsultas(Consulta consulta){
        // Validación Lexica
        double tiempoValLexica = 0.1; //
        // Validación Sintactica
        double tiempoValSintactica = calculador.genValorUniformeContinuo(0,1);
        // Validacion Semantica
        double tiempoValSemantica = calculador.genValorUniformeContinuo(0,2);
        // Verifiacion de Permisos
        double tiempoVerifPermisos = calculador.genValorExponencial(0.7);
        // Optimización de Consultas
        double tiempoOptimizacionConsultas;
        if(consulta.isReadOnly())
            tiempoOptimizacionConsultas = 0.1;
        else
            tiempoOptimizacionConsultas = 0.25;
        // TIEMPO TOTAL
        double tiempoTotal = tiempoValLexica + tiempoValSintactica + tiempoValSemantica + tiempoVerifPermisos + tiempoOptimizacionConsultas;



        double tiempoOcurrencia = tiempoTotal + sistemaPintoDB.reloj;
        double tiempoRestante = (tiempoTotal + sistemaPintoDB.reloj) - listaDeEventos.peek().getConsulta().getTiempoRestante();
        TipoConsulta tc = consulta.getTipoConsulta();
        listaDeEventos.add(new Evento(TipoModulo.ProcesadorDeConsultas, tiempoOcurrencia, new Consulta(tc, tiempoRestante), false));
        this.generarEntradaTAD(tiempoTotal);
    }

    private void generarEntradaTAD(double tiempo){
        double tiempoOcurrencia = tiempo + sistemaPintoDB.reloj;
        double tiempoRestante = (tiempo + sistemaPintoDB.reloj) - listaDeEventos.peek().getConsulta().getTiempoRestante();
        TipoConsulta tc = listaDeEventos.peek().getConsulta().getTipoConsulta();
        listaDeEventos.add(new Evento(TipoModulo.TransaccionYAccesoADatos, tiempoOcurrencia, new Consulta(tc,tiempoRestante) , true));
    }

}
