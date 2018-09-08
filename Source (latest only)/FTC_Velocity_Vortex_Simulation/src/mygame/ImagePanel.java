package mygame;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import javax.swing.JPanel;

public class ImagePanel extends JPanel
{
    private Image image;
    private Dimension screenSize;
    
    public ImagePanel(Image image, Dimension screenSize)
    {
        this.image = image;
        this.screenSize = screenSize;
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        g.drawImage(image, 0, 0, screenSize.width, screenSize.height,  new ImageObserver()
        {
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height)
            {
                 if((infoflags & ImageObserver.ALLBITS) == ImageObserver.ALLBITS)
                 {
                     return false;
                 }
                 return true;
            }
        });
    }
}