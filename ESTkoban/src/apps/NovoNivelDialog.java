package apps;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/** Janela de di�logo onde se pedem as informa��es para um novo n�vel.
 * As informa��es incluem as imagens do oper�rio bem como a imagem de fundo.
 * @author F. S�rgio Barbosa
 */
public class NovoNivelDialog extends JDialog {

	/** vers�o */
	private static final long serialVersionUID = 1L;

	/** indica que as informa��es do novo n�vel foram aprovadas */
	public static final int APROVADO = 0;
	
	/** indica que as informa��es do novo n�vel n�o est�o corretas ou n�o foram aprovadas */
	public static final int ERRO = 1;

	/** resultado da janela de cria��o de n�vel */ 
	private int resultado = ERRO;  // por omiss�o est� tudo errado
	
	// di�logo para a selec��o de ficheiros de imagem
	private JFileChooser imgChooser;

	// informa��o sobre os ficheiros escolhidos
	private File operarioFiles[] = new File[4];
	private File fundoFile;
	
	// componente onde ir� ser apresentado o nome do ficheiro da imagem de fundo
	private JTextField nomeFichFundo;
	
	/** construtor da janela de di�logo
     * @param owner dono da janela de di�logo
     * @param tamanhoAzulejo o tamanho dos azulejos
	 */
	public NovoNivelDialog(Frame owner, int tamanhoAzulejo ) {
		setTitle("Novo Nivel");

		setupAspeto( getContentPane(), tamanhoAzulejo );
		pack();
		setResizable( false );
		setModal( true );
		setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
		setLocationRelativeTo( owner );
		
		imgChooser = new JFileChooser("art");
		imgChooser.setFileSelectionMode(JFileChooser.FILES_ONLY );
		imgChooser.setFileFilter( new FileNameExtensionFilter( "Ficheiros de imagem", ImageIO.getReaderFileSuffixes()) );

	}

	/** Retorna o resultado das escolhas do utilizador. 
	 * @return APROVADO caso o utilizador tenha pressionado no OK e tudo esteja bem,
	 * ou ERRO se alguma coisa correu mal ou o utilizador cancelou a opera��o 
	 */
	public int getResultado() {
		return resultado;
	}
	
	/** retorna os ficheiros do oper�rio.<br>
	 * Os ficheiros est�o na sequencia: esquerda, cima, direita e baixo
	 * @return  os ficheiros do oper�rio.
	 */
	public File[] getOperarioFiles() {
		return operarioFiles;
	}
	
	/** retorna o ficheiro com a imagem de fundo do n�vel
	 * @return o ficheiro com a imagem de fundo do n�vel
	 */
	public File getFundoFile() {
		return fundoFile;
	}

	/** apresenta uma janela que permite ao utilizador escolher uma imagem
	 */
	protected File escolherFicheiroImagem( String msg ) {
		imgChooser.setDialogTitle( msg );
		int res = imgChooser.showOpenDialog( this );
		if( res == JFileChooser.APPROVE_OPTION )
			return imgChooser.getSelectedFile();
		else
			return null;
	}
	
	/** m�todo auxiliar para configurar o aspeto da janela de di�logo 
	 * @param janela janela � qual este di�logo pertence
	 */
	private void setupAspeto( Container janela, int tamanho ) {		
		janela.setLayout( new FlowLayout(FlowLayout.CENTER, 15,0) );//GridLayout(0, 1) );
		
		int largura = (int)(tamanho * 3);
		janela.setPreferredSize( new Dimension( largura, largura + 100) );
		
		operarioFiles[0] = new File("art/operario_esq.png");
		operarioFiles[1] = new File("art/operario_cima.png");
		operarioFiles[2] = new File("art/operario_dir.png");
		operarioFiles[3] = new File("art/operario_baixo.png");
		
		JPanel operarioPanel = new JPanel( new GridLayout(0,2) );
		operarioPanel.setPreferredSize( new Dimension( largura, largura ));
		operarioPanel.setBorder( BorderFactory.createTitledBorder("Operario") );
		String titulos[] = { "Operario esquerda", "Operario cima", "Operario esquerda", "Operario baixo" };

		for( int i=0; i < operarioFiles.length; i++ ) {
			JButton bt = new JButton( new ImageIcon( operarioFiles[i].getAbsolutePath() ) );
			final int idx = i;
			bt.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					File f = escolherFicheiroImagem( titulos[idx] );
					if( f != null ) {
						operarioFiles[ idx ] = f;
						bt.setIcon( new ImageIcon( operarioFiles[ idx ].getAbsolutePath() ) );
					}
				}
			});
			operarioPanel.add( bt );
		}
		janela.add( operarioPanel );

		fundoFile = new File("art/fundo.png");
		JPanel pnFundo = new JPanel( new GridLayout(0,1) );//new FlowLayout( FlowLayout.LEFT, 3, 0) );
		pnFundo.setBorder( BorderFactory.createTitledBorder("Fundo") );
		nomeFichFundo = new JTextField( fundoFile.getName(), 10 );
		nomeFichFundo.setEditable( false );
		pnFundo.add( nomeFichFundo );		
		JButton browse = new JButton("...");
		browse.setMargin( new Insets(0, 4, 0, 4) );
		pnFundo.add( browse );
		browse.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File f = escolherFicheiroImagem( "Imagem do Fundo" );
				if( f != null ) {
					fundoFile = f;
					nomeFichFundo.setText( fundoFile.getName() );
				}
			}
		});
		janela.add( pnFundo );
		
		JPanel okpanel = new JPanel( new FlowLayout( FlowLayout.CENTER, 0, 0 ) );
		JButton btOk = new JButton("Ok");
		okpanel.add( btOk );
		janela.add( okpanel );
		
		btOk.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					resultado = APROVADO;
					setVisible( false );
				}
				catch( Exception ex ){
					JOptionPane.showMessageDialog(null, "Parametros errados!", "Erro", JOptionPane.ERROR_MESSAGE ); 
				}
			}
		});
	}
}
