package azulejo;

import java.awt.Point;

import armazem.Armazem;
import armazem.Caixote;
import armazem.Operario;
import prof.jogos2D.image.ComponenteVisual;


/**
 * define um azulejo que e uma porta que pode estar aberta ou fechada.
 * Para abrir/fechar a porta existe outro azulejo - trigger - que aciona a porta
 * quando la é colocado um caixote.
 * 
 * TODO falta implememtar o mecanismo que permite que a porta saiba que o azulejo trigger tem um caixote.
 * TODO falta implememtar o mecanismo que permite que a porta saiba que o azulejo trigger tem um caixote.
 * TODO falta implememtar o mecanismo que permite que a porta saiba que o azulejo trigger tem um caixote.
 */
public class AzulejoPorta extends AzulejoChao {

	/** cimagens do azulejo, quando est� aberto e fechado */
	private ComponenteVisual imgAberto, imgFechado;
	
	// indica se esta aberto
	private boolean aberto;
	
	// a posicao onde esta o azulejo que e o trigger deste
	private Point trigger;
	
	/** construtor da porta
	 * @param imgAberto imagem da porta aberta
	 * @param imgFechado imagem da porta fechada
	 * @param trigger posicao do azulejo que e o trigger
	 * @param aberto indicacao se deve comecar aberta ou fechada
	 */
	public AzulejoPorta( ComponenteVisual imgAberto, ComponenteVisual imgFechado, Point trigger, boolean aberto ) {
		super( imgAberto );	
		this.aberto = aberto;
		this.imgAberto = imgAberto;
		this.imgFechado = imgFechado;
		this.trigger = trigger;
		if( !aberto )
			setVisual( imgFechado );
	}
	
	/** define o estado de abertura da porta
	 * @param aberto se o azulejo tem a porta aberta ou fechada
	 */
	public void setAberto(boolean aberto) {
		this.aberto = aberto;
	}
	
	/** retorna o estado de abertura da porta
	 * @return se o azulejo tem a porta aberta ou fechada
	 */
	public boolean estaAberto() {
		return aberto;
	}

	/** retorna a posicao do azulejo que e o trigger desta porta
	 * @return a posicao do azulejo que e o trigger desta porta
	 */
	public Point getTrigger() {
		return trigger;
	}
	
	/** define a posicao do azulejo que e o trigger desta porta
	 * @param trigger a posicao do azulejo que e o trigger desta porta
	 */
	public void setTrigger(Point trigger) {
		this.trigger = trigger;
		Azulejo a = getArmazem().getAzulejo( trigger );
		
	}
	
	@Override
	public void setArmazem(Armazem arm) {
		super.setArmazem(arm);
		Azulejo a = arm.getAzulejo( trigger );
	}

	@Override
	public boolean podeOcupar( Caixote op ){
		return aberto && super.podeOcupar( op );
	}	
	
	@Override
	public boolean podeOcupar(Operario op) {
		return aberto && super.podeOcupar(op);
	}
	
	@Override
	public void setPosicao(Point pos) {
		super.setPosicao(pos);
		Point pc = getArmazem().getEcran( pos );
		imgAberto.setPosicaoCentro( pc );
		imgFechado.setPosicaoCentro( pc );
	}
	
	@Override
	public void aceita(VisitanteAzulejos v) {
		v.visitaAzulejoPorta(this);
	}
	
	@Override
	public Azulejo clone() {
		AzulejoPorta a = (AzulejoPorta) super.clone();
		a.setVisual(getVisual().clone());
		a.imgFechado = imgFechado.clone();
		return a;
	}
	
	private void mudaEstadoPorta() {
		aberto = !aberto;
		if(!aberto)
			setVisual(imgFechado);
		else
			setVisual(imgAberto);
	}

	private boolean estaNoTrigger(Point p) {
		return p.x==getTrigger().getX() && p.y==getTrigger().getY();
	}
}
