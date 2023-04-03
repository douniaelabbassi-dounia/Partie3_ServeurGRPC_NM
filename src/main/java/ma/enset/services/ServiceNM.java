package ma.enset.services;
import io.grpc.stub.StreamObserver;
import ma.enset.stubs.Jeu;
import ma.enset.stubs.ServiceGrpcNM;
import java.util.HashMap;
import java.util.Map;

public class ServiceNM extends ServiceGrpcNM.NbrMgServiceImplBase {

    private static int lastClientId=0;

    private Map<Integer, StreamObserver<Jeu.Response>> clients = new HashMap<>();
    public  boolean fin;
    private int secretNumber;
    public ServiceNM(int randomNum) {
        super();
        this.secretNumber=randomNum;
    }

    @Override
    public StreamObserver<Jeu.Request> nbrMagique(StreamObserver<Jeu.Response> responseObserver) {

        int clientID= ++lastClientId;
        clients.put(clientID, responseObserver);
        Jeu.Response response= Jeu.Response.newBuilder()
                .setEvent("MARHABA Bonne Chance !!!")
                .build();
        responseObserver.onNext(response);

        return  new StreamObserver<Jeu.Request>() {
            @Override
            public void onNext(Jeu.Request request) {
            int NbrM =request.getNbrMg();
            String sender=request.getUser();
                    if(NbrM>secretNumber){
                        Jeu.Response response1= Jeu.Response.newBuilder()
                                .setUser(sender)
                                .setEvent("Votre nombre est plus grand")
                                .build();
                        responseObserver.onNext(response1);

                    } else if (NbrM <secretNumber) {
                        Jeu.Response response2= Jeu.Response.newBuilder()
                                .setUser(sender)
                                .setEvent("Votre nombre est plus petit ")
                                .build();
                        responseObserver.onNext(response2);
                    }
                    else {

                        Jeu.Response response3= Jeu.Response.newBuilder()
                                .setUser(sender )
                                .setEvent(" OOhhh "+sender+" BRAVO vous avez gagné  ")
                                .build();
                        responseObserver.onNext(response3);

                        for (Map.Entry<Integer, StreamObserver<Jeu.Response>> entry : clients.entrySet()) {
                            int clientId = entry.getKey();
                            StreamObserver<Jeu.Response> observer = entry.getValue();
                            if (clientId != clientID) {
                                Jeu.Response response4= Jeu.Response.newBuilder()
                                        .setUser("client X ")
                                        .setEvent(sender+ " a trouvez le nombre Magique")
                                        .build();
                                observer.onNext(response4);

                            }
                            observer.onCompleted();
                        }
                    }

            }

            @Override
            public void onError(Throwable throwable) {
           System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
           responseObserver.onCompleted();
            }
        };


    }
}
