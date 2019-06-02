package theInterloper.variables;

import basemod.abstracts.DynamicVariable;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import theInterloper.InterloperMod;

import static theInterloper.InterloperMod.makeID;

public class InterDex extends DynamicVariable {

    @Override
    public String key()
    {
        return makeID("InterDex");
    }

    @Override
    public boolean isModified(AbstractCard card)
    {
        return card.isBlockModified;
    }

    @Override
    public int value(AbstractCard card)
    {
        if (CardCrawlGame.isInARun()) {
            return card.block + InterloperMod.Dex;
        }else{
            return 6;
        }
    }

    @Override
    public int baseValue(AbstractCard card)
    {
        if (CardCrawlGame.isInARun()) {
            return card.baseBlock + InterloperMod.Dex;
        }else{
            return 6;
        }
    }

    @Override
    public Color getNormalColor() {
        return Color.GREEN;
    }

    @Override
    public Color getUpgradedColor() {
        return Color.GREEN;
    }

    @Override
    public Color getIncreasedValueColor() {
        return Color.GREEN;
    }

    @Override
    public Color getDecreasedValueColor() {
        return Color.GREEN;
    }

    @Override
    public boolean upgraded(AbstractCard card)
    {
        return card.upgradedBlock;
    }
}
