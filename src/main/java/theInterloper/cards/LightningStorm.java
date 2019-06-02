package theInterloper.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import theInterloper.InterloperMod;
import theInterloper.characters.TheInterloper;

import static theInterloper.InterloperMod.makeCardPath;

public class LightningStorm extends AbstractInterloperCard {

    public static final String ID = InterloperMod.makeID("LightningStorm");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("LightningStorm.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheInterloper.Enums.COLOR_BROWN;

    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DMG = 1;

    public int specialDamage;

    // /STAT DECLARATION/

    public LightningStorm() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        isMultiDamage = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            if (!mon.isDeadOrEscaped()) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(
                        new LightningEffect(mon.hb.cX, mon.hb.cY), 0.05F));
                AbstractDungeon.actionManager.addToBottom(
                        new DamageAction(mon, new DamageInfo(p, damage, damageTypeForTurn),
                                AbstractGameAction.AttackEffect.NONE));
            }
        }
        AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}