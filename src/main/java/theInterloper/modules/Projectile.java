package theInterloper.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import theInterloper.InterloperMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.lang.Math;

import java.awt.*;

public class Projectile
{
    public static final Logger logger = LogManager.getLogger(InterloperMod.class.getName());
    public static final float width = 6;
    public static final float height = 6; //Settings.scale?
    public double X;
    public double Y;
    public double startX;
    public double startY;
    public double moveX;
    public double moveY;
    public Rectangle collider;
    public Point direction;
    public int age;
    public PlayerIndicator target;
    public int damage;
    public final int speed = 350;
    AbstractCreature owner;

    public Projectile(float x, float y, PlayerIndicator pTarget, AbstractCreature mOwner, int damage) {
        this.age = 0;
        this.X = ((Settings.WIDTH/2)-(width/2))+x;
        this.Y = ((Settings.HEIGHT/2)-(height/2))+y;
        this.startX = X;
        this.startY = Y;
        this.moveX = 0;
        this.moveY = 0;
        this.collider = new Rectangle((int)X,(int)Y,(int)width,(int)height);
        this.target = pTarget;
        this.damage = damage;
        this.owner = mOwner;
    }

    public void updateCollider(){
        collider.setLocation((int)X, (int)Y);
    }

    public Shape getShape(){
        return collider;
    }

    public boolean done(Border border, PlayerIndicator player){
            if ( ((player.startX + (border.width/2) + 20) < X) ||
                ((player.startX - (border.width/2) - 20) > X) ||
                ((player.startY + (border.height/2) + 20) < Y) ||
                ((player.startY - (border.height/2) - 20) > Y) ){
                return true;
            }
            return false;
    }

    public void calculateRatio(){
        double speed = 0.5;
        float ratioX = direction.x - collider.x;
        float ratioY = direction.y - collider.y;
        double d = Math.sqrt(ratioX * ratioX + ratioY * ratioY);
        moveX = (ratioX / d)* speed;
        moveY = (ratioY / d)* speed;
    }

    public void calculateRatioBasedOnAngle(){
        float ratioX = direction.x - collider.x;
        float ratioY = direction.y - collider.y;
        if (Math.abs(ratioX) > Math.abs(ratioY)){
            moveX = (Math.abs(ratioX)/Math.abs(ratioY))/2;
            moveY = 0.5;
        }else{
            moveX = 0.5;
            moveY = (Math.abs(ratioY)/Math.abs(ratioX))/2;
        }
        if (ratioX<0)
            moveX = moveX * -1;
        if (ratioY<0)
            moveY = moveY * -1;
    }

    public void draw(SpriteBatch sb) {
        age++;
        if(age == 100){
            if (target != null){
                direction = new Point(target.getCenterLocation());
            } else {
                direction = new Point((int)X,(int)Y);
            }
            calculateRatio();
        }
        if(age > 100){
            X += ( Gdx.graphics.getDeltaTime() * speed) * moveX;
            Y += ( Gdx.graphics.getDeltaTime() * speed) * moveY;
        }
        sb.draw(BattleModule.Fireball[age%4], (float)X, (float)Y, width, height);
        updateCollider();
    }

}
