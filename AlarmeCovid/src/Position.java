public class Position {
    private int m;
    private int n;

    public Position(int m,int n){
        this.m = m;
        this.n = n;
    }

    public Position(Position p){
        this.m =p.getm();
        this.n =p.getn();
    }

    public int getm(){
        return m;
    }

    public int getn(){
        return n;
    }

    public boolean equal(Position p){
        if (p.getm() == this.getm() && p.getn() == this.getn())
            return true;
         else
            return false;
    }

    public Position clone(){

        return new Position(this);
    }
}
