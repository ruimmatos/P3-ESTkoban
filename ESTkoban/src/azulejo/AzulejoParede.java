package azulejo;


import prof.jogos2D.image.ComponenteVisual;


/**
 * define um azulejo que � uma parede, logo n�o pode ter nem caixotes nem oper�rios
 */
public class AzulejoParede extends AzulejoChao {

	/** Construtor do azulejo
	 * @param visual aspeto visual
	 */
	public AzulejoParede( ComponenteVisual visual ) {
		super(visual);	
	}

	
	@Override
	public void aceita(VisitanteAzulejos v) {
		v.visitaAzulejoParede(this);
	}
	
}
