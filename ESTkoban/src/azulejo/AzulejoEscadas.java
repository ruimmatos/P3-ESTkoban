package azulejo;

import armazem.Caixote;
import prof.jogos2D.image.ComponenteVisual;


/**
 * define um azulejo que e uma escada, em que so o operario pode atravessar.
 */
public class AzulejoEscadas extends AzulejoChao {

	/** construtor das escadas
	 * @param visual aspeto visual
	 */
	public AzulejoEscadas( ComponenteVisual visual ) {
		super(visual);	
	}

	@Override
	public boolean podeOcupar( Caixote op ){
		return false;
	}	
}
