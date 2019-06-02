package theInterloper.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.potions.AbstractPotion;


@SpirePatch(clz = AbstractPotion.class, method = SpirePatch.CLASS)
public class PotIsToolField {
    public static SpireField<Boolean> inTool = new SpireField<>(() -> false);

//    @SpirePatch(clz = AbstractPotion.class, method = "makeCopy")
//    public static class makeCopy {
//        public static AbstractPotion Postfix(AbstractPotion result, AbstractPotion self) {
//            inTool.set(result, inTool.get(self));
//            return result;
//        }
//    }

}