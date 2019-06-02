package theInterloper.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import javassist.CtBehavior;

@SpirePatch(
        clz = PotionPopUp.class,
        method = "render"
)
public class ToolTooltipPatch {
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"potion","x","y","isHidden","hbTop"}
    )

    public static void Insert(PotionPopUp __instance, SpriteBatch sb, AbstractPotion potion, float x, float y, boolean isHidden, Hitbox hbTop) {
        if (!isHidden && hbTop.hovered && PotIsToolField.inTool.get(potion)) {
            TipHelper.renderGenericTip(x + 124.0F * Settings.scale, y + 50.0F * Settings.scale, "USE", "USE TOOL");
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