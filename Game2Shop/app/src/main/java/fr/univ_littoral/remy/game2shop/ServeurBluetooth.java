package fr.univ_littoral.remy.game2shop;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

public class ServeurBluetooth extends Thread {
    private final BluetoothServerSocket blueServerSocket;
    private final static String NOM = "GameApp";
    private static final UUID MON_UUID = UUID.fromString("7be1fcb3-5776-42fb-91fd-2ee7b5bbb86d");

    BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();

    public ServeurBluetooth() {
        // On utilise un objet temporaire qui sera assigné plus tard à blueServerSocket car blueServerSocket est "final"
        BluetoothServerSocket tmp = null;
        try {
            // MON_UUID est l'UUID (comprenez identifiant serveur) de l'application. Cette valeur est nécessaire côté client également !
            tmp = blueAdapter.listenUsingRfcommWithServiceRecord(NOM, MON_UUID);
        } catch (IOException e) { }
        blueServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket blueSocket = null;
        // On attend une erreur ou une connexion entrante
        while (true) {
            try {
                blueSocket = blueServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            // Si une connexion est acceptée
            if (blueSocket != null) {
                // On fait ce qu'on veut de la connexion (dans un thread séparé), à vous de la créer
                manageConnectedSocket(blueSocket);
                cancel();
                break;
            }
        }
    }

    private void manageConnectedSocket(BluetoothSocket blueSocket) {
        System.out.println(blueSocket.isConnected());
    }

    // On stoppe l'écoute des connexions
    public void cancel() {
        try {
            blueServerSocket.close();
        } catch (IOException e) { }
    }
}
