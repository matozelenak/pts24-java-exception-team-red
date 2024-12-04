package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.CivilisationCard;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfacePlayerBoardGameBoard;

import java.util.Optional;

public class GetCard implements EvaluateCivilisationCardImmediateEffect {
    private final InterfaceCurrentThrow currentThrow;
    private final InterfaceCivilizationCardDeck cardDeck;

    public GetCard(InterfaceCurrentThrow currentThrow, InterfaceCivilizationCardDeck cardDeck) {
        this.currentThrow = currentThrow;
        this.cardDeck = cardDeck;
    }

    @Override
    public boolean performEffect(Player player, Effect choice) {
//        currentThrow.initiate(player, null, 0);

        Optional<CivilisationCard> deckTopCard = cardDeck.getTop();
        if(deckTopCard.isEmpty()) {
            return false;
        }

        InterfacePlayerBoardGameBoard playerBoard = player.getPlayerBoard();
        playerBoard.giveEndOfGameEffect(deckTopCard.get().getEndOfGameEffect());
        return true;
    }
}
