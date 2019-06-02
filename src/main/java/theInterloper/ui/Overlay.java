package theInterloper.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theInterloper.InterloperMod;
import theInterloper.actions.TargetAction;
import theInterloper.heropowers.AbstractHeropower;
import theInterloper.patches.HeropowerField;

public class Overlay {
    public static final Logger logger = LogManager.getLogger(InterloperMod.class.getName());

    private Texture OVERLAY_IMG = ImageMaster.loadImage("theInterloperResources/images/ui/overlay.png");

    private Texture BUTTON = ImageMaster.loadImage("theInterloperResources/images/ui/Button.png");
    private Texture BUTTONPRESED = ImageMaster.loadImage("theInterloperResources/images/ui/ButtonP.png");
    private Texture BACK = ImageMaster.loadImage("theInterloperResources/images/ui/back.png");
    private Texture GRID = ImageMaster.loadImage("theInterloperResources/images/ui/grid.png");
    private Texture SIN = ImageMaster.loadImage("theInterloperResources/images/ui/sin.png");
    private Texture PANEL = ImageMaster.loadImage("theInterloperResources/images/ui/side.png");

    private Texture USING = ImageMaster.loadImage("theInterloperResources/images/ui/fan.png");
    private Texture USED = ImageMaster.loadImage("theInterloperResources/images/ui/heropower/used.png");
    private Texture IMAGE = ImageMaster.loadImage("theInterloperResources/images/ui/heropower/fireball.png");

    private TargetAction targetRender = new TargetAction();
    private NumDisplay displayR = new NumDisplay('r');
    private NumDisplay displayG = new NumDisplay('g');
    private NumDisplay displayB = new NumDisplay('b');
    private Hitbox plusStr,plusDex,plusInt,heroPower;

    float dispScale,dispX,dispY,dispW,dispH,fanSize,borderSize,fontSpacing,fontSize,buttonSize,
            targetButtonX,targetButtonY,screenX,screenW,screenH,panelW,panelX,panelXtemp,
            buttonXtemp, panelXtarget, buttonXtarget;
    boolean isNotDoneHidding = false;
    boolean targeting = false;
    float rotation = 0;
    float sin = 0;
    boolean[] pressed = {false,false,false};

    public Overlay() {
    }

    public void updateSettings(float x, float y, float scale){
        dispScale = scale;
        dispX = x * Settings.scale;
        dispY = y * Settings.scale;
        dispW = (1.325f * scale) * Settings.scale;
        dispH = (1 * scale) * Settings.scale;
        fanSize = (0.4f * scale) * Settings.scale;
        fontSpacing = (0.105f * scale) * Settings.scale;
        fontSize = (10f * scale) * Settings.scale;
        buttonSize = (0.2f * scale) * Settings.scale;
        plusStr = new Hitbox(buttonSize, buttonSize);
        plusDex = new Hitbox(buttonSize, buttonSize);
        plusInt = new Hitbox(buttonSize, buttonSize);
        heroPower = new Hitbox(fanSize, fanSize);
        targetButtonX = dispX + ((1.35f * dispScale) * Settings.scale);
        targetButtonY = dispY + ((0.12f * dispScale) * Settings.scale);
        screenX = dispX + ((0.14f * dispScale) * Settings.scale);
        screenW = (0.4f * scale) * Settings.scale;
        screenH = (0.25f * scale) * Settings.scale;
        panelW = (0.35f * scale) * Settings.scale;
        panelX = dispX + ((1.00f * dispScale) * Settings.scale);

        panelXtemp = dispX + ((0.60f * dispScale) * Settings.scale);
        panelXtarget = dispX + ((0.60f * dispScale) * Settings.scale);

        buttonXtemp = dispX + ((0.95f * dispScale) * Settings.scale);
        buttonXtarget = dispX + ((0.95f * dispScale) * Settings.scale);
    }

    public void renderDisplay(SpriteBatch sb) {
        boolean hpUsed = false;

        AbstractHeropower power = HeropowerField.heropower.get(AbstractDungeon.player);
        if(power != null){
            hpUsed = power.used;
        }

        sb.setColor(Color.WHITE);
        useHeroPower(sb);
        sb.setColor(Color.WHITE);
        int[] StrTemp = GetDigits(InterloperMod.Str);
        int[] DexTemp = GetDigits(InterloperMod.Dex);
        int[] IntTemp = GetDigits(InterloperMod.Int);
        sb.draw(BACK, screenX, targetButtonY, screenW, screenH);
        sb.draw(GRID, screenX, targetButtonY, screenW, screenH);
        sb.draw(SIN, screenX, targetButtonY, 0, screenH/2, screenW*2, screenH, 1, 0.5f, 0, (int)(sin*348), 0, 348, 100, false, false);
        sin+= Gdx.graphics.getDeltaTime() * 0.25;
        //0.27
        if ( sin > 0.33){
            sin = 0;
        }
        if(InterloperMod.SkillPts > 0){
            if(panelXtemp < panelX){
                panelXtemp += Gdx.graphics.getDeltaTime() * ((0.60f * dispScale) * Settings.scale);
                isNotDoneHidding = true;
            }
            if(buttonXtemp < targetButtonX){
                buttonXtemp += Gdx.graphics.getDeltaTime() * ((0.60f * dispScale) * Settings.scale);
                isNotDoneHidding = true;
            }
            if(panelXtemp >= panelXtarget && buttonXtemp >= buttonXtarget){
                isNotDoneHidding = false;
            }
        }else{
            if(panelXtemp > panelXtarget){
                panelXtemp -= Gdx.graphics.getDeltaTime() * ((0.60f * dispScale) * Settings.scale);
                isNotDoneHidding = true;
            }
            if(buttonXtemp > buttonXtarget){
                buttonXtemp -= Gdx.graphics.getDeltaTime() * ((0.60f * dispScale) * Settings.scale);
                isNotDoneHidding = true;
            }
            if(panelXtemp <= panelXtarget && buttonXtemp <= buttonXtarget){
                panelXtemp = panelXtarget;
                buttonXtemp = buttonXtarget;
                isNotDoneHidding = false;
            }
        }
        if(InterloperMod.SkillPts > 0 || isNotDoneHidding){
            sb.draw(PANEL, panelXtemp, dispY, panelW*2, dispH);
            doTheButtonThingy(plusInt, sb, 0);
            doTheButtonThingy(plusDex, sb, 1);
            doTheButtonThingy(plusStr, sb, 2);
        }
        rotation+= Gdx.graphics.getDeltaTime() * 200;
        if(hpUsed){
            sb.draw(USED, dispX + ((0.14f * dispScale) * Settings.scale), dispY + ((0.468f * dispScale) * Settings.scale),
                    fanSize/2, fanSize/2, fanSize, fanSize,
                    1,1, 0f,
                    0, 0, 174, 174,
                    false, false);
        }else{
            sb.draw(IMAGE, dispX + ((0.14f * dispScale) * Settings.scale), dispY + ((0.468f * dispScale) * Settings.scale),
                    fanSize/2, fanSize/2, fanSize, fanSize,
                    1,1, 0f,
                    0, 0, 174, 174,
                    false, false);
        }
        sb.draw(OVERLAY_IMG, dispX, dispY, 0, 0, dispW, dispH, 1, 1, 0, 0, 0, 265, 200, false, false);
        if(targeting)
        sb.draw(USING, dispX + ((0.14f * dispScale) * Settings.scale), dispY + ((0.468f * dispScale) * Settings.scale),
                fanSize/2, fanSize/2, fanSize, fanSize,
                1,1, rotation,
                0, 0, 174, 174,
                false, false);
        float tempX = dispX + ((0.9f * dispScale) * Settings.scale);
        for (int number = 0; number < 3 ; number++) {
            displayR.updateSettings(tempX, dispY + ((0.76f * dispScale) * Settings.scale), (int)((0.02f * dispScale) * Settings.scale));
            displayR.renderDisplay(sb, StrTemp[number]);
            displayG.updateSettings(tempX, dispY + ((0.488f * dispScale) * Settings.scale), (int)((0.02f * dispScale) * Settings.scale));
            displayG.renderDisplay(sb, DexTemp[number]);
            displayB.updateSettings(tempX, dispY + ((0.212f * dispScale) * Settings.scale), (int)((0.02f * dispScale) * Settings.scale));
            displayB.renderDisplay(sb, IntTemp[number]);
            tempX += fontSpacing;
        }

    }

    public int[] GetDigits(int number) {
        int[] out = {0,0,0};
        int where = 2;
        while (number!= 0){
            out[where] = number%10;
            number = number/10;
            where--;
        }
        return out;
    }

    public void useHeroPower(SpriteBatch sb){
        heroPower.update(dispX + ((0.14f * dispScale) * Settings.scale), dispY + ((0.468f * dispScale) * Settings.scale));

        AbstractHeropower power = HeropowerField.heropower.get(AbstractDungeon.player);

        if(heroPower.hovered){
            power.displayTooltip(dispX ,dispY + (1.9f * dispScale));
        }

        if(heroPower.hovered && (InputHelper.justClickedLeft || InputHelper.isMouseDown) && !targeting && power.canUse() ) {
            targeting = true;
            targetRender.canTargetPlayer = power.targetsPlayer;
            if(power.targetable) {
                targetRender.open();
            }else{
                power.use(null);
            }
        }

        targeting = !targetRender.isHidden();

        if (targeting){
            targetRender.render(sb);
        }
    }

    public void doTheButtonThingy(Hitbox button, SpriteBatch sb, int stat){
        button.update(buttonXtemp, targetButtonY + (((0.28f * dispScale) * Settings.scale))*stat);
        if(button.hovered && (InputHelper.justClickedLeft || InputHelper.isMouseDown)){
            sb.draw(BUTTONPRESED, buttonXtemp, targetButtonY + ((((0.28f * dispScale) * Settings.scale))*stat), buttonSize, buttonSize);
        }else{
            sb.draw(BUTTON, buttonXtemp, targetButtonY + ((((0.28f * dispScale) * Settings.scale))*stat), buttonSize, buttonSize);
        }
        if(button.hovered && (InputHelper.justClickedLeft || InputHelper.isMouseDown) && !pressed[stat] && InterloperMod.SkillPts > 0) {
            switch (stat){
            case 0:
                InterloperMod.Int++;
                break;
            case 1:
                InterloperMod.Dex++;
                break;
            case 2:
                InterloperMod.Str++;
                break;
            default:
                break;
            }
            InterloperMod.SkillPts--;
            pressed[stat] = true;
        }else{
            if(!(button.hovered && (InputHelper.justClickedLeft || InputHelper.isMouseDown)))
                pressed[stat] = false;
        }
        button.render(sb);
    }

}
