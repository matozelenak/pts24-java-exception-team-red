package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.CivilisationCard;
import sk.uniba.fmph.dcs.stone_age.ImmediateEffect;
import sk.uniba.fmph.dcs.stone_age.InterfacePlayerBoardGameBoard;

public class GetCard implements EvaluateCivilisationCardImmediateEffect {
    private final InterfaceCurrentThrow currentThrow;
    private final InterfaceCivilizationCardDeck cardDeck;

    public GetCard(InterfaceCurrentThrow currentThrow, InterfaceCivilizationCardDeck cardDeck) {
        this.currentThrow = currentThrow;
        this.cardDeck = cardDeck;
    }

    @Override
    public boolean performEffect(Player player, ImmediateEffect choice) {
        currentThrow.initiate(player, null, 0);

        CivilisationCard deckTopCard;
        if(cardDeck.getTop().isEmpty()) {
            return false;
        } else {
            deckTopCard = cardDeck.getTop().get();
        }

        InterfacePlayerBoardGameBoard playerBoard = player.getPlayerBoard();
        playerBoard.giveEndOfGameEffect(deckTopCard.getEndOfGameEffect());
        return true;
    }
}
