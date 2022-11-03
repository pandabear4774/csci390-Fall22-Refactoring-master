import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Move {
	int startFile, startRank, endFile, endRank;
	
	char startFileChar, startRankChar, endFileChar, endRankChar;
	
	String promotionPiece = null;
	
	boolean capture;
	
	int length;
	
	boolean validMove = true;
	public Move(String move) {
		Pattern movePattern = Pattern.compile("^[a-h][1-8][-x][a-h][1-8][rnbqRNBQ]{0,1}");
        Matcher moveMatcher = movePattern.matcher(move);
        
        length = move.length();
        if(!moveMatcher.find()) {
			validMove = false;
		}
        
        startFile = calcFileIndex(move.charAt(0));

        startRank = calcRankIndex(move.charAt(1) - '0');
        
        endFile = calcFileIndex(move.charAt(3));
        
        endRank = calcRankIndex(move.charAt(4) - '0');
        
        capture = (move.charAt(2) == 'x');
        
        startFileChar = move.charAt(0);
        
        startRankChar = move.charAt(1);
        
        endFileChar = move.charAt(3);
        
        endRankChar = move.charAt(4);
        
        if(move.length() == 6) {
        	promotionPiece = String.valueOf(move.charAt(5));
        }
	}
	public Move(int startFile, int startRank, int endFile, int endRank) {
		this.startFile = startFile;
		this.startRank = startRank;
		this.endFile = endFile;
		this.endRank = endRank;
	}
	public boolean valid() {
		return validMove;
	}
	private int calcFileIndex(Character file) {
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

    private int calcRankIndex(int rankNumber) {
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
}
