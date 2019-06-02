package theInterloper.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class NumDisplay {

    public Texture[] images = new Texture[2];
    public static Integer[][] numbers =
            {{1, 1, 1, 0, 1, 1, 1},
                    {0, 1, 0, 0, 1, 0, 0},
                    {1, 1, 0, 1, 0, 1, 1},
                    {1, 1, 0, 1, 1, 0, 1},
                    {0, 1, 1, 1, 1, 0, 0},
                    {1, 0, 1, 1, 1, 0, 1},
                    {1, 0, 1, 1, 1, 1, 1},
                    {1, 1, 0, 0, 1, 0, 0},
                    {1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 0, 1},
                    {0, 0, 0, 0, 0, 0, 0}};

    float dispScale,dispX,dispY,dispW,dispH;
    int dispSrcW = 57;
    int dispSrcH = 20;

    public NumDisplay(char color) {
        switch (color){
            case 'r':
                images[1] = ImageMaster.loadImage("theInterloperResources/images/ui/segOnR.png");
                break;
            case 'b':
                images[1] = ImageMaster.loadImage("theInterloperResources/images/ui/segOnB.png");
                break;
            default:
                images[1] = ImageMaster.loadImage("theInterloperResources/images/ui/segOnG.png");
                break;
        }
        images[0] = ImageMaster.loadImage("theInterloperResources/images/ui/segOff.png");
    }

    public void updateSettings(float x, float y, float scale){
        dispScale = scale;
        dispX = x; //* Settings.scale??
        dispY = y;
        dispW = 2.85f * scale;
        dispH = 1 * scale;
    }

    public void renderDisplay(SpriteBatch sb, int input) {
        sb.setColor(Color.WHITE);
        sb.draw(images[numbers[input][0]], dispX, dispY + dispW,
                dispW / 2, dispH / 2, dispW, dispH,
                1, 1, 0,
                0, 0, dispSrcW, dispSrcH,
                false, false);
        sb.draw(images[numbers[input][1]], dispX + dispH + (0.5f * dispScale), dispY + (dispW / 2),
                dispW / 2, dispH / 2, dispW, dispH,
                1, 1, 90,
                0, 0, dispSrcW, dispSrcH,
                false, false);
        sb.draw(images[numbers[input][2]], dispX - dispH - (0.5f * dispScale), dispY + (dispW / 2),
                dispW / 2, dispH / 2, dispW, dispH,
                1, 1, 90,
                0, 0, dispSrcW, dispSrcH,
                false, false);
        sb.draw(images[numbers[input][3]], dispX, dispY,
                dispW / 2, dispH / 2, dispW, dispH,
                1, 1, 0,
                0, 0, dispSrcW, dispSrcH,
                false, false);
        sb.draw(images[numbers[input][4]], dispX + dispH + (0.5f * dispScale), dispY - (dispW / 2),
                dispW / 2, dispH / 2, dispW, dispH,
                1, 1, 90,
                0, 0, dispSrcW, dispSrcH,
                false, false);
        sb.draw(images[numbers[input][5]], dispX - dispH - (0.5f * dispScale), dispY - (dispW / 2),
                dispW / 2, dispH / 2, dispW, dispH,
                1, 1, 90,
                0, 0, dispSrcW, dispSrcH,
                false, false);
        sb.draw(images[numbers[input][6]], dispX, dispY - dispW,
                dispW / 2, dispH / 2, dispW, dispH,
                1, 1, 0,
                0, 0, dispSrcW, dispSrcH,
                false, false);
    }


}
