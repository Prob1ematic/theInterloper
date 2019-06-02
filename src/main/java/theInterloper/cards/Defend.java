package theInterloper.cards;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FlameBarrierPower;
import theInterloper.InterloperMod;
import theInterloper.characters.TheInterloper;

import static theInterloper.InterloperMod.makeCardPath;

public class Defend extends AbstractInterloperCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION 

    public static final String ID = InterloperMod.makeID("Defend");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("Defend.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION 	

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheInterloper.Enums.COLOR_BROWN;

    private static final int COST = 1;
    private static final int BLOCK = 0;
    private static final int MAGIC = 0;
    private static final int UPGRADE_MAGIC = 1;
    //private static final int UPGRADE_PLUS_BLOCK = 3;


    // /STAT DECLARATION/


    public Defend() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        magicNumber = baseMagicNumber = MAGIC;
        this.tags.add(BaseModCardTags.BASIC_DEFEND); //Tag your strike, defend and form cards so that they work correctly.
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        if (upgraded) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                    new FlameBarrierPower(p, magicNumber), magicNumber));
        }
    }


    @Override
    public void applyPowers() {
        this.baseBlock = InterloperMod.Dex;
        super.applyPowers();
    }

    //@Override
    //public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp) {
    //}


    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MAGIC);
            rawDescription = UPGRADE_DESCRIPTION;
            //upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
