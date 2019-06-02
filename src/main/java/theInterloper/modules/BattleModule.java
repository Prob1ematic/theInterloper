package theInterloper.modules;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputAction;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theInterloper.InterloperMod;
import theInterloper.characters.TheInterloper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.badlogic.gdx.graphics.Color;

import java.awt.*;
import java.awt.geom.Area;
import java.util.*;
import java.util.List;

public class BattleModule {
    public static final Logger logger = LogManager.getLogger(InterloperMod.class.getName());
    public static Texture PLAYER_INDICATOR_IMG = ImageMaster.loadImage("theInterloperResources/images/ui/player.png");
    public static Texture PLAYER_INDICATOR_DMG_IMG = ImageMaster.loadImage("theInterloperResources/images/ui/playerDmg.png");
    public static Texture PLAYER_INDICATOR_DMG_IMG2 = ImageMaster.loadImage("theInterloperResources/images/ui/playerDmg2.png");
    public static Texture[] Fireball = new Texture[5];
    public static Texture BORDER_IMG = ImageMaster.loadImage("theInterloperResources/images/ui/border.png");
    public static Sfx UFF = new Sfx("theInterloperResources/sfx/dmg.mp3");
    public final int playerSpeed = 200;
    public static final float targetDistFromOrigin = 215.0f * Settings.scale;

    public PlayerIndicator player;
    public Border border;
    public List<Projectile> attacks;

    public List<Pattern> patterns;

    public boolean active = false;
    public double time = -10;

    public BattleModule(){
        this.player = new PlayerIndicator();
        this.border = new Border();
        this.attacks = new ArrayList<>();
        this.patterns = new ArrayList<>();
        this.Fireball[0] = ImageMaster.loadImage("theInterloperResources/images/ui/simple1.png");
        this.Fireball[1] = ImageMaster.loadImage("theInterloperResources/images/ui/simple2.png");
        this.Fireball[2] = ImageMaster.loadImage("theInterloperResources/images/ui/simple3.png");
        this.Fireball[3] = ImageMaster.loadImage("theInterloperResources/images/ui/simple4.png");
    }

    public void move(int x,int y){
        float deltaX = (Gdx.graphics.getDeltaTime() * playerSpeed) * x;
        float deltaY = (Gdx.graphics.getDeltaTime() * playerSpeed) * y;
        if (player.X + deltaX > player.startX-((border.width/2)-(player.width/2)-5) && player.X + deltaX < player.startX+((border.width/2)-(player.width/2)-5))
            player.X = player.X + deltaX;
        if (player.Y + deltaY > player.startY-((border.height/2)-(player.height/2)-5) && player.Y + deltaY < player.startY+((border.height/2)-(player.height/2)-5))
            player.Y = player.Y + deltaY;

        player.updateCollider();
    }

    public boolean collides(Shape shapeA, Shape shapeB) {
        Area areaA = new Area(shapeA);
        areaA.intersect(new Area(shapeB));
        return !areaA.isEmpty();
    }

    public void spawn(){
        this.attacks.add(new Projectile(0,0, player,null, 0));
    }

    public void render(SpriteBatch sb) {
        if (AbstractDungeon.player != null && AbstractDungeon.player instanceof TheInterloper) {
            sb.setColor(Color.WHITE.cpy());
            if (InterloperMod.battleMode) {
                if (active == false) {
                    patterns.clear();
                    active = true;
                    for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                        if (!m.isDeadOrEscaped()) {
                            patterns.add(new Pattern(m, player));
                            AbstractDungeon.actionManager.addToBottom(new StunMonsterAction(m, AbstractDungeon.player, 1));
                        }
                    }
                    time = -10;
                }
                time += (Gdx.graphics.getDeltaTime() * 64.5);
                logger.info(time);
                border.draw(sb);
                player.draw(sb);
                for (Pattern pattern : patterns) {
                    if (!pattern.timers.isEmpty() && pattern.timers.get(0) <= time) {
                        attacks.add(pattern.projectiles.get(0));
                        pattern.projectiles.remove(0);
                        pattern.timers.remove(0);
                    }
                }
                if (attacks != null) {
                    attacks.removeIf(b -> b.done(border, player));
                    for (Projectile attack : attacks) {
                        attack.draw(sb);
                        if (collides(player.getShape(), attack.getShape())) {
                            if (player.iframes == 0) {
                                AbstractDungeon.player.damage(new DamageInfo(attack.owner, attack.damage));
                                player.gotHurt();
                                UFF.play(Settings.SOUND_VOLUME * Settings.MASTER_VOLUME * 120);
                            }

                        }
                    }
                }
                patterns.removeIf(b -> b.projectiles.isEmpty());
                if (attacks.isEmpty() && patterns.isEmpty()) {
                    active = false;
                    InterloperMod.battleMode = false;
                }
            }
        }
    }

    @SpirePatch(
            clz=InputAction.class,
            method="isPressed"
    )
    @SpirePatch(
            clz=InputAction.class,
            method="isJustPressed"
    )
    public static class BlockKeys
    {
        public static SpireReturn<Boolean> Prefix(InputAction __instance)
        {
            if (AbstractDungeon.player != null && AbstractDungeon.player instanceof TheInterloper && InterloperMod.battleMode) {
                return SpireReturn.Return(false);
            }
            return SpireReturn.Continue();
        }
    }
}
