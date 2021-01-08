package azulejo;

import java.awt.*;
import java.util.ArrayList;

import armazem.Armazem;
import armazem.Caixote;
import armazem.Operario;
import prof.jogos2D.image.ComponenteVisual;


/**
 * Classe que representa um azulejo do chao
 * Tambem define o comportamento basico de todos os azulejos.
 */
public class AzulejoChao implements Azulejo {
	
	// a figura e posicao do azulejo
	private ComponenteVisual visual;
	private Point casa; 
	
	// o armazem onde estao colocado
	private Armazem armazem;
	
	/** construtor do azulejo
	 * @param visual o aspeto do azulejo
	 */
	public AzulejoChao( ComponenteVisual visual ){
		this.visual = visual;
	}

	@Override
	public void setVisual(ComponenteVisual cv) {
		visual = cv;
	}
	
	@Override
	public void desenhar(Graphics2D g) {
		visual.desenhar( g );		
	}
	
	@Override
	public ComponenteVisual getVisual() {
		return visual;
	}

	@Override
	public Point getPosicao() {
		return casa;
	}

	@Override
    public void setPosicao( Point pos ){
    	casa = pos;
    	visual.setPosicaoCentro( getArmazem().getEcran( pos ) );
    }
	
	@Override
	public Armazem getArmazem() {
		return armazem;
	}
	
	@Override
	public void setArmazem( Armazem arm ){
		armazem = arm;
	}    
	
	@Override
	public void ocupar( Operario op ){
		
	}

	@Override
	public void ocupar( Caixote c ){
		notificaMovimentoCaixote();
	}
	
	@Override
	public boolean podeOcupar( Operario op ){	
		return true;
	}
	
	@Override
	public boolean podeOcupar( Caixote c ){
		return c != null && getArmazem().getCaixote( getPosicao() ) == null;
	}
	
	@Override
	public boolean podeRemover( Operario op ){
		return true;
	}
	
	@Override
	public boolean podeRemover( Caixote c ){
		return true;
	}
	
	@Override
	public void remover( Operario op ){
		
	}
	
	@Override
	public void remover( Caixote c ){
		notificaMovimentoCaixote();
	}

	@Override
	public void aceita(VisitanteAzulejos v) {
		v.visitaAzulejoChao(this);
	}
	
	@Override
	public Azulejo clone() {
		try {
			Azulejo a = (Azulejo)super.clone();
			a.setVisual(getVisual().clone());
			return a;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
	@Override
	public void setTrigger(Point p) {
		
	}
	
private ArrayList<MovimentoListener> ouvintesAzulejos = new ArrayList<MovimentoListener>();
	
	private void notificaMovimentoCaixote() {
    	for( int i = ouvintesAzulejos.size() -1; i >= 0; i-- ) {
    		ouvintesAzulejos.get( i ).updatePosicaoCaixote(this);
       	}
    }
	
	public void addListeners( MovimentoListener c) {
		ouvintesAzulejos.add(c);
	}
	
	public void removeListeners( MovimentoListener c ) {
		ouvintesAzulejos.remove(c);
	}

	@Override
	public void updatePosicaoCaixote(Azulejo a) {
		
	}
	
}
