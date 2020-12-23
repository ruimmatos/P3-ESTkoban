package armazem;

import java.awt.*;
import java.util.ArrayList;

import azulejo.Azulejo;
import azulejo.AzulejoFinal;
import prof.jogos2D.image.ComponenteVisual;

/**
 * Classe que representa o armazém onde irá decorrer o jogo
 */
public class Armazem {

    private ComponenteVisual figura;  // figura associado ao fundo do armazém
    private int dimensaoAzulejo;      // a dimensão de cada azulejo
    private Point posicao;            // posição da imagem do armazém
	private Operario operario;        // o operário que aqui trabalha

	/** o array de azulejos */
	private Azulejo osAzulejos[][] = new Azulejo[16][16];

	/** o array de caixotes */
	private Caixote caixotes[][] = new Caixote[16][16];

	/**
	 * construtor do armazém
	 * @param fig a figura associada ao fundo do armazém
	 * @param t a coordenada do armazém
	 * @param dimAzul a dimensão de cada azulejo 
	 */
    public Armazem( ComponenteVisual fig, Point t, int dimAzul ){
    	dimensaoAzulejo = dimAzul;
    	posicao = t;
    	figura = fig;
    	figura.setPosicao( t );
    }
    
    /** Construtor do armazém
	 * @param t a coordenada do armazém
	 * @param dimAzul a dimensão de cada azulejo 
     */
    public Armazem( Point t, int dimAzul ){
    	dimensaoAzulejo = dimAzul;
    	posicao = t;
    }

    
    /** devolve a altura do armazém
     * @return a altura do armazém
     */
    public int getAltura(){
    	return 16;
    }

    /** devolve o comprimento do armazém
     * @return o comprimento do armazém
     */
    public int getComprimento(){
    	return 16;
    }

    /** desenha o armazém
     * @param g o ambiente gráfico onde desenhar
     */
    public void desenhar( Graphics2D g ) {    	
    	figura.desenhar( g );

        for( int x = 0; x < osAzulejos.length; x++ )
             for( int y = 0; y < osAzulejos[x].length; y++ ){
                  if( osAzulejos[x][y] != null )
                	  osAzulejos[x][y].desenhar( g );
                  if( caixotes[x][y] != null )
                	  caixotes[x][y].desenhar( g );
             }
        if( operario != null )
        	operario.desenhar( g );
    }

   /** indica se a coordenada é valida neste armazém
    * @param p a coordenada a verificar
    * @return se a coordenada é valida neste armazém
    */
    public boolean eCoordenadaValida( Point p ) {
        return p != null && p.x>=0 && p.x < osAzulejos.length && p.y>=0 && p.y< osAzulejos[0].length;
    }

    /** coloca um azulejo no armazém. 
     * @param p posição onde colocar o azulejo
     * @param azul azulejo a colocar
     */
    public void colocarAzulejo( Point p, Azulejo azul ){
        if( azul == null ) {
        	removerAzulejo(p);
        	return;
        }
        if( !eCoordenadaValida( p ) )
            return;
        osAzulejos[p.x][p.y] = azul;        
        azul.setArmazem( this );
        azul.setPosicao( p );                
    }
    
    /** coloca o caixote numa dada posição do armazém. NÃO testa se a colocação é válida 
     * @param p onde colocar o caixote
     * @param caixa caixote a colocar
     */
    public void colocarCaixote( Point p, Caixote caixa ){
        if( !eCoordenadaValida( p ) )
            return;
        // se não tem azulejo nessa posição sai
        Azulejo az = osAzulejos[p.x][p.y];
        if( az == null )
        	return;
        
        // colocar o caixote
        az.ocupar( caixa );
        caixotes[p.x][p.y] = caixa;        
        if( caixa == null ) return;
        caixa.setArmazem( this );
        caixa.setPosicao( p );
        
    }
    
    /** coloca o operário numa dada posição do armazém. NÃO testa se a colocação é válida
     * @param p onde colocar o operário
     * @param op o operário
     */
    public void colocarOperario( Point p, Operario op  ){
        if( !eCoordenadaValida( p ) )
            return;        
        // se não tem azulejo nessa posição sai
        Azulejo az = osAzulejos[p.x][p.y];
        if( az == null )
        	return;

        // se o operário não pode ir para o azulejo sai
        az.ocupar( op );

        if( operario != null )
        	removerOperario( operario.getPosicao() );
        operario = op;
        if( op == null ) return;
        
        op.setArmazem( this );
        op.setPosicao( p );                
    }
  
    /**
     * remove do armazém o azulejo da coordenada indicada   
     * @param p coordenda de onde retirar o azulejo 
     * @return o azulejo retirado
     */
    public Azulejo removerAzulejo( Point p ){
        if( !eCoordenadaValida( p ) )
            return null;
            
        Azulejo old = osAzulejos[p.x][p.y];
        osAzulejos[p.x][p.y] = null;
        // se se remove o azulejo também se tem de remover o caixote, e o operário
        caixotes[p.x][p.y] = null; 
        if( operario != null && operario.getPosicao().equals( p ) )
        	operario = null;
        return old;        
    }

    /**
     * remove do armazém o caixote presente na coordenada p 
     * @param p a coordenada de onde retirar o caixote
     * @return o caixote retirado
     */
    public Caixote removerCaixote( Point p ){
        if( !eCoordenadaValida( p ) )
            return null;
        
        // ver se lá tem azulejo, senão não pode retirar caixote
        Azulejo az = osAzulejos[p.x][p.y];
        if( az == null )
        	return null;
        
        Caixote old = caixotes[p.x][p.y];
        
        // retirar o caixote do azulejo
        az.remover( old );
        caixotes[p.x][p.y] = null;
        return old;        
    }
    
    /**
     * remove do armazém o operário presente na coordenada p 
     * @param p a coordenada de onde remover o operário
     * @return o operário retirado
     */
    public Operario removerOperario( Point p ){
        if( !eCoordenadaValida( p ) )
            return null;
        Operario old = operario;
        operario = null;
        return old;        
    }

    /** devolve o azulejo presente na coordenada indicada
     * @param p a coordenada onde está o azulejo pretendido
     * @return o azulejo presente na coordenada indicada
     */
    public Azulejo getAzulejo( Point p ){
        if( !eCoordenadaValida( p ) )
            return null;
        
        return osAzulejos[ p.x ][ p.y ];        
    }
    
    /** devolve o caixote presente na coordenada indicada
     * @param p a coordenada onde está o caixote pretendido
     * @return o caixote presente na coordenada indicada
     */
    public Caixote getCaixote( Point pt ){
        if( !eCoordenadaValida( pt ) )
            return null;
        
        return caixotes[ pt.x ][ pt.y ];        
    }
    
    /** converte coordenadas do écran para coordenadas do armazém
     * @param ecran coordenadas do écran a converter
     * @return a correspondente coordenada do armazém, null se não for dentro do armazém 
     */
    public Point doEcranParaArmazem( Point ecran ){
        // calcular em que casa se premiu
        int x = ((ecran.x - posicao.x) / dimensaoAzulejo);
        int y = ((ecran.y - posicao.y) / dimensaoAzulejo);

        // verificar se a casa está dentro do tabuleiro
        // se não estiver retornar (0,0)
        Point casa = new Point( x, y);
        if( !eCoordenadaValida( casa ) )
            return null;
        return casa;
    }

    /** converte coordenadas do armazém para o écran
     * @param casa coordenada do armazém
     * @return a respetiva coordenada em pixeis (no meio do azulejo correspondente)
     */
    public Point getEcran( Point p ){
        int x = (p.x) * dimensaoAzulejo + posicao.x + dimensaoAzulejo/2;
        int y = (p.y) * dimensaoAzulejo + posicao.y + dimensaoAzulejo/2;
        return new Point(x,y);
    }
    
    /** limpa o armazém todo
     */
    public void limpar( ){
   	    for( int x = 0; x < 8; x++ )
   	         for( int y = 0; y < 8; y++ ){
   	              osAzulejos[x][y] = null;
   	              caixotes[x][y] = null;
   	         }
   	    operario = null;
    }

    /** define a imagem de fundo
     * @param fundo a nova imagem de fundo
     */
    public void setFundo( ComponenteVisual fundo ){
    	figura = fundo;
    	fundo.setPosicao( posicao );
    }
    
    /** indica quantos caixotes tem este armazém
     * @return o número de caixotes presentes no armazém
     */
    public int getNCaixotes(){
    	int n = 0;
        for( int x = 0; x < caixotes.length; x++ )
            for( int y = 0; y < caixotes[x].length; y++ ){
                 if( caixotes[x][y] != null )
               	   n++;                  
            }    	
    	return n;
    }
    
    /** devolve os caixotes presentes no armazém
     * @return os caixotes presentes no armazém
     */
    public java.util.List<Caixote> getCaixotes(){
    	ArrayList<Caixote> cxs = new ArrayList<Caixote>();
    	
        for( int x = 0; x < caixotes.length; x++ )
            for( int y = 0; y < caixotes[x].length; y++ ){
                 if( caixotes[x][y] != null )
               	  	cxs.add( caixotes[x][y] );                  
            }    	
    	return cxs;
    }
    
    /** devolve os azulejos presentes no armazém
     * @return os azulejos presentes no armazém
     */
    public java.util.List<Azulejo> getAzulejos(){
    	ArrayList<Azulejo> as = new ArrayList<Azulejo>();
    	
        for( int x = 0; x < osAzulejos.length; x++ )
            for( int y = 0; y < osAzulejos[x].length; y++ ){
                 if( osAzulejos[x][y] != null )
               	  	as.add( osAzulejos[x][y] );                  
            }    	
    	return as;
    }
    
    /** devolve o operário que trabalha no armazém
     * @return o operário que trabalha no armazém
     */
    public Operario getOperario(){
    	return operario;
    }

    /** constantes para definir a validade do jogo
     */
    public static final int CORRETO = 0;
    public static final int SEM_OPERARIO = 1;
    public static final int SEM_CAIXOTES = 2;
    public static final int FINAIS_DIFERENTE_CAIXOTES = 3;
    public static final int SEM_SOLUCAO = 4;

    /** indica se o armazém está bem ou mal formado
     * @return uma indicação de como está o armazém validado. <br>
     * retorna CORRETO se estiver tudo bem e um código de erro se alguma coisa estiver mal.
     */
    public int testaValidade( ) {
    	if( operario == null )
    		return SEM_OPERARIO;

    	int nCaixas = getNCaixotes();
    	if( nCaixas == 0 )
    		return SEM_CAIXOTES;

    	int nFinais = 0;
        for( int x = 0; x < osAzulejos.length; x++ )
            for( int y = 0; y < osAzulejos[x].length; y++ ){
                 if( osAzulejos[x][y] != null && osAzulejos[x][y] instanceof AzulejoFinal )
                	 nFinais++;
            }
    	if( nFinais == 0 || nFinais != nCaixas )
    		return FINAIS_DIFERENTE_CAIXOTES;
    	
    	return CORRETO;
    }
}
