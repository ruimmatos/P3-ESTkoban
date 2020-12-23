package prof.jogos2D.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Representa um componente visual composto apenas por uma imagem
 * @author F. Sérgio Barbosa
 */
public class ComponenteSimples implements ComponenteVisual {

	// posição do componente visual no ecran
	private Point posicao;
	
	// imagem do componente
	private Image sprite;
		
	// rotação do componente
	private double angulo = 0;
	
	
	// se o componente repete animações
	private boolean ciclico = true;
	
	/**
	 * Cria um componente simples sem imagem e na posição (0,0) do ecran
	 */
	public ComponenteSimples( ){	
		posicao = new Point();
	}

	/**
	 * cria um componente simples com a imagem contida no ficheiro fichImagem
	 * @param fichImagem nome do ficheiro com a imagem que representará o componente
	 */ 
	public ComponenteSimples( String fichImagem) throws IOException {
		this( new Point(0,0), fichImagem);		
	}

	/**
	 * cria um componente simples com a imagem img
	 * @param img a imagem que representa o componente
	 */
	public ComponenteSimples( Image img ){
		this(new Point(0,0), img);
	}
	
	
	/**
	 * cria um componente simples na posição p e com a imagem contida no ficheiro fichImagem
	 * @param p posição onde irá ficar o componente
	 * @param fichImagem nome do ficheiro com a imagem que representará o componente
	 */ 
	public ComponenteSimples( Point p, String fichImagem) throws IOException {
		posicao = p;
		sprite = ImageIO.read( new File( fichImagem ) );
	}
	
	/**
	 * cria um componente simples na posição p e com a imagem img
	 * @param p posição onde irá ficar o componente
	 * @param img a imagem que representa o componente
	 */
	public ComponenteSimples( Point p, Image img){
		setPosicao( p );
		sprite = img;
	}
			
	/**
	 * desenha o componente no ambiente gráfico g
	 */
	public void desenhar(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		if( sprite != null ){
			//AffineTransform t = g2.getTransform();
			Point meio = getPosicaoCentro();
			g2.translate( meio.x, meio.y );
			g2.rotate( angulo );
			g2.translate( -meio.x, -meio.y );
			g2.drawImage(sprite, getPosicao().x, getPosicao().y, null);
			//g2.setTransform( t );
		}
		g2.dispose();
	}

	public int getAltura() {
		// se tem imagem retorna a altura da imagem senão retorna 0
		return sprite == null? 0: sprite.getHeight( null );
	}

	public int getComprimento() {
		// se tem imagem retona o comprimento da imagem senão retortna 0
		return sprite == null? 0: sprite.getWidth( null );
	}

	public Point getPosicao() {
		return posicao;
	}

	public void setPosicao(Point p) {
		posicao = p;
	}
	
	public void setPosicaoCentro(Point p) {
		posicao = new Point( p.x - getComprimento()/2, p.y - getAltura()/2 );
	}	
	
	public Point getPosicaoCentro() {
		return new Point( getPosicao().x + getComprimento()/2, getPosicao().y + getAltura() / 2 );
	}
	
	public Rectangle getBounds(){
		return new Rectangle( getPosicao().x, getPosicao().y, getComprimento(), getAltura() );
	}

	public Image getSprite() {
		return sprite;
	}

	public void setSprite(Image sprite) {
		this.sprite = sprite;
	}

	public void rodar( double angulo ){
		this.angulo += angulo;
	}
	
	public void setAngulo( double angulo ){
		this.angulo = angulo;
	}	
	
	public double getAngulo() {
		return angulo;
	}
	
	public int numCiclosFeitos() {
		return 0;
	}
	
	@Override
	public boolean eCiclico() {
		return ciclico;
	}
	
	@Override
	public void setCiclico(boolean ciclico) {
		this.ciclico= ciclico;
	}
	
	public void reset() {
		
	}
	
	public ComponenteSimples clone() {
		ComponenteSimples sp = new ComponenteSimples( posicao != null? (Point)posicao.clone(): null, sprite );
		sp.setAngulo( angulo );
		return sp;
	}
}
