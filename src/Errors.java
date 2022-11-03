public enum Errors {
    InvalidKnightMove(0,"Invalid move for Knight"),
    InvalidBishopPath(1,"Cannot create valid path for Bishop."),
    InvalidQueenPath(2,"Cannot create valid path for Queen."),
    InvalidKingPath(3, "Cannot create valid path for King."),
    InvalidPawnPath(4, "Cannot create valid path for pawn."),
    PawnPromotionNull(5, "Pawn Promotion Piece must be specified for this pawn move"),
    PawnPromotionColorError(6, "Pawn Promotion Piece must be for White. Input should be uppercase."),
    PawnPromotionColor2Error(7, "Pawn Promotion Piece must be for black. Input should be lowercase."),
    RookNotMoving(8, "Rook must move at least 1 square."),
    InvalideRookPath(9, "Cannot create valid path for Rook."),
    NoPieceSelected(10, "Select a square witha piece."),
    WrongPlayerMovingPiece(11, "Select the correct player's piece to move."),
    GoodMove(12, "Move looks good"),
    PieceNotCaptured(13, "No piece was captured...");
    
    private final int code;
	private final String description;
	
	private Errors(int code, String description) {
		this.code = code;
		this.description = description;
	}
	
	@Override
	public String toString() {
		return code + ": " + description;
	}
	
}
