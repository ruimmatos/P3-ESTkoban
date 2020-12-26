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

public class GravaAzulejo extends VisitanteAzulejosDefault {
	
	PrintWriter fout;
	MapaFicheiros mapaFich;
	
	public GravaAzulejo(PrintWriter fout, MapaFicheiros mapaFich) {
		this.fout = fout;
		this.mapaFich = mapaFich;
	}

	@Override
	public void visitaAzulejoChao(AzulejoChao a) {
		fout.print( "chao " + a.getPosicao().x + "," + a.getPosicao().y );
		fout.println( " " + mapaFich.getFicheiroAzulejo( a.getPosicao() ) );
	}

	@Override
	public void visitaAzulejoDirecional(AzulejoDirecional a) {
		AzulejoDirecional ad = (AzulejoDirecional)a; 
		fout.print( "direcao " + a.getPosicao().x + "," + a.getPosicao().y + " " + ad.getDx() + " "+ ad.getDy() );
		fout.println( " " + mapaFich.getFicheiroAzulejo( a.getPosicao() ) );

	}

	@Override
	public void visitaAzulejoEscadas(AzulejoEscadas a) {
		fout.print( "escadas " + a.getPosicao().x + "," + a.getPosicao().y );
		fout.println( " " + mapaFich.getFicheiroAzulejo( a.getPosicao() ) );

	}

	@Override
	public void visitaAzulejoFinal(AzulejoFinal a) {
		fout.print( "final " + a.getPosicao().x + "," + a.getPosicao().y );
		fout.println( " " + mapaFich.getFicheiroAzulejo( a.getPosicao() ) );

	}

	@Override
	public void visitaAzulejoParede(AzulejoParede a) {
		fout.print( "parede " + a.getPosicao().x + "," + a.getPosicao().y );
		fout.println( " " + mapaFich.getFicheiroAzulejo( a.getPosicao() ) );

	}

}
