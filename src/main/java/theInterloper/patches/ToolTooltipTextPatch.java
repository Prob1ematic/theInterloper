package theInterloper.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import javassist.CtBehavior;

@SpirePatch(
        clz = PotionPopUp.class,
        method = "render"
)
public class ToolTooltipTextPatch {
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"potion","label"}
    )

    public static void Insert(PotionPopUp __instance, SpriteBatch sb, AbstractPotion potion, @ByRef String[] label) {
        if (PotIsToolField.inTool.get(potion)) {
            label[0] = "USE";
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderFontCenteredWidth");
            return LineFinder.findInOrder(ctMethodToPatch,finalMatcher);
        }
    }
}