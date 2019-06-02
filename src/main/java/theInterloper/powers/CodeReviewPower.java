package theInterloper.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import theInterloper.InterloperMod;
import theInterloper.modules.Pattern;
import theInterloper.util.TextureLoader;

import static theInterloper.InterloperMod.makePowerPath;

public class CodeReviewPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = InterloperMod.makeID("CodeReviewPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("CodeReview_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("CodeReview_power32.png"));

    public CodeReviewPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = true;
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }


    //@Override
    //public void onUseCard(final AbstractCard card, final UseCardAction action) {
    //    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner,
    //            new DexterityPower(owner, amount), amount));
    //}

    // Note: If you want to apply an effect when a power is being applied you have 3 options:
    //onInitialApplication is "When THIS power is first applied for the very first time only."
    //onApplyPower is "When the owner applies a power to something else (only used by Sadistic Nature)."
    //onReceivePowerPower from StSlib is "When any (including this) power is applied to the owner."

    @Override
    public void atEndOfTurn(final boolean isPlayer) {
        flash();

        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                switch (m.intent){
                    case ATTACK:
                    case ATTACK_BUFF:
                    case ATTACK_DEBUFF:
                    case ATTACK_DEFEND:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player,
                                new WeakPower(m, amount, false), amount));
                        break;
                    case DEFEND:
                    case DEFEND_BUFF:
                    case DEFEND_DEBUFF:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player,
                                new FrailPower(m, amount, false), amount));
                        break;
                    case BUFF:
                    case MAGIC:
                    case STRONG_DEBUFF:
                    case DEBUFF:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player,
                                new VulnerablePower(m, amount, false), amount));
                        break;
                    default:
                        AbstractDungeon.actionManager.addToBottom(new StunMonsterAction(m, AbstractDungeon.player, 1));
                        break;

                }
            }
        }

    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + DESCRIPTIONS[3];
        } else if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2] + DESCRIPTIONS[3];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new CodeReviewPower(owner, source, amount);
    }
}
