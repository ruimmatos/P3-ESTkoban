package apps;

import java.awt.Point;

/** Classe que guarda os nomes dos ficheiros usados
 * um dado n�vel. Esta informa��o � importante quando se est� a gravar
 * um ficheiro. 
 */
public class MapaFicheiros {

	private String ficheiroOperario;
	private String ficheiroFundo;
	private String ficheirosImagensAzulejos[][] = new String[16][16];
	private String ficheirosImagensCaixotes[][] = new String[16][16];
	
	public MapaFicheiros() {
	}

	/** adiciona o nome do ficheiro do azulejo colocado na coordenada pt
	 * Se tiver mais que um nome, estes devem estar separados por ' ' (espa�o)
	 * @param pt coordenada do azulejo
	 * @param s nome do ficheiro
	 */
	public void addFicheiroAzulejo( Point pt, String s ) {
		ficheirosImagensAzulejos[pt.x][pt.y] = s;
	}
	
	/** retorna o nome do ficheiro do azulejo numadada posi��o
	 * @param p coordenada do azulejo
	 * @return o nome do ficheiro associado
	 */
	public String getFicheiroAzulejo( Point p ) {
		return ficheirosImagensAzulejos[p.x][p.y];
	}
	
	/** adiciona o nome do ficheiro do caixote colocado na coordenada pt
	 * @param pt coordenada do caixote
	 * @param s nome do ficheiro
	 */
	public void addFicheiroCaixote( Point pt, String s ) {
		ficheirosImagensCaixotes[pt.x][pt.y] = s;
	}
	
	/** retorna o nome do ficheiro do caixote numa dada posi��o
	 * @param p coordenada do caixote
	 * @return o nome do ficheiro associado
	 */
	public String getFicheiroCaixote( Point p ) {
		return ficheirosImagensCaixotes[p.x][p.y];
	}

	/** armazena o nome dos ficheiros associados ao oper�rio. <br>
	 * Os nomes devem estar separados por ' ' (espa�o)
	 * @param ficheiroOperario o nome dos ficheiros associados ao oper�rio
	 */
	public void setFicheiroOperario(String ficheiroOperario) {
		this.ficheiroOperario = ficheiroOperario;
	}
	
	/** Retorna o nome dos ficheiros associados ao oper�rio. <br>
	 * Os nomes est�o separados por ' ' (espa�o)
	 * @param ficheiroOperario o nome dos ficheiros associados ao oper�rio
	 */
	public String getFicheirosOperario() {
		return ficheiroOperario;
	}

	/** armazena o nome do ficheiro de fundo
	 * @param ficheiroFundo o nome do ficheiro de fundo
	 */
	public void setFicheiroFundo(String ficheiroFundo) {
		this.ficheiroFundo = ficheiroFundo;
	}
	
	/** retorna o nome do ficheiro de fundo
	 * @return o nome do ficheiro de fundo
	 */
	public String getFicheiroFundo() {
		return ficheiroFundo;
	}
}
