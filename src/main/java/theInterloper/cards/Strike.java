package theInterloper.cards;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import theInterloper.InterloperMod;
import theInterloper.characters.TheInterloper;

import static theInterloper.InterloperMod.makeCardPath;

public class Strike extends AbstractInterloperCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Strike Deal 7(9) damage.
     */

    // TEXT DECLARATION

    public static final String ID = InterloperMod.makeID("Strike");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("Strike.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name
    // and make sure it's in the correct folder. That's all.
    // There's makeCardPath, makeRelicPath, power, orb, event, etc..
    // The list of all of them can be found in the main InterloperMod.java file in the
    // ==INPUT TEXTURE LOCATION== section under ==MAKE IMAGE PATHS==


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheInterloper.Enums.COLOR_BROWN;

    private static final int COST = 1;
    private static final int DAMAGE = 0;
    private static final int MAGIC = 0;
    private static final int UPGRADE_MAGIC = 1;
    //private static final int UPGRADE_PLUS_DMG = 0;

    // Hey want a second damage/magic/block/unique number??? Great!
    // Go check out LightningStorm and theDefault.variable.DefaultCustomVariable
    // that's how you get your own custom variable that you can use for anything you like.
    // Feel free to explore other mods to see what variabls they personally have and create your own ones.

    // /STAT DECLARATION/

    public Strike() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.baseDamage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = MAGIC;
        this.tags.add(BaseModCardTags.BASIC_STRIKE); //Tag your strike, defend and form (Shadow form, demon form, echo form, etc.) cards so that they function correctly.
        this.tags.add(CardTags.STRIKE);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        if (upgraded) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p,
                    new VulnerablePower(m, magicNumber, false), magicNumber));
        }
    }

    @Override
    public void applyPowers() {
        this.baseDamage = InterloperMod.Str;
        super.applyPowers();
    }

    //@Override
    //public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp) {
    //}

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MAGIC);
            rawDescription = UPGRADE_DESCRIPTION;
            //upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
