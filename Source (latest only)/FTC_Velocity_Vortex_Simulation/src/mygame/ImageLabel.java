package mygame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import javax.swing.JLabel;

public class ImageLabel extends JLabel
{
    private Image image;
    private int id, pxX, pxY;
    
    
    public ImageLabel(Image image, int pxX, int pxY)
    {
        this.image = image;
        this.id = id;
        this.pxY = pxY;
        this.pxX = pxX;
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
}
