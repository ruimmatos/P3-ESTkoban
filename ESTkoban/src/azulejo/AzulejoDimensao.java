package azulejo;

import java.awt.Point;

import armazem.Armazem;
import armazem.Caixote;
import prof.jogos2D.image.ComponenteVisual;

public class AzulejoDimensao extends AzulejoChao{
	
	Caixote c;
	
	private Point trigger;

	public AzulejoDimensao(ComponenteVisual visual, Point trigger) {
		super(visual);
		this.trigger = trigger;
	}
	
	public Point getTrigger() {
		return trigger;
	}
	
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
	public void aceita(VisitanteAzulejos v) {
		v.visitaAzulejoDimensao(this);
	}
	
	//c = getArmazem().removerCaixote(getPosicao());

	private boolean estaNoTrigger(Point p) {
		return p.x==getTrigger().getX() && p.y==getTrigger().getY();
	}

}
