//********************************************************
//Final Program
//Programmer:  Brandon Grant
//Due:  5/8/07
//Program file name:  Fighter.java
//Create a fighter character.
//********************************************************

import java.awt.*;
import java.applet.*;

public class Fighter
{
    private int posX, posY, figWidth, figHeight, figNumX, figNumY;
    private int shadowX, shadowY, shadowWidth, shadowHeight, shadowNumX, shadowNumY;
    private int state, facing, health = 10, speed = 5;
    private int startX, startY, startFace;
    public boolean dead;
    
    private final int STILL = 0;
    private final int RIGHT = 1;
    private final int LEFT = -1;
    private final int DOWN = 1;
    private final int UP = -1;

    private final int NORMAL = 0;
    private final int JUMPINGUP = 1;
    private final int JUMPINGDOWN = 2;
    private final int PUNCHING = 3;
    private final int KICKING = 4;
    private final int FALLING = 5;
    private final int JUMPKICKING = 6;
    private final int LYING = 7;
    private final int GOTHIT = 8;
    private final int VICTORY = 9;
    private final int BLOCKING = 10;
    private final int UNBLOCKING = 11;

    private SFS parent;

    public Fighter( int x, int y, int face, SFS setting )
    {
        posX = startX = x;
        posY = startY = y;
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
        facing = startFace = face;
        dead = false;
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

    public void move( int xDir, int yDir )
    {
        if( state == NORMAL )
        {
            if( xDir != STILL || yDir != STILL )
            {
                if( figNumX == 1 && figNumY == 1 )
                    figNumX = 2;
                else if( figNumX == 2 && figNumY == 1 )
                    figNumX = 1;
            }
        }
        else if( state == BLOCKING )
        {
            xDir = yDir = STILL;
            figNumX = 3;
            figNumY = 4;
        }
        else if( state == UNBLOCKING )
        {
            figNumX = figNumY = 1;
            state = NORMAL;
        }
        else if( state == PUNCHING )
        {
            if( figNumX == 3 && figNumY == 1 )
            {
                figNumX = 1;
                state = NORMAL;
            }
            else if( figNumX != 3 && figNumY == 1 )
                figNumX = 3;
        }
        else if( state == KICKING )
        {
            if( figNumX == 4 && figNumY == 1 )
            {
                figNumX = 1;
                state = NORMAL;
            }
            else if( figNumX != 4 && figNumY == 1)
                figNumX = 4;
        }
        else if( state == GOTHIT )
        {
            if( figNumX == 5 && figNumY == 1 )
            {
                figNumX = 1;
                state = NORMAL;
            }
            else if( figNumX !=5 && figNumY == 1 )
                figNumX = 5;
        }
        else if( state == VICTORY )
        {
            if( figNumY != 4 )
            {
                figNumX = 1;
                figNumY = 4;
            }
            else if( figNumX == 1 )
                figNumX = 2;
            else
                figNumX = 1;
        }

        if( state == JUMPINGUP || state == JUMPKICKING )
        {
            if( !atLeftBarrier() && !atRightBarrier() )
                posX += speed * xDir;

            posY -= speed * 7;

            if( state == JUMPKICKING && figNumY != 3 )
            {
                figNumX = 1;
                figNumY = 3;
                posX -= 23;
                figWidth = 87;
            }
            else if( state != JUMPKICKING )
                figNumX = figNumY = 1;

            if( shadowNumX == 1 && shadowNumY == 1 )
                shadowNumX = 2;
            else if( shadowNumX == 2 && shadowNumY == 1 )
                shadowNumX = 3;
            else
                state = JUMPINGDOWN;

            shadowX = posX + (figWidth - shadowWidth) / 2;
        }
        else if( state == JUMPINGDOWN )
        {
            if( !atLeftBarrier() && !atRightBarrier() )
                posX += speed * xDir;

            posY += speed * 7;

            if( shadowNumX == 3 && shadowNumY == 1 )
                shadowNumX = 2;
            else if( shadowNumX == 2 && shadowNumY == 1 )
                shadowNumX = 1;
            else
            {
                state = NORMAL;

                if( figNumY == 3 )
                {
                    figNumY = 1;
                    posX += 23;
                    figWidth = 40;
                }
            }

            shadowX = posX + (figWidth - shadowWidth) / 2;
        }
        else if( state == FALLING )
        {
            if( figNumY != 2 )
            {
                figNumX = 1;
                figNumY = 2;
                shadowNumX = 1;
                shadowNumY = 2;
                figWidth = 87;
                posX -= 23;
                shadowX = posX;
                shadowWidth = figWidth;
                shadowY = posY + 58;
            }
            else if( figNumX == 1 )
                figNumX = 2;
            else
                state = LYING;
        }
        else if( state == LYING )
        {
            if( !dead )
            {
                figNumX = figNumY = 1;
                state = NORMAL;
                shadowNumX = 1;
                shadowNumY = 1;
                figWidth = 40;
                posX += 23;
                shadowX = posX;
                shadowWidth = figWidth;
                shadowY = posY + 68;
            }
        }
        else if( !atLeftBarrier() && !atRightBarrier() && !atTopBarrier() && !atBottomBarrier() )
        {
            posX += speed * xDir;
            posY += speed * yDir;
            shadowX = posX;
            shadowY = posY + 68;
        }
        else
        {
            if( atLeftBarrier() )
            {
                if( xDir == RIGHT )
                    posX += speed;
                else if( xDir == LEFT && yDir == DOWN )
                    posX = getLeftBarrier() - speed;
                else
                    posX = getLeftBarrier();

                posY += speed * yDir;
            }
            else if( atRightBarrier() )
            {
                if( xDir == LEFT )
                    posX -= speed;
                else if( xDir == RIGHT && yDir == DOWN )
                    posX = getRightBarrier() - shadowWidth + speed;
                else
                    posX = getRightBarrier() - shadowWidth;

                posY += speed * yDir;
            }
        
            if( atTopBarrier() )
            {
                shadowY = getTopBarrier();
                posY = shadowY - 68;

                if( yDir == DOWN )
                    posY += speed;

                posX += speed * xDir;
            }
            else if( atBottomBarrier() )
            {
                shadowY = getBottomBarrier() - shadowHeight;
                posY = shadowY - 68;

                if( yDir == UP )
                    posY -= speed;

                posX += speed * xDir;
            }

            shadowX = posX;
            shadowY = posY + 68;
        }
    }

    private boolean atLeftBarrier()
    {
        if( shadowX <= getLeftBarrier() )
            return true;
        else
            return false;
    }

    private boolean atRightBarrier()
    {
        if( shadowX + shadowWidth >= getRightBarrier() )
            return true;
        else
            return false;
    }

    private boolean atTopBarrier()
    {
        if( shadowY <= getTopBarrier() )
            return true;
        else
            return false;
    }

    private boolean atBottomBarrier()
    {
        if( shadowY + shadowHeight >= getBottomBarrier() )
            return true;
        else
            return false;
    }

    private int getLeftBarrier()
    {
        return (450 - shadowY) / 4;
    }

    private int getRightBarrier()
    {
        return (shadowY + shadowWidth + 1172) / 4;
    }

    private int getTopBarrier()
    {
        return 170;
    }

    private int getBottomBarrier()
    {
        return 340;
    }

    public void setState( int s )
    {
        state = s;
    }

    public void jump()
    {
        if( able() )
            state = JUMPINGUP;
    }

    public void punch()
    {
        if( able() && state != PUNCHING )
            state = PUNCHING;
    }

    public void kick()
    {
        if( able() && state != KICKING )
            state = KICKING;
        else if( state == JUMPINGUP )
            state = JUMPKICKING;
    }

    public void fall()
    {
        if( able() )
            state = FALLING;
    }

    public void die()
    {
        if( !dead )
        {
            state = FALLING;
            dead = true;
        }
    }

    public void block()
    {
        if( state == NORMAL || state == UNBLOCKING )
            state = BLOCKING;
    }

    public void unblock()
    {
        if( state == BLOCKING )
            state = UNBLOCKING;
    }

    public void victory()
    {
        if( able() )
            state = VICTORY;
    }

    public void restart()
    {
        state = NORMAL;
        health = 10;
        facing = startFace;
        dead = false;
        figNumX = figNumY = shadowNumX = shadowNumY = 1;
        figWidth = shadowWidth = 40;
        figHeight = 87; shadowHeight = 30;
        posX = startX; posY = startY;
        shadowX = posX; shadowY = posY + 68;
    }

    public boolean able()
    {
        if( !dead && state != JUMPINGUP && state != JUMPINGDOWN && state != FALLING &&
          state != JUMPKICKING && state != LYING && state != VICTORY &&
          state != BLOCKING && state != UNBLOCKING )
            return true;
        else
            return false;
    }

    public void switchFacing()
    {
        facing = -facing;
    }

    public boolean hit( Fighter f )
    {
        if( atEnemyBarrier( f ) )
        {
            if( state == PUNCHING || state == KICKING )
                return f.gotHit();
            else if( state == JUMPKICKING )
                return f.gotHitHard();
        }

        return false;
    }

    public boolean inHittingState()
    {
        if( state == PUNCHING || state == KICKING || state == JUMPKICKING )
            return true;
        else
            return false;
    }

    public boolean atEnemyBarrier( Fighter f )
    {
        if( getYDistance( f ) >= -speed && getYDistance( f ) <= speed )
        {
            if( getXDistance( f ) <= 1 )
                return true;
        }

        return false;
    }

    public int getXDistance( Fighter f )
    {
        if( facing == RIGHT )
            return f.getX() - posX - figWidth;
        else
            return posX - f.getX() - f.getWidth();
    }

    public int getYDistance( Fighter f )
    {
        return shadowY - f.getY();
    }

    public boolean gotHit()
    {
        if( able() )
        {
            health--;

            if( health <= 0 )
                die();
            else
                state = GOTHIT;

            return true;
        }
        else if( state == BLOCKING )
            return true;

        return false;
    }

    public boolean gotHitHard()
    {
        if( able() )
        {
            health--;

            if( health <= 0 )
                die();
            else
                state = FALLING;

            return true;
        }
        else if( state == BLOCKING )
            return true;

        return false;
    }

    public int getHealth()
    {
        return health;
    }

    public boolean isAlive()
    {
        if( !dead )
            return true;
        else
            return false;
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
        posX = startX = x;
        shadowX = x;
    }

    public void setY( int y )
    {
        posY = startY = y;
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
