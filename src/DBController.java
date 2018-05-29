import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBController {
    static Map<String,ServerModel> serverMap = new HashMap<>();
    public ServerModel getServer(String fun){
        //test
//        ServerModel serverModel = new ServerModel("localhost",3000);
//        return  serverModel;
        return serverMap.getOrDefault(fun.toLowerCase(),null);
    }
    public void registerServer(List<String> functions, ServerModel server){
        System.out.println("DBController: register server " + server.getIp());
        for (String fun :
                functions) {
            serverMap.put(fun.toLowerCase(), server);
        }
    }
}
