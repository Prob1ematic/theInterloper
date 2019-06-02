package theInterloper.variables;

import basemod.abstracts.DynamicVariable;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import theInterloper.InterloperMod;

import static theInterloper.InterloperMod.makeID;

public class InterStr extends DynamicVariable {

    @Override
    public String key()
    {
        return makeID("InterStr");
    }

    @Override
    public boolean isModified(AbstractCard card)
    {
        return card.isDamageModified;
    }

    @Override
    public int value(AbstractCard card)
    {
        if (CardCrawlGame.isInARun()) {
            return card.damage + InterloperMod.Str;
        }else{
            return 6;
        }
    }

    @Override
    public int baseValue(AbstractCard card)
    {
        if (CardCrawlGame.isInARun()) {
            return card.baseDamage + InterloperMod.Str;
        }else{
            return 6;
        }
    }

    @Override
    public Color getNormalColor() {
        return Color.RED;
    }

    @Override
    public Color getUpgradedColor() {
        return Color.RED;
    }

    @Override
    public Color getIncreasedValueColor() {
        return Color.RED;
    }

    @Override
    public Color getDecreasedValueColor() {
        return Color.RED;
    }

    @Override
    public boolean upgraded(AbstractCard card)
    {
        return card.upgradedDamage;
    }
}
