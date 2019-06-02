package theInterloper.relics;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theInterloper.InterloperMod;
import theInterloper.patches.BottledPlaceholderField;
import theInterloper.util.TextureLoader;

import java.util.function.Predicate;

import static theInterloper.InterloperMod.makeRelicOutlinePath;
import static theInterloper.InterloperMod.makeRelicPath;

public class RefactoringTask extends CustomRelic implements CustomBottleRelic, CustomSavable<Integer> {

    private static AbstractCard card;
    private boolean cardSelected = true;

    public static final String ID = InterloperMod.makeID("RefactoringTask");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("RefactoringTask.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("RefactoringTask.png"));

    public RefactoringTask() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.CLINK);
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public Predicate<AbstractCard> isOnCard() {
        return BottledPlaceholderField.inRefactoringTaskField::get;
    }

    @Override
    public Integer onSave() {
        if (card != null) {
            return AbstractDungeon.player.masterDeck.group.indexOf(card);
        } else {
            return -1;
        }
    }

    @Override
    public void onLoad(Integer cardIndex) {
        if (cardIndex == null) {
            return;
        }
        if (cardIndex >= 0 && cardIndex < AbstractDungeon.player.masterDeck.group.size()) {
            card = AbstractDungeon.player.masterDeck.group.get(cardIndex);
            if (card != null) {
                BottledPlaceholderField.inRefactoringTaskField.set(card, true);
                setDescriptionAfterLoading();
            }
        }
    }


    @Override
    public void onEquip() {
        cardSelected = false;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        CardGroup group = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck); // 5. Get a card group of all currently unbottled cards
        AbstractDungeon.gridSelectScreen.open(group, 1, DESCRIPTIONS[3] + name + DESCRIPTIONS[2], false, false, false, false);
    }


    @Override
    public void onUnequip() {
        if (card != null) {
            AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(card);
            if (cardInDeck != null) {
                BottledPlaceholderField.inRefactoringTaskField.set(cardInDeck, false);
            }
        }
    }

    @Override
    public void update() {
        super.update();

        if (!cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            cardSelected = true;
            card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            BottledPlaceholderField.inRefactoringTaskField.set(card, true);
            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.INCOMPLETE) {
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            setDescriptionAfterLoading();
        }
    }

    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if (BottledPlaceholderField.inRefactoringTaskField.get(targetCard)) {
            this.flash();
            int random = (int)(Math.random() * 5);
            if(random > 0) {
                if (targetCard.cost > 0) {
                    targetCard.modifyCostForCombat(targetCard.cost - 1);
                } else {
                    if (targetCard.baseDamage != -1) {
                        targetCard.baseDamage += targetCard.baseDamage;
                    }
                    if (targetCard.baseBlock != -1) {
                        targetCard.baseBlock += targetCard.baseBlock;
                    }
                    if (targetCard.baseMagicNumber != -1) {
                        targetCard.baseMagicNumber += targetCard.baseMagicNumber;
                        targetCard.magicNumber = targetCard.baseMagicNumber;
                    }
                }
            }else{
                if(targetCard.baseDamage != -1){
                    targetCard.baseDamage = targetCard.baseDamage/2;
                }
                if(targetCard.baseBlock != -1){
                    targetCard.baseBlock = targetCard.baseBlock/2;
                }
                if(targetCard.baseMagicNumber != -1){
                    targetCard.baseMagicNumber = targetCard.baseMagicNumber/2;
                    targetCard.magicNumber = targetCard.baseMagicNumber;
                }
            }
            targetCard.initializeDescription();
        }
    }

    public void setDescriptionAfterLoading() {
        this.description = DESCRIPTIONS[1] + FontHelper.colorString(card.name, "y") + DESCRIPTIONS[2];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
