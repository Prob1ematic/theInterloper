package theInterloper.modules;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;

public class Border {
    public float width = 245;
    public float height = 184; //Settings.scale?
    public float X;
    public float Y;

    public Border() {
        X = (Settings.WIDTH/2) - (width/2);
        Y = (Settings.HEIGHT/2) - (height/2);
    }

    public void draw(SpriteBatch sb) {
        sb.draw(BattleModule.BORDER_IMG, X, Y, width, height);
    }
}
