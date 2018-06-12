import java.lang.Math;
/**
 *
 * 
 */
public class CalculadorValoresAleatorios {
    private double tablaDistribucionConsultas[] = new double[4];
    
    
    
    public CalculadorValoresAleatorios(){
        
    }
    
    public double genNumAle(){
        double random = Math.random()*1;
        return random;
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
