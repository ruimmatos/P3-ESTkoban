package azulejo;

import java.awt.Graphics2D;
import java.awt.Point;

import armazem.Armazem;
import armazem.Caixote;
import armazem.Operario;
import prof.jogos2D.image.ComponenteVisual;

/**
 * Representa os azulejos do jogo 
 */
public interface Azulejo extends Cloneable, OperarioListener {

	/** retorna a imagem do azulejo
	 * @return a imagem do azulejo
	 */
	ComponenteVisual getVisual();
	
	/** define a imagem do azulejo
	 * @param cv a nova imagem do azulejo
	 */
	void setVisual( ComponenteVisual cv );
	
	/** desenha o azulejo
	 * @param g onde desenhar
	 */
	void desenhar( Graphics2D g );
	
	/**
	 * define a posicao do azulejo. Deve ser chamada apenas pelo armazem
	 * @param pos a nova posicao do azulejo
	 */
    public void setPosicao( Point pos );
    
    /**
     * devolve a posicao do azulejo
     * @return a posicao do azulejo
     */
    public Point getPosicao( );
	
    /**
     * define qual o armazem associado a este azulejo.
     * So deve ser chamado pelo proprio armazem
     * @param arm o armazem
     */
	public void setArmazem( Armazem arm );
	
	/**
	 * devolve o armazem associado ao azulejo
	 * @return o armazem associado ao azulejo
	 */
	public Armazem getArmazem( );
	
	/**
	 * indica se o operario pode ocupar este azulejo
	 * @param op o operario 
	 * @return se o operario pode ocupar o azulejo
	 */
	public boolean podeOcupar( Operario op );
	
	/**
	 * indica se o caixote pode ocupar o azulejo
	 * @param c o caixote a verificar
	 * @return se o caixote pode ocupar o azulejo
	 */
	public boolean podeOcupar( Caixote c );
	
	/**
	 * Coloca o operario no azulejo, mesmo que nao obedeca as regras. <br>
	 * Antes deste metodo deve-se chamar o podeOcupar, para saber se o movimento é valido.
	 * @param op o operario
	 */
	public void ocupar( Operario op );
	
	/**
	 * Coloca o caixote no azulejo, mesmo que nao obedeca as regras. <br>
	 * Antes deste metodo deve-se chamar o podeOcupar, para saber se o movimento é valido.
	 * @param x o caixote a coloar
	 */
	public void ocupar( Caixote c );

	/**
	 * Indica se o operario pode ser removido do caixote
	 * @param op o operario a remover
	 * @return se o operario pode ser removido
	 */
	public boolean podeRemover( Operario op );
	
	/**
	 * Indica se o caixote pode ser removido do azulejo
	 * @param c o caixote a remover
	 * @return se o caixote pode ser removido
	 */
	public boolean podeRemover( Caixote c );
	
	/**
	 * Remover o operario do azulejo, mesmo que nao obedeca as regras. <br>
	 * Antes deste metodo deve-se chamar o podeRemover, para saber se o movimento e valido.
	 * @param op o operario a remover
	 */
	public void remover( Operario op );
	
	/**
	 * Remove o caixote do azulejo, mesmo que nao obedeca as regras. <br>
	 * Antes deste metodo deve-se chamar o podeRemover, para saber se o movimento e valido.
	 * @param c o caixote a remover
	 */
	public void remover( Caixote c );
	
	/**
	 * metodo que aceita um visitante - Pattern Visitor
	 * @param v visitante 
	 */
	public void aceita(VisitanteAzulejos v);

	/**
	 * metodo que clona um Azulejo - Pattern Prototype
	 * @return retorna o clone de um azulejo
	 */
	public Azulejo clone();
	
	public void updatePosicaoOperario(Point p);
	
	public void addListeners( OperarioListener m );
	
	public void notificaListeners(Point p);

}
