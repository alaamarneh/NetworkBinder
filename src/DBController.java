import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBController {
    static Map<String,ServerModel> serverMap = new HashMap<>();
    public ServerModel getServer(String fun){
        //test
//        ServerModel serverModel = new ServerModel("localhost",3000);
//        return  serverModel;
        ServerModel serverModel = serverMap.getOrDefault(fun.toLowerCase(),null);
        if(serverModel == null)
            return null;
        long t = new Date().getTime() - serverModel.getAddedTime();
        if( t > 5 * 1000){
            System.out.println("DBController: server died");
            return null;
        }
        return serverModel;
    }
    public void registerServer(List<String> functions, ServerModel server){
        server.setAddedTime(new Date().getTime());
        System.out.println("DBController: register server " + server.getIp());
        for (String fun :
                functions) {
            serverMap.put(fun.toLowerCase(), server);
        }
    }
}
