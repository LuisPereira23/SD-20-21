import java.io.Serial;
import java.io.Serializable;
import java.util.*;
public class UserMap implements Serializable {
    @Serial
    private static final long serialVersionUID = 337319187162577375L;
    private Map<Position, List<User>> map;

    public UserMap(){
        this.map = new HashMap<>();
    }

    public void addUser(Position p, User user){
        List<User> users = this.map.get(p);

        if(users==null){
            users = new ArrayList<>();
        }

        boolean existsUser = users.stream().anyMatch(u->u.getUsername().equals(user.getUsername()));
        if(!existsUser){
            users.add(user);
        }
        this.map.put(p,users);
    }

    public String stringMap() {
        StringBuffer result = new StringBuffer();


        for (Map.Entry<Position,List<User>> e : this.map.entrySet()){
            int infected = 0;
            int users = 0;
            Position p = e.getKey();
            List<User> list = e.getValue();
            result.append("Posição: (").append(p.getm()).append(",").append(p.getn()).append(") -> [");

            for(User user : list){

                if(user.getState() == true){
                    infected++;
                } else{
                    users++;
                }
            }

            result.append("#Users: "+users+" | #Infetados: "+infected+" ]\n");
        }
        return result.toString();
    }

}
