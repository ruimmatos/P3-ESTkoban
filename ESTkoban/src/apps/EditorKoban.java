package apps;

import java.awt.BorderLayout;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JToggleButton;
import armazem.Armazem;
import armazem.Caixote;
import armazem.Operario;
import azulejo.*;
import prof.jogos2D.image.ComponenteSimples;
import prof.jogos2D.image.ComponenteVisual;

import javax.swing.JToolBar;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Esta classe � o editor de n�veis do ESTkoban
 */
public class EditorKoban extends JFrame {

	// vers�o
	private static final long serialVersionUID = 1L;

	// constantes com as dimens�es do jogo
	private static final int TAMANHO_AZULEJO = 40;
	private static final int DIMENSAO_ARMAZEM = 16;
	
	private EstadoNada estadoNada = new EstadoNada();
	private EstadoPorOperario estadoPorOperario = new EstadoPorOperario();
	private EstadoPorCaixote estadoPorCaixote = new EstadoPorCaixote();
	private EstadoPorAzulejo estadoPorAzulejo = new EstadoPorAzulejo();
	private EstadoPorPorta estadoPorPorta = new EstadoPorPorta();
	private EstadoPorTrigger estadoPorTrigger = new EstadoPorTrigger();
	
	// constantes para os v�rios azulejos a inserir
//	private static final int VAZIO = 0;	
//	private static final int CHAO = 1;
//	private static final int PAREDE = 2;
//	private static final int FINAL = 3;
//	private static final int ESCADAS = 4;
//	private static final int DIRECIONAL_ESQ = 5;
//	private static final int DIRECIONAL_DIR = 6;
//	private static final int DIRECIONAL_CIMA = 7;
//	private static final int DIRECIONAL_BAIXO = 8;
//	private static final int PORTA_ABERTA = 9;
//	private static final int PORTA_FECHADA = 10;

	// qual a accao que se est� a realizar
	//private int acaoAtual = NADA;
	private EstadoEdicao estadoAtual = estadoNada; 

	// indica qual o azulejo que se est� a inserir
	//private int azulejoSel = VAZIO;
	private Azulejo azulejoSel = null;
	
	// informa��es obre o ficheiro a editar atualmente
	private String ficheiro;
	private boolean alterado = false;

	// t�tulo da aplica��o
	private String titulo = "Editor ESTkoban";

	// o armazem e o oper�rio que se v�o editar
	private Armazem armazem;
	private Operario operario;
	
	// informa��es obre os ficheiros de imagens a usar no n�vel em edi��o
	private File ficheirosUsados[] = new File[2];
	private ComponenteVisual imagens[];
	private File operarioFiles[];
	private File fundoFile;
	private MapaFicheiros mapaFicheiros = new MapaFicheiros();
	
	// No azulejo porta � preciso saber qual �, para depois se atribuir o trigger
	private AzulejoPorta portaCriada;

	/** coisas para a interface gr�fica */
	private JToolBar tools;
	// di�logo para a selec��o de ficheiros
	private JFileChooser fileChooser; 
	private JFileChooser imgChooser;
	
	private JPanel jContentPane = null;
	private JPanel painelArmazem = null;
	private ButtonGroup btGroup = new ButtonGroup(); 
	
	/** Construtor por defeito */
	public EditorKoban() {
		super();
		initialize();
		novoNivel();
	}

	/** define que o n�vel foi alterado e altera o t�tulo da janela
	 * @param b se foi alterado ou n�o
	 */
	private void setAlterado(boolean b) {
		alterado = b;
		setTitle( titulo + " - " + (ficheiro==null? "sem nome" : ficheiro) + (alterado? "*": "") );		
	}

	/** m�todo que processa a ri��o de um novo n�vel
	 */
	private void novoNivel(){
		// pedir os dados do oper�rio e do fundo
		NovoNivelDialog novoDlg = new NovoNivelDialog( this, TAMANHO_AZULEJO );
		novoDlg.setVisible( true );
		
		// guarda as informa��es das imagens usadas no oper�rio e fundo
		fundoFile = novoDlg.getFundoFile();
		operarioFiles= novoDlg.getOperarioFiles();
		
		// ainda n�o tem nome nem foi alterado
		ficheiro = null;
		setAlterado( false );
		try {
			// criar o armazem para este n�vel
			armazem =  new Armazem( new ComponenteSimples( fundoFile.getAbsolutePath() ), new Point(0,0), 40);

			// ler as informa��es sobre os ficheiros de imagens do oper�rio
			ComponenteVisual imgs[] = new ComponenteVisual[ operarioFiles.length];
			String opFichs = ""; 
			for( int i=0; i < imgs.length; i++ ) {
				imgs[i] = new ComponenteSimples( operarioFiles[i].getAbsolutePath() );
				opFichs += operarioFiles[i].getName() + " ";
			}
			
			// criar o oper�rio e armazenar a informa��o dos respetivos ficheiros
			operario = new Operario( imgs );
			mapaFicheiros = new MapaFicheiros();
			mapaFicheiros.setFicheiroOperario( opFichs );
			mapaFicheiros.setFicheiroFundo( fundoFile.getName() );			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Nao encontrei os ficheiros com o fundo e operario" );
			System.exit( 1 );
		} 

		// definir que a acao atual é nao fazer nada
		estadoAtual = estadoNada;
		// nenhum dos botoes esta selecionado
		btGroup.clearSelection();
		painelArmazem.repaint();
	}
	
	/** m�todo que l� um ficheiro
	 */
	private void lerFicheiro() {
		// ver se h� altera��e spor gravar e perguntar se as quer gravar
		if( alterado ) {
			int res = JOptionPane.showConfirmDialog(this, "Ha alteracoes nao gravadas! Deseja prosseguir sem gravar?" );
			if( res == JOptionPane.CANCEL_OPTION )
				return;
			else if( res == JOptionPane.NO_OPTION ) {
				gravar( ficheiro );
			}			
		}
		
		// abertura do ficheiro com a informa��o do n�vel
		fileChooser.setDialogTitle("Abrir");               
		int returnVal = fileChooser.showOpenDialog(null);  // abrir a janela de escolha de ficheiro				
		if (returnVal == JFileChooser.APPROVE_OPTION) {    // se se escolheu um ficheiro, vai-se abrir
			File file = fileChooser.getSelectedFile();
			try {	            			            			           
	            Armazem arm = new Armazem( new Point(0,0), TAMANHO_AZULEJO );
	            mapaFicheiros = new MapaFicheiros();
	            LeitorFicheirosESTkoban.lerFicheiro( file.getAbsolutePath(), arm, mapaFicheiros );
	            armazem = arm;
	            operario = arm.getOperario();
	            ficheiro = file.getName();
	            setAlterado( false );
	            painelArmazem.repaint();
			}
			catch( Exception e ) {
				e.printStackTrace();
				JOptionPane.showMessageDialog( null, "Erro na leitura do ficheiro " + file.getName(), "ERRO", JOptionPane.ERROR_MESSAGE );
			}			
		}
	}
	
	/**M�todo que grava um ficheiro com um dado nome
	 * @param ficheiro nome do ficheiro a usar 
	 */
	private void gravar(String ficheiro) {
		// se ainda n�o tem um nome, deve-se perguntar o nome
		if( ficheiro == null)
			gravarComo();	
		else {
            try {          
				LeitorFicheirosESTkoban.gravarFicheiro( "niveis/" + ficheiro, armazem, mapaFicheiros );
	            setAlterado( false );
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro a gravar o ficheiro " + ficheiro, "Erro de leitura", JOptionPane.ERROR_MESSAGE);
			}			
		}
	}
	
	/** m�todo chamado quando for necess�rio gravar o ficheiro com outro nome 
	 */
	private void gravarComo( ) {
		fileChooser.setDialogTitle("Gravar como");               
		int returnVal = fileChooser.showOpenDialog(null);   // abrir a janela de escolha de ficheiro				
		if (returnVal == JFileChooser.APPROVE_OPTION) {     // se se escolheu um ficheiro, vai-se abrir
            File file = fileChooser.getSelectedFile();	
            if( file.exists() ) {
            	int res = JOptionPane.showConfirmDialog(this, "O ficheiro ja existe, tem a certeza que o quer substituir?",
            			                                "Atencao", JOptionPane.YES_NO_OPTION );
            	if( res == JOptionPane.NO_OPTION )
            		return;
            }
            
            try {
            	ficheiro = file.getName();            
				LeitorFicheirosESTkoban.gravarFicheiro( "niveis/" + ficheiro, armazem, mapaFicheiros );
	            setAlterado( false );
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro a gravar o ficheiro " + file.getAbsolutePath(), "Erro de leitura", JOptionPane.ERROR_MESSAGE);
			}
		}		
	}
	
	/** Executa o jogo para experimentar o n�vel no ESTkoban 
	 */
	private void experimentaNivel() {
		// se n�o est� gravado
		if( alterado )
			gravar( ficheiro );
		// se ainda n�o estiver gravado � porque isto falhou
		if( alterado )
			return;

		// cria o jogo e joga o n�vel associado
		ESTkoban est = new ESTkoban();
		est.setVisible( true );
		est.testaFicheiro( "niveis/" +ficheiro );
	}


	/**
	 * metodo chamado quando se carrega no rato na zona de edicao
	 * @param e o evento associado ao rato
	 */
	private void ratoPremido( MouseEvent e ){
		setAlterado( true );
		Point p = armazem.doEcranParaArmazem( e.getPoint() );
		estadoAtual.ratoPremido(e, p);
		//painelArmazem.repaint();
	}

	/**
	 * metodo chamado quando se arrasta no rato na zona de edicao
	 * @param e o evento associado ao rato
	 */
	private void ratoArrastado( MouseEvent e ){
		Point p = armazem.doEcranParaArmazem( e.getPoint() );
		estadoAtual.ratoArrastado(e, p);
		//painelArmazem.repaint();
	}

	/** processa a criacao de um dado azulejo
	 * @param p ponto onde colocar o azulejo
	 */
	private void criarAzulejo(Point p) {
		// TODO acabar com este switch
		// TODO acabar com este switch
		// TODO acabar com este switch
		try {
			if(azulejoSel != null)
				
				armazem.colocarAzulejo( p, azulejoSel.clone() ) ;
			else
				armazem.colocarAzulejo(p, null);
			
//			switch( azulejoSel ){
//			case VAZIO:	 armazem.colocarAzulejo( p, null ); break;
//			case CHAO: 	 armazem.colocarAzulejo( p, new AzulejoChao( criaImagem( 0 ) ) ); break;
//			case PAREDE: armazem.colocarAzulejo( p, new AzulejoParede( criaImagem( 0 ) ) ); break;
//			case FINAL:  armazem.colocarAzulejo( p, new AzulejoFinal( criaImagem( 0 ), criaImagem( 1 ) ) ); break;
//			case PORTA_ABERTA:  armazem.colocarAzulejo( p, new AzulejoPorta( criaImagem(0), criaImagem(1), new Point( 5,5), true ) ); break;
//			case PORTA_FECHADA: armazem.colocarAzulejo( p, new AzulejoPorta( criaImagem(0), criaImagem(1), new Point( 5,5), false ) ); break;
//			case ESCADAS:armazem.colocarAzulejo( p, new AzulejoEscadas( criaImagem(0) ) ); break;
//			case DIRECIONAL_ESQ: armazem.colocarAzulejo( p, new AzulejoDirecional( criaImagem(0), 1, 0 ) ); break;
//			case DIRECIONAL_DIR: armazem.colocarAzulejo( p, new AzulejoDirecional( criaImagem(0), -1, 0 ) ); break;
//			case DIRECIONAL_CIMA: armazem.colocarAzulejo( p, new AzulejoDirecional( criaImagem(0), 0, 1 ) ); break;
//			case DIRECIONAL_BAIXO: armazem.colocarAzulejo( p, new AzulejoDirecional( criaImagem(0), 0, -1 ) ); break;
//			}
			
			// assinalar que ja tem imagens e registar os ficheiros de imagens usados
			//pedeImagens = false;
			if( ficheirosUsados[0] != null )
				mapaFicheiros.addFicheiroAzulejo(p, ficheirosUsados[0].getName() + (ficheirosUsados[1]!= null? " " + ficheirosUsados[1].getName() : "") );
		} 
		catch( Exception e ){
			// se aconteceu algo errado (n�o escolheu bem uma imagem, por exemplo), desligar a a��o atual e por tudo ao in�cio
			//e.printStackTrace();
			estadoAtual = estadoNada;
			btGroup.clearSelection();
		}
		
	}

	
	/** pede ao utilizador uma imagem a usar num dos elementos gr�ficos
	 * @param msg mensagem a indicar para que serva a imagem
	 * @param idx �ndice do ficheiro a ser usado
	 * @return o componente visual criado pela imagem
	 */
	private ComponenteSimples pedeImagem( String msg, int idx ) {
		imgChooser.setDialogTitle( msg );
		int res = imgChooser.showOpenDialog( this );
		if( res == JFileChooser.APPROVE_OPTION )
			ficheirosUsados[idx] = imgChooser.getSelectedFile();
		
		try {
			ComponenteSimples cs = new ComponenteSimples( ficheirosUsados[idx].getAbsolutePath() );
			return cs;
		} catch (IOException e) {
			e.printStackTrace();
			// se aconteceu algo errado (n�o escolheu bem uma imagem, por exemplo), desligar a a��o atual e por tudo ao in�cio
			//e.printStackTrace();
			estadoAtual = estadoNada;
			btGroup.clearSelection();
			return null;
		}
	}
	
	/** cria uma imagem com base nos ficheiros escolhidos anteriormente
	 * @param idx �ndice do ficheiro a ser usado
	 * @return o componente visual criado pela imagem
	 */
	private ComponenteSimples criaImagem( int idx ) {
		try {
			ComponenteSimples cs = new ComponenteSimples( ficheirosUsados[idx].getAbsolutePath() );
			return cs;
		} catch (IOException e) {
			e.printStackTrace();
			// se aconteceu algo errado (n�o escolheu bem uma imagem, por exemplo), desligar a a��o atual e por tudo ao in�cio
			//e.printStackTrace();
			estadoAtual = estadoNada;
			btGroup.clearSelection();
			return null;
		}
	}
	
	/** Cria��o dos v�rios bot�es da barra de ferramentas	
	 */
	private JToolBar getTools() {
		tools = new JToolBar();
		tools.add( setupNovoBt(  new JButton( new ImageIcon("icons/new.gif" ))) );
		tools.add( setupLoadBt(  new JButton( new ImageIcon("icons/load.gif"))) );
		tools.add( setupSaveBt(  new JButton( new ImageIcon("icons/save.gif"))) );
		tools.add( setupSaveAsBt(new JButton( new ImageIcon("icons/saveAs.gif"))) );
		tools.add( setupRunBt(   new JButton( new ImageIcon("icons/compile.gif"))) );
		 
		tools.add( setupBotaoOperario( criaBotaoAcao( "icons/operario.gif") ) );
		tools.add( setupBotaoCaixote(  criaBotaoAcao( "icons/caixote.gif") ) );
		
		tools.add( setupBotaoAzulejoVazio( criaBotaoAcao( "icons/vazio.gif" ) ) );
		tools.add( setupBotaoAzulejoParede( criaBotaoAcao( "icons/parede2.gif") ) );
		tools.add( setupBotaoAzulejoChao( criaBotaoAcao( "icons/chao.gif" ) ) );
		tools.add( setupBotaoAzulejoFinal( criaBotaoAcao( "icons/final.gif" ) ) );
		tools.add( setupBotaoAzulejoEscadas( criaBotaoAcao( "icons/escadas.gif" ) ) );
		tools.add( setupBotaoAzulejoDirecional( -1, 0, criaBotaoAcao( "icons/esquerda.gif" ) ) );
		tools.add( setupBotaoAzulejoDirecional( 0, 1, criaBotaoAcao( "icons/cima.gif" ) ) );
		tools.add( setupBotaoAzulejoDirecional( 1, 0, criaBotaoAcao( "icons/direita.gif") ) );
		tools.add( setupBotaoAzulejoDirecional( 0, -1, criaBotaoAcao( "icons/baixo.gif" ) ) );
		tools.add( setupBotaoAzulejoPorta( true, criaBotaoAcao("icons/porta_aberta.gif") ) );
		tools.add( setupBotaoAzulejoPorta( false, criaBotaoAcao( "icons/porta_fechada.gif") ) );
		return tools;
	}
	
	/** faz o setup do bot�o de a��o
	 * @param icon nome do ficheiro com o icon a usar
	 * @return o bot�o j� configurado
	 */
	private JToggleButton criaBotaoAcao( String icon ) {
		JToggleButton bt = new JToggleButton( new ImageIcon( icon ) );
		btGroup.add( bt );
		return bt;
	}

	/** faz o setup do bot�o que ir� servir para inserir o oper�rio
	 * @param bt bot�o a configurar
	 * @return o bot�o j� configurado
	 */
	private JToggleButton setupBotaoOperario( JToggleButton bt ) {
		bt.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				estadoAtual = estadoPorOperario;
			}
		});
		return bt;
	}
	
	/** faz o setup do bot�o que ir� servir para inserir um caixote
	 * @param bt bot�o a configurar
	 * @return o bot�o j� configurado
	 */
	private JToggleButton setupBotaoCaixote( JToggleButton bt ) {
		bt.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				estadoAtual = estadoPorCaixote;
				pedeImagem( "Imagem do caixote", 0 );
			}
		});
		return bt;
	}
	
	/** faz o setup do bot�o que ir� servir para remover azulejos do mapa
	 * @param bt bot�o a configurar
	 * @return o bot�o j� configurado
	 */
	private JToggleButton setupBotaoAzulejoVazio( JToggleButton bt ) {
		bt.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				estadoAtual = estadoPorAzulejo;
				azulejoSel = null;
			}
		});
		return bt;
	}

	/** faz o setup do bot�o que ir� servir para inserir um azulejo ch�o
	 * @param bt bot�o a configurar
	 * @return o bot�o j� configurado
	 */
	private JToggleButton setupBotaoAzulejoChao( JToggleButton bt ) {
		bt.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				estadoAtual = estadoPorAzulejo;
				resetImagens();
				imagens[0] = pedeImagem( "Imagem do ch�o", 0 );
				azulejoSel = new AzulejoChao( criaImagem( 0 ) );

			}
		});
		return bt;
	}

	/** faz o setup do bot�o que ir� servir para inserir um azulejo parede
	 * @param bt bot�o a configurar
	 * @return o bot�o j� configurado
	 */
	private JToggleButton setupBotaoAzulejoParede( JToggleButton bt ) {
		bt.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				estadoAtual = estadoPorAzulejo;
				resetImagens();
				imagens[0] = pedeImagem( "Imagem da parede", 0 );
				azulejoSel = new AzulejoParede( criaImagem( 0 ) );
			}
		});
		return bt;
	}

	/** faz o setup do bot�o que ir� servir para inserir um azulejo final
	 * @param bt bot�o a configurar
	 * @return o bot�o j� configurado
	 */
	private JToggleButton setupBotaoAzulejoFinal( JToggleButton bt ) {
		bt.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				estadoAtual = estadoPorAzulejo;
				resetImagens();
				imagens[0] = pedeImagem( "Imagem do final livre", 0 );
				imagens[1] = pedeImagem( "Imagem do final ocupado", 1 );
				azulejoSel = new AzulejoFinal( criaImagem( 0 ), criaImagem( 1 ) );
			}
		});
		return bt;
	}
	
	/** faz o setup do bot�o que ir� servir para inserir um azulejo escadas
	 * @param bt bot�o a configurar
	 * @return o bot�o j� configurado
	 */
	private JToggleButton setupBotaoAzulejoEscadas( JToggleButton bt ) {
		bt.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				estadoAtual = estadoPorAzulejo;
				resetImagens();
				imagens[0] = pedeImagem( "Imagem das escadas", 0 );
				azulejoSel = new AzulejoEscadas( criaImagem(0) );
			}
		});
		return bt;
	}

	/** faz o setup do bot�o que ir� servir para inserir um azulejo direcional
	 * @param dx a dire��o em x que o azulejo deve aceitar
	 * @param dy a dire��o em y que o azulejo deve aceitar
	 * @param bt bot�o a configurar
	 * @return o bot�o j� configurado
	 */
	private JToggleButton setupBotaoAzulejoDirecional( int dx, int dy, JToggleButton bt ) {
		bt.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				estadoAtual = estadoPorAzulejo;
				resetImagens();
				imagens[0] = pedeImagem( "Imagem do direcional", 0 );
				if( dx == -1 )
					azulejoSel = new AzulejoDirecional( criaImagem(0), 1, 0 );
				else if( dx == 1 )
					azulejoSel = new AzulejoDirecional( criaImagem(0), -1, 0 );
				else if( dy == -1 )
					azulejoSel = new AzulejoDirecional( criaImagem(0), 0, -1 );
				else if( dy == 1 )
					azulejoSel = new AzulejoDirecional( criaImagem(0), 0, 1 );
				
			}
		});
		return bt;
	}

	/** faz o setup do bot�o que ir� servir para inserir um azulejo direcional
	 * @param aberto se inicialmente a porta deve estar aberta
	 * @param bt bot�o a configurar
	 * @return o bot�o j� configurado
	 */
	private JToggleButton setupBotaoAzulejoPorta( boolean aberto, JToggleButton bt ) {
		bt.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				estadoAtual = estadoPorPorta;
				resetImagens();
				imagens[0] = pedeImagem( "Imagem da porta aberta", 0 );
				imagens[1] = pedeImagem( "Imagem da porta fechada", 1 );
				azulejoSel = aberto ? new AzulejoPorta( criaImagem(0), criaImagem(1), new Point( 5,5), true ): new AzulejoPorta( criaImagem(0), criaImagem(1), new Point( 5,5), false );
			}
		});
		return bt;
	}
	
	/** faz o reset �s imagens, j� que se v�o usar novas
	 */
	private void resetImagens() {
		ficheirosUsados = new File[2];
		imagens = new ComponenteVisual[2];
	}
	
	/** m�todo que inicializa a interface gr�fica
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());		
		this.setTitle( titulo );
		pack();

		// preparar os escolhedores de ficheiros
		imgChooser = new JFileChooser("art");
		imgChooser.setFileSelectionMode(JFileChooser.FILES_ONLY );
		imgChooser.setFileFilter( new FileNameExtensionFilter( "Ficheiros de imagem", ImageIO.getReaderFileSuffixes()) );
		fileChooser =  new JFileChooser("niveis");  
		fileChooser.setFileFilter( new FileNameExtensionFilter( "Ficheiros de nivel", "txt" ) );
	}

	/** This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getPainelArmazem(), BorderLayout.CENTER);
			jContentPane.add(getTools(), BorderLayout.NORTH);
		}
		return jContentPane;
	}

	/** This method initializes painelArmazem	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPainelArmazem() {
		if (painelArmazem == null) {
			painelArmazem = new JPanel(){
				private static final long serialVersionUID = 1L;

				protected void paintComponent(Graphics g) {	
					super.paintComponent(g);
					armazem.desenhar( (Graphics2D)g);
				}
			};
			painelArmazem.setLayout(new GridBagLayout());
			painelArmazem.setPreferredSize(new Dimension( TAMANHO_AZULEJO * DIMENSAO_ARMAZEM + 120, TAMANHO_AZULEJO * DIMENSAO_ARMAZEM));
			painelArmazem.setBackground(new Color(0, 33, 69));
			// adicionar os listeners para o rato
			painelArmazem.addMouseListener( new MouseAdapter(){
				public void mousePressed(MouseEvent e) {
					ratoPremido( e );
				}				
			});
			painelArmazem.addMouseMotionListener( new MouseAdapter(){
				
				public void mouseDragged(MouseEvent e) {
					ratoArrastado( e );
				}
			});
		}
		return painelArmazem;
	}
	
	/** configura o bot�o de novo n�vel	 */
	private JButton setupNovoBt( JButton bt ) {
		bt.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				novoNivel();
			}
		});
		return bt;
	}

	/** configura o bot�o de ler n�vel	 */
	private JButton setupLoadBt( JButton bt ) {
		bt.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				lerFicheiro();
			}
		});
		return bt;
	}

	/** configura o bot�o de gravar n�vel	 */
	private JButton setupSaveBt( JButton bt ) {
		bt.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				gravar( ficheiro );
			}
		});
		return bt;
	}

	/** configura o bot�o de gravar n�vel, dando um nome ao ficheiro */
	private JButton setupSaveAsBt( JButton bt ) {
		bt.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				gravarComo();
			}
		});
		return bt;
	}

	/** configura o bot�o de testar o n�vel	 */
	private JButton setupRunBt( JButton bt ) {
		bt.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				experimentaNivel();					
			}
		});
		return bt;
	}

	/** m�todo auixiliar que coloca todos os elementos de um contentor ativos/inativos
	 * @param toolBar o contentor a desativar
	 * @param b ativar ou desativar
	 */
	private void enableToolBar(Container toolBar, boolean b) {
		toolBar.setEnabled( b );
		Component comps[] = toolBar.getComponents();
		for( Component c : comps ) {
			c.setEnabled(b);
			if( c instanceof Container )
				enableToolBar( (Container)c, b );
		}
		
	}
	
	private class EstadoEdicao {
		
		void ratoPremido( MouseEvent e, Point p ) {
			
		}
		void ratoArrastado(MouseEvent e, Point p  ) {
			
		}
	}
	
	private class EstadoNada extends EstadoEdicao {
		
	}
	private class EstadoPorOperario extends EstadoEdicao {
		@Override
		void ratoPremido(MouseEvent e, Point p) {
			armazem.colocarOperario( p, operario );
			painelArmazem.repaint();
		}
	}
	private class EstadoPorCaixote extends EstadoEdicao {
		@Override
		void ratoPremido(MouseEvent e, Point p) {
			try {
				if( armazem.getCaixote( p ) == null ) {
					armazem.colocarCaixote( p, new Caixote( criaImagem(0) ) );
					mapaFicheiros.addFicheiroCaixote( p, ficheirosUsados[0].getName() );
				}
				else { 
					armazem.removerCaixote( p );
					mapaFicheiros.addFicheiroCaixote( p, null );
				}
				painelArmazem.repaint();
			} catch (Exception e1) {
				// se aconteceu algo errado (n�o escolheu bem uma imagem, por exemplo), desligar a a��o atual e por tudo ao in�cio
				//e.printStackTrace();
				estadoAtual = estadoNada;
				btGroup.clearSelection();
			}
		}
	}
	private class EstadoPorAzulejo extends EstadoEdicao {
		@Override
		void ratoPremido(MouseEvent e, Point p) {
			criarAzulejo( p );
			painelArmazem.repaint();
		}
		@Override
		void ratoArrastado(MouseEvent e , Point p) {
			if( p != null ){
				setAlterado( true );
				criarAzulejo( p );
				painelArmazem.repaint();
			}
		}
	}
	private class EstadoPorPorta extends EstadoEdicao {
		@Override
		void ratoPremido(MouseEvent e, Point p) {
			criarAzulejo( p );
			portaCriada = (AzulejoPorta)armazem.getAzulejo( p );
			painelArmazem.repaint();
			estadoAtual = estadoPorTrigger;
			enableToolBar( tools, false );
			painelArmazem.setCursor( Cursor.getPredefinedCursor( Cursor.CROSSHAIR_CURSOR ));
		}
	}
	private class EstadoPorTrigger extends EstadoEdicao {
		@Override
		void ratoPremido(MouseEvent e, Point p) {
			estadoAtual = estadoPorPorta;
			portaCriada.setTrigger( p );
			painelArmazem.setCursor( Cursor.getDefaultCursor() );
			enableToolBar( tools, true );
		}
	}


	/**
	 * programa para arrancar o editor
	 */
	public static void main(String[] args) {
		EditorKoban editor = new EditorKoban();
		editor.setVisible( true );
		editor.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
}  