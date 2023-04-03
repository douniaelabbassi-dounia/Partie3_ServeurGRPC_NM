package ma.enset.server;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import ma.enset.services.ServiceNM;

import java.util.Random;

public class ServeurGRPC {

    public static void main(String[] args) throws Exception {
        int randomNum = new Random().nextInt(1000) + 1;
        Server server = ServerBuilder.forPort(500)
                .addService(new ServiceNM(randomNum))
                .build();
        server.start();
        System.out.println("Le serveur GRPC est démarré : " + randomNum);
        server.awaitTermination();
    }
}

