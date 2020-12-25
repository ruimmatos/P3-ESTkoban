package apps;

import java.io.PrintWriter;

import azulejo.AzulejoChao;
import azulejo.AzulejoDirecional;
import azulejo.AzulejoEscadas;
import azulejo.AzulejoFinal;
import azulejo.AzulejoParede;
import azulejo.AzulejoPorta;
import azulejo.VisitanteAzulejos;

public class GravaPortas implements VisitanteAzulejos{

	PrintWriter fout;
	MapaFicheiros mapaFich;
	
	public GravaPortas(PrintWriter fout, MapaFicheiros mapaFich) {
		this.fout = fout;
		this.mapaFich = mapaFich;
	}

	@Override
	public void visitaAzulejoChao(AzulejoChao a) {

	}

	@Override
	public void visitaAzulejoDirecional(AzulejoDirecional a) {
	
	}

	@Override
	public void visitaAzulejoEscadas(AzulejoEscadas a) {
	}

	@Override
	public void visitaAzulejoFinal(AzulejoFinal a) {
	}

	@Override
	public void visitaAzulejoParede(AzulejoParede a) {
	}

	@Override
	public void visitaAzulejoPorta(AzulejoPorta a) {
		AzulejoPorta ap = (AzulejoPorta)a; 
		fout.print( "porta " + a.getPosicao().x + "," + a.getPosicao().y + " " + (ap.estaAberto()? "A ": "F ") + ap.getTrigger().x + "," + ap.getTrigger().y );
		fout.println( " " + mapaFich.getFicheiroAzulejo( a.getPosicao() ) );
	}

}
