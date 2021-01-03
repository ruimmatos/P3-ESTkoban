package armazem;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import azulejo.Azulejo;
import azulejo.OperarioListener;
import prof.jogos2D.image.ComponenteVisual;

/**
 * Classe que representa o oper�rio que empurra os caixotes 
 */
public class Operario {
	
	/** constantes para definir a dire��o para onde est� virado */
	private static final int ESQUERDA = 0;
	private static final int CIMA = 1;
	private static final int DIREITA = 2;
	private static final int BAIXO = 3;
	
	// a imagem e posi��o do oper�rio
	private ComponenteVisual figura, figuras[];
	private Point posicao; 
	
	// o armaz�m onde ele se mexe
	protected Armazem armazem;
	
	/**
	 * Construtor do oper�rio
	 * @param fig as imagens do oper�rio. Devem estar na ordem: esquerda, cima, direita, baixo.
	 */
	public Operario( ComponenteVisual[] figuras ){
		this.figuras = figuras;
		figura = figuras[ESQUERDA];
	}

	/**
	 * indica se o oper�rio se pode deslocar
	 * @param dx o deslocamento em x
	 * @param dy o deslocamento em y
	 * @return se se pode deslocar, ou n�o
	 */
	public boolean podeDeslocar(int dx, int dy) {
		Point dest = (Point)getPosicao().clone();
		dest.translate(dx,dy);
				
		if( !armazem.eCoordenadaValida(dest) )
			return false;
		
		Caixote c = armazem.getCaixote( dest );
		if( c != null && !c.podeDeslocar(dx, dy) )
			return false;
			
		Azulejo a = armazem.getAzulejo( dest );
		if( a == null  || !a.podeOcupar( this ) )
			return false;
		
		return true;
	}

	/**
	 * desloca o oper�rio
	 * @param dx o deslocamento em x
	 * @param dy o deslocamento em y
	 * @return se o oper�rio se deslocou, ou n�o
	 */
	public boolean deslocar( int dx, int dy ) {
		Point pc = figura.getPosicaoCentro();
		if( dx > 0 )
			figura = figuras[ DIREITA ];
		else if( dx < 0 )
			figura = figuras[ ESQUERDA ];
		else if( dy < 0 )
			figura = figuras[ CIMA ];
		else 
			figura = figuras[ BAIXO ];
		figura.setPosicaoCentro( pc );
		
		// ver se pode deslocar, sen�o n�o se desloca
	    if( !podeDeslocar( dx, dy ) )
	        return false;
	    
		Point dest = (Point)getPosicao().clone();
		dest.translate(dx,dy);
	    
	    // ver se mexe o caixote
		Caixote c = armazem.getCaixote( dest );
		if( c != null  )
			c.deslocar(dx, dy);
	    
	    // o mover tem de ser repartido com o armaz�m,
	    // fica aqui, mas tamb�m podia ficar no armaz�m
	    armazem.colocarOperario( dest, this );
	    
	    return true;
	}	
	
	/**
	 * devolve a posi��o do oper�rio
	 * @return a posi��o dooper�rio
	 */
	public Point getPosicao() {
		return posicao;
	}

	/**
	 * define a posi��o dooper�rio
	 * @param pos a posi��o do oper�rio
	 */
    public void setPosicao( Point pos ){
    	posicao = pos;
    	figura.setPosicaoCentro( getArmazem().getEcran(pos) );
    }
	
    /**
     * indica em que armaz�m o oper�rio trabalha
     * @return o armaz�m em que o oper�rio trabalha
     */
	public Armazem getArmazem() {
		return armazem;
	}
	
	/**
	 * define o armazem onde o oper�rio trabalha.
	 * S� deve ser chamado pelo pr�prio armaz�m.
	 * @param arm o armaz�m onde o oper�rio vai trabalhar
	 */
	public void setArmazem( Armazem arm ){
		armazem = arm;
	}    

	/**
	 * devolve as imagens associadas ao oper�rio. As imagens est�o na ordem: esquerda, cima, direita, baixo.
	 * @return as imagens associadas ao oper�rio
	 */
	public ComponenteVisual[] getFiguras( ) {
		return figuras;		
	}
	
	/**
	 * define as imagens associadas ao oper�rio. As imagens devem estar na ordem: esquerda, cima, direita, baixo.
	 * @param img as novas imagens do oper�rio.
	 */
	public void setFiguras(ComponenteVisual[] img ) {
		figuras = img;
		figura = figuras[ESQUERDA];
	}	
	
	
	/** desenha o oper�rio
	 * @param g onde desenhar
	 */
	public void desenhar( Graphics2D g) {
		figura.desenhar( g );
	}
	
}
