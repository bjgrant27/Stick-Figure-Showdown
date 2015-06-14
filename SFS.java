//********************************************************
//Final Program
//Programmer:  Brandon Grant
//Due:  5/8/07
//Program file name:  SFS.java
//Stick figure fighting game.
//********************************************************

import java.awt.*;
import java.applet.*;
import java.awt.event.*;

public class SFS extends Applet implements Runnable, KeyListener
{
    int appletWidth, appletHeight;
    int bgX, bgWidth;

    Image offscreenImage, bg, title, title2, ropes, shadows, shadows2;
    Image figs1, figs2, fanfigs1, fanfigs2, fanfigs3, fanfigs4;
    Graphics offscreenGraphics;

    AudioClip bgMusic;
    AudioClip cheer;
    AudioClip boo;
    AudioClip hit;

    int round = 1, wait = 40;
    int xDir1, yDir1;
    int xDir2, yDir2;
    int roundsWon1 = 0, roundsWon2 = 0;
    int health1 = 10, health2 = 10;

    boolean musicPlaying = true;
    boolean stopMusic = false;

    final int STILL = 0;
    final int RIGHT = 1;
    final int LEFT = -1;
    final int DOWN = 1;
    final int UP = -1;

    int difficulty;

    final int EASY = 3;
    final int MODERATE = 2;
    final int HARD = 1;

    int state;

    final int STARTING1 = 0;
    final int STARTING2 = 1;
    final int PLAYING = 2;
    final int PAUSED = 3;
    final int WONROUND = 4;
    final int LOSTROUND = 5;
    final int WONGAME = 6;
    final int LOSTGAME = 7;

    Fighter fighter1, fighter2;
    Fan fan1, fan2, fan3, fan4;

    Thread newThread;

    MediaTracker mt;

    public void init()
    {
        appletWidth = getSize().width;
        appletHeight = getSize().height;

        mt = new MediaTracker( this );

        bg = getImage( getDocumentBase(), "IMAGES/bg.gif" );
        title = getImage( getDocumentBase(), "IMAGES/title.gif" );
        title2 = getImage( getDocumentBase(), "IMAGES/title2.gif" );
        figs1 = getImage( getDocumentBase(), "IMAGES/figs1.gif" );
        figs2 = getImage( getDocumentBase(), "IMAGES/figs2.gif" );
        fanfigs1 = getImage( getDocumentBase(), "IMAGES/fanfigs1.gif" );
        fanfigs2 = getImage( getDocumentBase(), "IMAGES/fanfigs2.gif" );
        fanfigs3 = getImage( getDocumentBase(), "IMAGES/fanfigs3.gif" );
        fanfigs4 = getImage( getDocumentBase(), "IMAGES/fanfigs4.gif" );
        ropes = getImage( getDocumentBase(), "IMAGES/ropes.gif" );
        shadows = getImage( getDocumentBase(), "IMAGES/shadows.gif" );
        shadows2 = getImage( getDocumentBase(), "IMAGES/shadows2.gif" );

        mt.addImage( bg, 0 );
        mt.addImage( title, 0 );
        mt.addImage( title2, 0 );
        mt.addImage( figs1, 0 );
        mt.addImage( figs2, 0 );
        mt.addImage( fanfigs1, 0 );
        mt.addImage( fanfigs2, 0 );
        mt.addImage( fanfigs3, 0 );
        mt.addImage( fanfigs4, 0 );
        mt.addImage( ropes, 0 );
        mt.addImage( shadows, 0 );

        try
        {
            mt.waitForAll();
        }
        catch( InterruptedException e ){}

        bgMusic = getAudioClip( getDocumentBase(), "SOUNDS/bgMusic.au" );
        cheer = getAudioClip( getDocumentBase(), "SOUNDS/cheer.wav" );
        boo = getAudioClip( getDocumentBase(), "SOUNDS/boo.wav" );
        hit = getAudioClip( getDocumentBase(), "SOUNDS/hit.wav" );

        bgWidth = bg.getWidth( this );
        bgX = (appletWidth - bgWidth) / 2;
        bgX = 0;

        state = STARTING1;
        difficulty = MODERATE;

        xDir1 = yDir1 = xDir2 = yDir2 = STILL;

        fighter1 = new Fighter( bgWidth / 2 - 100, appletHeight / 2 - 50, RIGHT, this );
        fighter2 = new Fighter( bgWidth / 2 + 60, appletHeight / 2 - 50, LEFT, this );
        fan1 = new Fan( bgWidth / 2 - 200, appletHeight / 2 - 80, RIGHT, this );
        fan2 = new Fan( bgWidth / 2 + 160, appletHeight / 2 - 80, LEFT, this );
        fan3 = new Fan( bgWidth / 2 + 170, appletHeight / 2 - 30, LEFT, this );
        fan4 = new Fan( bgWidth / 2 - 205, appletHeight / 2 - 30, RIGHT, this );

        offscreenImage = createImage( appletWidth, appletHeight );
        offscreenGraphics = offscreenImage.getGraphics();

        setBackground( Color.black );

        this.addKeyListener( this );
    }

    public void start()
    {
        newThread = new Thread( this );
        newThread.start();
    }

    public void stop()
    {
        if( newThread != null )
        {
            if( bgMusic != null ) bgMusic.stop();
            newThread = null;
        }
    }

    public void paint( Graphics g ) 
    {
        offscreenGraphics.setColor( Color.black );
        offscreenGraphics.fillRect( 0, 0, getWidth(), getHeight() );

        if( state == STARTING1 )
        {
            offscreenGraphics.drawImage( title, 80, 100, this );
            offscreenGraphics.drawImage( title2, 45, 230, this );
            offscreenGraphics.setColor( Color.white );
            offscreenGraphics.drawString( "By:  Brandon Grant", appletWidth - 120, appletHeight - 10 );
            g.drawImage( offscreenImage, 0, 0, this ); 
            return;
        }

        offscreenGraphics.drawImage( bg, bgX, 0, this );

        if( fighter1.getY() >= fighter2.getY() )
        {
            fighter2.paint( offscreenGraphics, figs2, shadows );
            fighter1.paint( offscreenGraphics, figs1, shadows );
        }
        else
        {
            fighter1.paint( offscreenGraphics, figs1, shadows );
            fighter2.paint( offscreenGraphics, figs2, shadows );
        }

        fan1.paint( offscreenGraphics, fanfigs1, shadows2 );
        fan2.paint( offscreenGraphics, fanfigs2, shadows2 );
        fan3.paint( offscreenGraphics, fanfigs3, shadows2 );
        fan4.paint( offscreenGraphics, fanfigs4, shadows2 );

        offscreenGraphics.drawImage( ropes, bgX + 218, 276, this );
        offscreenGraphics.setColor( Color.white );
        offscreenGraphics.fillRect( 50, 20, 100, 10 );
        offscreenGraphics.fillRect( appletWidth - 150, 20, 100, 10 );
        offscreenGraphics.drawString( "Slim Stallone", 50, 50 );
        offscreenGraphics.drawString( "Thin Diesel", appletWidth - 150, 50 );
        offscreenGraphics.setColor( Color.red );
        offscreenGraphics.fillRect( 50, 20, health1 * 10, 10 );
        offscreenGraphics.setColor( Color.blue );
        offscreenGraphics.fillRect( appletWidth - 150, 20, health2 * 10, 10 );
        offscreenGraphics.setColor( Color.white );
        offscreenGraphics.drawString( "Round " + round, appletWidth - 230, 30 );

        if( state == PLAYING )
            offscreenGraphics.drawString( "FIGHT!", appletWidth / 2 - 20, 60 );
        else if( state == PAUSED )
            offscreenGraphics.drawString( "PAUSED", appletWidth / 2 - 25, 60 );
        else if( state == WONGAME )
            offscreenGraphics.drawString( "YOU WON!", appletWidth / 2 - 30, 60 );
        else if( state == LOSTGAME )
            offscreenGraphics.drawString( "YOU LOST!", appletWidth / 2 - 30, 60 );

        if( difficulty == EASY )
            offscreenGraphics.drawString( "Difficulty:  EASY", 10, 75 );
        else if( difficulty == MODERATE )
            offscreenGraphics.drawString( "Difficulty:  MODERATE", 10, 75 );
        else
            offscreenGraphics.drawString( "Difficulty:  HARD", 10, 75 );

        g.drawImage( offscreenImage, 0, 0, this ); 
    }

    public void update( Graphics g )
    {
        paint( g );
    }

    public void run()
    {
        if( bgMusic != null )
            bgMusic.loop();

        while( newThread != null )
        {
            try
            {
                Thread.sleep( 80 );

                if( stopMusic )
                {
                    if( musicPlaying && bgMusic != null )
                    {
                        bgMusic.stop();
                        musicPlaying = false;
                    }
                }
                else
                {
                    if( !musicPlaying && bgMusic != null )
                    {
                        bgMusic.loop();
                        musicPlaying = true;
                    }
                }

                if( state != PAUSED )
                {
                    fan1.cheer();
                    fan2.cheer();
                    fan3.watch();
                    fan4.watch();
                    fan1.move();
                    fan2.move();
                    fan3.move();
                    fan4.move();
                }

                if( state == STARTING1 )
                {
                    if( wait == 0 )
                    {
                        state = STARTING2;
                        wait = 40;
                    }
                    else
                        wait--;
                }
                else if( state == STARTING2 )
                {
                    if( bgX > (appletWidth - bgWidth) / 2 )
                    {
                        fighter1.setX( fighter1.getX() - 4 );
                        fighter2.setX( fighter2.getX() - 4 );
                        fan1.setX( fan1.getX() - 4 );
                        fan2.setX( fan2.getX() - 4 );
                        fan3.setX( fan3.getX() - 4 );
                        fan4.setX( fan4.getX() - 4 );
                        bgX -= 4;
                    }
                    else
                        state = PLAYING;
                }
                else if( state == WONROUND )
                {
                    if( wait == 0 )
                    {
                        round++;
                        fighter1.restart();
                        fighter2.restart();
                        state = PLAYING;
                        wait = 40;
                    }
                    else
                    {
                        fighter1.victory();
                        wait--;
                    }

                    fighter1.move( STILL, STILL );
                    fighter2.move( STILL, STILL );
                }
                else if( state == LOSTROUND )
                {
                    if( wait == 0 )
                    {
                        round++;
                        fighter1.restart();
                        fighter2.restart();
                        state = PLAYING;
                        wait = 40;
                    }
                    else
                    {
                        fighter2.victory();
                        wait--;
                    }

                    fighter1.move( STILL, STILL );
                    fighter2.move( STILL, STILL );
                }
                else if( state == WONGAME )
                {
                    fighter1.victory();
                    fighter1.move( STILL, STILL );
                    fighter2.move( STILL, STILL );
                }
                else if( state == LOSTGAME )
                {
                    fighter2.victory();
                    fighter1.move( STILL, STILL );
                    fighter2.move( STILL, STILL );
                }
                else if( state == PLAYING )
                {
                    AI( difficulty );
                    fighter1.move( xDir1, yDir1 );
                    fighter2.move( xDir2, yDir2 );

                    if( fighter1.hit( fighter2 ) )
                        hit.play();
                    if( fighter2.hit( fighter1 ) )
                        hit.play();

                    if( fighter1.getFacing() == RIGHT && fighter1.getX() > fighter2.getX() )
                        fighter1.switchFacing();
                    else if( fighter1.getFacing() == LEFT && fighter1.getX() < fighter2.getX() )
                        fighter1.switchFacing();
                    else if( fighter2.getFacing() == RIGHT && fighter2.getX() > fighter1.getX() )
                        fighter2.switchFacing();
                    else if( fighter2.getFacing() == LEFT && fighter2.getX() < fighter1.getX() )
                        fighter2.switchFacing();

                    health1 = fighter1.getHealth();
                    health2 = fighter2.getHealth();

                    if( !fighter2.isAlive() )
                    {
                        roundsWon1++;

                        if( roundsWon1 == 2 )
                            state = WONGAME;
                        else
                            state = WONROUND;

                        cheer.play();
                    }

                    if( !fighter1.isAlive() )
                    {
                        roundsWon2++;

                        if( roundsWon2 == 2 )
                            state = LOSTGAME;
                        else
                            state = LOSTROUND;

                        boo.play();
                    }
                }
            }
            catch( InterruptedException e ){}

            repaint();
        }
    }

    public void destroy()
    {
        offscreenGraphics.dispose();
    }

    public void keyTyped( KeyEvent e )
    {
    }

    public void keyPressed( KeyEvent e )
    {
        int key = e.getKeyCode();

        if( key == KeyEvent.VK_LEFT )
            xDir1 = LEFT;
        else if( key == KeyEvent.VK_RIGHT )
            xDir1 = RIGHT;
        else if( key == KeyEvent.VK_UP )
            yDir1 = UP;
        else if( key == KeyEvent.VK_DOWN )
            yDir1 = DOWN;
        else if( key == KeyEvent.VK_SPACE )
            fighter1.punch();
        else if( key == KeyEvent.VK_X )
            fighter1.kick();
        else if( key == KeyEvent.VK_Z )
            fighter1.jump();
        else if( key == KeyEvent.VK_B )
            fighter1.block();
        else if( key == KeyEvent.VK_P )
        {
            if( state == PLAYING )
                state = PAUSED;
            else if( state == PAUSED )
                state = PLAYING;
        }
        else if( key == KeyEvent.VK_ENTER )
        {
            if( state == STARTING1 || state == LOSTROUND || state == WONROUND )
                wait = 0;
            else if( state == LOSTGAME || state == WONGAME )
            {
                wait = 40;
                round = 1;
                roundsWon1 = roundsWon2 = 0;
                fighter1.restart();
                fighter2.restart();
                state = PLAYING;
            }
        }
        else if( key == KeyEvent.VK_M )
        {
            if( musicPlaying )
                stopMusic = true;
            else
                stopMusic = false;
        }
        else if( key == KeyEvent.VK_D )
        {
            if( difficulty == EASY )
                difficulty = MODERATE;
            else if( difficulty == MODERATE )
                difficulty = HARD;
            else
                difficulty = EASY;
        }
    }

    public void keyReleased( KeyEvent e )
    {
        int key = e.getKeyCode();

        if( key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT )
            xDir1 = STILL;
        else if( key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN )
            yDir1 = STILL;
        else if( key == KeyEvent.VK_B )
            fighter1.unblock();
    }

    public void AI( int diff )
    {
        fighter2.setSpeed( 7 - diff );

        int rand = (int)(Math.random() * 4);

        if( rand == 0 )
            xDir2 = LEFT;
        else if( rand == 1 )
            xDir2 = RIGHT;
        else if( rand == 2 )
            yDir2 = UP;
        else
            yDir2 = DOWN;

        if( fighter2.getXDistance( fighter1 ) >= diff * 5 )
            xDir2 = fighter2.getFacing();

        if( fighter2.getYDistance( fighter1 ) <= -diff * 5 )
            yDir2 = DOWN;
        else if( fighter2.getYDistance( fighter1 ) >= diff * 5 )
            yDir2 = UP;

        fighter2.unblock();

        if( diff == HARD && fighter1.inHittingState() && fighter2.getXDistance( fighter1 ) <= 10 &&
          fighter2.getYDistance( fighter1 ) >= -10 )
        {
            rand = (int)(Math.random() * 2 );
            if( rand == 0 ) fighter2.block();
        }

        if( fighter2.atEnemyBarrier( fighter1 ) )
        {
            rand = (int)(Math.random() * (4 + diff) );

            if( rand == 0 || rand == 1 )
                fighter2.punch();
            else if( rand == 2 || rand == 3 )
                fighter2.kick();
            else if( rand == 4 )
            {
                fighter2.jump();
                fighter2.kick();
            }
        }
    }
}
