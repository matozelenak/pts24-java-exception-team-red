package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;

import java.util.Map;

public class PlayerBoard implements InterfaceGetState {

    private int points;
    private int houses;
    private PlayerCivilisationCards cards;
    private PlayerTools tools;
    private InterfacePlayerResourcesAndFood resourcesAndFood;
    private InterfaceTribeFedStatus fedStatus;
    private InterfacePlayerFigures figures;


    public PlayerBoard(
            int points,
            int houses,
            PlayerCivilisationCards cards,
            PlayerTools tools,
            InterfacePlayerResourcesAndFood resourcesAndFood,
            InterfaceTribeFedStatus fedStatus,
            InterfacePlayerFigures figures
    ) {
        // Initial situation when the game begins
        this.points = points;
        this.houses = houses;
        this.cards = cards;
        this.tools = tools;
        this.resourcesAndFood = resourcesAndFood;
        this.fedStatus = fedStatus;
        this.figures = figures;
    }

    public int getPoints() {
        return points;
    }

    public int getHouses() {
        return houses;
    }

    public PlayerCivilisationCards getPlayerCivilisationCards() {
        return cards;
    }

    public PlayerTools getPlayerTools() {
        return tools;
    }

    public PlayerResourcesAndFood getPlayerResourcesAndFood() {
        return (PlayerResourcesAndFood) resourcesAndFood;
    }

    public TribeFedStatus getTribeFedStatus() {
        return (TribeFedStatus) fedStatus;
    }

    public PlayerFigures getPlayerFigures() {
        return (PlayerFigures) figures;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void addHouse() {
        this.houses++;
    }

    public void addEndOfGamePoints() {
        points += cards.calculateEndOfGameCivilisationCardPoints(
            houses,
            tools.getToolCount(),
            fedStatus.getFields(),
            figures.getTotalFigures()
        );
        points += resourcesAndFood.numberOfResourcesForFinalPoints();
    }

    @Override
    public String state() {
        JSONObject obj = new JSONObject();
        obj.put("points", String.valueOf(points));
        obj.put("houses", String.valueOf(houses));
        obj.put("civilisation cards", new JSONObject(cards.state()));
        obj.put("tools", new JSONObject(tools.state()));
        obj.put("resources and food", new JSONObject(resourcesAndFood.state()));
        obj.put("tribe fed status", new JSONObject(fedStatus.state()));
        obj.put("figures", new JSONObject(figures.state()));
        return obj.toString();
    }
}
