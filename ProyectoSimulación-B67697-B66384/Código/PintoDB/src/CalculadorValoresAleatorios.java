import java.lang.Math;

/**
 * Clase CalculadorValoresAleatorios.
 * Se encarga de calcular valores aleatorios para las
 * variables aleatorias que siguen distintas distribuciones
 * en este DBMS.
 */
public class CalculadorValoresAleatorios {
    private double tablaDistribucionConsultas[] = new double[4];

    /**
     * Constructor de la clase.
     * Inicializa ta tabla de distribuciones para los tipos
     * de consulta.
     */
    public CalculadorValoresAleatorios(){
        tablaDistribucionConsultas[0] = 0.3;
        tablaDistribucionConsultas[1] = 0.55;
        tablaDistribucionConsultas[2] = 0.9;
        tablaDistribucionConsultas[3] = 1;
    }

    /**
     * Calcula un número aleatorio que está entre 0 y 1.
     *
     * @return Número aleatorio.
     */
    public double genNumAle(){
        return Math.random()*1;
    }

    /**
     * Utiliza el método del muestreo de Montecarlo.
     * Calcula un número aleatorio y dependiendo de su
     * valor, asigna el tipo con respecto a la tabla de
     * distribución de los tipos de consulta.
     *
     * @return El tipo de consulta.
     */
    public TipoConsulta genMonteCarloConsulta(){
        double randomNumber = genNumAle();
        TipoConsulta tipoConsulta;

        if(randomNumber < tablaDistribucionConsultas[0]){
            tipoConsulta = TipoConsulta.SELECT;
        }else if(randomNumber < tablaDistribucionConsultas[1]){
            tipoConsulta = TipoConsulta.UPDATE;
        }else if(randomNumber < tablaDistribucionConsultas[2]){
            tipoConsulta = TipoConsulta.JOIN;
        }else{
            tipoConsulta = TipoConsulta.DDL;
        }

        return tipoConsulta;
    }

    /**
     * Calcula un valor aleatorio para una variable aleatoria
     * que siga una distribución normal con media de 1 y
     * varianza de 0.01.
     * Se derivó la fórmula utilizando el método de convolución.
     *
     * @return Valor aleatorio para la variable aleatoria.
     */
    public double genValorRandomNormal(){
        double sumNumAl = 0.0;
        for(int i = 0; i < 12 ; ++i){
            sumNumAl += this.genNumAle();
        }
        double zeta = sumNumAl - 6;
        return (1 + (zeta * 0.1)); //sqrt(0.01) = 0.1
    }

    /**
     * Calcula un valor aleatorio para cualquier variable aleatoria
     * que siga una distribución uniforme continua.
     * Se derivó la fórmula con el método de la transformación inversa.
     *
     * @param cotaInferior a
     * @param cotaSuperior b
     * @return Valor aleatorio para la variable aleatoria.
     */
    public double genValorUniformeContinuo(double cotaInferior, double cotaSuperior){
        return cotaInferior + ( (cotaSuperior - cotaInferior) * this.genNumAle());  
    }

    /**
     * Calcula un valor aleatorio para una variable aleatoria que siga una
     * distribución uniforme discreta y cuyo dominio esté restringido entre
     * 1 y 64.
     *
     * @return Número entero entre 1 y 64.
     */
    public int genValorUniformeDiscreto(){
        return (int)(Math.random() * 63) + 1; 
    }

    /**
     * Calcula un valor aleatorio para una variable aleatoria que sigue una
     * distribución exponencial.
     * Se derivó la fórmula utilzando el método de la transformación inversa.
     *
     * @param media 1/lambda
     * @return Valor aleatorio para la variable aleatoria.
     */
    public double genValorExponencial(double media){
        double rand = this.genNumAle();
        return ( (-1 * media) * Math.log(rand)); 
    }
    
}
