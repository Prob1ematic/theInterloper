package theInterloper.modules;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Pattern {

    public List<Projectile> projectiles;
    public List<Integer> timers;

    public Pattern(AbstractMonster monster, PlayerIndicator player) {
        Random random = new Random();
        int X, Y, dmg;
        this.projectiles = new ArrayList<>();
        this.timers =  new ArrayList<>();
        switch (monster.intent) {
            case ATTACK:
            case ATTACK_BUFF:
            case ATTACK_DEBUFF:
            case ATTACK_DEFEND:
            case BUFF:
            case DEBUFF:
            case STRONG_DEBUFF:
            case DEBUG:
            case DEFEND:
            case DEFEND_DEBUFF:
            case DEFEND_BUFF:
            case ESCAPE:
            case MAGIC:
            case NONE:
            case SLEEP:
            case STUN:
            case UNKNOWN:
            default:
                dmg = 0;
                for (DamageInfo attack : monster.damage) {
                    dmg += attack.output;
                }
                dmg = dmg/2;
                for (int c=0; c < 300; c+=20){
                    if(random.nextBoolean()){
                        X = 0;
                        if(random.nextBoolean()) {
                            Y = 92;
                        }else {
                            Y = -92;
                        }
                    }else{
                        Y = 0;
                        if(random.nextBoolean()) {
                            X = 122;
                        }else {
                            X = -122;
                        }
                    }
                    projectiles.add(new Projectile(X,Y, player, monster, dmg));
                    timers.add(c);
                }
                break;
        }
    }

}
