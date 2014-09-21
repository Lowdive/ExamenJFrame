/**
 * AppletExamen
 *
 * Personaje para juego creado en Examen
 *
 * @author José Elí Santiago Rodríguez A07025007, Alex Martinez Quintanilla A00810480
 * @version 1.01 2014/09/17
 */
 
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.JFrame;

public class AppletExamen extends JFrame implements Runnable, KeyListener {

    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image    imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private URL urlImagenBackG = this.getClass().getResource("espacio.jpg");
    private int iVidas;
    private Personaje perNena;
    private int iDireccionNena;
    private LinkedList lnkCaminadores;
    private LinkedList lnkCorredores;
    private int iScore;
    private int iColisionesNena;
    private SoundClip scSonidoColisionCorredor;     // Objeto SoundClip sonido Corredor
    private SoundClip scSonidoColisionCaminador; //Objeto SoundClip sonido Caminador
    
    
    //Constructor de AppletExamen
    public AppletExamen() {
	init();
	start();
}
    /** 
     * init
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos
     * a usarse en el <code>Applet</code> y se definen funcionalidades.
     */
    public void init() {
        // hago el applet de un tamaño 800,600
        setSize(800, 600);
        
        // introducir instrucciones para iniciar juego
        
        //Inicializamos las vidas al azar entre 3 y 5
        iVidas = (int) (Math.random() * (6 - 3) + 3);
        
        //Inicializamos el score en 0
        iScore = 0;
        
        //inicializamos las colisiones de los corredores con nena
        iColisionesNena = 0;
        
        // se crea imagen de Nena
        URL urlImagenNena = this.getClass().getResource("nena.gif");
        
        // se crea a Nena 
	perNena = new Personaje(getWidth() / 2, getHeight()  / 2,
                Toolkit.getDefaultToolkit().getImage(urlImagenNena));
        //Se inicializa con velocidad 3
        perNena.setVelocidad(3);
        
        // se posiciona a Nena en el centro de la pantalla
        perNena.setX((getWidth() / 2) - (perNena.getAncho()/2));
        perNena.setY((getHeight() / 2) - (perNena.getAlto()/2));
        
        // se posiciona a Susy en alguna parte al azar del cuadrante 
        // superior izquierdo
	int posX;
        int posY;   
	URL urlImagenCaminador = this.getClass().getResource("alien1Camina.gif");
        
        lnkCaminadores = new LinkedList();
        
        //en este for se crean de 8 a 10 caminadores y se guardan en la lista de caminadores
        int iAzar = (int) (Math.random() * (11 - 8) + 8);
        for (int iK=1; iK <= iAzar; iK++) {
            posX = 0;    
            posY = (int) (Math.random() * getHeight());
            // se crea el personaje caminador
            Personaje perCaminador;
            perCaminador = new Personaje(posX,posY,
                Toolkit.getDefaultToolkit().getImage(urlImagenCaminador));
            perCaminador.setX(0 - perCaminador.getAncho());
            perCaminador.setY((int) (Math.random() * (getHeight() - perCaminador.getAlto())));
            perCaminador.setVelocidad((int) (Math.random() * (5 - 3) + 3));
            lnkCaminadores.add(perCaminador);
            }
        
        URL urlImagenCorredor = this.getClass().getResource("alien2Corre.gif");
        
        lnkCorredores = new LinkedList();
        
        //en este for se crean de 8 a 10 caminadores y se guardan en la lista de caminadores
        iAzar = (int) (Math.random() * (16 - 10) + 10);
        for (int iK=1; iK <= iAzar; iK++) {
            posX = (int) (Math.random() * getHeight());    
            posY = 0;
            // se crea el personaje caminador
            Personaje perCorredor;
            perCorredor = new Personaje(posX,posY,
                Toolkit.getDefaultToolkit().getImage(urlImagenCorredor));
            perCorredor.setX((int) (Math.random() * (getWidth() - perCorredor.getAncho())));
            perCorredor.setY(-perCorredor.getAlto() - ((int) (Math.random() * getWidth())));
            lnkCorredores.add(perCorredor);
            }
        
        //creo el sonido de los corredores chocando
	//URL urlSonidoColisionCorredor = this.getClass().getResource("chimpcom.wav");
        scSonidoColisionCorredor = new SoundClip ("chimpcom.wav");
        //creo el sonido  de los  caminadores chocando
        //URL urlSonidoColisionCaminador = this.getClass().getResource("hmscream.wav");
        scSonidoColisionCaminador = new SoundClip ("hmscream.wav");
        addKeyListener(this);
    }
	
    /** 
     * start
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o 
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>Applet</code>
     * 
     */
    public void start () {
        // Declaras un hilo
        Thread th = new Thread (this);
        // Empieza el hilo
        th.start ();
    }
	
    /** 
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run () {
        // se realiza el ciclo del juego en este caso nunca termina
        while (iVidas > 0) {
            /* mientras dure el juego, se actualizan posiciones de jugadores
               se checa si hubo colisiones para desaparecer jugadores o corregir
               movimientos y se vuelve a pintar todo
            */ 
            actualiza();
            checaColision();
            repaint();
            try	{
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError)	{
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
	}
    }
	
    /** 
     * actualiza
     * 
     * Metodo que actualiza la posicion del objeto elefante 
     * 
     */
    public void actualiza(){
        // instrucciones para actualizar personajes
        
        //Nena actualiza movimiento dependiendo de la tecla que se presionó
        switch (iDireccionNena) {
            case 1: perNena.abajo();
                break;
            case 2: perNena.arriba();
                break;
            case 3: perNena.derecha();
                break;
            case 4: perNena.izquierda();
                break;
        }
        //Actualiza el movimiento de los caminadores
        for ( Object lnkCaminadores : lnkCaminadores ) {
        Personaje perCaminador = (Personaje)lnkCaminadores;
        perCaminador.derecha();
        }
        //Actualiza el movimiento de los corredores
        for ( Object lnkCorredores : lnkCorredores ) {
        Personaje perCorredor = (Personaje)lnkCorredores;
        perCorredor.setVelocidad(6 - iVidas);
        perCorredor.abajo();
        }
        if (iColisionesNena >= 5) {
            iColisionesNena = 0;
            iVidas = iVidas - 1;
        }
    }
	
    /**
     * checaColision
     * 
     * Metodo usado para checar la colision del objeto elefante
     * con las orillas del <code>Applet</code>.
     * 
     */
    public void checaColision(){
        // instrucciones para checar colision y reacomodar personajes si 
        // es necesario
        //Checa colisiones de Nena
        if (perNena.getY() + perNena.getAlto() > getHeight()) {
            perNena.setY(getHeight() - perNena.getAlto()); 
        }
        if (perNena.getY() <= 0) {
            perNena.setY(0);  
        }
        if ((perNena.getX() + perNena.getAncho()) >= getWidth()) {
            perNena.setX(getWidth() - perNena.getAncho());
        }
        if (perNena.getX() <= 0) {
            perNena.setX(0);
        }
        //Checa colisiones de los caminadores
        for ( Object lnkCaminadores : lnkCaminadores ) {
            Personaje perCaminador = (Personaje)lnkCaminadores;
            if (perCaminador.getX() + perCaminador.getAncho() >= getWidth()) {
                perCaminador.setX(0 - perCaminador.getAncho());
                perCaminador.setY((int) (Math.random() * (getHeight() - perCaminador.getAlto())));
                perCaminador.setVelocidad((int) (Math.random() * (5 - 3) + 3));
            }
            if (perCaminador.colisiona(perNena)) {
                perCaminador.setX(0 - perCaminador.getAncho());
                perCaminador.setY((int) (Math.random() * (getHeight() - perCaminador.getAlto())));
                perCaminador.setVelocidad((int) (Math.random() * (5 - 3) + 3));
                iScore = iScore + 1;
                scSonidoColisionCaminador.play();
            }
            }
        //Checa colisiones de los corredores
        for ( Object lnkCorredores : lnkCorredores ) {
            Personaje perCorredor = (Personaje)lnkCorredores;
            if (perCorredor.getY() + perCorredor.getAlto() >= getWidth()) {
                perCorredor.setX((int) (Math.random() * (getHeight() - perCorredor.getAncho())));
                perCorredor.setY(0 - perCorredor.getAlto());
                perCorredor.setVelocidad(6 - iVidas);
            }
            if (perCorredor.colisiona(perNena)) {
                perCorredor.setX((int) (Math.random() * (getHeight() - perCorredor.getAncho())));
                perCorredor.setY(0 - perCorredor.getAlto());
                perCorredor.setVelocidad(6 - iVidas);
                iColisionesNena = iColisionesNena + 1;
                scSonidoColisionCorredor.play();
            }
            }
    }
	
    /**
     * update
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y 
     * define cuando usar ahora el paint
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void paint (Graphics graGrafico){
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }
        // creo imagen para el background
        Image imaImagenBackG = Toolkit.getDefaultToolkit().getImage(urlImagenBackG);
        // Despliego la imagen
        graGraficaApplet.drawImage(imaImagenBackG, 0, 0, 
                getWidth(), getHeight(), this);

        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint1(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    /**
     * paint1
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * @param g es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void paint1(Graphics g) {
        if (perNena != null & lnkCaminadores != null & lnkCorredores != null) {
            
        //Dibuja la imagen de Nena en la posicion actualizada
        g.drawImage(perNena.getImagen(), perNena.getX(),
        perNena.getY(), this);
        
        //Dibuja la imagen de los caminadores en la posicion actualizada
        for ( Object lnkCaminadores : lnkCaminadores ) {
        Personaje perCaminador = (Personaje)lnkCaminadores;
        g.drawImage(perCaminador.getImagen(), perCaminador.getX(),
        perCaminador.getY(), this);
        }
        
        //Dibuja la imagen de los corredores en la posicion actualizada
        for ( Object lnkCorredores : lnkCorredores ) {
        Personaje perCorredor = (Personaje)lnkCorredores;
        g.drawImage(perCorredor.getImagen(), perCorredor.getX(),
        perCorredor.getY(), this);
        }
        
        //Despliega las vidas restantes
        g.setColor(Color.red);
        Font fFont = new Font("Verdana", Font.BOLD, 18);
        g.setFont(fFont);
        g.drawString("Quedan " + iVidas + " vidas!", 50, 50);
        g.drawString("Score : " + iScore, 50, 30);
        }
        if (iVidas <= 0) {
        
        //subo la imagen del bg
        URL urlImagenGOver = this.getClass().getResource("game_over.jpg");
        // creo imagen para el background
        Image imaImagenGOver = Toolkit.getDefaultToolkit().getImage(urlImagenGOver);
        // Despliego la imagen
        graGraficaApplet.drawImage(imaImagenGOver, 0, 0, 
                getWidth(), getHeight(), this);
        g.drawString("Quedan " + iVidas + " vidas!", 50, 50);
        g.drawString("Score : " + iScore, 50, 30);
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        // si presiono flecha para abajo
        if(keyEvent.getKeyCode() == KeyEvent.VK_S) {    
                iDireccionNena = 1;  // cambio la dirección hacia abajo
        }
        // si presiono flecha para arriba
        if(keyEvent.getKeyCode() == KeyEvent.VK_W) {    
                iDireccionNena = 2;   // cambio la dirección hacia arriba
        }
        // si presiono flecha para abajo
        if(keyEvent.getKeyCode() == KeyEvent.VK_D) {    
                iDireccionNena = 3;  // cambio la dirección hacia abajo
        }
        // si presiono flecha para arriba
        if(keyEvent.getKeyCode() == KeyEvent.VK_A) {    
                iDireccionNena = 4;   // cambio la dirección hacia arriba
        }
    }
}