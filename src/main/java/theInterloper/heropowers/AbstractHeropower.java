package theInterloper.heropowers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import theInterloper.InterloperMod;

public class AbstractHeropower {
    public static boolean used = false;
    public static int cost = 1;
    public static int costForTurn = cost;
    public static boolean targetable = true;
    public static boolean freeToPlayOnce = false;
    public static boolean targetsPlayer = false;
    public static String baseName = "Fireball";
    public static String[] baseDesc = {"Deal "," dmg to enemy. NL Affected by Int."};
    public static String name = "Fireball";
    public static String desc = "Deal damage";

    public AbstractHeropower(){

    }

    public void displayTooltip(float x, float y){
        preRenderUpdate();
        TipHelper.renderGenericTip((x * Settings.scale), (y * Settings.scale), name, desc);
    }

    public void preRenderUpdate(){
        updateDesc();
        updateName();
    }

    public void updateDesc(){
        desc = baseDesc[0]+InterloperMod.Int+baseDesc[1];
    }

    public void updateName(){
        name = baseName+" - "+costForTurn+" [E] ";
    }

    public boolean canUse() {
        if (AbstractDungeon.actionManager.turnHasEnded) {
            return false;
        }else if(used) {
            return false;
        }else if (this.freeToPlayOnce) {
            return true;
        } else {
            if (EnergyPanel.totalCount >= this.costForTurn) {
                return true;
            }else{
                return false;
            }
        }

    }

    public void use(AbstractCreature target){
        AbstractDungeon.player.energy.use(cost);
        used = true;
        target.damage(new DamageInfo(AbstractDungeon.player, InterloperMod.Int));
    }
}
