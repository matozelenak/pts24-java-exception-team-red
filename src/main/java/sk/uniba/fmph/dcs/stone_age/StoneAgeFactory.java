package sk.uniba.fmph.dcs.stone_age;

import sk.uniba.fmph.dcs.game_board.*;
import sk.uniba.fmph.dcs.game_phase_controller.GamePhaseController;
import sk.uniba.fmph.dcs.game_phase_controller.GamePhaseControllerFactory;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardFactory;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoneAgeFactory {

    public static StoneAgeGame createStoneAge(int numPlayers, CivilizationCardDeck cardDeck, List<BuildingTile> buildingTiles) {
        if (numPlayers > 4) return null;

        // PLAYER BOARD
        // create PlayerOrder objects
        Map<Integer, PlayerOrder> playerOrders = new HashMap<>();
        for (int i = 0; i < numPlayers; i++) {
            playerOrders.put(i, new PlayerOrder(i, numPlayers));
        }

        // map of PlayerBoard | PlayerBoardGameBoardFacade
        Map<Integer, Map.Entry<PlayerBoard, PlayerBoardGameBoardFacade>> playerBoardsWithFacades = new HashMap<>();
        for (int i = 0; i < numPlayers; i++) {
            playerBoardsWithFacades.put(i, PlayerBoardFactory.createPlayerBoard());
        }

        // create list of Player objects
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(playerOrders.get(i), playerBoardsWithFacades.get(i).getValue()));
        }

        // list of InterfaceGetState (PlayerBoard)
        List<InterfaceGetState> playerBoardStates = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++)
            playerBoardStates.add(playerBoardsWithFacades.get(i).getKey());


        // GAME BOARD
        GameBoardFactory.ThrowMock throwMock = new GameBoardFactory.ThrowMock();
        GameBoard gameBoard = GameBoardFactory.createGameBoard(players, throwMock, cardDeck, buildingTiles);

        // CONTROLLER
        Map<PlayerOrder, InterfaceFeedTribe> playerFeedTribe = new HashMap<>();
        for (int i = 0; i < numPlayers; i++)
            playerFeedTribe.put(playerOrders.get(i), playerBoardsWithFacades.get(i).getValue());

        Map<PlayerOrder, InterfaceNewTurn> playerNewTurn = new HashMap<>();
        for (int i = 0; i < numPlayers; i++)
            playerNewTurn.put(playerOrders.get(i), playerBoardsWithFacades.get(i).getValue());

        GamePhaseController gamePhaseController = (GamePhaseController) GamePhaseControllerFactory.createGamePhaseController(
                gameBoard.getAllLocations(),
                playerFeedTribe,
                playerNewTurn,
                gameBoard.getCurrentThrow(),
                gameBoard.getRewardMenu(),
                playerOrders.get(0));


        return new StoneAgeGame(playerOrders, gamePhaseController, gameBoard, playerBoardStates);
    }

}
