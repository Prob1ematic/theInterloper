package theInterloper.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import theInterloper.heropowers.AbstractHeropower;


@SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
public class HeropowerField {
    public static SpireField<AbstractHeropower> heropower = new SpireField<>(() -> new AbstractHeropower());

//    @SpirePatch(clz = AbstractPotion.class, method = "makeCopy")
//    public static class makeCopy {
//        public static AbstractPotion Postfix(AbstractPotion result, AbstractPotion self) {
//            inTool.set(result, inTool.get(self));
//            return result;
//        }
//    }

}