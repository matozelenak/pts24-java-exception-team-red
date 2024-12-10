package sk.uniba.fmph.dcs.game_board;

import org.json.JSONTokener;
import sk.uniba.fmph.dcs.stone_age.CivilisationCard;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;
import sk.uniba.fmph.dcs.stone_age.ImmediateEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class GameBoardFactory {

    public static GameBoard createGameBoard(List<Player> players, InterfaceThrow throwDices, CivilizationCardDeck cardDeck, List<BuildingTile> tiles) {
        ToolMakerHutFields toolMakerHutFields = new ToolMakerHutFields();

        CurrentThrow currentThrow = new CurrentThrow(throwDices);
        int maxFigureColorsResourceSource;
        switch (players.get(0).getPlayerOrder().getPlayers()) {
            case 3:
                maxFigureColorsResourceSource = 2;
                break;
            case 2:
                maxFigureColorsResourceSource = 1;
                break;
            default:
                maxFigureColorsResourceSource = 4;
                break;
        }
        ResourceSource forest = new ResourceSource(currentThrow, "Forest", Effect.WOOD, 7, maxFigureColorsResourceSource);
        ResourceSource clay = new ResourceSource(currentThrow, "Clay mould", Effect.CLAY, 7, maxFigureColorsResourceSource);
        ResourceSource quarry = new ResourceSource(currentThrow, "Quarry", Effect.STONE, 7, maxFigureColorsResourceSource);
        ResourceSource river = new ResourceSource(currentThrow, "River", Effect.GOLD, 7, maxFigureColorsResourceSource);
        ResourceSource huntingGrounds = new ResourceSource(currentThrow, "Hunting Grounds", Effect.FOOD, Integer.MAX_VALUE, Integer.MAX_VALUE);

        RewardMenu rewardMenu = new RewardMenu(players);
        CivilizationCardPlace civilizationCardPlace1 = new CivilizationCardPlace(1, cardDeck, currentThrow, rewardMenu, throwDices);
        CivilizationCardPlace civilizationCardPlace2 = new CivilizationCardPlace(2, cardDeck, currentThrow, rewardMenu, throwDices);
        CivilizationCardPlace civilizationCardPlace3 = new CivilizationCardPlace(3, cardDeck, currentThrow, rewardMenu, throwDices);
        CivilizationCardPlace civilizationCardPlace4 = new CivilizationCardPlace(4, cardDeck, currentThrow, rewardMenu, throwDices);
        civilizationCardPlace1.setup(null, civilizationCardPlace2);
        civilizationCardPlace2.setup(civilizationCardPlace1, civilizationCardPlace3);
        civilizationCardPlace3.setup(civilizationCardPlace2, civilizationCardPlace4);
        civilizationCardPlace4.setup(civilizationCardPlace3, null);

        BuildingTile buildingTile1 = tiles.size() > 0 ? tiles.get(0) : null;
        BuildingTile buildingTile2 = tiles.size() > 1 ? tiles.get(1) : null;
        BuildingTile buildingTile3 = tiles.size() > 2 ? tiles.get(2) : null;
        BuildingTile buildingTile4 = tiles.size() > 3 ? tiles.get(3) : null;

        return new GameBoard(players, toolMakerHutFields, forest, clay, quarry, river, huntingGrounds, currentThrow,
                civilizationCardPlace1, civilizationCardPlace2, civilizationCardPlace3, civilizationCardPlace4,
                cardDeck, rewardMenu, buildingTile1, buildingTile2, buildingTile3, buildingTile4);
    }

    // used in GameBoardIntegrationTest
    public static CivilizationCardDeck createCardDeck1() {
        Stack<CivilisationCard> stack = new Stack<>();
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.Gold}, new EndOfGameEffect[]{EndOfGameEffect.Music}));
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.Food}, new EndOfGameEffect[]{EndOfGameEffect.Writing}));
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.Point}, new EndOfGameEffect[]{EndOfGameEffect.Builder}));
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.AllPlayersTakeReward}, new EndOfGameEffect[]{EndOfGameEffect.Shaman}));
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.Gold}, new EndOfGameEffect[]{EndOfGameEffect.Medicine}));
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.ArbitraryResource}, new EndOfGameEffect[]{EndOfGameEffect.Art}));
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.Card}, new EndOfGameEffect[]{EndOfGameEffect.Farmer}));
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.ThrowStone}, new EndOfGameEffect[]{EndOfGameEffect.ToolMaker}));
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.Wood}, new EndOfGameEffect[]{EndOfGameEffect.Pottery}));
        return new CivilizationCardDeck(stack);
    }

    // used in StoneAgeIntegrationTest
    public static CivilizationCardDeck createCardDeck2() {
        Stack<CivilisationCard> stack = new Stack<>();
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.Gold}, new EndOfGameEffect[]{EndOfGameEffect.Medicine}));
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.Food}, new EndOfGameEffect[]{EndOfGameEffect.Writing}));
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.Wood}, new EndOfGameEffect[]{EndOfGameEffect.Shaman, EndOfGameEffect.Shaman}));
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.ThrowStone}, new EndOfGameEffect[]{EndOfGameEffect.ToolMaker, EndOfGameEffect.ToolMaker}));
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.ThrowGold}, new EndOfGameEffect[]{EndOfGameEffect.Farmer, EndOfGameEffect.Farmer}));
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.Point}, new EndOfGameEffect[]{EndOfGameEffect.Builder}));
        return new CivilizationCardDeck(stack);
    }

    // used in StoneAgeIntegrationTest
    public static CivilizationCardDeck createCardDeck3() {
        Stack<CivilisationCard> stack = new Stack<>();
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.Stone}, new EndOfGameEffect[]{EndOfGameEffect.Pottery}));
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.Gold}, new EndOfGameEffect[]{EndOfGameEffect.Medicine}));
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.Point}, new EndOfGameEffect[]{EndOfGameEffect.Writing}));
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.ThrowClay}, new EndOfGameEffect[]{EndOfGameEffect.Shaman, EndOfGameEffect.Shaman}));
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.Card}, new EndOfGameEffect[]{EndOfGameEffect.ToolMaker, EndOfGameEffect.ToolMaker}));
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.AllPlayersTakeReward}, new EndOfGameEffect[]{EndOfGameEffect.Farmer, EndOfGameEffect.Farmer}));
        stack.push(new CivilisationCard(new ImmediateEffect[] {ImmediateEffect.ArbitraryResource}, new EndOfGameEffect[]{EndOfGameEffect.Builder}));
        return new CivilizationCardDeck(stack);
    }

    public static List<BuildingTile> createBuildingTiles1_4Players() { // TODO when BuildingTile fix merged
        List<BuildingTile> buildingTiles = new ArrayList<>();
        // TODO cakat kym sa opravi BuildingTile
        buildingTiles.add(new BuildingTile(new ArrayList<>(), new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.WOOD, Effect.CLAY)))));
        buildingTiles.add(new BuildingTile(new ArrayList<>(), new VariableBuilding(4, 2)));
        buildingTiles.add(new BuildingTile(new ArrayList<>(), new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.STONE, Effect.CLAY)))));
        buildingTiles.add(new BuildingTile(new ArrayList<>(), new ArbitraryBuilding(7)));

        return buildingTiles;
    }

    public static List<BuildingTile> createBuildingTiles2_2Players() {
        List<BuildingTile> buildingTiles = new ArrayList<>();
        buildingTiles.add(new BuildingTile(new ArrayList<>(), new ArbitraryBuilding(7)));
        buildingTiles.add(new BuildingTile(new ArrayList<>(), new VariableBuilding(4, 2)));
        buildingTiles.add(new BuildingTile(new ArrayList<>(), new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.WOOD, Effect.CLAY)))));
        buildingTiles.add(new BuildingTile(new ArrayList<>(), new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.CLAY)))));

        return buildingTiles;
    }


    public static class ThrowMock implements InterfaceThrow {
        public int[] result = new int[]{};
        @Override
        public int[] throwDices(int dices) {
            return result;
        }
    }
}
