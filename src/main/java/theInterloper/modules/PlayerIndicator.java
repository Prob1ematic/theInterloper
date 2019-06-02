package theInterloper.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.awt.*;
import java.util.Random;

public class PlayerIndicator
{
    public static final float width = 20;
    public static final float height = 20; //Settings.scale?
    public float X;
    public float Y;
    public float startX;
    public float startY;
    public Rectangle collider;
    public int iframes;

    public PlayerIndicator() {
        X = (Settings.WIDTH/2)-(width/2);
        Y = (Settings.HEIGHT/2)-(height/2);
        startX = X;
        startY = Y;
        collider = new Rectangle((int)X,(int)Y,(int)width,(int)height);
        iframes = 0;
    }

    public Shape getShape(){
        return collider;
    }

    public void updateCollider(){
        collider.setLocation((int)X, (int)Y);
    }

    public void gotHurt(){
        iframes = 100;
    }

    public Point getLocation(){
        return collider.getLocation();
    }

    public Point getCenterLocation(){
        return new Point((int)(X+(width/2)),(int)(Y+(height/2)));
    }

    public void draw(SpriteBatch sb) {
        if(iframes > 0){
            iframes-=(Gdx.graphics.getDeltaTime() * 64.5);
            if(iframes%2==0)
                sb.draw(BattleModule.PLAYER_INDICATOR_DMG_IMG, X, Y, width, height);
            else
                sb.draw(BattleModule.PLAYER_INDICATOR_DMG_IMG2, X, Y, width, height);
        }else{
            sb.draw(BattleModule.PLAYER_INDICATOR_IMG, X, Y, width, height);
        }
    }

}
