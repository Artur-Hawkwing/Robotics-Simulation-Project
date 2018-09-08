package mygame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import javax.swing.JButton;

public class ImageButton extends JButton implements MouseListener
{
    private Image image;
    private ImageButtonListener l;
    private int id, pxX, pxY;
    
    
    public ImageButton(Image image, int id, ImageButtonListener l, int pxX, int pxY)
    {
        this.image = image;
        this.id = id;
        this.l = l;
        this.pxY = pxY;
        this.pxX = pxX;
        enableInputMethods(true);   
        addMouseListener(this);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        g.drawImage(image, 0, 0, pxX, pxY,  new ImageObserver()
        {
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height)
            {
                 if((infoflags & ImageObserver.ALLBITS) == ImageObserver.ALLBITS)
                 {
                     System.out.println("YAY!!!!");
                     return false;
                     
                 }
                 return true;
            }
        });
    }

    public int getpxX()
    {
        return pxX;
    }
    public int getpxY()
    {
        return pxY;
    }
    public void mouseClicked(MouseEvent e) 
    {
        l.OnImageButton(id);
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
