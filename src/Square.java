public class Square {
    private int rankIndex;
    private int fileIndex;

    public Square(int file, int rank) {
    	fileIndex = file;
    	rankIndex= rank;
    }
    public int getRankIndex() {
        return rankIndex;
    }

    public void setRankIndex(int rankIndex) {
        this.rankIndex = rankIndex;
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public void setFileIndex(int fileIndex) {
        this.fileIndex = fileIndex;
    }


}
