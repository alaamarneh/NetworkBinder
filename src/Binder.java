import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class RequestHandler implements Callable<Void> {
    private Socket socket;
    private PrintStream writer;
    RequestHandler(Socket socket){
        this.socket = socket;
    }
    @Override
    public Void call() throws Exception {
        System.out.println("connect");//test
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintStream(socket.getOutputStream());
        String line;
        if ( (line=reader.readLine()) != null ){
            System.out.println("binder: receive: " + line);

            JSONObject jsonObjectRequest = new JSONObject(line);
            if(jsonObjectRequest.has("op")){// from server
                handleServerRequest(jsonObjectRequest);

            }else if(jsonObjectRequest.has("fun")){ // from client
                handleClientRequest(jsonObjectRequest);
            }




        }
        socket.close();

        return null;
    }

    private void handleClientRequest(JSONObject jsonObjectRequest) throws Exception {
        System.out.println("handleClientRequest");
        String funName = jsonObjectRequest.getString("fun");
        ServerModel serverModel =  new DBController().getServer(funName);
        if(serverModel != null){ // server exists
            JSONObject jsonObjectResponse = new JSONObject();
            jsonObjectResponse.put("status","found");
            jsonObjectResponse.put("serverIp",serverModel.getIp());
            jsonObjectResponse.put("serverPort",serverModel.getPort());
            writer.println(jsonObjectResponse.toString());
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status","failed");
            writer.println(jsonObject.toString());
        }
    }

    private void handleServerRequest(JSONObject jsonObjectRequest)throws Exception {
        System.out.println("handleServerRequest");
        /*
          jsonRequest template:
          {
           'op':'register',
           'functions':['Add','Mul'],
           'serverIp':3000,
           'serverIp':"localhost'
           }
         */
        String op = jsonObjectRequest.getString("op");
        if(op.equalsIgnoreCase("register")){ //register server
            ServerModel serverModel = new ServerModel();
            serverModel.setPort(jsonObjectRequest.getInt("serverPort"));
            serverModel.setIp(jsonObjectRequest.getString("serverIp"));

            List<String> funNames = new ArrayList<>();
            JSONArray jsonArrayFunctions = jsonObjectRequest.getJSONArray("functions");
            for (int i =0;i<jsonArrayFunctions.length();i++){
                funNames.add(jsonArrayFunctions.getString(i));
            }

            new DBController().registerServer(funNames,serverModel);

        }

    }
}
public class Binder {
    public static void main(String[] args) throws Exception{
        System.out.println("start binder");
        ServerSocket serverSocket = new ServerSocket(4000);
        ExecutorService service = Executors.newFixedThreadPool(5);
        while (true){
            Socket socket = serverSocket.accept();
            service.submit(new RequestHandler(socket));
        }

    }
}
