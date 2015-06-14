//********************************************************
//Final Program
//Programmer:  Brandon Grant
//Due:  5/8/07
//Program file name:  Fan.java
//Create a fan character.
//********************************************************

import java.awt.*;
import java.applet.*;

public class Fan
{
    private int posX, posY, figWidth, figHeight, figNumX, figNumY;
    private int shadowX, shadowY, shadowWidth, shadowHeight, shadowNumX, shadowNumY;
    private int state, facing, speed = 1, wait = 10;
    
    private final int RIGHT = 1;
    private final int LEFT = -1;

    private final int NORMAL = 0;
    private final int JUMPINGUP = 1;
    private final int JUMPINGDOWN = 2;
    private final int WATCHING = 3;

    private SFS parent;

    public Fan( int x, int y, int face, SFS setting )
    {
        posX = x;
        posY = y;
        figNumX = figNumY = 1;
        figWidth = 40;
        figHeight = 87;

        shadowX = x;
        shadowY = y + 68;
        shadowNumX = 1;
        shadowNumY = 1;
        shadowWidth = 40;
        shadowHeight = 30;

        parent = setting;
        state = NORMAL;
        facing = face;
    }

    public void paint( Graphics g, Image figs, Image shadows ) 
    {
        if( facing == RIGHT )
        {
            g.drawImage( shadows, shadowX, shadowY, shadowWidth + shadowX, shadowHeight + shadowY,
              shadowWidth * (shadowNumX - 1), shadowHeight * (shadowNumY - 1),
              shadowWidth * shadowNumX, shadowHeight * shadowNumY, parent );
            g.drawImage( figs, posX, posY, figWidth + posX, figHeight + posY,
              figWidth * (figNumX - 1), figHeight * (figNumY - 1),
              figWidth * figNumX, figHeight * figNumY, parent );
        }
        else
        {
            g.drawImage( shadows, shadowX, shadowY, shadowWidth + shadowX, shadowHeight + shadowY,
              shadowWidth * shadowNumX, shadowHeight * (shadowNumY - 1),
              shadowWidth * (shadowNumX - 1), shadowHeight * shadowNumY, parent );
            g.drawImage( figs, posX, posY, figWidth + posX, figHeight + posY,
              figWidth * figNumX, figHeight * (figNumY - 1),
              figWidth * (figNumX - 1), figHeight * figNumY, parent );
        }
    }

    public void move()
    {
        if( state == NORMAL )
        {
            figNumX = 1;
        }
        else if( state == JUMPINGUP )
        {
            posY -= speed * 7;

            figNumX = 2;

            if( shadowNumX == 1 && shadowNumY == 1 )
                shadowNumX = 2;
            else if( shadowNumX == 2 && shadowNumY == 1 )
                shadowNumX = 3;
            else
                state = JUMPINGDOWN;

            shadowX = posX;
        }
        else if( state == JUMPINGDOWN )
        {
            posY += speed * 7;

            figNumX = 1;

            if( shadowNumX == 3 && shadowNumY == 1 )
                shadowNumX = 2;
            else if( shadowNumX == 2 && shadowNumY == 1 )
                shadowNumX = 1;
            else
                state = NORMAL;

            shadowX = posX;
        }
        else if( state == WATCHING )
        {
            if( wait == 0 )
            {
                if( figNumX == 1 )
                    figNumX = 2;
                else
                    figNumX = 1;

                wait = 10;
            }
            else
                wait--;
        }
    }

    public void setState( int s )
    {
        state = s;
    }

    public void cheer()
    {
        if( state == NORMAL )
            state = JUMPINGUP;
    }

    public void watch()
    {
        if( state == NORMAL )
            state = WATCHING;
    }

    public void switchFacing()
    {
        facing = -facing;
    }

    public int getX()
    {
        return shadowX;
    }

    public int getY()
    {
        return shadowY;
    }

    public void setX( int x )
    {
        posX = x;
        shadowX = x;
    }

    public void setY( int y )
    {
        posY = y;
        shadowY = y;
    }

    public void setSpeed( int x )
    {
        speed = x;
    }

    public int getWidth()
    {
        return figWidth;
    }

    public int getHeight()
    {
        return figHeight;
    }

    public int getFacing()
    {
        return facing;
    }
}
