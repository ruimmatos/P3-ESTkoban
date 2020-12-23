package armazem;
import java.awt.Graphics2D;
import java.awt.Point;

import azulejo.Azulejo;
import prof.jogos2D.image.ComponenteVisual;

/**
 * Classe que representa o operário que empurra os caixotes 
 */
public class Operario {
	
	/** constantes para definir a direção para onde está virado */
	private static final int ESQUERDA = 0;
	private static final int CIMA = 1;
	private static final int DIREITA = 2;
	private static final int BAIXO = 3;
	
	// a imagem e posição do operário
	private ComponenteVisual figura, figuras[];
	private Point posicao; 
	
	// o armazém onde ele se mexe
	protected Armazem armazem;
	
	/**
	 * Construtor do operário
	 * @param fig as imagens do operário. Devem estar na ordem: esquerda, cima, direita, baixo.
	 */
	public Operario( ComponenteVisual[] figuras ){
		this.figuras = figuras;
		figura = figuras[ESQUERDA];
	}

	/**
	 * indica se o operário se pode deslocar
	 * @param dx o deslocamento em x
	 * @param dy o deslocamento em y
	 * @return se se pode deslocar, ou não
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
	 * desloca o operário
	 * @param dx o deslocamento em x
	 * @param dy o deslocamento em y
	 * @return se o operário se deslocou, ou não
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
		
		// ver se pode deslocar, senão não se desloca
	    if( !podeDeslocar( dx, dy ) )
	        return false;
	    
		Point dest = (Point)getPosicao().clone();
		dest.translate(dx,dy);
	    
	    // ver se mexe o caixote
		Caixote c = armazem.getCaixote( dest );
		if( c != null  )
			c.deslocar(dx, dy);
	    
	    // o mover tem de ser repartido com o armazém,
	    // fica aqui, mas também podia ficar no armazém
	    armazem.colocarOperario( dest, this );
	    return true;
	}	
	
	/**
	 * devolve a posição do operário
	 * @return a posição dooperário
	 */
	public Point getPosicao() {
		return posicao;
	}

	/**
	 * define a posição dooperário
	 * @param pos a posição do operário
	 */
    public void setPosicao( Point pos ){
    	posicao = pos;
    	figura.setPosicaoCentro( getArmazem().getEcran(pos) );
    }
	
    /**
     * indica em que armazém o operário trabalha
     * @return o armazém em que o operário trabalha
     */
	public Armazem getArmazem() {
		return armazem;
	}
	
	/**
	 * define o armazem onde o operário trabalha.
	 * Só deve ser chamado pelo próprio armazém.
	 * @param arm o armazém onde o operário vai trabalhar
	 */
	public void setArmazem( Armazem arm ){
		armazem = arm;
	}    

	/**
	 * devolve as imagens associadas ao operário. As imagens estão na ordem: esquerda, cima, direita, baixo.
	 * @return as imagens associadas ao operário
	 */
	public ComponenteVisual[] getFiguras( ) {
		return figuras;		
	}
	
	/**
	 * define as imagens associadas ao operário. As imagens devem estar na ordem: esquerda, cima, direita, baixo.
	 * @param img as novas imagens do operário.
	 */
	public void setFiguras(ComponenteVisual[] img ) {
		figuras = img;
		figura = figuras[ESQUERDA];
	}	
	
	
	/** desenha o operário
	 * @param g onde desenhar
	 */
	public void desenhar( Graphics2D g) {
		figura.desenhar( g );
	}
}
