package theInterloper.cards;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import org.apache.logging.log4j.Logger;
import theInterloper.InterloperMod;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


public abstract class AbstractInterloperCard extends CustomCard {


    public Integer socketCount = 0;

    //public InterloperMod.chipTypes thisChipType = null;
    //public ArrayList<InterloperMod.chipTypes> sockets = new ArrayList<>();

    public int baseSecondaryM;
    public int secondaryM;
    public boolean isSecondaryMModified;
    public float titleOffset = 5.0F;
    private Texture FAN = ImageMaster.loadImage("theInterloperResources/images/ui/fan.png");

    public AbstractInterloperCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color,
                                  CardRarity rarity, CardTarget target) {

        super(id, name, img, cost, rawDescription, type,
                color, rarity, target);

        textureBannerLargeImg = InterloperMod.BanBig;
        textureBannerSmallImg = InterloperMod.BanSmall;
        textureBackgroundSmallImg = InterloperMod.BgSmall;
        textureBackgroundLargeImg = InterloperMod.BgBig;

    }


    public void upgradeDefaultSecondMagicNumber(int amount){
        this.baseSecondaryM += amount;
        this.secondaryM = this.baseSecondaryM;
        this.isSecondaryMModified = true;
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        float scale = this.drawScale * Settings.scale;
        float drawX = this.current_x - 256.0F;
        float drawY = this.current_y - 256.0F;
        //sb.draw(FAN, drawX, drawY, 256.0F, 256.0F, 512.0F, 512.0F, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, 512, 512, false, false);

    }



}