// Nicholas Espinosa
// COP 3503 - 0011
// 3.3.2016
// Recitation Assignment 7
// Modified functions are labled as such

import java.applet.Applet;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MediaTracker;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;

import javax.imageio.ImageIO;

public class Floodfill extends Applet implements MouseListener
{
	Color m_objSelectedColor = Color.blue;
	int m_nSelectedColor = 0xff0000ff;
	BufferedImage m_objShape;
	MediaTracker tracker = new MediaTracker(this);
	
	int m_nTestShapeX = 100;
	int m_nTestShapeY = 100;
	
	static Color[] m_Colors =
	{
		Color.blue, Color.red, Color.green, Color.yellow,
		Color.gray, Color.magenta, Color.orange, Color.cyan
	};
	
	int m_nUpperLeftX = 10;
	int m_nUpperLeftY = 10;
	int m_nColorWidth = 50;
	int m_nColorHeight = 50;
	int m_nLowerRightX;
	int m_nLowerRightY;
	
    CheckboxGroup lngGrp = new CheckboxGroup();
    Checkbox full = new Checkbox("Full Recursion", lngGrp, true);
    Checkbox partial = new Checkbox("Partial Recursion", lngGrp, true);
    
	public void init()
	{
		addMouseListener(this);
        setSize(1020,700);

        add(partial);
        add(full);
        
        try 
        {
			m_objShape = ImageIO.read(Floodfill.class.getResourceAsStream("Untitled.png"));
			tracker.addImage(m_objShape, 100);
			tracker.waitForAll();
		} 
        catch (Exception e1) 
        {
		}
		
	}

	void DrawColors( Graphics canvas )
	{
		for( int i=0; i<m_Colors.length; i++ )
		{
			canvas.setColor( m_Colors[i] );
			canvas.fillRect(m_nUpperLeftX, m_nUpperLeftY + i * m_nColorHeight, m_nColorWidth, m_nColorHeight );
			canvas.setColor( Color.black );
			canvas.drawRect(m_nUpperLeftX, m_nUpperLeftY + i * m_nColorHeight, m_nColorWidth, m_nColorHeight );
			
			m_nLowerRightX = m_nUpperLeftX + m_nColorWidth;
			m_nLowerRightY = ( i + 1 ) * m_nColorHeight;
		}
		
	}
	
	void DrawTestShape( Graphics canvas )
	{
		canvas.drawImage(m_objShape, m_nTestShapeX, m_nTestShapeY, null);
	}
	
	void SetPixel( int x, int y, Graphics canvas )
	{
		canvas.drawLine(x, y, x, y);
	}
	
	void SetPixel( int x, int y, int nColor )
	{
		m_objShape.setRGB(x, y, nColor);
	}
	
	public int GetPixel( int x, int y )
	{
		return( m_objShape.getRGB(x, y) );
	}
	
	public void paint( Graphics canvas )
	{
		DrawColors( canvas );
		DrawTestShape( canvas );
	}
	
	void DoRecursiveFill( int x, int y )
	{
		x -= m_nTestShapeX;
		y -= m_nTestShapeY;
		m_nStartColor = GetPixel(x,y) | 0xff000000;
		Graphics canvas = getGraphics();
		canvas.setColor( m_objSelectedColor );
		
		int w = m_objShape.getWidth();
		int h = m_objShape.getHeight();

		if( m_nStartColor == m_nSelectedColor)
		{
			return;
		}
		
		RecursiveFill( x, y, w, h, canvas);
	}
	
	// Modified by Nicholas Espinosa
	void RecursiveFill( int x, int y, int w, int h, Graphics canvas )
	{
		// If not in bounds
		if(x < 0 || y < 0 || x >= w || y >= h)
			return;
		
		// If not equal to the starting color
		if(GetPixel(x, y) != m_nStartColor)
			return;
		
		// Set the pixel to the new color
		SetPixel(x, y, m_nSelectedColor);
		SetPixel(x + m_nTestShapeX, y + m_nTestShapeY, canvas);

		
		// Recursively move throughout the remaining bounds
		RecursiveFill(x - 1, y, w, h, canvas);
		RecursiveFill(x + 1, y, w, h, canvas);
		RecursiveFill(x, y - 1, w, h, canvas);
		RecursiveFill(x, y + 1, w, h, canvas);
	}
	
	int m_nStartX, m_nStartY, m_nStartColor;
	void DoFloodFill( int x, int y )
	{
		x -= m_nTestShapeX;
		y -= m_nTestShapeY;
		m_nStartColor = GetPixel(x,y) | 0xff000000;
		Graphics canvas = getGraphics();
		canvas.setColor( m_objSelectedColor );
		
		int w = m_objShape.getWidth();
		int h = m_objShape.getHeight();

		if( m_nStartColor == m_nSelectedColor)
		{
			return;
		}
		
		FloodFill( x, y, w, h, canvas);
	}
	
	// Modified by Nicholas Espinosa
	void FloodFill( int x, int y, int w, int h, Graphics canvas )
	{
		// Is out of bounds
		if(x < 0 || y < 0 || x >= w || y >= h)
			return;
		
		// If not equal to the starting color
		if(GetPixel(x, y) != m_nStartColor)
			return;
		
		// Setting start color
		m_nStartColor = GetPixel(x, y);
		
		int xx = x;
		
		// While xx is in bounds and going right and 
		// the pixel is not the start color
		while(xx >= 0 && xx < w && GetPixel(xx, y) == m_nStartColor)
		{
			// Set the color to the selected color
			SetPixel(xx, y, m_nSelectedColor);
			SetPixel(xx + m_nTestShapeX, y + m_nTestShapeY, canvas);
			
			// Increment xx
			xx++;
		}
		
		// right is set to the result of xx
		int right = xx;
		
		// xx is set to 1 minus the mid point
		xx = x - 1;
		
		// While xx is in bounds and going left
		// and the given pixel is not the start color
		while(xx >=0 && xx < w && GetPixel(xx, y) == m_nStartColor)
		{
			// The pixel is set to the selected color
			SetPixel(xx, y, m_nSelectedColor);
			SetPixel(xx + m_nTestShapeX, y + m_nTestShapeY, canvas);
			
			// xx is decremented
			xx--;
		}
		// Left is set to whatever xx is
		int left = xx;
		
		// Flood fill is called recursively from the value of left to right
		// while the y value is adjusted by 1
		for(xx = left; xx < right; xx++)
		{
			FloodFill(xx, y - 1, w, h, canvas);
			FloodFill(xx, y + 1, w, h, canvas);
		}

	}

	@Override
	public void mouseClicked(MouseEvent ms) 
	{
	}

	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
	}

	@Override
	public void mouseExited(MouseEvent arg0) 
	{
	}

	@Override
	public void mousePressed(MouseEvent ms) 
	{
		if( ms.getX() >= m_nUpperLeftX &&
				ms.getY() >= m_nUpperLeftY &&
				ms.getX() < m_nLowerRightX &&
				ms.getY() < m_nLowerRightY )
			{
				int nColorIndex = ( ms.getY() - m_nUpperLeftY ) / m_nColorHeight;
				if( nColorIndex >= 0 && nColorIndex <= 7 )
				{
					m_objSelectedColor = m_Colors[nColorIndex];
					m_nSelectedColor = m_Colors[nColorIndex].getRGB();
				}
			}
			
			else if( ms.getX() >= m_nTestShapeX &&
				ms.getY()>=m_nTestShapeY &&
				ms.getX() < m_nTestShapeX + m_objShape.getWidth() &&
				ms.getY() < m_nTestShapeY + m_objShape.getHeight())
			{
				if( full.getState() )
				{
					DoRecursiveFill( ms.getX(), ms.getY());
				}
				else
				{
					DoFloodFill( ms.getX(), ms.getY());
				}
			}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
	}
	
}
