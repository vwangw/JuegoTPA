import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private float xDelta=100, yDelta=100;
    private float xDir=0.1f, yDir=0.1f;
    private int frames=0;
    private long lastCheck=0;
    private Color color=new Color(150,100,120);
    private Random random;

    public GamePanel(){
        random=new Random();
        mouseInputs=new MouseInputs(this);
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }
    public void changeXDelta(int value){
        this.xDelta += value;

    }

    public void changeYDelta(int value){
        this.yDelta += value;

    }

    public void setRectPos(int x, int y){
        this.xDelta=x;
        this.yDelta=y;

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);//clean JPanel before using it

        updateRectangle();
        g.setColor(color);
        g.fillRect((int)xDelta,(int)yDelta ,50,50);
        frames++;
        if(System.currentTimeMillis()-lastCheck>=1000){
            lastCheck=System.currentTimeMillis();
            System.out.println("FPS: "+frames);
            frames=0;
        };
    }
    private void updateRectangle(){
        xDelta+=xDir;
        if(xDelta>325 || xDelta<0){
            xDir*=-1;
            color=getRndColor();
        }
        yDelta+=yDir;
        if(yDelta>325 || yDelta<0){
            yDir*=-1;
            color=getRndColor();
        }
    };
    private Color getRndColor(){
        int r=random.nextInt(255);
        int b=random.nextInt(255);
        int g=random.nextInt(255);
        return new Color(r,b,g);
    }
}
