import java.lang.Math;
/**
 *
 * 
 */
public class CalculadorValoresAleatorios {
    private double tablaDistribucionConsultas[] = new double[4];
    
    
    
    public CalculadorValoresAleatorios(){
        tablaDistribucionConsultas[0] = 0.3;
        tablaDistribucionConsultas[1] = 0.55;
        tablaDistribucionConsultas[2] = 0.85;
        tablaDistribucionConsultas[3] = 1;
    }

    //Devuelve un número aleatorio entre 0 y 1
    public double genNumAle(){
        return Math.random()*1;
    }

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
    
    public double genValorRandomNormal(){
        double sumNumAl = 0.0;
        for(int i = 0; i < 12 ; ++i){
            sumNumAl += this.genNumAle();
        }
        double zeta = sumNumAl - 6;
        return (1 + (zeta * 0.1));
    }

    public double genValorUniformeContinuo(double cotaInferior, double cotaSuperior){
        return cotaInferior + ( (cotaSuperior - cotaInferior) * this.genNumAle());  
    }
    
    public int genValorUniformeDiscreto(){
        return (int)(Math.random() * 63) + 1; 
    }
    
    public double genValorExponencial(double media){
        double rand = this.genNumAle();
        return ( (-1 * media) * Math.log(rand)); 
    }
    
}
