package apps;

import java.io.PrintWriter;

import azulejo.AzulejoDimensao;
import azulejo.AzulejoPorta;
import azulejo.VisitanteAzulejosDefault;

public class GravaUltimos extends VisitanteAzulejosDefault {

	PrintWriter fout;
	MapaFicheiros mapaFich;
	
	public GravaUltimos(PrintWriter fout, MapaFicheiros mapaFich) {
		this.fout = fout;
		this.mapaFich = mapaFich;
	}

	@Override
	public void visitaAzulejoPorta(AzulejoPorta a) {
		AzulejoPorta ap = (AzulejoPorta)a; 
		fout.print( "porta " + a.getPosicao().x + "," + a.getPosicao().y + " " + (ap.estaAberto()? "A ": "F ") + ap.getTrigger().x + "," + ap.getTrigger().y );
		fout.println( " " + mapaFich.getFicheiroAzulejo( a.getPosicao() ) );
	}
	
	@Override
	public void visitaAzulejoDimensao(AzulejoDimensao a) {
		AzulejoDimensao ad = (AzulejoDimensao)a;
		fout.print( "dimensao " + a.getPosicao().x + "," + a.getPosicao().y + " " + ad.getTrigger().x + "," + ad.getTrigger().y );
		fout.println( " " + mapaFich.getFicheiroAzulejo( a.getPosicao() ) );
	}

}
