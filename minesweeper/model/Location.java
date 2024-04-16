package minesweeper.model;

public class Location{
    private final int row;
    private final int col;
    private boolean isCovered;
    private Artifact artifact;

    public Location(int row, int col, Artifact artifact){
        this.row = row;
        this.col = col;
        this.isCovered = true;
        this.artifact = artifact;
    }

    public int getRow(){
        return this.row;
    }

    public int getCol(){
        return this.col;
    }

    public Artifact getPiece() {
        return this.artifact;
    }

    public boolean isCovered(){
        return this.isCovered;
    }
    
    public void setArtifact(Artifact artifact){
        this.artifact = artifact;
    }

    public void uncover(){
        this.isCovered = false;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Location){
            Location other = (Location)obj;
            return this.hashCode() == other.hashCode();
        }else{
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + this.row + ", " + this.col + ")";
    }
}