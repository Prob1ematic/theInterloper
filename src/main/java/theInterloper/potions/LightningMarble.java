package theInterloper.potions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import com.megacrit.cardcrawl.vfx.FlashPotionEffect;
import javassist.CtBehavior;
import theInterloper.InterloperMod;
import theInterloper.patches.PotIsToolField;

import java.util.Iterator;

public class LightningMarble extends AbstractPotion {

    public static final String POTION_ID = InterloperMod.makeID("LightningMarble");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public LightningMarble() {
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.M, PotionColor.SMOKE);
        potency = getPotency();
        description = DESCRIPTIONS[0];
        isThrown = false;
        tips.add(new PowerTip(name, description));
        PotIsToolField.inTool.set(this, true);
    }

    @Override
    public void renderLightOutline(SpriteBatch sb) {
            sb.setColor(new Color(0.0F, 0.0F, 0.0F, 0.25F));
            sb.draw(InterloperMod.lightningMarbleOutline, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, 0.0F, 0, 0, 64, 64, false, false);

    }

    @Override
    public void renderOutline(SpriteBatch sb) {
            sb.setColor(new Color(0.0F, 0.0F, 0.0F, 0.5F));
            sb.draw(InterloperMod.lightningMarbleOutline, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, 0.0F, 0, 0, 64, 64, false, false);
    }

    @Override
    public void shopRender(SpriteBatch sb) {
        //this.updateFlash();
        if (this.hb.hovered) {
            TipHelper.renderGenericTip((float) InputHelper.mX + 50.0F * Settings.scale, (float)InputHelper.mY, this.name, this.description);
            this.scale = 1.5F * Settings.scale;
        } else {
            this.scale = MathHelper.scaleLerpSnap(this.scale, 1.2F * Settings.scale);
        }

        this.renderOutline(sb);

        sb.draw(InterloperMod.lightningMarble, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, 0.0F, 0, 0, 64, 64, false, false);

        if (this.hb != null) {
            this.hb.render(sb);
        }
    }


    @Override
    public void render(SpriteBatch sb) {
        //this.updateFlash();

        sb.setColor(Color.WHITE);
        sb.draw(InterloperMod.lightningMarble, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, 0.0F, 0, 0, 64, 64, false, false);

        if (this.hb != null) {
            this.hb.render(sb);
        }

    }

    @Override
    public void use(AbstractCreature target) {
        target = AbstractDungeon.player;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if (AbstractDungeon.player.maxOrbs == 0) {
                AbstractDungeon.actionManager.addToBottom(new IncreaseMaxOrbAction(1));
            }
            AbstractDungeon.actionManager.addToBottom(new ChannelAction(new Lightning()));
        }
    }
    
    @Override
    public AbstractPotion makeCopy() {
        return new LightningMarble();
    }

    @Override
    public int getPotency(final int potency) {
        return 1;
    }

}


