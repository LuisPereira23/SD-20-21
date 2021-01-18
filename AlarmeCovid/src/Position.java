import java.io.Serial;
import java.io.Serializable;

public class Position implements Serializable {
    @Serial
    private static final long serialVersionUID = 2558133998386380997L;
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

    public int hashCode() {
        int hash = 17;
        hash = hash * 31 + this.getm();
        hash = hash * 31 + this.getn();
        return hash;
    }

@Override
    public boolean equals(Object obj) {
        if(this==obj) {
            return true;
        }
        if(obj==null||this.getClass()!=obj.getClass()) {
            return false;
        }
        Position p = (Position) obj;
        int hash = 17;
        hash = hash * 31 + this.getm();
        hash = hash * 31 + this.getn();

        int hashp = 17;
        hashp = hashp * 31 + p.getm();
        hashp = hashp * 31 + p.getn();

        return (hash == hashp);
    }

    public Position clone(){

        return new Position(this);
    }

    public String toString() {
        return "("+this.m+","+this.n+")";
    }
}
