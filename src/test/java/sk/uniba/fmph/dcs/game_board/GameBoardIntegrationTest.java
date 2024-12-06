package sk.uniba.fmph.dcs.game_board;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

import static org.junit.Assert.*;

public class GameBoardIntegrationTest {

    PlayerBoardMock playerBoardMock1, playerBoardMock2, playerBoardMock3, playerBoardMock4;
    PlayerOrder player1, player2, player3, player4;
    List<Player> players;
    GameBoard gameBoard;
    GameBoardFactory.ThrowMock throwMock;
    InterfaceFigureLocation civilizationCard1, civilizationCard2, civilizationCard3, civilizationCard4;
    InterfaceFigureLocation buildingTile1, buildingTile2, buildingTile3, buildingTile4;

    @Before
    public void setup() {
        playerBoardMock1 = new PlayerBoardMock();
        playerBoardMock2 = new PlayerBoardMock();
        playerBoardMock3 = new PlayerBoardMock();
        playerBoardMock4 = new PlayerBoardMock();
        this.player1 = new PlayerOrder(0, 4);
        this.player2 = new PlayerOrder(1, 4);
        this.player3 = new PlayerOrder(2, 4);
        this.player4 = new PlayerOrder(3, 4);

        Player player1 = new Player(this.player1, playerBoardMock1);
        Player player2 = new Player(this.player2, playerBoardMock2);
        Player player3= new Player(this.player3, playerBoardMock3);
        Player player4 = new Player(this.player4, playerBoardMock4);
        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        throwMock = new GameBoardFactory.ThrowMock();
        gameBoard = GameBoardFactory.createGameBoard(players, throwMock, GameBoardFactory.createCardDeck1(), GameBoardFactory.createBuildingTiles1_4Players());

        civilizationCard1 = gameBoard.getLocation(Location.CIVILISATION_CARD1);
        civilizationCard2 = gameBoard.getLocation(Location.CIVILISATION_CARD2);
        civilizationCard3 = gameBoard.getLocation(Location.CIVILISATION_CARD3);
        civilizationCard4 = gameBoard.getLocation(Location.CIVILISATION_CARD4);

        buildingTile1 = gameBoard.getLocation(Location.BUILDING_TILE1);
        buildingTile2 = gameBoard.getLocation(Location.BUILDING_TILE2);
        buildingTile3 = gameBoard.getLocation(Location.BUILDING_TILE3);
        buildingTile4 = gameBoard.getLocation(Location.BUILDING_TILE4);
    }

    @Test
    public void test1() {
        assertTrue(civilizationCard1.placeFigures(player1, 1));
        assertTrue(civilizationCard2.placeFigures(player2, 1));
        assertTrue(civilizationCard3.placeFigures(player3, 1));
        assertTrue(civilizationCard4.placeFigures(player4, 1));

        Effect[] inputResources;
        Effect[] outputResources;

        // player1 GetSomethingFixed 1 WOOD
        inputResources = new Effect[] {Effect.CLAY};
        outputResources = new Effect[] {};
        assertEquals(ActionResult.ACTION_DONE, civilizationCard1.makeAction(player1, inputResources, outputResources));
        assertEquals(new ArrayList<>(Arrays.asList(Effect.WOOD)), playerBoardMock1.givenEffects);
        assertEquals(new ArrayList<>(Arrays.asList(EndOfGameEffect.Pottery)), playerBoardMock1.givenEndOfGameEffects);

        // player2 GetSomethingThrow STONE
        throwMock.result = new int[] {5, 5};
        inputResources = new Effect[] {Effect.WOOD, Effect.CLAY};
        outputResources = new Effect[] {};
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, civilizationCard2.makeAction(player2, inputResources, outputResources));
        assertTrue(gameBoard.getCurrentThrow().useTool(6));
        assertTrue(gameBoard.getCurrentThrow().finishUsingTools());
        assertEquals(new ArrayList<>(Arrays.asList(Effect.STONE, Effect.STONE, Effect.STONE)), playerBoardMock2.givenEffects);
        assertEquals(new ArrayList<>(Arrays.asList(EndOfGameEffect.ToolMaker)), playerBoardMock2.givenEndOfGameEffects);

        // player3 GetCard
        inputResources = new Effect[] {Effect.WOOD, Effect.CLAY, Effect.WOOD};
        outputResources = new Effect[] {};
        assertEquals(ActionResult.ACTION_DONE, civilizationCard3.makeAction(player3, inputResources, outputResources));
        assertEquals(new ArrayList<>(Arrays.asList(EndOfGameEffect.Farmer, EndOfGameEffect.Medicine)), playerBoardMock3.givenEndOfGameEffects);

        // player4 GetChoice
        inputResources = new Effect[] {Effect.WOOD, Effect.CLAY, Effect.WOOD, Effect.WOOD};
        outputResources = new Effect[] {Effect.GOLD, Effect.STONE};
        assertEquals(ActionResult.ACTION_DONE, civilizationCard4.makeAction(player4, inputResources, outputResources));
        assertEquals(new ArrayList<>(Arrays.asList(Effect.GOLD, Effect.STONE)), playerBoardMock4.givenEffects);
        assertEquals(new ArrayList<>(Arrays.asList(EndOfGameEffect.Art)), playerBoardMock4.givenEndOfGameEffects);


        System.out.println(gameBoard.state());

        // ROUND 2
        playerBoardMock1.givenEffects.clear();
        playerBoardMock1.givenEndOfGameEffects.clear();
        playerBoardMock1.usedToolID = 0;
        playerBoardMock1.takeFiguresAmount = 0;
        playerBoardMock2.givenEffects.clear();
        playerBoardMock2.givenEndOfGameEffects.clear();
        playerBoardMock2.usedToolID = 0;
        playerBoardMock2.takeFiguresAmount = 0;
        playerBoardMock3.givenEffects.clear();
        playerBoardMock3.givenEndOfGameEffects.clear();
        playerBoardMock3.usedToolID = 0;
        playerBoardMock3.takeFiguresAmount = 0;
        playerBoardMock4.givenEffects.clear();
        playerBoardMock4.givenEndOfGameEffects.clear();
        playerBoardMock4.usedToolID = 0;
        playerBoardMock4.takeFiguresAmount = 0;

        assertFalse(civilizationCard1.newTurn());
        assertFalse(civilizationCard2.newTurn());
        assertFalse(civilizationCard3.newTurn());
        assertFalse(civilizationCard4.newTurn());

        civilizationCard1.placeFigures(player1, 1);
        civilizationCard2.placeFigures(player2, 1);
        civilizationCard3.placeFigures(player3, 1);
        civilizationCard4.placeFigures(player4, 1);

        // player1 AllPlayersTakeReward (RewardMenu test)
        throwMock.result = new int[] {4, 6, 5, 3}; // RewardMenu should contain Stone, Gold, Tool, Field
        inputResources = new Effect[] {Effect.CLAY};
        outputResources = new Effect[] {};
        assertEquals(ActionResult.ACTION_DONE_ALL_PLAYERS_TAKE_A_REWARD, civilizationCard1.makeAction(player1, inputResources, outputResources));
        assertEquals(new ArrayList<>(Arrays.asList(EndOfGameEffect.Shaman)), playerBoardMock1.givenEndOfGameEffects);

        assertTrue(gameBoard.getRewardMenu().takeReward(player1, Effect.GOLD));
        assertTrue(gameBoard.getRewardMenu().takeReward(player2, Effect.STONE));
        assertFalse(gameBoard.getRewardMenu().takeReward(player2, Effect.TOOL));
        assertTrue(gameBoard.getRewardMenu().takeReward(player3, Effect.TOOL));
        assertTrue(gameBoard.getRewardMenu().takeReward(player4, Effect.FIELD));
        assertFalse(gameBoard.getRewardMenu().takeReward(player4, Effect.GOLD));
        assertEquals(0, ((RewardMenu)gameBoard.getRewardMenu()).getPlayersLeftCount());

        // SKIP further actions
        assertFalse(civilizationCard1.skipAction(player1));
        assertTrue(civilizationCard2.skipAction(player2));
        assertTrue(civilizationCard3.skipAction(player3));
        assertTrue(civilizationCard4.skipAction(player4));

        // try ROUND 3
        assertFalse(civilizationCard1.newTurn());
        assertFalse(civilizationCard2.newTurn());
        assertFalse(civilizationCard3.newTurn());
        assertTrue(civilizationCard4.newTurn()); // should indicate end of game
    }


    @Test
    public void test2() {
        assertTrue(buildingTile1.placeFigures(player1, 1));
        assertFalse(buildingTile1.placeFigures(player1, 1));
        assertFalse(buildingTile1.placeFigures(player2, 1));
        assertTrue(buildingTile2.placeFigures(player2, 1));
        assertTrue(buildingTile3.placeFigures(player3, 1));
        assertTrue(buildingTile4.placeFigures(player4, 1));

        Effect[] inputResources;
        Effect[] outputResources;

        // player1 SimpleBuilding 1 WOOD 1 CLAY
        inputResources = new Effect[] {Effect.CLAY, Effect.WOOD};
        outputResources = new Effect[] {};
        assertEquals(ActionResult.ACTION_DONE, buildingTile1.makeAction(player1, inputResources, outputResources));
    }

    private static class PlayerBoardMock implements InterfacePlayerBoardGameBoard {
        public boolean expectedHasFigures = true;
        public int takeFiguresAmount = 0;
        public List<Effect> givenEffects = new ArrayList<>();
        public List<EndOfGameEffect> givenEndOfGameEffects = new ArrayList<>();
        public boolean expectedHasResources = true;
        public Effect[] takenResources;
        public boolean expectedHasTools = true;
        public int usedToolID;

        @Override
        public void giveEffect(Effect[] stuff) {
            for (Effect e : stuff)
                givenEffects.add(e);
        }

        @Override
        public void giveEndOfGameEffect(EndOfGameEffect[] stuff) {
            for (EndOfGameEffect e : stuff)
                givenEndOfGameEffects.add(e);
        }

        @Override
        public boolean takeResources(Effect[] stuff) {
            this.takenResources = stuff;
            return expectedHasResources;
        }

        @Override
        public boolean takeFigures(int count) {
            takeFiguresAmount = count;
            return expectedHasFigures;
        }

        @Override
        public boolean hasFigures(int count) {
            return expectedHasFigures;
        }

        @Override
        public boolean hasSufficientTools(int goal) {
            return expectedHasTools;
        }

        @Override
        public OptionalInt useTool(int idx) {
            usedToolID = idx;
            return null;
        }

        @Override
        public boolean addNewFigure() {
            return false;
        }
    }
}
