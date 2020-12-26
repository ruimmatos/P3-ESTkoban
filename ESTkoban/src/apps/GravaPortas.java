package apps;

import java.io.PrintWriter;

import azulejo.AzulejoChao;
import azulejo.AzulejoDirecional;
import azulejo.AzulejoEscadas;
import azulejo.AzulejoFinal;
import azulejo.AzulejoParede;
import azulejo.AzulejoPorta;
import azulejo.VisitanteAzulejos;
import azulejo.VisitanteAzulejosDefault;

public class GravaPortas extends VisitanteAzulejosDefault {

	PrintWriter fout;
	MapaFicheiros mapaFich;
	
	public GravaPortas(PrintWriter fout, MapaFicheiros mapaFich) {
		this.fout = fout;
		this.mapaFich = mapaFich;
	}

	@Override
	public void visitaAzulejoPorta(AzulejoPorta a) {
		AzulejoPorta ap = (AzulejoPorta)a; 
		fout.print( "porta " + a.getPosicao().x + "," + a.getPosicao().y + " " + (ap.estaAberto()? "A ": "F ") + ap.getTrigger().x + "," + ap.getTrigger().y );
		fout.println( " " + mapaFich.getFicheiroAzulejo( a.getPosicao() ) );
	}

}
