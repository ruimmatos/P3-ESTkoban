package apps;

import java.awt.Point;
import java.io.*;

import armazem.Armazem;
import armazem.Caixote;
import armazem.Operario;
import azulejo.*;
import prof.jogos2D.image.ComponenteSimples;
import prof.jogos2D.image.ComponenteVisual;

/**
 * Classe que trata da leitura e grava��o dos n�veis.
 */
public class LeitorFicheirosESTkoban {
	// diret�rio onde est�o as imagens
	private static String artDir = "art/";
	
	private static ESTkoban est;
	
	public static void getESTkoban (ESTkoban estkoban) {
		est = estkoban;
	}

	/**
	 * M�todo que faz a leitura de um ficheiro de n�vel e retorna o armaz�m
	 * descrito pelo ficheiro
	 * @param nomeFich nome do ficheiro que cont�m o n�vel
	 * @param arm o marz�m onde colocar os elementos 
	 * @param mapa o mapa com os ficheiros de imagens usados neste n�vel
	 * @throws IOException quando a leitura corre mal
	 */
	public static Armazem lerFicheiro(String nomeFich, Armazem arm, MapaFicheiros mapa) throws IOException {		
		BufferedReader in = new BufferedReader( new FileReader( nomeFich ));

		// come�ar com o fundo do n�vel
		String linha = in.readLine();
		arm.setFundo( new ComponenteSimples( artDir + linha ) );
		mapa.setFicheiroFundo( linha );

		// agora ler a informa��o dos azulejos
		linha = in.readLine();
		while( linha != null ) {
			String info[] = linha.split(" ");
			switch( info[0] ){
			case "parede": 
				addParede( arm, mapa, info );
				break;
			case "chao":
				addChao( arm, mapa, info );
				break;
			case "final":
				addFinal( arm, mapa, info );
				break;
			case "caixote":
				addCaixote( arm, mapa, info );
				break;
			case "operario":
				addOperario( arm, mapa, info );
				break;
			case "escadas":
				addEscadas( arm, mapa, info );
				break;					
			case "direcao":
				addDirecao(arm, mapa, info);
				break;
			case "porta":
				addPorta(arm, mapa, info);
				break;
			}
			linha = in.readLine();
		}
		in.close();
		return arm;
	}

	private static void addParede(Armazem arm, MapaFicheiros mapa, String[] info) throws IOException {
		Point p = lerPosicao( info[1] );
		AzulejoParede ap = new AzulejoParede( new ComponenteSimples( artDir + info[2] ) );
		mapa.addFicheiroAzulejo(p, info[2] );
		arm.colocarAzulejo( p, ap );
	}

	private static void addChao(Armazem arm, MapaFicheiros mapa, String[] info) throws IOException {
		Point p = lerPosicao( info[1] );
		AzulejoChao ac = new AzulejoChao( new ComponenteSimples( artDir + info[2] ) );
		arm.colocarAzulejo( p, ac );
		mapa.addFicheiroAzulejo(p, info[2] );
	}

	private static void addFinal(Armazem arm, MapaFicheiros mapa, String[] info) throws IOException {
		Point p = lerPosicao( info[1] );
		AzulejoFinal ac = new AzulejoFinal( new ComponenteSimples( artDir + info[2] ), new ComponenteSimples( artDir + info[3] ) );
		arm.colocarAzulejo( p, ac );
		mapa.addFicheiroAzulejo(p, info[2] + " " + info[3] );
		
		ac.addListeners(est);
	}

	private static void addEscadas(Armazem arm, MapaFicheiros mapa, String[] info) throws IOException {
		Point p = lerPosicao( info[1] );
		AzulejoEscadas ac = new AzulejoEscadas( new ComponenteSimples( artDir + info[2] ) );
		arm.colocarAzulejo( p, ac );
		mapa.addFicheiroAzulejo(p, info[2] );
	}

	private static void addDirecao(Armazem arm, MapaFicheiros mapa, String[] info) throws IOException {
		Point p = lerPosicao( info[1] );
		int dx = Integer.parseInt( info[2] );
		int dy = Integer.parseInt( info[3] );
		AzulejoDirecional ac = new AzulejoDirecional( new ComponenteSimples( artDir + info[4] ), dx, dy );
		arm.colocarAzulejo( p, ac );
		mapa.addFicheiroAzulejo(p, info[4] );
	}

	private static void addPorta(Armazem arm, MapaFicheiros mapa, String[] info) throws IOException {
		Point p = lerPosicao( info[1] );
		boolean aberta = info[2].equalsIgnoreCase("A");
		Point p2 = lerPosicao( info[3] ); 
		AzulejoPorta ac = new AzulejoPorta( new ComponenteSimples( artDir + info[4] ), new ComponenteSimples( artDir + info[5] ), p2, aberta );
		arm.colocarAzulejo( p, ac );
		mapa.addFicheiroAzulejo(p, info[4] + " " + info[5] );
		
		for(Azulejo a : arm.getAzulejos()) {
			a.addListeners(ac);
		}
	}

	private static void addCaixote(Armazem arm, MapaFicheiros mapa, String[] info) throws IOException {
		Point p = lerPosicao( info[1] );
		Caixote c = new Caixote( new ComponenteSimples( artDir + info[2] ) );
		arm.colocarCaixote( p, c );
		mapa.addFicheiroCaixote(p, info[2] );
	}

	private static void addOperario(Armazem arm, MapaFicheiros mapa, String[] info) throws IOException {
		Point p = lerPosicao( info[1] );
		ComponenteVisual imgs[] = { new ComponenteSimples( artDir + info[2] ),
				new ComponenteSimples( artDir + info[3] ),
				new ComponenteSimples( artDir + info[4] ),
				new ComponenteSimples( artDir + info[5] ) };

		Operario op = new Operario( imgs );
		arm.colocarOperario( p, op );
		mapa.setFicheiroOperario( info[2] + " " + info[3] + " " + info[4] + " " + info[5] );
	}

	/** l� a posi��o a partir de uma string */
	private static Point lerPosicao(String pos) {
		String xy[] = pos.split(",");
		return new Point( Integer.parseInt(xy[0]), Integer.parseInt(xy[1]) );
	}

	/***
	 * Metodo que grava um armazem num ficheiro
	 * @param nomeFich nome do ficheiro onde gravar
	 * @param arm armazem que representa o nivel
	 * @mapaFich o mapa com os nomes dos ficheiros de imagem
	 * @throws IOException caso haja erros de escrita
	 */
	public static void gravarFicheiro(String nomeFich, Armazem arm, MapaFicheiros mapaFich ) throws IOException {
		PrintWriter fout;  // escritor de ficheiros
		//ArrayList<Azulejo> ultimos = new ArrayList<Azulejo>();

		try {
			fout = new PrintWriter(new BufferedWriter(new FileWriter(nomeFich)));

			// escrever o fundo
			fout.println( mapaFich.getFicheiroFundo() );
			// escrever a matriz dos azulejos
			VisitanteAzulejos v = new GravaAzulejo(fout, mapaFich);
			for ( Azulejo a : arm.getAzulejos() ) {
				if( a == null )
					continue;
				
//				// TODO estes instanceofs devem desaparecer todos
//				// TODO estes instanceofs devem desaparecer todos
//				// TODO estes instanceofs devem desaparecer todos
	
				a.aceita(v);
			}		
			
			VisitanteAzulejos vPortas = new GravaPortas(fout, mapaFich);
			
			for( Azulejo a :  arm.getAzulejos() ) {				
				if( a == null )
					continue;
				
				// TODO estes instanceofs devem desaparecer todos
				// TODO estes instanceofs devem desaparecer todos
				// TODO estes instanceofs devem desaparecer todos

				a.aceita(vPortas);
			}

			fout.println( );
			// escrever a matriz dos caixotes
			for ( Point pc = new Point(0,0); pc.y < arm.getAltura(); pc.y++) {
				for ( pc.x = 0; pc.x < arm.getComprimento(); pc.x++) {
					Caixote c = arm.getCaixote( pc );
					if (c == null )
						continue;
					fout.println( "caixote " + pc.x + "," + pc.y + " " + mapaFich.getFicheiroCaixote( pc ) );
				}
			}

			fout.println( );
			// escrever a posicao do operario
			if( arm.getOperario() != null ) {
				Point po = arm.getOperario().getPosicao();
				fout.println("operario "+ po.x + "," + po.y + " " + mapaFich.getFicheirosOperario() );
			}
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException();
		}
	}	
}