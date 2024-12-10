package sk.uniba.fmph.dcs.stone_age;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.game_board.GameBoard;
import sk.uniba.fmph.dcs.game_phase_controller.GamePhaseController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StoneAgeGame implements InterfaceStoneAgeGame {

    private Map<Integer, PlayerOrder> players;
    private GamePhaseController gamePhaseController;
    private InterfaceGetState gameBoard;
    private List<InterfaceGetState> playerBoardStates;

    public StoneAgeGame(Map<Integer, PlayerOrder> players, GamePhaseController gamePhaseController, InterfaceGetState gameBoard, List<InterfaceGetState> playerBoardStates) {
        this.players = players;
        this.gamePhaseController = gamePhaseController;
        this.gameBoard = gameBoard;
        this.playerBoardStates = playerBoardStates;
    }

    @Override
    public boolean placeFigures(int playerId, Location location, int figuresCount) {
        if (!players.containsKey(playerId)) return false;

        return gamePhaseController.placeFigures(players.get(playerId), location, figuresCount);
    }

    @Override
    public boolean makeAction(int playerId, Location location, Effect[] usedResources, Effect[] desiredResources) {
        if (!players.containsKey(playerId)) return false;

        return gamePhaseController.makeAction(players.get(playerId), location, new ArrayList<>(Arrays.asList(usedResources)), new ArrayList<>(Arrays.asList(desiredResources)));
    }

    @Override
    public boolean skipAction(int playerId, Location location) {
        if (!players.containsKey(playerId)) return false;

        return gamePhaseController.skipAction(players.get(playerId), location);
    }

    @Override
    public boolean useTools(int playerId, int toolIndex) {
        if (!players.containsKey(playerId)) return false;

        return gamePhaseController.useTools(players.get(playerId), toolIndex);
    }

    @Override
    public boolean noMoreToolsThisThrow(int playerId) {
        if (!players.containsKey(playerId)) return false;

        return gamePhaseController.noMoreToolsThisThrow(players.get(playerId));
    }

    @Override
    public boolean feedTribe(int playerId, Effect[] resources) {
        if (!players.containsKey(playerId)) return false;

        return gamePhaseController.feedTribe(players.get(playerId), new ArrayList<>(Arrays.asList(resources)));
    }

    @Override
    public boolean doNotFeedThisTurn(int playerId) {
        if (!players.containsKey(playerId)) return false;

        return gamePhaseController.doNotFeedThisTurn(players.get(playerId));
    }

    @Override
    public boolean makeAllPlayersTakeARewardChoice(int playerId, Effect reward) {
        if (!players.containsKey(playerId)) return false;

        return gamePhaseController.makeAllPlayersTakeARewardChoice(players.get(playerId), reward);
    }

    public String state() {
        JSONObject obj = new JSONObject();
        obj.put("GameBoard", new JSONObject(gameBoard.state()));
        JSONObject playerBoards = new JSONObject();
        for (int i = 0; i < players.size(); i++) {
            playerBoards.put(i+"", new JSONObject(playerBoardStates.get(i).state()));
        }
        obj.put("PlayerBoards", playerBoards);
        obj.put("GamePhaseController", new JSONObject(gamePhaseController.state()));
        return obj.toString();
    }

    public Map<Integer, PlayerOrder> getPlayers() {
        return players;
    }

    public GamePhaseController getGamePhaseController() {
        return gamePhaseController;
    }

    public InterfaceGetState getGameBoard() {
        return gameBoard;
    }
}
