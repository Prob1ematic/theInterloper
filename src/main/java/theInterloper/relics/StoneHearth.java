package theInterloper.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import theInterloper.InterloperMod;
import theInterloper.util.TextureLoader;

import static theInterloper.InterloperMod.makeRelicOutlinePath;
import static theInterloper.InterloperMod.makeRelicPath;

public class StoneHearth extends CustomRelic implements ClickableRelic {

    public static final String ID = InterloperMod.makeID("StoneHearth");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("default_clickable_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("default_clickable_relic.png"));
    private boolean usedThisTurn = false;

    public StoneHearth() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
        tips.clear();
        tips.add(new PowerTip(name, description));
    }


    @Override
    public void onRightClick() {
        if (!isObtained || usedThisTurn) {
            return;
        }
        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            usedThisTurn = true;
            flash();
            stopPulse();

            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));

            //AbstractDungeon.actionManager.addToBottom(new TalkAction(true, DESCRIPTIONS[1], 4.0f, 2.0f));

            //AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_COLLECTOR_DEBUFF")); // Sound Effect Action of The Collector Nails
            //AbstractDungeon.actionManager.addToBottom(new VFXAction( // Visual Effect Action of the nails applies on a random monster's position.
            //        new CollectorCurseEffect(AbstractDungeon.getRandomMonster().hb.cX, AbstractDungeon.getRandomMonster().hb.cY), 2.0F));
            //AbstractDungeon.actionManager.addToBottom(new EvokeOrbAction(1)); // Evoke your rightmost orb

            //InterloperMod.battleMode = true;
        }
    }

    //public void atTurnStart() {
    //    usedThisTurn = false;
    //    beginLongPulse();
    //}

    @Override
    public void atPreBattle() {
        usedThisTurn = false;
        beginLongPulse();
    }

    @Override
    public void onVictory() {
        stopPulse();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
