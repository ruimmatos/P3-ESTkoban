package azulejo;

import armazem.Caixote;
import armazem.Operario;
import prof.jogos2D.image.ComponenteVisual;


/**
 * Define um azulejo que e direcional, isto é, aceita caixotes e operarios que venham de uma dada direcao.
 */
public class AzulejoDirecional extends AzulejoChao {

	/** a direcao de onde devem vir os caixotes*/
	private int dx, dy;

	/** Cria o azulejo direcional
	 * @param visual o aspeto visual
	 * @param dx a direcao em x de onde devem vir os caixotes/operario
	 * @param dy a direcao em y de onde devem vir os caixotes/operario
	 */
	public AzulejoDirecional( ComponenteVisual visual, int dx, int dy ) {
		super(visual);
		this.dx = dx;
		this.dy = dy;
	}
	
	/** retorna a direcao em x de onde devem vir os caixotes/operario
	 * @return a direcao em x de onde devem vir os caixotes/operario
	 */ 
	public int getDx() {
		return dx;
	}

	/** define a direcao em x de onde devem vir os caixotes/operario
	 * @param dx  a direcao em x de onde devem vir os caixotes/operario
	 */
	public void setDx(int dx) {
		this.dx = dx;
	}
	
	/** retorna a direcao em y de onde devem vir os caixotes/operario
	 * @return a direcao em y de onde devem vir os caixotes/operario
	 */ 
	public int getDy() {
		return dy;
	}
	
	/** Define a direcao em y de onde devem vir os caixotes/operario
	 * @param dy a direcao em y de onde devem vir os caixotes/operario
	 */
	public void setDy(int dy) {
		this.dy = dy;
	}
	
	@Override
	public boolean podeOcupar( Caixote c ){
		if( !super.podeOcupar(c) )
			return false;
		int dx = c.getPosicao().x - getPosicao().x;
		int dy = c.getPosicao().y - getPosicao().y;
		return this.dx == dx && this.dy == dy;
	}	

	@Override
	public boolean podeOcupar(Operario op) {
		if( !super.podeOcupar(op) )
			return false;
		int dx = op.getPosicao().x - getPosicao().x;
		int dy = op.getPosicao().y - getPosicao().y;
		return this.dx == dx && this.dy == dy;
	}
}
