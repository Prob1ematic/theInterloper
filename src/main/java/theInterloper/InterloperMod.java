package theInterloper;

import basemod.BaseMod;
import basemod.ModLabel;
import basemod.ModPanel;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.PlayerTurnEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theInterloper.cards.*;
import theInterloper.characters.TheInterloper;
import theInterloper.events.IdentityCrisisEvent;
import theInterloper.heropowers.AbstractHeropower;
import theInterloper.modules.BattleModule;
import theInterloper.patches.HeropowerField;
import theInterloper.potions.LightningMarble;
import theInterloper.relics.RefactoringTask;
import theInterloper.relics.StoneHearth;
import theInterloper.ui.Overlay;
import theInterloper.util.IDCheckDontTouchPls;
import theInterloper.util.TextureLoader;
import theInterloper.variables.InterDex;
import theInterloper.variables.InterInt;
import theInterloper.variables.InterStr;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import static com.megacrit.cardcrawl.helpers.ImageMaster.loadImage;

@SpireInitializer
public class InterloperMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber,
        RenderSubscriber,
        OnPlayerDamagedSubscriber,
        PreRoomRenderSubscriber,
        OnStartBattleSubscriber,
        PostDungeonInitializeSubscriber,
        CustomSavable<Integer[]> {

    public static final Logger logger = LogManager.getLogger(InterloperMod.class.getName());
    private static String modID;
    private static final String MODNAME = "The Interloper";
    private static final String AUTHOR = "Problematic";
    private static final String DESCRIPTION = "Eh?";

    // =============== INPUT TEXTURE LOCATION =================

    // Colors (RGB)
    // Character Color
    public static final Color INTERLOPER_BROWN = CardHelper.getColor(219.0f, 135.0f, 0.0f);
    public static final Color COLORBROWN = CardHelper.getColor(100.0f, 25.0f, 10.0f); // Super Dark Red/Brown

    // Card backgrounds - The actual rectangular card.
    private static final String ATTACK_DEFAULT_GRAY = "theInterloperResources/images/512/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY = "theInterloperResources/images/512/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY = "theInterloperResources/images/512/bg_power_default_gray.png";

    private static final String ENERGY_ORB_DEFAULT_GRAY = "theInterloperResources/images/512/card_default_gray_orb.png";
    private static final String CARD_ENERGY_ORB = "theInterloperResources/images/512/card_small_orb.png";

    private static final String ATTACK_DEFAULT_GRAY_PORTRAIT = "theInterloperResources/images/1024/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY_PORTRAIT = "theInterloperResources/images/1024/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY_PORTRAIT = "theInterloperResources/images/1024/bg_power_default_gray.png";
    private static final String ENERGY_ORB_DEFAULT_GRAY_PORTRAIT = "theInterloperResources/images/1024/card_default_gray_orb.png";

    // Character assets
    private static final String THE_DEFAULT_BUTTON = "theInterloperResources/images/charSelect/Button.png";
    private static final String THE_DEFAULT_PORTRAIT = "theInterloperResources/images/charSelect/DefaultCharacterPortraitBG.png";
    public static final String THE_DEFAULT_SHOULDER_1 = "theInterloperResources/images/char/defaultCharacter/shoulder.png";
    public static final String THE_DEFAULT_SHOULDER_2 = "theInterloperResources/images/char/defaultCharacter/shoulder2.png";
    public static final String THE_DEFAULT_CORPSE = "theInterloperResources/images/char/defaultCharacter/corpse.png";

    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "theInterloperResources/images/Badge.png";

    // Atlas and JSON files for the Animations
    public static final String THE_DEFAULT_SKELETON_ATLAS = "theInterloperResources/images/char/defaultCharacter/skeleton.atlas";
    public static final String THE_DEFAULT_SKELETON_JSON = "theInterloperResources/images/char/defaultCharacter/skeleton.json";

    // Backgrounds
    public static final String BgSmall = "theInterloperResources/images/512/bg_default.png";
    public static final String BgBig = "theInterloperResources/images/1024/bg_default.png";

    // Banners
    public static final String BanBig = "theInterloperResources/images/1024/banner.png";
    public static final String BanSmall = "theInterloperResources/images/512/banner.png";

    // System variables
    public static BattleModule fight;
    public static Boolean battleMode;
    public static Overlay topOverlay;

    //Temp Global textures
    public static Texture lightningMarble;
    public static Texture lightningMarbleOutline;

    public static Integer Str,Dex,Int,SkillPts;

    //TODO: Reproduce Energy tooltip crash aka. AtlasRegion
    //TODO: In Minigame: Block End Turn (Mby playing cards too?)

    // =============== MAKE IMAGE PATHS =================

    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/orbs/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }

    // =============== /MAKE IMAGE PATHS/ =================

    // =============== /INPUT TEXTURE LOCATION/ =================

    // =============== SUBSCRIBE, CREATE THE COLOR_BROWN, INITIALIZE =================

    public InterloperMod() {
        logger.info("Subscribe to BaseMod hooks");

        BaseMod.subscribe(this);
        setModID("theInterloper");

        logger.info("Done subscribing");

        logger.info("Creating the color " + TheInterloper.Enums.COLOR_BROWN.toString());

        BaseMod.addColor(TheInterloper.Enums.COLOR_BROWN, INTERLOPER_BROWN, INTERLOPER_BROWN, INTERLOPER_BROWN,
                INTERLOPER_BROWN, INTERLOPER_BROWN, INTERLOPER_BROWN, INTERLOPER_BROWN,
                ATTACK_DEFAULT_GRAY, SKILL_DEFAULT_GRAY, POWER_DEFAULT_GRAY, ENERGY_ORB_DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY_PORTRAIT, SKILL_DEFAULT_GRAY_PORTRAIT, POWER_DEFAULT_GRAY_PORTRAIT,
                ENERGY_ORB_DEFAULT_GRAY_PORTRAIT, CARD_ENERGY_ORB);

        logger.info("Done creating the color");
    }

    public static void setModID(String ID) {
        Gson coolG = new Gson();
        //   String IDjson = Gdx.files.internal("IDCheckStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        InputStream in = InterloperMod.class.getResourceAsStream("/IDCheckStrings.json");
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class);

        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) {
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION);
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) {
            modID = EXCEPTION_STRINGS.DEFAULTID;
        } else {
            modID = ID;
        }
    }

    public static String getModID() {
        return modID;
    }

    private static void pathCheck() {
        Gson coolG = new Gson();
        //   String IDjson = Gdx.files.internal("IDCheckStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        InputStream in = InterloperMod.class.getResourceAsStream("/IDCheckStrings.json");
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class);

        String packageName = InterloperMod.class.getPackage().getName();
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources");
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) {
            if (!packageName.equals(getModID())) {
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID());
            }
            if (!resourcePathExists.exists()) {
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources");
            }
        }
    }


    @SuppressWarnings("unused")
    public static void initialize() {
        logger.info("==== TheInterloper init ====");
        InterloperMod defaultmod = new InterloperMod();
        logger.info("==== TheInterloper done ====");
    }

    // ============== /SUBSCRIBE, CREATE THE COLOR_BROWN, INITIALIZE/ =================


    // =============== LOAD THE CHARACTER =================

    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + TheInterloper.Enums.THE_INTERLOPER.toString());

        BaseMod.addCharacter(new TheInterloper("the Interloper", TheInterloper.Enums.THE_INTERLOPER),
                THE_DEFAULT_BUTTON, THE_DEFAULT_PORTRAIT, TheInterloper.Enums.THE_INTERLOPER);

        receiveEditPotions();
        logger.info("Added " + TheInterloper.Enums.THE_INTERLOPER.toString());
    }

    // =============== /LOAD THE CHARACTER/ =================


    // =============== POST-INITIALIZE =================

    @Override
    public Type savedType()
    {
        return new TypeToken<Integer[]>(){}.getType();
    }

    @Override
    public Integer[] onSave(){
        logger.info("SAVED STATS");
        Integer[] save = {Str,Dex,Int,SkillPts};
        return save;
    }

    @Override
    public void onLoad(Integer[] save){
        logger.info("LOADED STATS");
        if (save != null) {
            Str = save[0];
            Dex = save[1];
            Int = save[2];
            SkillPts = save[3];
        }
    }


    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        lightningMarble = TextureLoader.getTexture("theInterloperResources/images/pots/lMarble.png");
        lightningMarbleOutline = TextureLoader.getTexture("theInterloperResources/images/pots/lMarbleB.png");
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        settingsPanel.addUIElement(new ModLabel("InterloperMod doesn't have any settings! An example of those may come later.", 400.0f, 700.0f,
                settingsPanel, (me) -> {
        }));
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        // =============== EVENTS =================

        // This event will be exclusive to the City (act 2). If you want an event that's present at any
        // part of the game, simply don't include the dungeon ID
        // If you want to have a character-specific event, look at slimebound (CityRemoveEventPatch).
        // Essentially, you need to patch the game and say "if a player is not playing my character class, remove the event from the pool"
        BaseMod.addEvent(IdentityCrisisEvent.ID, IdentityCrisisEvent.class, TheCity.ID);

        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");
        BaseMod.addSaveField("charStats", this);
        fight = new BattleModule();
        battleMode = false;
        topOverlay = new Overlay();
        topOverlay.updateSettings(30, 280, 200);
    }

    // =============== / POST-INITIALIZE/ =================

    // =============== PATCHES AND RECEIVERS ================


    @Override
    public void receiveRender(SpriteBatch sb) {
        if (AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT &&
                AbstractDungeon.player != null && !AbstractDungeon.isScreenUp){

            if(AbstractDungeon.player instanceof TheInterloper) {
                if(topOverlay != null) {
                    topOverlay.renderDisplay(sb);
                }
            }

            if(fight != null && battleMode) {
                if (Gdx.input.isKeyPressed(Input.Keys.UP))
                    fight.move(0, 1);

                if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
                    fight.move(0, -1);

                if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                    fight.move(-1, 0);

                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                    fight.move(1, 0);

                if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
                    fight.spawn();

                fight.render(sb);
            }
        }

    }

    @Override
    public void receivePostDungeonInitialize() {
        //if(AbstractDungeon.player instanceof TheInterloper) {
            Str = 6;
            Dex = 6;
            Int = 6;
            SkillPts = 0;
        //}
    }

    @Override
    public int receiveOnPlayerDamaged(int i, DamageInfo damageInfo) {
        //logger.info("Took dmg: "+i);
        return i;
    }

    @Override
    public void receivePreRoomRender(SpriteBatch sb) {


    }

    @Override
    public void receiveOnBattleStart(AbstractRoom var1){

    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "die",
            paramtypez = {boolean.class}
    )
    public static class onEnemyKill{
        public static void Prefix(AbstractMonster __instance, boolean triggerRelics) {
            if(AbstractDungeon.player instanceof TheInterloper) {
                boolean isMinion = false;
                for (AbstractPower power : __instance.powers) {
                    if (power instanceof MinionPower) {
                        isMinion = true;
                    }
                }

                if (!isMinion) {
                    switch (__instance.type) {
                        case NORMAL:
                            SkillPts++;
                            break;
                        case ELITE:
                            SkillPts += 2;
                            break;
                        case BOSS:
                            SkillPts += 5;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = PlayerTurnEffect.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {}
    )
    public static class onNewTurn {
        public static void Prefix(PlayerTurnEffect __instance) {
            if (AbstractDungeon.player instanceof TheInterloper) {
                AbstractHeropower power = HeropowerField.heropower.get(AbstractDungeon.player);
                if (power != null) {
                    power.used = false;
                }
            }
        }
    }

    // =============== / PATCHES AND RECEIVERS / ================

    // ================ ADD POTIONS ===================


    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");

        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "TheDefaultEnum.THE_INTERLOPER".
        // Remember, you can press ctrl+P inside parentheses like addPotions)
        //BaseMod.addPotion(LightningMarble.class, PLACEHOLDER_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, PLACEHOLDER_POTION_SPOTS, LightningMarble.POTION_ID, TheInterloper.Enums.THE_INTERLOPER);
        BaseMod.addPotion(LightningMarble.class, COLORBROWN, COLORBROWN, COLORBROWN, LightningMarble.POTION_ID);

        logger.info("Done editing potions");
    }

    // ================ /ADD POTIONS/ ===================


    // ================ ADD RELICS ===================

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        BaseMod.addRelicToCustomPool(new RefactoringTask(), TheInterloper.Enums.COLOR_BROWN);
        BaseMod.addRelicToCustomPool(new StoneHearth(), TheInterloper.Enums.COLOR_BROWN);

        // This adds a relic to the Shared pool. Every character can find this relic.
        //BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);

        // Mark relics as seen (the others are all starters so they're marked as seen in the character file
        UnlockTracker.markRelicAsSeen(RefactoringTask.ID);
        logger.info("Done adding relics!");
    }

    // ================ /ADD RELICS/ ===================


    // ================ ADD CARDS ===================

    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        //Ignore this
        pathCheck();
        // Add the Custom Dynamic Variables
        logger.info("Add variabls");
        // Add the Custom Dynamic variabls
        BaseMod.addDynamicVariable(new InterStr());
        BaseMod.addDynamicVariable(new InterDex());
        BaseMod.addDynamicVariable(new InterInt());
        //BaseMod.addDynamicVariable(new ExploitDmg());
        //BaseMod.addDynamicVariable(new ExploitDmg());

        logger.info("Adding cards");
        // Add the cards
        // Don't comment out/delete these cards (yet). You need 1 of each type and rarity (technically) for your game not to crash
        // when generating card rewards/shop screen items.
        BaseMod.addCard(new Strike());
        BaseMod.addCard(new Defend());
        BaseMod.addCard(new LightningStorm());

        BaseMod.addCard(new OrbSkill());
        BaseMod.addCard(new DefaultSecondMagicNumberSkill());
        BaseMod.addCard(new Exploit());
        BaseMod.addCard(new LightningStorm());
        BaseMod.addCard(new DefaultUncommonSkill());
        BaseMod.addCard(new DefaultUncommonAttack());
        BaseMod.addCard(new DefaultUncommonPower());
        BaseMod.addCard(new DefaultRareAttack());
        BaseMod.addCard(new DefaultRareSkill());
        BaseMod.addCard(new DefaultRarePower());
        //BaseMod.addCard(new CodeReview());

        logger.info("Making sure the cards are unlocked.");
        // Unlock the cards
        // This is so that they are all "seen" in the library, for people who like to look at the card list
        // before playing your mod.
        UnlockTracker.unlockCard(OrbSkill.ID);
        UnlockTracker.unlockCard(DefaultSecondMagicNumberSkill.ID);
        UnlockTracker.unlockCard(Strike.ID);
        UnlockTracker.unlockCard(Defend.ID);
        UnlockTracker.unlockCard(Exploit.ID);
        UnlockTracker.unlockCard(LightningStorm.ID);
        UnlockTracker.unlockCard(DefaultUncommonSkill.ID);
        UnlockTracker.unlockCard(DefaultUncommonAttack.ID);
        UnlockTracker.unlockCard(DefaultUncommonPower.ID);
        UnlockTracker.unlockCard(DefaultRareAttack.ID);
        UnlockTracker.unlockCard(DefaultRareSkill.ID);
        UnlockTracker.unlockCard(DefaultRarePower.ID);
        //UnlockTracker.unlockCard(CodeReview.ID);

        logger.info("Done adding cards!");
    }

    // There are better ways to do this than listing every single individual card, but I do not want to complicate things
    // in a "tutorial" mod. This will do and it's completely ok to use. If you ever want to clean up and
    // shorten all the imports, go look take a look at other mods, such as Hubris.

    // ================ /ADD CARDS/ ===================


    // ================ LOAD THE TEXT ===================

    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings");

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/eng/InterloperMod-Card-Strings.json");

        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/eng/InterloperMod-Power-Strings.json");

        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/eng/InterloperMod-Relic-Strings.json");

        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/eng/InterloperMod-Event-Strings.json");

        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/eng/InterloperMod-Potion-Strings.json");

        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/eng/InterloperMod-Character-Strings.json");

        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                getModID() + "Resources/localization/eng/InterloperMod-Orb-Strings.json");

        logger.info("Done edittting strings");
    }

    // ================ /LOAD THE TEXT/ ===================

    // ================ LOAD THE KEYWORDS ===================

    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword

        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/eng/InterloperMod-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }

    // ================ /LOAD THE KEYWORDS/ ===================    

    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }


}
