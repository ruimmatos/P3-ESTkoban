package azulejo;

import java.awt.Graphics2D;

import armazem.Caixote;
import prof.jogos2D.image.ComponenteVisual;


/**
 * Classe que repesenta um azulejo final, ou seja, o azulejo
 * onde os caixotes devem ser colocados. 
 * Sempre que se coloca aqui um caixote o azulejo deve avisar o ESTkoban de que 
 * deve diminuir o numero de caixotes fora do sitio. Quando se retira daqui um
 * caixote, deve avisar que o numero aumentou
 */
public class AzulejoFinal extends AzulejoChao {
	
	// compoonente visual de que esta ocupado
	private ComponenteVisual imgOcupado;
	
	// indica se esta ocupado ou nao
	private boolean ocupado = false;
	
	
	/** construtor do azulejo final
	 * @param imgLivre aspeto visual quando est� desocupado
	 * @param imgOcupado aspeto visual quando est� ocupado
	 */
	public AzulejoFinal(ComponenteVisual imgLivre, ComponenteVisual imgOcupado ) {
		super( imgLivre );
		this.imgOcupado = imgOcupado;
	}

	@Override
	public void ocupar( Caixote c ){
		ocupado = true;
	}
	
	@Override
	public void remover( Caixote c ){
		ocupado = false;
	}
	
	/** indica se o azulejo final esta ocupado 
	 * @return se o azulejo final esta ocupado
	 */
	public boolean estaOcupado() {
		return ocupado;
	}
	
	@Override
	public void desenhar(Graphics2D g) {
		if( ocupado ) {
			imgOcupado.setPosicaoCentro( getVisual().getPosicaoCentro() );
			imgOcupado.desenhar(g);
		}
		else
			super.desenhar(g);
	}	
	
	@Override
	public void aceita(VisitanteAzulejos v) {
		v.visitaAzulejoFinal(this);
	}
	
	@Override
	public Azulejo clone() {
		AzulejoFinal a = (AzulejoFinal) super.clone();
		a.setVisual(getVisual().clone());
		a.imgOcupado = imgOcupado.clone();
		return a;
	}
	
}
