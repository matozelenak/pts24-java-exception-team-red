package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfaceToolUse;

import java.util.*;

public class CurrentThrow implements InterfaceCurrentThrow, InterfaceToolUse {

    private Effect throwsFor;
    private int throwResult;
    private InterfaceThrow throwDices;
    private Player player;
    private int throwSum, divBy;
    private boolean used;

    public CurrentThrow(InterfaceThrow throwDices) {
        this.throwDices = throwDices;
        used = false;
    }

    @Override
    public void initiate(Player player, Effect effect, int dices) {
        used = false;
        this.throwsFor = effect;
        this.player = player;

        List<Integer> list = new ArrayList<>();
        int[] res = throwDices.throwDices(dices);
        int sum = 0;
        for (int i = 0; i < dices; i++) {
            sum += res[i];
        }

        int divBy = 1;
        switch (effect) {
            case FOOD:
                divBy = 2;
                break;
            case WOOD:
               divBy = 3;
                break;
            case CLAY:
                divBy = 4;
                break;
            case STONE:
                divBy = 5;
                break;
            case GOLD:
                divBy = 6;
                break;
            default:
                divBy = 1;
                break;
        }

        this.throwSum = sum;
        this.divBy = divBy;
        this.throwResult = sum / divBy;
    }

    @Override
    public int getThrowResult() {
        return throwResult;
    }

    @Override
    public String state() {
        Map<String, String> map = new HashMap<>();
        map.put("throwsFor", throwsFor != null ? throwsFor.toString() : "null");
        map.put("throwResult", throwResult+"");
        map.put("used", used ? "true" : "false");
        return new JSONObject(map).toString();
    }

    @Override
    public boolean useTool(int idx) {
        if (!canUseTools()) return false;

        OptionalInt oint = player.getPlayerBoard().useTool(idx);
        if (oint.isEmpty()) {
            return false;
        }

        this.throwSum += oint.getAsInt();
        this.throwResult = this.throwSum / this.divBy;

        return true;
    }

    @Override
    public boolean canUseTools() {
        return throwsFor.isResourceOrFood();
    }

    @Override
    public boolean finishUsingTools() {
        if (used) return false;
        Effect[] result = new Effect[throwResult];
        for (int i = 0; i < throwResult; i++) {
            result[i] = throwsFor;
        }
        player.getPlayerBoard().giveEffect(result);
        used = true;
        return true;
    }

    public InterfaceThrow getThrowDices() {
        return throwDices;
    }
}
