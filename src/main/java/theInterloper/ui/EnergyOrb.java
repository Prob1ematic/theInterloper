package theInterloper.ui;

import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;

//CODE BASED ON RUNESMITH CUSTOM ENERGY ORB BY BLIZZARRE

public class EnergyOrb extends CustomEnergyOrb {
        private Texture ENERGY_LAYER1 = ImageMaster.loadImage("theInterloperResources/images/ui/orb/1.png");
        private Texture ENERGY_LAYER2 = ImageMaster.loadImage("theInterloperResources/images/ui/orb/2.png");
        private Texture ENERGY_LAYER3 = ImageMaster.loadImage("theInterloperResources/images/ui/orb/3.png");
        private Texture ENERGY_LAYER4 = ImageMaster.loadImage("theInterloperResources/images/ui/orb/4.png");
        private Texture ENERGY_LAYER1D = ImageMaster.loadImage("theInterloperResources/images/ui/orb/1d.png");
        private Texture ENERGY_LAYER2D = ImageMaster.loadImage("theInterloperResources/images/ui/orb/2d.png");
        private Texture ENERGY_LAYER3D = ImageMaster.loadImage("theInterloperResources/images/ui/orb/3d.png");
        private Texture ENERGY_LAYER4D = ImageMaster.loadImage("theInterloperResources/images/ui/orb/4d.png");
        private Texture ENERGY_BACKGROUND = ImageMaster.loadImage("theInterloperResources/images/ui/orb/5.png");
        private Texture ENERGY_BORDER = ImageMaster.loadImage("theInterloperResources/images/ui/orb/border.png");
        private Texture ENERGY_BORDER_D = ImageMaster.loadImage("theInterloperResources/images/ui/orb/borderD.png");
        private Texture ENERGY_MASK = ImageMaster.loadImage("theInterloperResources/images/ui/orb/mask.png");
        private static final float ORB_SCALE;
        private float angle4 = 0.0F;
        private float angle3 = 0.0F;
        private float angle2 = 0.0F;
        private float angle1 = 0.0F;
        private FrameBuffer fbo;

        public EnergyOrb() {
            super((String[]) null, (String) null, (float[]) null);
            this.fbo = new FrameBuffer(Format.RGBA8888, Settings.M_W, Settings.M_H, false, false);
        }

        public void updateOrb(int orbCount) {
            if (orbCount == 0) {
                this.angle1 += Gdx.graphics.getDeltaTime() * -8.0F;
                this.angle2 += Gdx.graphics.getDeltaTime() * 5.0F;
                this.angle3 += Gdx.graphics.getDeltaTime() * -4.0F;
                this.angle4 += Gdx.graphics.getDeltaTime() * 8.0F;
            } else {
                this.angle1 += Gdx.graphics.getDeltaTime() * -40.0F;
                this.angle2 += Gdx.graphics.getDeltaTime() * 20.0F;
                this.angle3 += Gdx.graphics.getDeltaTime() * -16.0F;
                this.angle4 += Gdx.graphics.getDeltaTime() * 40.0F;
            }

        }

        public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
                float X_OFFSET = -128.0F;
                float Y_OFFSET = -125.0F;
                sb.setColor(Color.WHITE);
                sb.end();

                sb.begin();
                sb.draw(this.ENERGY_BACKGROUND, current_x - 128.0F - Settings.VERT_LETTERBOX_AMT, current_y - 128.0F - Settings.HORIZ_LETTERBOX_AMT, 128.0F, 128.0F, 256.0F, 256.0F, ORB_SCALE, ORB_SCALE, 0.0F, 0, 0, 256, 256, false, false);
                sb.end();

                this.fbo.begin();
                {
                    Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                    Gdx.gl.glColorMask(true, true, true, true);
                    sb.begin();
                    {
                        sb.setColor(Color.WHITE);
                        sb.setBlendFunction(-1, -1);//disable spritebatch blending override
                        Gdx.gl.glBlendFuncSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
                        if (enabled) {
                            sb.draw(this.ENERGY_LAYER1, current_x + X_OFFSET - Settings.VERT_LETTERBOX_AMT, current_y + Y_OFFSET - Settings.HORIZ_LETTERBOX_AMT, 128.0F, 128.0F, 256.0F, 256.0F, ORB_SCALE, ORB_SCALE, this.angle1, 0, 0, 256, 256, false, false);
                            sb.draw(this.ENERGY_LAYER2, current_x + X_OFFSET - Settings.VERT_LETTERBOX_AMT, current_y + Y_OFFSET - Settings.HORIZ_LETTERBOX_AMT, 128.0F, 128.0F, 256.0F, 256.0F, ORB_SCALE, ORB_SCALE, this.angle2, 0, 0, 256, 256, false, false);
                            sb.draw(this.ENERGY_LAYER3, current_x + X_OFFSET - Settings.VERT_LETTERBOX_AMT, current_y + Y_OFFSET - Settings.HORIZ_LETTERBOX_AMT, 128.0F, 128.0F, 256.0F, 256.0F, ORB_SCALE, ORB_SCALE, this.angle3, 0, 0, 256, 256, false, false);
                            sb.setBlendFunction(770, 1);
                            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.5F));
                            sb.draw(this.ENERGY_LAYER4, current_x + X_OFFSET - Settings.VERT_LETTERBOX_AMT, current_y + Y_OFFSET - Settings.HORIZ_LETTERBOX_AMT, 128.0F, 128.0F, 256.0F, 256.0F, ORB_SCALE, ORB_SCALE, this.angle4, 0, 0, 256, 256, false, false);
                        } else {
                            sb.draw(this.ENERGY_LAYER1D, current_x + X_OFFSET - Settings.VERT_LETTERBOX_AMT, current_y + Y_OFFSET - Settings.HORIZ_LETTERBOX_AMT, 128.0F, 128.0F, 256.0F, 256.0F, ORB_SCALE, ORB_SCALE, this.angle1, 0, 0, 256, 256, false, false);
                            sb.draw(this.ENERGY_LAYER2D, current_x + X_OFFSET - Settings.VERT_LETTERBOX_AMT, current_y + Y_OFFSET - Settings.HORIZ_LETTERBOX_AMT, 128.0F, 128.0F, 256.0F, 256.0F, ORB_SCALE, ORB_SCALE, this.angle2, 0, 0, 256, 256, false, false);
                            sb.draw(this.ENERGY_LAYER3D, current_x + X_OFFSET - Settings.VERT_LETTERBOX_AMT, current_y + Y_OFFSET - Settings.HORIZ_LETTERBOX_AMT, 128.0F, 128.0F, 256.0F, 256.0F, ORB_SCALE, ORB_SCALE, this.angle3, 0, 0, 256, 256, false, false);
                            sb.draw(this.ENERGY_LAYER4D, current_x + X_OFFSET - Settings.VERT_LETTERBOX_AMT, current_y + Y_OFFSET - Settings.HORIZ_LETTERBOX_AMT, 128.0F, 128.0F, 256.0F, 256.0F, ORB_SCALE, ORB_SCALE, this.angle4, 0, 0, 256, 256, false, false);
                        }
                        sb.setBlendFunction(0, 770);
                        sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
                        sb.draw(this.ENERGY_MASK, current_x - 128.0F - Settings.VERT_LETTERBOX_AMT, current_y - 128.0F - Settings.HORIZ_LETTERBOX_AMT, 128.0F, 128.0F, 256.0F, 256.0F, ORB_SCALE, ORB_SCALE, 0.0F, 0, 0, 256, 256, false, false);
                        sb.setBlendFunction(770, 771);
                    }
                    sb.end();
                }
                this.fbo.end();

                sb.begin();
                TextureRegion drawTex = new TextureRegion(this.fbo.getColorBufferTexture());
                drawTex.flip(false, true);
                sb.draw(drawTex, 0.0F - Settings.VERT_LETTERBOX_AMT, 0.0F - Settings.HORIZ_LETTERBOX_AMT);
                if (enabled) {
                    sb.draw(this.ENERGY_BORDER, current_x - 128.0F - Settings.VERT_LETTERBOX_AMT, current_y - 128.0F - Settings.HORIZ_LETTERBOX_AMT, 128.0F, 128.0F, 256.0F, 256.0F, ORB_SCALE, ORB_SCALE, 0.0F, 0, 0, 256, 256, false, false);
                } else {
                    sb.draw(this.ENERGY_BORDER_D, current_x - 128.0F - Settings.VERT_LETTERBOX_AMT, current_y - 128.0F - Settings.HORIZ_LETTERBOX_AMT, 128.0F, 128.0F, 256.0F, 256.0F, ORB_SCALE, ORB_SCALE, 0.0F, 0, 0, 256, 256, false, false);
                }
        }

        static {
            ORB_SCALE = 1.15F * Settings.scale;
        }

}
