import java.util.*;
public class UserMap {
    private Map<Position, List<User>> map;

    public UserMap(){
        this.map = new HashMap<>();
    }

    public void addUser(Position p, User user){
        List<User> users = this.map.get(p);

        if(users == null){
            users = new ArrayList<>();
        }
        if(!users.stream().anyMatch(u->u.getUsername().equals(user.getUsername()))){
            users.add(user);
        }
        this.map.put(p,users);
    }


    public Boolean checkEmpty(Position p){
        List<User> users = this.map.get(p);
        boolean result = true;
        if(users!=null){
            Iterator<User> iterator = users.iterator();
            while (iterator.hasNext()&&result){
                User user = iterator.next();
                result = !user.getCurrent().equals(p);
            }
        }
        return result;
    }


}
