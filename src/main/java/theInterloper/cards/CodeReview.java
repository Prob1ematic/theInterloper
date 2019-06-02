package theInterloper.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theInterloper.InterloperMod;
import theInterloper.characters.TheInterloper;
import theInterloper.powers.CodeReviewPower;

import static theInterloper.InterloperMod.makeCardPath;

public class CodeReview extends AbstractInterloperCard {

    public static final String ID = InterloperMod.makeID("CodeReview");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("codeReview.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    //public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheInterloper.Enums.COLOR_BROWN;

    private static final int COST = 1;
    private static final int MAGIC = 1;
    private static final int UPGRADE_MAGIC = 1;


    public CodeReview() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new CodeReviewPower(p, p, magicNumber), magicNumber));
    }

    @Override
    public void applyPowers() {
        this.magicNumber = InterloperMod.Int/5;
        super.applyPowers();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MAGIC);
            //rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}