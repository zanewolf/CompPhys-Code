import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.lang.*;
import java.util.Random;
/*      <applet code="pong" width=1000 height=550>
         </applet>
*/
public class pong extends Applet implements   KeyListener, Runnable {
  int x = 150, y = 50, r=70;         
  int dx = 3, dy = 2, dyo, dxo;      
  int n=1;
  int m=1;
  int xscale=100, yscale=100;
  Dimension size;                  // The size of the applet
  Image buffer;                    // The off-screen image for double-buffering
  Image ball,fire,barrel;
  Graphics2D big;
  Graphics bufferGraphics;         // A Graphics object for the buffer
  Thread animator;                 // Thread that performs the animation
  Color background;
  boolean please_stop;             // A flag asking animation thread to stop
  Random m_r=new Random();
  double pi = 4.0*Math.atan(1.0); 
  double vi, vix, viy, xo,yo,t,dt,tx,ty ; 
  double eps=0.5;
  int nflag=0;
  int xblock, yblock, lxblock, lyblock;
  boolean loser;
  boolean loser2;

  /** Set up an off-screen Image for double-buffering */
  public void init() {
    size = this.size();
    buffer = this.createImage(size.width, size.height);
    ball = getImage(getCodeBase(), "ball.gif" );
    fire = getImage(getCodeBase(), "fire.gif" );
    barrel = getImage(getCodeBase(), "barrel.jpg");
    bufferGraphics = buffer.getGraphics();
    background = new Color(255,255,255);
    setBackground(background);
    tx=0;
    ty=0;
    dt=.005;
    xblock=950;
    yblock=100;
    lxblock=50;
    lyblock=100;
    xo=0;
    yo=120;
    vi=5000;

    vix=vi*Math.cos(45.*pi/180.);
    viy=vi*2*Math.sin(15.*pi/180.);

      addKeyListener( this );
  }


  /** Draw the circle at its current position, using double-buffering */
  public void paint(Graphics g) {
  Graphics2D g2 = (Graphics2D) g;
    // Draw into the off-screen buffer.
  bufferGraphics.setColor(background);
  bufferGraphics.fillRect(0, 0, 3*size.width, 3*size.height); // clear the buffer
                    bufferGraphics.drawImage(ball,x,y,this);
    bufferGraphics.setColor(Color.black);
    bufferGraphics.fillRect(xblock,yblock,20,50);
    bufferGraphics.fillRect(lxblock,lyblock,20,50);
       g2.drawImage(buffer, 0, 0, this);

          
    
  }

  public void update(Graphics g) { paint(g); }

  /** The body of the animation thread */
  public void run() {
    while(!please_stop) {
              
      
      // Move the ball 
      tx=tx+dt;
      ty=ty+dt;
      x=(int)(xo+vix*tx);
      y=(int)(yo+viy*ty);
        if(y>500){
          yo=y;
          viy=-viy;
          ty=0; }
        // if(x<0){ System.out.println("Right Player Wins");

        // if(x>1000){System.out.println("Left Player Wins");
        //   // TextField winner= new TextField("Left Player Wins",16);
        //   //  add(winner);
        //   }}
        if (y<0){viy=-viy;          
          yo=y;
          ty=0;}
        if(y<=lyblock+40 && y>=lyblock-70 && x>=(lxblock) && x<=(lxblock+20) && vix<0){ 
          vix=-vix;
          xo=x;
          tx=0;
        }
        if(y<=yblock+40 && y>=yblock-70 && x>=(xblock-40) && x<=(xblock+20) && vix>0){
          vix=-vix;
          xo=x;
          tx=0;
        }
        

        repaint();

      // Now pause some milliseconds before drawing the ball again.
      try {
      Thread.sleep(100);
      } catch (InterruptedException e) { ; }
    }
    animator = null;
  }
  //  public void keyPressed( KeyEvent f ) {
  //      int key = f.getKeyCode(); 
  //  if (key == KeyEvent.VK_q) {
  //        lyblock -= 18;
  //        repaint();
  //     }
  //     else if (key == KeyEvent.VK_z) {
  //        lyblock += 18;
  //        repaint();
  //     }

  // }
  // public void keyReleased( KeyEvent f) { }
  // public void keyTyped( KeyEvent f ) {
  //  }
  public void keyPressed( KeyEvent e ) {
  
 int key = e.getKeyCode(); 
   if (key == KeyEvent.VK_UP) {
         yblock -= 18;
         repaint();
      }
      else if (key == KeyEvent.VK_DOWN) {
         yblock += 18;
         repaint();
      } else if (key == KeyEvent.VK_Q) {
         lyblock -= 18;
         repaint();
      }
      else if (key == KeyEvent.VK_Z) {
         lyblock += 18;
         repaint();
      }

 }
   public void keyReleased( KeyEvent e ) { }
   public void keyTyped( KeyEvent e ) {
   }


  /** Start the animation thread */
  public void start() {
    if (animator == null) {
      please_stop = false;
      animator = new Thread(this);
      animator.start();
    }
  }

  /** Stop the animation thread */
  public void stop() { please_stop = true; }

  // /** Allow the user to start and stop the animation by clicking */
  // public boolean mouseDown(Event e, int x, int y) {
  //   if (animator != null) please_stop = true;  // if running request a stop
  //   else start();                              // otherwise start it.
  //   return true;
  // }
}


