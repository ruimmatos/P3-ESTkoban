package armazem;

import java.awt.*;
import azulejo.Azulejo;
import prof.jogos2D.image.ComponenteVisual;

/**
 * Classe que representa um caixote. 
 */
public class Caixote {

	// imagem e posi��o do caixote
	private ComponenteVisual figura;
	private Point posicao; 
	
	// em que armazem est� colocado
	protected Armazem armazem;
	
	/**
	 * construtor do caixote
	 * @param fig imagem representativa do caixote
	 */
	public Caixote( ComponenteVisual fig ){
		figura = fig;
	}
	
	
	/** desenhar o caixote
	 * @param g onde desenhar
	 */
	public void desenhar( Graphics2D g ){
		figura.desenhar( g );
	}
	
	/**
	 * indica se o caixote se pode deslocar de uma dada dist�ncia
	 * @param dx deslocamento em x
	 * @param dy deslocamento em y
	 * @return true se se pode deslocar
	 */
    public boolean podeDeslocar( int dx, int dy ){
    	// ver o ponto de destino
		Point dest = (Point)getPosicao().clone();
		dest.translate(dx,dy);
				
		//ver se o destino � no armaz�m
		if( !armazem.eCoordenadaValida(dest) )
			return false;
				
		// ver se no destino j� tem caixote
		if( armazem.getCaixote( dest ) != null )
			return false;
			
		// ver qual o azulejo no destino e se pode ir para l�
		Azulejo a = armazem.getAzulejo( dest );
		if( a == null  || !a.podeOcupar( this ) )
			return false;
		
		return true;
    }
    
    /**
     * desloca o caixote
     * @param dx dist�ncia em x
     * @param dy dist�ncia em y
     * @return se se deslocou ou n�o
     */
	public boolean deslocar( int dx, int dy ) {
		// ver se pode mover, senao nao move
	    if( !podeDeslocar( dx, dy ) )
	        return false;
	    
	    // o deslocar tem de ser repartido com o armaz�m,
	    // fica aqui, mas tamb�m podia ficar no armaz�m
	    armazem.removerCaixote( posicao );
	    armazem.colocarCaixote( new Point( posicao.x + dx, posicao.y + dy), this );
	    return true;
	}	

	/**
	 * devolve a posi��o do caixote
	 * @return a posi��o do caixote
	 */
	public Point getPosicao() {
		return posicao;
	}

	/**
	 * define a posi��o do caixote. Deve ser chamado apenas pelo Armaz�m
	 * @param pos a nova posi�oo a ocupar pelo caixote
	 */
    public void setPosicao( Point pos ){
    	posicao = pos;
    	figura.setPosicaoCentro( getArmazem().getEcran( pos ));
    }

    /**
     * devolve o armaz�m onde este caixote esta colocado
     * @return o armaz�m onde este caixote esta colocado
     */
	public Armazem getArmazem() {
		return armazem;
	}
	
	/**
	 * define qual o armaz�m onde o caixote est� colocado.
	 * S� deve ser chamado pelo pr�prio Armaz�m, dai ter n�vel package 
	 * @param arm o aramz�m
	 */
	void setArmazem( Armazem arm ){
		armazem = arm;
	}    
	
	/**
	 * devolve a figura do caixote
	 * @return a figura do caixote
	 */
	public ComponenteVisual getFigura( ) {
		return figura;		
	}
	
	
	/**
	 * define a figura do caixote
	 * @param fig a nova figura do caixote 
	 */
	public void setFigura(ComponenteVisual fig) {
		figura = fig;
	}	
}
