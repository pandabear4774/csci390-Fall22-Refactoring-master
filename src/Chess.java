import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chess {

    Piece[][] board = new Piece[8][8];
    boolean playerTurnIsWhite;

    public Chess() {
        setupBlackPieces();

        board[6][0] = Piece.P;
        board[6][1] = Piece.P;
        board[6][2] = Piece.P;
        board[6][3] = Piece.P;
        board[6][4] = Piece.P;
        board[6][5] = Piece.P;
        board[6][6] = Piece.P;
        board[6][7] = Piece.P;
        board[7][0] = Piece.R;
        board[7][1] = Piece.N;
        board[7][2] = Piece.B;
        board[7][3] = Piece.Q;
        board[7][4] = Piece.K;
        board[7][5] = Piece.B;
        board[7][6] = Piece.N;
        board[7][7] = Piece.R;

        playerTurnIsWhite = true;
    }

    private void setupBlackPieces() {
        board[0][0] = Piece.r;
        board[0][1] = Piece.n;
        board[0][2] = Piece.b;
        board[0][3] = Piece.q;
        board[0][4] = Piece.k;
        board[0][5] = Piece.b;
        board[0][6] = Piece.n;
        board[0][7] = Piece.r;
        board[1][0] = Piece.p;
        board[1][1] = Piece.p;
        board[1][2] = Piece.p;
        board[1][3] = Piece.p;
        board[1][4] = Piece.p;
        board[1][5] = Piece.p;
        board[1][6] = Piece.p;
        board[1][7] = Piece.p;
    }

    public void play() throws IOException {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        while(!gameIsOver()) {
            printBoardToConsole();

            // TODO: Homework - Is this really the chess game's responsibility to do the move conversion?
            //                       Refactor this code to make it the responsibility of a different class.
            // We'll be doing a simpler notation for our chess game. Notation will be a 5 or 6 character length. Form
            //       will take the shape of {a-h}{1-8}(\-x}{a-h}{1-8}{rnbqRNBQ}.
            //       First character is the from file
            //       Second character is the from rank
            //       Third character is - for move, x for capture
            //       Forth character is the to file
            //       Fifth character is the to rank
            //       Sixth character is the promotion of a pawn to a piece type. This is optional
            
            
            Move m = new Move(inputReader.readLine());
            
            if(!m.valid()) {
                //Move is invalid;
                System.out.println("Move is invalid. Please input a valid move.");
                continue;
            }
            
            if(!m.capture) {
                Errors gamestate = movePiece(m);
                if(gamestate != Errors.GoodMove) {
                	System.out.println(gamestate.toString());
                }
            } else {
            	Errors gamestate = capturePiece(m);
                if(gamestate != Errors.GoodMove) {
                	System.out.println(gamestate.toString());
                }
            }
            // We are not going to worry about special moves like castling and en passant
        }

        System.out.println("Game over.");
        System.out.println("Thanks for playing!");
    }

    private Errors movePiece(Move m) {
    	
    	int fromFileIndex = m.startFile;
    	int fromRankIndex = m.startRank;
    	int toFileIndex = m.endFile;
    	int toRankIndex = m.endRank;
    	String pawnPromotionPiece = m.promotionPiece;
    	
        Piece fromPiece = board[fromRankIndex][fromFileIndex];

        if(fromPiece == null) {
            return Errors.NoPieceSelected;
        }

        if (correctPlayerNotMovingTheirPiece(fromPiece)) return Errors.WrongPlayerMovingPiece;

        //Validate Piece Movement
        if(fromPiece.toString().equalsIgnoreCase("n")) {
            if (!((Math.abs(fromFileIndex - toFileIndex) == 2 && Math.abs(fromRankIndex - toRankIndex) == 1) || (Math.abs(fromFileIndex - toFileIndex) == 1 && Math.abs(fromRankIndex - toRankIndex) == 2))) {
                return Errors.InvalidKnightMove;
            }
        } else if(fromPiece.toString().equalsIgnoreCase("r")) {
            return validateRookMove(toFileIndex, toRankIndex, fromFileIndex, fromRankIndex);
        } else if(fromPiece.toString().equalsIgnoreCase("b")) {
            if(fromFileIndex == toFileIndex || toRankIndex == fromRankIndex) {
                return Errors.InvalidBishopPath;
            } else if(Math.abs(fromFileIndex- toFileIndex) != Math.abs(fromRankIndex- toRankIndex)) {
                return Errors.InvalidBishopPath;
            } else {
                if(fromFileIndex < toFileIndex && fromRankIndex < toRankIndex) {
                    for(int i = 1; i <= toFileIndex -fromFileIndex; i++) {
                        if(board[fromRankIndex+i][fromFileIndex+i] != null) {
                        	return Errors.InvalidBishopPath;
                        }
                    }
                } else if(fromFileIndex < toFileIndex && fromRankIndex > toRankIndex) {
                    for(int i = 1; i <= toFileIndex -fromFileIndex; i++) {
                        if(board[fromRankIndex-i][fromFileIndex+i] != null) {
                        	return Errors.InvalidBishopPath;
                        }
                    }
                } else if(fromFileIndex > toFileIndex && fromRankIndex > toRankIndex) {
                    for(int i = 1; i <= fromFileIndex- toFileIndex; i++) {
                        if(board[fromRankIndex-i][fromFileIndex-i] != null) {
                        	return Errors.InvalidBishopPath;
                        }
                    }
                } else if(fromFileIndex > toFileIndex && fromRankIndex < toRankIndex) {
                    for(int i = 1; i <= fromFileIndex- toFileIndex; i++) {
                        if(board[fromRankIndex+i][fromFileIndex-i] != null) {
                        	return Errors.InvalidBishopPath;
                        }
                    }
                }
            }
        } else if (fromPiece.toString().equalsIgnoreCase("q")) {
            if(fromFileIndex == toFileIndex && toRankIndex == fromRankIndex) {
            	return Errors.InvalidQueenPath;
            } else if(fromFileIndex == toFileIndex) {
                if(toRankIndex >fromRankIndex) {
                    for(int i = fromRankIndex+1; i<= toRankIndex; i++) {
                        if(board[i][fromFileIndex] != null) {
                        	return Errors.InvalidQueenPath;
                        }
                    }
                } else {
                    for(int i = fromRankIndex-1; i>= toRankIndex; i--) {
                        if(board[i][fromFileIndex] != null) {
                        	return Errors.InvalidQueenPath;
                        }
                    }
                }
            } else if(fromRankIndex == toRankIndex){
                if(toFileIndex >fromFileIndex) {
                    for(int i = fromFileIndex+1; i<= toFileIndex; i++) {
                        if(board[fromRankIndex][i] != null) {
                        	return Errors.InvalidQueenPath;
                        }
                    }
                } else {
                    for(int i=fromFileIndex-1; i>=fromFileIndex; i--) {
                        if(board[fromRankIndex][i] != null) {
                        	return Errors.InvalidQueenPath;
                        }
                    }
                }
            } else if(Math.abs(fromFileIndex- toFileIndex) != Math.abs(fromRankIndex- toRankIndex)) {
            	return Errors.InvalidQueenPath;
            } else {
                if (fromFileIndex < toFileIndex && fromRankIndex < toRankIndex) {
                    for (int i = 1; i <= toFileIndex - fromFileIndex; i++) {
                        if (board[fromRankIndex + i][fromFileIndex + i] != null) {
                        	return Errors.InvalidQueenPath;
                        }
                    }
                } else if (fromFileIndex < toFileIndex && fromRankIndex > toRankIndex) {
                    for (int i = 1; i <= toFileIndex - fromFileIndex; i++) {
                        if (board[fromRankIndex - i][fromFileIndex + i] != null) {
                        	return Errors.InvalidQueenPath;
                        }
                    }
                } else if (fromFileIndex > toFileIndex && fromRankIndex > toRankIndex) {
                    for (int i = 1; i <= fromFileIndex - toFileIndex; i++) {
                        if (board[fromRankIndex - i][fromFileIndex - i] != null) {
                        	return Errors.InvalidQueenPath;
                        }
                    }
                } else if (fromFileIndex > toFileIndex && fromRankIndex < toRankIndex) {
                    for (int i = 1; i <= fromFileIndex - toFileIndex; i++) {
                        if (board[fromRankIndex + i][fromFileIndex - i] != null) {
                        	return Errors.InvalidQueenPath;
                        }
                    }
                }
            }
        } else if(fromPiece.toString().equalsIgnoreCase("k")) {
            if(fromFileIndex == toFileIndex && toRankIndex == fromRankIndex) {
            	return Errors.InvalidKingPath;
            } else if (Math.abs(fromFileIndex- toFileIndex) > 1) {
            	return Errors.InvalidKingPath;
            } else if (Math.abs(fromRankIndex- toRankIndex) > 1) {
            	return Errors.InvalidKingPath;
            } else if (board[toRankIndex][toFileIndex] != null) {
            	return Errors.InvalidKingPath;
            }
        } else if (fromPiece.toString().equalsIgnoreCase("p")) {
            if(fromFileIndex != toFileIndex) {
            	return Errors.InvalidPawnPath;
            }
            if(playerTurnIsWhite) {
                if(fromRankIndex == 6) {
                    int rankDelta = fromRankIndex - toRankIndex;
                    if(rankDelta > 2 || rankDelta < 1) {
                    	return Errors.InvalidPawnPath;
                    } else if (rankDelta == 1) {
                        if(board[toRankIndex][toFileIndex] != null) {
                        	return Errors.InvalidPawnPath;
                        }
                    } else if (rankDelta == 2) {
                        if(board[toRankIndex][toFileIndex] != null || board[toRankIndex -1][toFileIndex] != null) {
                        	return Errors.InvalidPawnPath;
                        }
                    }
                } else {
                    int rankDelta = fromRankIndex - toRankIndex;
                    if(rankDelta != 1) {
                    	return Errors.InvalidPawnPath;
                    } else {
                        if(board[toRankIndex][toFileIndex] != null) {
                        	return Errors.InvalidPawnPath;
                        }
                    }
                }
            } else {
                if(fromRankIndex == 1) {
                    int rankDelta = fromRankIndex - toRankIndex;
                    if(rankDelta < -2 || rankDelta > -1) {
                    	return Errors.InvalidPawnPath;
                    } else if (rankDelta == -1) {
                        if(board[toRankIndex][toFileIndex] != null) {
                        	return Errors.InvalidPawnPath;
                        }
                    } else if (rankDelta == -2) {
                        if(board[toRankIndex][toFileIndex] != null || board[toRankIndex +1][toFileIndex] != null) {
                        	return Errors.InvalidPawnPath;
                        }
                    }
                } else {
                    int rankDelta = fromRankIndex - toRankIndex;
                    if(rankDelta != -1) {
                    	return Errors.InvalidPawnPath;
                    } else {
                        if(board[toRankIndex][toFileIndex] != null) {
                        	return Errors.InvalidPawnPath;
                        }
                    }
                }
            }
        }

        //Handle the promotion of a pawn.
        if(fromPiece.toString().equalsIgnoreCase("p")) {
            if(playerTurnIsWhite && toRankIndex == 0) {
                if(pawnPromotionPiece == null) {
                	return Errors.PawnPromotionNull;
                }
                if(!pawnPromotionPiece.toUpperCase().equals(pawnPromotionPiece)) {
                	return Errors.PawnPromotionColorError;
                }
                fromPiece = Piece.valueOf(pawnPromotionPiece);
            } else if(!playerTurnIsWhite && toRankIndex == 7) {
                if(pawnPromotionPiece == null) {
                	return Errors.PawnPromotionNull;
                }
                if(!pawnPromotionPiece.toLowerCase().equals(pawnPromotionPiece)) {
                	return Errors.PawnPromotionColor2Error;
                }
                fromPiece = Piece.valueOf(pawnPromotionPiece);
            }
        }

        //If we have gotten here, that means the move is valid and update the board position
        board[toRankIndex][toFileIndex] = fromPiece;
        board[fromRankIndex][fromFileIndex] = null;

        //Change the player's turn
        playerTurnIsWhite = !playerTurnIsWhite;
        
        return Errors.GoodMove;
    }

    private Errors validateRookMove(int toFileIndex, int toRankIndex, int fromFileIndex, int fromRankIndex) {
        if(fromFileIndex == toFileIndex && toRankIndex == fromRankIndex) {
            return Errors.RookNotMoving;
        } else if(fromFileIndex == toFileIndex) {
            if(toRankIndex > fromRankIndex) {
                for(int i = fromRankIndex +1; i<= toRankIndex; i++) {
                    if(board[i][fromFileIndex] != null) {
                        return Errors.InvalideRookPath;
                    }
                }
            } else {
                for(int i = fromRankIndex -1; i>= toRankIndex; i--) {
                    if(board[i][fromFileIndex] != null) {
                    	return Errors.InvalideRookPath;
                    }
                }
            }
        } else if(fromRankIndex == toRankIndex){
            if(toFileIndex > fromFileIndex) {
                for(int i = fromFileIndex +1; i<= toFileIndex; i++) {
                    if(board[fromRankIndex][i] != null) {
                    	return Errors.InvalideRookPath;
                    }
                }
            } else {
                for(int i = fromFileIndex -1; i>= fromFileIndex; i--) {
                    if(board[fromRankIndex][i] != null) {
                    	return Errors.InvalideRookPath;
                    }
                }
            }
        } else {
        	return Errors.InvalideRookPath;
        }
        return Errors.GoodMove;
    }

    private boolean correctPlayerNotMovingTheirPiece(Piece fromPiece) {
        //Check that the piece is owned by the correct player.
        if(playerTurnIsWhite) {
            if(fromPiece.toString().toLowerCase() == fromPiece.toString()) {
                System.out.println("Select a square with a white piece.");
                return true;
            }
        } else {
            if(fromPiece.toString().toUpperCase() == fromPiece.toString()) {
                System.out.println("Select a square with a black piece.");
                return true;
            }
        }
        return false;
    }

    // TODO: Homework - Refactor this method to use a single parameter

    //add pawn promotion
    private Errors capturePiece(Move m) {
    	
    	int fromFileIndex = m.startFile;
    	int fromRankIndex = m.startRank;
    	int toFileIndex = m.endFile;
    	int toRankIndex = m.endRank;
    	String pawnPromotionPiece = m.promotionPiece;
    	
        Piece fromPiece = board[fromRankIndex][fromFileIndex];
        
        if(fromPiece == null) {
            return Errors.NoPieceSelected;
        }
        
        if (correctPlayerNotMovingTheirPiece(fromPiece)) return Errors.WrongPlayerMovingPiece;
        

        //Validate Piece Movement
        if(fromPiece.toString().equalsIgnoreCase("n")) {
            if (!((Math.abs(fromFileIndex - toFileIndex) == 2 && Math.abs(fromRankIndex - toRankIndex) == 1) || (Math.abs(fromFileIndex - toFileIndex) == 1 && Math.abs(fromRankIndex - toRankIndex) == 2))) {
                return Errors.InvalidKnightMove;
            }
        } else if(fromPiece.toString().equalsIgnoreCase("r")) {
            Errors rook = validateRookMove(toFileIndex, toRankIndex, fromFileIndex, fromRankIndex);
            if(rook != Errors.GoodMove)
            return rook;
        } else if(fromPiece.toString().equalsIgnoreCase("b")) {
            if(fromFileIndex == toFileIndex || toRankIndex == fromRankIndex) {
            	return Errors.InvalidBishopPath;
            } else if(Math.abs(fromFileIndex- toFileIndex) != Math.abs(fromRankIndex- toRankIndex)) {
            	return Errors.InvalidBishopPath;
            } else {
                if(fromFileIndex < toFileIndex && fromRankIndex < toRankIndex) {
                    for(int i = 1; i <= toFileIndex -fromFileIndex; i++) {
                        if(board[fromRankIndex+i][fromFileIndex+i] != null) {
                        	return Errors.InvalidBishopPath;
                        }
                    }
                } else if(fromFileIndex < toFileIndex && fromRankIndex > toRankIndex) {
                    for(int i = 1; i <= toFileIndex -fromFileIndex; i++) {
                        if(board[fromRankIndex-i][fromFileIndex+i] != null) {
                        	return Errors.InvalidBishopPath;
                        }
                    }
                } else if(fromFileIndex > toFileIndex && fromRankIndex > toRankIndex) {
                    for(int i = 1; i <= fromFileIndex- toFileIndex; i++) {
                        if(board[fromRankIndex-i][fromFileIndex-i] != null) {
                        	return Errors.InvalidBishopPath;
                        }
                    }
                } else if(fromFileIndex > toFileIndex && fromRankIndex < toRankIndex) {
                    for(int i = 1; i <= fromFileIndex- toFileIndex; i++) {
                        if(board[fromRankIndex+i][fromFileIndex-i] != null) {
                        	return Errors.InvalidBishopPath;
                        }
                    }
                }
            }
        } else if (fromPiece.toString().equalsIgnoreCase("q")) {
            if(fromFileIndex == toFileIndex && toRankIndex == fromRankIndex) {
            	return Errors.InvalidQueenPath;
            } else if(fromFileIndex == toFileIndex) {
                if(toRankIndex >fromRankIndex) {
                    for(int i = fromRankIndex+1; i<= toRankIndex; i++) {
                        if(board[i][fromFileIndex] != null) {
                        	return Errors.InvalidQueenPath;
                        }
                    }
                } else {
                    for(int i = fromRankIndex-1; i>= toRankIndex; i--) {
                        if(board[i][fromFileIndex] != null) {
                        	return Errors.InvalidQueenPath;
                        }
                    }
                }
            } else if(fromRankIndex == toRankIndex){
                if(toFileIndex >fromFileIndex) {
                    for(int i = fromFileIndex+1; i<= toFileIndex; i++) {
                        if(board[fromRankIndex][i] != null) {
                        	return Errors.InvalidQueenPath;
                        }
                    }
                } else {
                    for(int i=fromFileIndex-1; i>=fromFileIndex; i--) {
                        if(board[fromRankIndex][i] != null) {
                        	return Errors.InvalidQueenPath;
                        }
                    }
                }
            } else if(Math.abs(fromFileIndex- toFileIndex) != Math.abs(fromRankIndex- toRankIndex)) {
            	return Errors.InvalidQueenPath;
            } else {
                if (fromFileIndex < toFileIndex && fromRankIndex < toRankIndex) {
                    for (int i = 1; i <= toFileIndex - fromFileIndex; i++) {
                        if (board[fromRankIndex + i][fromFileIndex + i] != null) {
                        	return Errors.InvalidQueenPath;
                        }
                    }
                } else if (fromFileIndex < toFileIndex && fromRankIndex > toRankIndex) {
                    for (int i = 1; i <= toFileIndex - fromFileIndex; i++) {
                        if (board[fromRankIndex - i][fromFileIndex + i] != null) {
                        	return Errors.InvalidQueenPath;
                        }
                    }
                } else if (fromFileIndex > toFileIndex && fromRankIndex > toRankIndex) {
                    for (int i = 1; i <= fromFileIndex - toFileIndex; i++) {
                        if (board[fromRankIndex - i][fromFileIndex - i] != null) {
                        	return Errors.InvalidQueenPath;
                        }
                    }
                } else if (fromFileIndex > toFileIndex && fromRankIndex < toRankIndex) {
                    for (int i = 1; i <= fromFileIndex - toFileIndex; i++) {
                        if (board[fromRankIndex + i][fromFileIndex - i] != null) {
                        	return Errors.InvalidQueenPath;
                        }
                    }
                }
            }
        } else if(fromPiece.toString().equalsIgnoreCase("k")) {
            if(fromFileIndex == toFileIndex && toRankIndex == fromRankIndex) {
            	return Errors.InvalidKingPath;
            } else if (Math.abs(fromFileIndex- toFileIndex) > 1) {
            	return Errors.InvalidKingPath;
            } else if (Math.abs(fromRankIndex- toRankIndex) > 1) {
            	return Errors.InvalidKingPath;
            } else if (board[toRankIndex][toFileIndex] != null) {
            	return Errors.InvalidKingPath;
            }
        } else if (fromPiece.toString().equalsIgnoreCase("p")) {
            if(Math.abs(toRankIndex - toFileIndex) != 1) {
            	return Errors.InvalidPawnPath;
            }
            if(playerTurnIsWhite) {
            	int rankDelta = fromRankIndex - toRankIndex;
                if(rankDelta != 1) {
                	return Errors.InvalidPawnPath;
                } else {
                	if(board[toRankIndex][toFileIndex + 1] == null || board[toRankIndex][toFileIndex - 1] == null) {
                		return Errors.InvalidPawnPath;
                    }
                }
            } else {
            	int rankDelta = fromRankIndex - toRankIndex;
                if(rankDelta != -1) {
                	return Errors.InvalidPawnPath;
                } else {
                    if(board[toRankIndex][toFileIndex - 1] == null || board[toRankIndex][toFileIndex + 1] == null) {

                        return Errors.InvalidPawnPath;
                    }
                }
            }
        }

      //Handle the promotion of a pawn.
        if(fromPiece.toString().equalsIgnoreCase("p")) {
            if(playerTurnIsWhite && toRankIndex == 0) {
                if(pawnPromotionPiece == null) {
                	return Errors.PawnPromotionNull;
                }
                if(!pawnPromotionPiece.toUpperCase().equals(pawnPromotionPiece)) {
                	return Errors.PawnPromotionColorError;
                }
                fromPiece = Piece.valueOf(pawnPromotionPiece);
            } else if(!playerTurnIsWhite && toRankIndex == 7) {
                if(pawnPromotionPiece == null) {
                	return Errors.PawnPromotionNull;
                }
                if(!pawnPromotionPiece.toLowerCase().equals(pawnPromotionPiece)) {
                	return Errors.PawnPromotionColor2Error;
                }
                fromPiece = Piece.valueOf(pawnPromotionPiece);
            }
        }


        
        Piece toPiece = board[toRankIndex][toFileIndex];
        
        if(toPiece == null) {
        	board[toRankIndex][toFileIndex] = fromPiece;
            board[fromRankIndex][fromFileIndex] = null;
            return Errors.PieceNotCaptured;
        }
        
        
        switch(toPiece) {
        case P:
        case N:
        case B:
        case K:
        case Q:
        case R:
        	if(!playerTurnIsWhite) {
        		board[toRankIndex][toFileIndex] = fromPiece;
                board[fromRankIndex][fromFileIndex] = null;
        	}
        	break;
        case p:
        case n:
        case b:
        case r:
        case k:
        case q:
        	if(playerTurnIsWhite) {
        		board[toRankIndex][toFileIndex] = fromPiece;
                board[fromRankIndex][fromFileIndex] = null;
        	}
        	break;
        }
        

        //Change the player's turn
        playerTurnIsWhite = !playerTurnIsWhite;
        
        return Errors.GoodMove;
    }

    private static int calcFileIndex(Character file) {
        // Files are associated as follows: a->7, b->6, c->5, d->4, e->3, f->2, g->1, h->0
        switch(file) {
            case 'a' :
                return 0;
            case 'b' :
                return 1;
            case 'c' :
                return 2;
            case 'd' :
                return 3;
            case 'e' :
                return 4;
            case 'f' :
                return 5;
            case 'g' :
                return 6;
            case 'h' :
                return 7;
            default :
                throw new IllegalArgumentException("File Character '" + file + "' is invalid.");
        }
    }

    private static int calcRankIndex(int rankNumber) {
        // Ranks are associated as follows: 1->7, 2->6, 3->5, 4->4, 5->3, 6->2, 7->1, 8->0
        switch(rankNumber) {
            case 1 :
                return 7;
            case 2 :
                return 6;
            case 3 :
                return 5;
            case 4 :
                return 4;
            case 5 :
                return 3;
            case 6 :
                return 2;
            case 7 :
                return 1;
            case 8 :
                return 0;
            default:
                throw new IllegalArgumentException("Rank Value '" + rankNumber + "' is invalid.");

        }
    }

    private boolean gameIsOver() {
        return isPositionCheckmate() || isPositionStalemate();
    }

    private boolean isPositionStalemate() {
        return false;
    }

    private boolean isPositionCheckmate() {
        return false;
    }

    private void printBoardToConsole() {
        StringBuilder sb = new StringBuilder();
        int rankNum = 8;
        for (Piece[] rank : board) {
            sb.append(rankNum + " ");
            for(Piece piece : rank) {
                if(piece != null) {
                    sb.append(piece);
                } else {
                    sb.append(" ");
                }
            }
            sb.append("\n");
            rankNum--;
        }
        sb.append("  abcdefgh");
        System.out.println(sb);
    }
}
