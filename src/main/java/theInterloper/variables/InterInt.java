package theInterloper.variables;

import basemod.abstracts.DynamicVariable;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import theInterloper.InterloperMod;

import static theInterloper.InterloperMod.makeID;

public class InterInt extends DynamicVariable {

    @Override
    public String key()
    {
        return makeID("InterInt");
    }

    @Override
    public boolean isModified(AbstractCard card)
    {
        return card.isMagicNumberModified;
    }

    @Override
    public int value(AbstractCard card)
    {
        if (CardCrawlGame.isInARun()) {
            return card.magicNumber + InterloperMod.Int;
        }else{
            return 6;
        }
    }

    @Override
    public int baseValue(AbstractCard card)
    {
        if (CardCrawlGame.isInARun()) {
            return card.baseMagicNumber + InterloperMod.Int;
        }else{
            return 6;
        }
    }

    @Override
    public Color getNormalColor() {
        return Color.BLUE;
    }

    @Override
    public Color getUpgradedColor() {
        return Color.BLUE;
    }

    @Override
    public Color getIncreasedValueColor() {
        return Color.BLUE;
    }

    @Override
    public Color getDecreasedValueColor() {
        return Color.BLUE;
    }

    @Override
    public boolean upgraded(AbstractCard card)
    {
        return card.upgradedMagicNumber;
    }
}
