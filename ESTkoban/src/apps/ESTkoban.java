package apps;


import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import armazem.Armazem;
import armazem.Operario;
import azulejo.Azulejo;
import azulejo.MovimentoListener;


/**
 * Jogo ESTkoban
 */
public class ESTkoban extends JFrame implements ActionListener, MovimentoListener {

	/** vers�o */
	private static final long serialVersionUID = 1L;

	// as dimens�es do armaz�m s�o de 40 por 40
    private static int DIMCASA = 40;

    // o armaz�m e oper�rio a serem usados no jogo
	private Armazem oArmazem;
	private Operario operario;
	
	// qual o n�vel em que se est� a jogar
	private int nivel = 1;
	
	// a zona de jogo onde se vai desenhar o armaz�m
	private PainelDesenho zonaJogo;
	
	// fonte e cores a usar no texto de informa��o do jogo
	private Font fontInfo;
	private Color corInfo = new Color(200,67,4);
	private Color corInfoSombra = new Color(248,209,108);
	
	// quantos caixotes faltam colocar no s�tio e quantos passos se deram 
	private int nCaixotesForaSitio = 0;
	private int nPassos = 0;
	
	/**
	 * Construtor por defeito
	 */
	public ESTkoban() {
		super( "ESTkoban" );
		setupMenus();		
		setupZonaJogo();	
		setupFont();

		pack();
		zonaJogo.requestFocusInWindow();
		iniciarJogo();
	}

	private void setupFont() {
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont( Font.createFont(Font.TRUETYPE_FONT, new File("art/font.ttf") ) ); 
			fontInfo = new Font( "Kelt Caps Freehand", Font.BOLD, 30);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Eror na leitura da fonte", "Fonte desconhecida", JOptionPane.ERROR_MESSAGE );
			System.exit( 1 );
		}
	}

	/**
	 * faz o setup dos menus
	 */
	private void setupMenus() {
		JMenuBar barra =  new JMenuBar( );
		
		// menu jogo
		JMenu jogoMenu = new JMenu( "Jogo" );
		JMenuItem reinicarMenu = new JMenuItem( "Reiniciar Nivel" );
		reinicarMenu.setActionCommand( "nivel" );
		reinicarMenu.addActionListener( this );
		jogoMenu.add( reinicarMenu );

		JMenuItem novoMenu = new JMenuItem( "Reiniciar Jogo" );
		novoMenu.setActionCommand( "jogo" );
		novoMenu.addActionListener( this );
		jogoMenu.add( novoMenu );
		
		JMenuItem sairMenu = new JMenuItem( "Sair" );
		sairMenu.setActionCommand( "sair" );
		sairMenu.addActionListener( this );
		jogoMenu.add( sairMenu );
		barra.add( jogoMenu );
		setJMenuBar( barra );
	}

	/**
	 * m�todo que inicia o jogo
	 */
	public void iniciarJogo(){
	    lerNivel( nivel );
	    nPassos = 0;
	    zonaJogo.repaint();
	}
	
	/**
	 * m�todo que l� o ficheiro associado a um dado n�vel
	 * @param nivel n�vel a carregar
	 */
	private void lerNivel( int nivel ){
		String file = "niveis/nivel"+nivel+".txt";
		playFicheiro( file );
	}
	
	/** m�todo usado para testar um n�vel
	 * @param ficheiro o ficheiro com o n�vel a testar
	 */
	public void testaFicheiro( String ficheiro ) {
		nivel = -1;
		getJMenuBar().setVisible( false );
		playFicheiro( ficheiro );
	}
	
	/**
	 * m�todo que vai jogar um dado ficheiro
	 * @param ficheiro nome do ficheiro a jogar
	 */
	public void playFicheiro( String ficheiro ){
		oArmazem = new Armazem( new Point(0,0), 40);
		try {
			LeitorFicheirosESTkoban.getESTkoban(this);
			LeitorFicheirosESTkoban.lerFicheiro( ficheiro, oArmazem, new MapaFicheiros() );
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog( null, "Erro na leitura do ficheiro " + ficheiro, "ERRO", JOptionPane.ERROR_MESSAGE );
			System.exit( 1 );			
		}
		operario = oArmazem.getOperario();
		nCaixotesForaSitio = oArmazem.getNCaixotes();
		
		int correto = oArmazem.testaValidade();
		if( correto != Armazem.CORRETO ) {
			switch( correto ) {
			case Armazem.SEM_OPERARIO:
				JOptionPane.showMessageDialog( this, "O nivel nao tem operario!", "Erro", JOptionPane.ERROR_MESSAGE );
				break;
			case Armazem.SEM_CAIXOTES:
				JOptionPane.showMessageDialog( this, "O nivel nao tem caixotes!", "Erro", JOptionPane.ERROR_MESSAGE );
				break;
			case Armazem.FINAIS_DIFERENTE_CAIXOTES:
				JOptionPane.showMessageDialog( this, "O nivel nao tem finais suficientes!", "Erro", JOptionPane.ERROR_MESSAGE );
				break;
			case Armazem.SEM_SOLUCAO:
				JOptionPane.showMessageDialog( this, "O nivel nao tem solucao!", "Erro", JOptionPane.ERROR_MESSAGE );
				break;
			}
			setVisible( false );
		}
	}
	
	/**
	 * m�todo que responde a uma tecla ser premida
	 * @param e evento associado ao premir da tecla
	 */
	private void teclaPremida(KeyEvent e) {
		// ver se se mexeu
		int oldPassos = nPassos;
		if( e.getKeyCode() == KeyEvent.VK_UP ){
			if( operario.deslocar( 0, -1 ) )
				nPassos++;
		}
		else if( e.getKeyCode() == KeyEvent.VK_DOWN ){
			if( operario.deslocar( 0, 1 ) )
				nPassos++;
		}
		else if( e.getKeyCode() == KeyEvent.VK_LEFT ){
			if( operario.deslocar( -1, 0 ) )
				nPassos++;
		}
		else if( e.getKeyCode() == KeyEvent.VK_RIGHT ){
			if( operario.deslocar( 1, 0 ) )
				nPassos++;
		}
		// mandar actualizar o desenho das coisas
		zonaJogo.repaint();
		
		// se se mexeu tem-se de testar o final do jogo
		if( nPassos != oldPassos ){
			if( ganhou() )
				ganhouJogo();
		}
	}
	
	/** teste se ganhou o jogo
	 * @return se ganhou o jogo
	 */
	private boolean ganhou() {
		// TODO esta forma esta muito mal feita, pois é preciso verificar quais
		// os azulejos finais que estao vazios, o que leva a percorrer TODOS os azulejos
		// para ver qual o seu tipo.
//		for( Azulejo a : oArmazem.getAzulejos() )
//			if( a instanceof AzulejoFinal )
//				// se um esta vazio nao ganhou
//				if( !((AzulejoFinal) a).estaOcupado() )
//					return false;
//		return true;
		if(nCaixotesForaSitio!=0)
			return false;
		return true;
	}
	
	/** M�todo chamado quando se ganha um jogo
	 */
	private void ganhouJogo() {
		JOptionPane.showMessageDialog(null, "Parabens, terminou o nivel " + nivel );
		if( nivel == -1 ) {
			dispose();
			return;
		}
		nivel++;
		iniciarJogo();		
	}
	
	@Override
	public void updatePosicaoCaixote(Azulejo a) {
		// TODO Auto-generated method stub
		
	}

	/** zona de jogo
	 */
	private class PainelDesenho extends JPanel {
		private static final long serialVersionUID = 1L;

		public PainelDesenho() {
			Dimension size = new Dimension(DIMCASA*16+120,DIMCASA*16);
			setSize( size );
			setPreferredSize( size );
			setMinimumSize( size );
			addKeyListener( new KeyAdapter(){				
				public void keyPressed(KeyEvent e) {
					teclaPremida( e );
				}
			});
		}
		
		public void paintComponent( Graphics g ){			
			super.paintComponent( g );
			oArmazem.desenhar( (Graphics2D) g );
			
			g.setColor( corInfo );
			g.setFont( fontInfo );
			g.drawString("Nivel", 652, 42);
			g.drawString(""+nivel, 672, 82);
			g.drawString("Caixas", 652, 122);
			g.drawString(""+nCaixotesForaSitio, 672, 162);
			g.drawString("Passos", 652, 202);
			g.drawString(""+nPassos, 672, 242);
			
			g.setColor( corInfoSombra );
			g.drawString("Nivel", 652, 40);
			g.drawString(""+nivel, 672, 80);
			g.drawString("Caixas", 650, 120);
			g.drawString(""+nCaixotesForaSitio, 670, 160);
			g.drawString("Passos", 650, 200);
			g.drawString(""+nPassos, 670, 240);
		}
	}
	
	/** resposta aos menus
	 */
	public void actionPerformed( ActionEvent e){
		String cmd = e.getActionCommand();
		if( cmd.equals( "sair" ) ){
			System.exit( 0 );
		}
		else if( cmd.equals("nivel" )){
			iniciarJogo();
		}
		else if( cmd.equals("jogo" )){
			nivel = 1;
			iniciarJogo();
		}
	}	
	
	/** faz o setup da zona de jogo
	 */
	private void setupZonaJogo() {
		zonaJogo = new PainelDesenho( );
		zonaJogo.setBackground( Color.BLUE );		
		getContentPane().add( zonaJogo, BorderLayout.CENTER );
	}	
	
	/** arrancar com este jogo
	 */
	public static void main(String[] args) {		
		ESTkoban est = new ESTkoban( );
		est.setVisible( true );
		est.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
	}
	

}
