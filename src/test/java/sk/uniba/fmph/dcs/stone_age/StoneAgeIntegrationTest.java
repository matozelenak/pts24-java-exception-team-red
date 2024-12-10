package sk.uniba.fmph.dcs.stone_age;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.game_board.CurrentThrow;
import sk.uniba.fmph.dcs.game_board.GameBoard;
import sk.uniba.fmph.dcs.game_board.GameBoardFactory;
import sk.uniba.fmph.dcs.game_board.Player;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;

import static org.junit.Assert.*;

public class StoneAgeIntegrationTest {

    private StoneAgeGame game;
    private GameBoardFactory.ThrowMock throwMock;


    @Test
    public void test1() {
        game = StoneAgeFactory.createStoneAge(4, GameBoardFactory.createCardDeck2(), GameBoardFactory.createBuildingTiles1_4Players());
        throwMock = (GameBoardFactory.ThrowMock) ((CurrentThrow)((GameBoard) game.getGameBoard()).getCurrentThrow()).getThrowDices();

        // dat kazdemu 9 FOOD
        Effect[] food = new Effect[9];
        for (int i = 0; i < 9; i++)
            food[i] = Effect.FOOD;
        GameBoard gameBoard = ((GameBoard)game.getGameBoard());
        gameBoard.getPlayers().get(0).getPlayerBoard().giveEffect(food);
        gameBoard.getPlayers().get(1).getPlayerBoard().giveEffect(food);
        gameBoard.getPlayers().get(2).getPlayerBoard().giveEffect(food);
        gameBoard.getPlayers().get(3).getPlayerBoard().giveEffect(food);

        // placing figures
        assertTrue(game.placeFigures(0, Location.FOREST, 3));
        assertTrue(game.placeFigures(1, Location.QUARY, 3));
        assertTrue(game.placeFigures(2, Location.FIELD, 1));
        assertTrue(game.placeFigures(3, Location.TOOL_MAKER, 1));

        assertTrue(game.placeFigures(0, Location.HUT, 2));
        assertTrue(game.placeFigures(1, Location.CLAY_MOUND, 2));
        assertTrue(game.placeFigures(2, Location.HUNTING_GROUNDS, 4));
        assertTrue(game.placeFigures(3, Location.RIVER, 4));

        assertEquals("0", getPlayerFigures(0).get("figures"));
        assertEquals("0", getPlayerFigures(1).get("figures"));
        assertEquals("0", getPlayerFigures(2).get("figures"));
        assertEquals("0", getPlayerFigures(3).get("figures"));

        Effect[] usedResources;
        Effect[] desiredResources;

        // player 0 makeAction HUT
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        assertTrue(game.makeAction(0, Location.HUT, usedResources, desiredResources));
        assertEquals("6", getPlayerFigures(0).get("totalFigures"));
        assertEquals("5",getPlayerFigures(1).get("totalFigures"));

        // player 0 makeAction FOREST
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        throwMock.result = new int[] {6, 6, 5};
        assertTrue(game.makeAction(0, Location.FOREST, usedResources, desiredResources));
        assertTrue(game.noMoreToolsThisThrow(0));
        assertEquals("5", getPlayerRes(0).get("WOOD"));


        // player 1 makeAction QUARRY
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        throwMock.result = new int[] {6, 6, 5};
        assertTrue(game.makeAction(1, Location.QUARY, usedResources, desiredResources));
        assertTrue(game.noMoreToolsThisThrow(1));
        assertEquals("3", getPlayerRes(1).get("STONE"));

        // player 1 makeAction CLAY
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        throwMock.result = new int[] {6, 6};
        assertTrue(game.makeAction(1, Location.CLAY_MOUND, usedResources, desiredResources));
        assertTrue(game.noMoreToolsThisThrow(1));
        assertEquals("3", getPlayerRes(1).get("CLAY"));

        // player 2 makeAction FIELD
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        assertTrue(game.makeAction(2, Location.FIELD, usedResources, desiredResources));
        assertEquals("1", getTribeFed(2).get("fields"));

        // player 2 makeAction HUNTING
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        throwMock.result = new int[] {2, 2, 2, 2};
        assertTrue(game.makeAction(2, Location.HUNTING_GROUNDS, usedResources, desiredResources));
        assertTrue(game.noMoreToolsThisThrow(2));
        assertEquals("13", getPlayerRes(2).get("FOOD"));

        // player 3 makeAction TOOLMAKER
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        assertTrue(game.makeAction(3, Location.TOOL_MAKER, usedResources, desiredResources));
        assertEquals("1 unused", getPlayerTools(3).get("tool slot 0"));

        // player 3 makeAction RIVER
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        throwMock.result = new int[] {5, 4, 3, 5};
        assertTrue(game.makeAction(3, Location.RIVER, usedResources, desiredResources));
        assertTrue(game.useTools(3, 0));
        assertFalse(game.useTools(3, 0));
        assertTrue(game.noMoreToolsThisThrow(3));
        assertEquals("3", getPlayerRes(3).get("GOLD"));

        // automatic tribe feeding should happen now
        assertEquals("3", getPlayerRes(0).get("FOOD"));
        assertEquals("4", getPlayerRes(1).get("FOOD"));
        assertEquals("9", getPlayerRes(2).get("FOOD")); // 5 figures, 1 field, has 13 FOOD -> took 4 food
        assertEquals("4", getPlayerRes(3).get("FOOD"));


        // ROUND 2
        // placing figures
        assertTrue(game.placeFigures(1, Location.CIVILISATION_CARD1, 1));
        assertTrue(game.placeFigures(2, Location.CIVILISATION_CARD2, 1));
        assertTrue(game.placeFigures(3, Location.CIVILISATION_CARD3, 1));
        assertTrue(game.placeFigures(0, Location.CIVILISATION_CARD4, 1));

        assertTrue(game.placeFigures(1, Location.RIVER, 2));
        assertTrue(game.placeFigures(2, Location.FIELD, 1));
        assertTrue(game.placeFigures(3, Location.TOOL_MAKER, 1));
        assertTrue(game.placeFigures(0, Location.HUT, 2));

        assertTrue(game.placeFigures(1, Location.CLAY_MOUND, 2));
        assertTrue(game.placeFigures(2, Location.FOREST, 3));
        assertTrue(game.placeFigures(3, Location.QUARY, 3));
        assertTrue(game.placeFigures(0, Location.HUNTING_GROUNDS, 3));

        // player 1 makeAction RIVER
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        throwMock.result = new int[] {6, 6};
        assertTrue(game.makeAction(1, Location.RIVER, usedResources, desiredResources));
        assertTrue(game.noMoreToolsThisThrow(1));
        assertEquals("2", getPlayerRes(1).get("GOLD"));

        // player 1 makeAction CLAY
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        throwMock.result = new int[] {6, 6};
        assertTrue(game.makeAction(1, Location.CLAY_MOUND, usedResources, desiredResources));
        assertTrue(game.noMoreToolsThisThrow(1));
        assertEquals("6", getPlayerRes(1).get("CLAY"));

        // player 1 makeAction CIVILISATIONCARD1
        usedResources = new Effect[] {Effect.CLAY};
        desiredResources = new Effect[] {};
        assertTrue(game.makeAction(1, Location.CIVILISATION_CARD1, usedResources, desiredResources));
        assertEquals("1", getPlayerBoardState(1).get("points"));
        assertEquals("1", getCivCards(1).get("builders"));

        // player 2 makeAction FIELD
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        assertTrue(game.makeAction(2, Location.FIELD, usedResources, desiredResources));
        assertEquals("2", getTribeFed(2).get("fields"));

        // player 2 makeAction WOOD
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        throwMock.result = new int[] {6, 4, 4};
        assertTrue(game.makeAction(2, Location.FOREST, usedResources, desiredResources));
        assertTrue(game.noMoreToolsThisThrow(2));
        assertEquals("4", getPlayerRes(2).get("WOOD"));

        // player 2 makeAction CIVILISATIONCARD2
        usedResources = new Effect[] {Effect.WOOD, Effect.WOOD};
        desiredResources = new Effect[] {};
        throwMock.result = new int[] {4, 4};
        assertTrue(game.makeAction(2, Location.CIVILISATION_CARD2, usedResources, desiredResources));
        assertTrue(game.noMoreToolsThisThrow(2));
        assertEquals("1", getPlayerRes(2).get("GOLD"));
        assertEquals("2", getCivCards(2).get("farmers"));

        // player 3 makeAction TOOLMAKER
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        assertTrue(game.makeAction(3, Location.TOOL_MAKER, usedResources, desiredResources));
        assertEquals("1 unused", getPlayerTools(3).get("tool slot 1"));

        // player 3 makeAction STONE
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        throwMock.result = new int[] {6, 4, 3};
        assertTrue(game.makeAction(3, Location.QUARY, usedResources, desiredResources));
        assertTrue(game.useTools(3, 0));
        assertTrue(game.useTools(3, 1));
        assertTrue(game.noMoreToolsThisThrow(3));
        assertEquals("3", getPlayerRes(3).get("STONE"));

        // player 3 makeAction CIVILISATIONCARD3
        usedResources = new Effect[] {Effect.STONE, Effect.STONE, Effect.STONE};
        desiredResources = new Effect[] {};
        throwMock.result = new int[] {4, 4};
        assertTrue(game.makeAction(3, Location.CIVILISATION_CARD3, usedResources, desiredResources));
        assertTrue(game.noMoreToolsThisThrow(3));
        assertEquals("1", getPlayerRes(3).get("STONE"));
        assertEquals("2", getCivCards(3).get("toolmakers"));

        // player 0 makeAction HUT
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        assertTrue(game.makeAction(0, Location.HUT, usedResources, desiredResources));
        assertEquals("7", getPlayerFigures(0).get("totalFigures"));

        // player 0 makeAction HUNTING
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        throwMock.result = new int[] {6, 4, 3};
        assertTrue(game.makeAction(0, Location.HUNTING_GROUNDS, usedResources, desiredResources));
        assertTrue(game.noMoreToolsThisThrow(0));
        assertEquals("9", getPlayerRes(0).get("FOOD"));

        // player 0 makeAction CIVILISATIONCARD4
        usedResources = new Effect[] {Effect.WOOD, Effect.WOOD, Effect.WOOD, Effect.WOOD};
        desiredResources = new Effect[] {};
        assertTrue(game.makeAction(0, Location.CIVILISATION_CARD4, usedResources, desiredResources));
        assertEquals("2", getPlayerRes(0).get("WOOD"));
        assertEquals("2", getCivCards(0).get("shamans"));

        // players 1 and 3 don't have enough food
        assertTrue(game.feedTribe(1, new Effect[] {Effect.CLAY})); // player 1 missing 1 FOOD
        assertTrue(game.feedTribe(3, new Effect[] {Effect.STONE})); // player 3 missing 1 FOOD

        // not enough civilisation cards to fill empty spaces, so game ends

        // GAME END
        assertEquals("GAME_END", getControllerState().get("game phase"));

        for (Player player : ((GameBoard) game.getGameBoard()).getPlayers())
            ((PlayerBoardGameBoardFacade)player.getPlayerBoard()).addEndOfGamePoints();

//        printState();

        assertEquals("16", getPlayerBoardState(0).get("points"));
        assertEquals("10", getPlayerBoardState(1).get("points"));
        assertEquals("7", getPlayerBoardState(2).get("points"));
        assertEquals("7", getPlayerBoardState(3).get("points"));
    }

    @Test
    public void test2_2Players() {
        game = StoneAgeFactory.createStoneAge(2, GameBoardFactory.createCardDeck3(), GameBoardFactory.createBuildingTiles2_2Players());
        throwMock = (GameBoardFactory.ThrowMock) ((CurrentThrow)((GameBoard) game.getGameBoard()).getCurrentThrow()).getThrowDices();

        // dat kazdemu 9 FOOD
        Effect[] food = new Effect[9];
        for (int i = 0; i < 9; i++)
            food[i] = Effect.FOOD;
        GameBoard gameBoard = ((GameBoard)game.getGameBoard());
        gameBoard.getPlayers().get(0).getPlayerBoard().giveEffect(food);
        gameBoard.getPlayers().get(1).getPlayerBoard().giveEffect(food);


        // placing figures
        assertTrue(game.placeFigures(0, Location.CIVILISATION_CARD1, 1));
        assertTrue(game.placeFigures(1, Location.CIVILISATION_CARD3, 1));

        assertTrue(game.placeFigures(0, Location.BUILDING_TILE1, 1));
        assertTrue(game.placeFigures(1, Location.BUILDING_TILE2, 1));

        assertTrue(game.placeFigures(0, Location.CLAY_MOUND, 1));
        assertTrue(game.placeFigures(1, Location.FOREST, 1));

        assertTrue(game.placeFigures(0, Location.HUT, 2));
        assertTrue(game.placeFigures(1, Location.QUARY, 2));


        Effect[] usedResources;
        Effect[] desiredResources;

        // player 0 makeAction CLAY
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        throwMock.result = new int[] {6};
        assertTrue(game.makeAction(0, Location.CLAY_MOUND, usedResources, desiredResources));
        assertTrue(game.noMoreToolsThisThrow(0));
        assertEquals("1", getPlayerRes(0).get("CLAY"));

        // player 0 makeAction HUT
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        assertTrue(game.makeAction(0, Location.HUT, usedResources, desiredResources));
        assertEquals("6", getPlayerFigures(0).get("totalFigures"));

        // player 0 makeAction BUILDING 1
        usedResources = new Effect[] {Effect.CLAY};
        desiredResources = new Effect[] {};
        assertTrue(game.makeAction(0, Location.BUILDING_TILE1, usedResources, desiredResources));
        assertEquals("1", getPlayerBoardState(0).get("houses"));

        // player 0 makeAction CIVILISATIONCARD 1 arbitrary
        gameBoard.getPlayers().get(0).getPlayerBoard().giveEffect(new Effect[] {Effect.CLAY}); // for civ card
        usedResources = new Effect[] {Effect.CLAY};
        desiredResources = new Effect[] {Effect.GOLD, Effect.STONE};
        assertTrue(game.makeAction(0, Location.CIVILISATION_CARD1, usedResources, desiredResources));
        assertEquals("1", getCivCards(0).get("builders"));
        assertEquals("1", getPlayerRes(0).get("GOLD"));
        assertEquals("1", getPlayerRes(0).get("STONE"));

        // player 1 makeAction FOREST
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        throwMock.result = new int[] {6};
        assertTrue(game.makeAction(1, Location.FOREST, usedResources, desiredResources));
        assertTrue(game.noMoreToolsThisThrow(1));
        assertEquals("2", getPlayerRes(1).get("WOOD"));

        // player 1 makeAction QUARRY
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        throwMock.result = new int[] {6, 5};
        assertTrue(game.makeAction(1, Location.QUARY, usedResources, desiredResources));
        assertTrue(game.noMoreToolsThisThrow(1));
        assertEquals("2", getPlayerRes(1).get("STONE"));

        // player 1 makeAction BUILDING 2
        usedResources = new Effect[] {Effect.WOOD, Effect.WOOD, Effect.STONE, Effect.STONE};
        desiredResources = new Effect[] {};
        assertTrue(game.makeAction(1, Location.BUILDING_TILE2, usedResources, desiredResources));
        assertEquals("1", getPlayerBoardState(1).get("houses"));

        // player 1 makeAction CIVILISATIONCARD 3 getcard
        gameBoard.getPlayers().get(1).getPlayerBoard().giveEffect(new Effect[] {Effect.CLAY, Effect.CLAY, Effect.CLAY}); // for civ card
        usedResources = new Effect[] {Effect.CLAY, Effect.CLAY, Effect.CLAY};
        desiredResources = new Effect[] {};
        assertTrue(game.makeAction(1, Location.CIVILISATION_CARD3, usedResources, desiredResources));
        assertEquals("2", getCivCards(1).get("toolmakers"));
        assertEquals("1", getCivCards(1).get("writing"));


        // ROUND 2

        // placing figures
        assertTrue(game.placeFigures(1, Location.FOREST, 3));
        assertTrue(game.placeFigures(0, Location.CLAY_MOUND, 4));

        assertTrue(game.placeFigures(1, Location.TOOL_MAKER, 1));
        assertTrue(game.placeFigures(0, Location.FIELD, 1));

        assertTrue(game.placeFigures(1, Location.CIVILISATION_CARD2, 1));
        assertTrue(game.placeFigures(0, Location.CIVILISATION_CARD1, 1));


        // player 1 makeAction WOOD
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        throwMock.result = new int[] {6, 6, 6};
        assertTrue(game.makeAction(1, Location.FOREST, usedResources, desiredResources));
        assertTrue(game.noMoreToolsThisThrow(1));
        assertEquals("6", getPlayerRes(1).get("WOOD"));

        // player 1 makeAction TOOLMAKER
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        assertTrue(game.makeAction(1, Location.TOOL_MAKER, usedResources, desiredResources));
        assertEquals("1 unused", getPlayerTools(1).get("tool slot 0"));

        // player 1 makeAction CIVILISATIONCARD 2 throw clay
        usedResources = new Effect[] {Effect.WOOD, Effect.WOOD};
        desiredResources = new Effect[] {};
        throwMock.result = new int[] {5, 6};
        assertTrue(game.makeAction(1, Location.CIVILISATION_CARD2, usedResources, desiredResources));
        assertTrue(game.useTools(1, 0));
        assertTrue(game.noMoreToolsThisThrow(1));
        assertEquals("1 used", getPlayerTools(1).get("tool slot 0"));
        assertEquals("3", getPlayerRes(1).get("CLAY"));
        assertEquals("2", getCivCards(1).get("shamans"));

        // player 0 makeAction CLAY
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        throwMock.result = new int[] {6, 6, 6, 6};
        assertTrue(game.makeAction(0, Location.CLAY_MOUND, usedResources, desiredResources));
        assertTrue(game.noMoreToolsThisThrow(0));
        assertEquals("6", getPlayerRes(0).get("CLAY"));

        // player 0 makeAction FIELD
        usedResources = new Effect[] {};
        desiredResources = new Effect[] {};
        assertTrue(game.makeAction(0, Location.FIELD, usedResources, desiredResources));
        assertEquals("1", getTribeFed(0).get("fields"));

        // player 0 makeAction CIVILISATIONCARD 1 allplayerstakereward
        usedResources = new Effect[] {Effect.STONE};
        desiredResources = new Effect[] {};
        throwMock.result = new int[] {5, 6};
        assertTrue(game.makeAction(0, Location.CIVILISATION_CARD1, usedResources, desiredResources));
        assertEquals("2", getCivCards(0).get("farmers"));

        assertTrue(game.makeAllPlayersTakeARewardChoice(0, Effect.FIELD)); // player 0 select FIELD reward
//        assertTrue(game.makeAllPlayersTakeARewardChoice(1, Effect.TOOL)); // happens automatically
        assertEquals("2", getTribeFed(0).get("fields"));
        assertEquals("1 unused", getPlayerTools(1).get("tool slot 1"));

        // feed tribe
        assertTrue(game.feedTribe(1, new Effect[] {Effect.WOOD}));
        assertTrue(game.doNotFeedThisTurn(0));
        assertEquals("-6", getPlayerBoardState(0).get("points"));

        assertEquals("GAME_END", getControllerState().get("game phase"));

        for (Player player : ((GameBoard) game.getGameBoard()).getPlayers())
            ((PlayerBoardGameBoardFacade)player.getPlayerBoard()).addEndOfGamePoints();

        assertEquals("6", getPlayerBoardState(0).get("points"));
        assertEquals("37", getPlayerBoardState(1).get("points"));

//        printState();
    }

    private JSONObject getPlayerBoardState(int id) {
        JSONObject obj = new JSONObject(game.state());

        JSONObject boards = (JSONObject) obj.get("PlayerBoards");
        JSONObject pb = (JSONObject) boards.get(id+"");
        return pb;
    }

    private JSONObject getPlayerTools(int id) {
        JSONObject pb = getPlayerBoardState(id);
        return (JSONObject) pb.get("tools");
    }

    private JSONObject getPlayerRes(int id) {
        JSONObject pb = getPlayerBoardState(id);
        return (JSONObject) pb.get("resources and food");
    }

    private JSONObject getPlayerFigures(int id) {
        JSONObject pb = getPlayerBoardState(id);
        return (JSONObject) pb.get("figures");
    }

    private JSONObject getTribeFed(int id) {
        JSONObject pb = getPlayerBoardState(id);
        return (JSONObject) pb.get("tribe fed status");
    }

    private JSONObject getCivCards(int id) {
        JSONObject pb = getPlayerBoardState(id);
        return (JSONObject) pb.get("civilisation cards");
    }

    private JSONObject getControllerState() {
        return (JSONObject) new JSONObject(game.state()).get("GamePhaseController");
    }

    private void printState() {
        System.out.println(new JSONObject(game.state()).toString(2));
    }
}
