package sk.uniba.fmph.dcs.game_board;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;
import sk.uniba.fmph.dcs.stone_age.InterfacePlayerBoardGameBoard;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.OptionalInt;

import static org.junit.Assert.*;

public class CurrentThrowTest {

    CurrentThrow currentThrow;
    ThrowMock throwMock;
    Player player1;
    PlayerBoardMock playerBoardMock1;

    @Before
    public void setup() {
        throwMock = new ThrowMock();
        currentThrow = new CurrentThrow(throwMock);
        playerBoardMock1 = new PlayerBoardMock();
        player1 = new Player(new PlayerOrder(0, 4), playerBoardMock1);
    }

    @Test
    public void testThrowFood() {
        throwMock.expectedResult = new int[] {1, 2, 3, 4, 5, 6};
        currentThrow.initiate(player1, Effect.FOOD, 6);
        assertEquals(currentThrow.getThrowResult(), 10);

        assertTrue(currentThrow.canUseTools());

        playerBoardMock1.expectedHasSufficientTools = false;
        playerBoardMock1.expectedUseTool = -1;
        assertFalse(currentThrow.useTool(0));
        assertNull(playerBoardMock1.givenEffects);

        Effect[] expectedList = new Effect[11];
        for (int i = 0; i < 11; i++) expectedList[i] = Effect.FOOD;
        playerBoardMock1.expectedHasSufficientTools = true;
        playerBoardMock1.expectedUseTool = 2;
        assertTrue(currentThrow.useTool(0));
        assertEquals(currentThrow.getThrowResult(), 11);
        assertTrue(currentThrow.finishUsingTools());
        assertArrayEquals(playerBoardMock1.givenEffects, expectedList);
    }

    @Test
    public void testThrowWood() {
        throwMock.expectedResult = new int[] {3, 2, 5};
        currentThrow.initiate(player1, Effect.WOOD, 3);
        assertEquals(currentThrow.getThrowResult(), 3);

        assertTrue(currentThrow.canUseTools());

        playerBoardMock1.expectedHasSufficientTools = false;
        playerBoardMock1.expectedUseTool = -1;
        assertFalse(currentThrow.useTool(0));

        playerBoardMock1.expectedHasSufficientTools = true;
        playerBoardMock1.expectedUseTool = 4;
        assertTrue(currentThrow.useTool(0));
        assertEquals(currentThrow.getThrowResult(), 4);
        assertTrue(currentThrow.finishUsingTools());
        Effect[] expectedList = new Effect[4];
        for (int i = 0; i < 4; i++) expectedList[i] = Effect.WOOD;
        assertArrayEquals(playerBoardMock1.givenEffects, expectedList);
    }

    @Test
    public void testThrowClay() {
        throwMock.expectedResult = new int[] {4};
        currentThrow.initiate(player1, Effect.CLAY, 1);
        assertEquals(currentThrow.getThrowResult(), 1);

        assertTrue(currentThrow.canUseTools());

        playerBoardMock1.expectedHasSufficientTools = false;
        playerBoardMock1.expectedUseTool = -1;
        assertFalse(currentThrow.useTool(0));

        playerBoardMock1.expectedHasSufficientTools = true;
        playerBoardMock1.expectedUseTool = 6;
        assertTrue(currentThrow.useTool(0));
        assertEquals(currentThrow.getThrowResult(), 2);
        assertTrue(currentThrow.finishUsingTools());
        Effect[] expectedList = new Effect[2];
        for (int i = 0; i < 2; i++) expectedList[i] = Effect.CLAY;
        assertArrayEquals(playerBoardMock1.givenEffects, expectedList);
    }

    @Test
    public void testThrowStone() {
        throwMock.expectedResult = new int[] {6, 2, 3, 1};
        currentThrow.initiate(player1, Effect.STONE, 4);
        assertEquals(currentThrow.getThrowResult(), 2);

        assertTrue(currentThrow.canUseTools());

        playerBoardMock1.expectedHasSufficientTools = false;
        playerBoardMock1.expectedUseTool = -1;
        assertFalse(currentThrow.useTool(0));

        playerBoardMock1.expectedHasSufficientTools = true;
        playerBoardMock1.expectedUseTool = 3;
        assertTrue(currentThrow.useTool(0));
        assertEquals(currentThrow.getThrowResult(), 3);
        assertTrue(currentThrow.finishUsingTools());
        Effect[] expectedList = new Effect[3];
        for (int i = 0; i < 3; i++) expectedList[i] = Effect.STONE;
        assertArrayEquals(playerBoardMock1.givenEffects, expectedList);
    }

    @Test
    public void testThrowGold() {
        throwMock.expectedResult = new int[] {6, 4, 2, 5, 2};
        currentThrow.initiate(player1, Effect.GOLD, 5);
        assertEquals(currentThrow.getThrowResult(), 3);

        assertTrue(currentThrow.canUseTools());

        playerBoardMock1.expectedHasSufficientTools = false;
        playerBoardMock1.expectedUseTool = -1;
        assertFalse(currentThrow.useTool(0));

        playerBoardMock1.expectedHasSufficientTools = true;
        playerBoardMock1.expectedUseTool = 10;
        assertTrue(currentThrow.useTool(0));
        assertEquals(currentThrow.getThrowResult(), 4);

        playerBoardMock1.expectedHasSufficientTools = true;
        playerBoardMock1.expectedUseTool = 7;
        assertTrue(currentThrow.useTool(0));
        assertEquals(currentThrow.getThrowResult(), 6);
        assertTrue(currentThrow.finishUsingTools());
        Effect[] expectedList = new Effect[6];
        for (int i = 0; i < 6; i++) expectedList[i] = Effect.GOLD;
        assertArrayEquals(playerBoardMock1.givenEffects, expectedList);
    }



    private static class ThrowMock implements InterfaceThrow {
        public int[] expectedResult;
        @Override
        public int[] throwDices(int dices) {
            return expectedResult;
        }
    }

    private static class PlayerBoardMock implements InterfacePlayerBoardGameBoard {
        public boolean expectedHasSufficientTools;
        public int expectedUseTool;
        public Effect[] givenEffects;

        @Override
        public void giveEffect(Effect[] stuff) {
            this.givenEffects = stuff;
        }

        @Override
        public void giveEndOfGameEffect(EndOfGameEffect[] stuff) {

        }

        @Override
        public boolean takeResources(Effect[] stuff) {
            return false;
        }

        @Override
        public boolean takeFigures(int count) {
            return false;
        }

        @Override
        public boolean hasFigures(int count) {
            return false;
        }

        @Override
        public boolean hasSufficientTools(int goal) {
            return expectedHasSufficientTools;
        }

        @Override
        public OptionalInt useTool(int idx) {
            if (expectedUseTool == -1) return OptionalInt.empty();
            return OptionalInt.of(expectedUseTool);
        }

        @Override
        public boolean addNewFigure() {
            return false;
        }
    }
}
