package fr.univ_littoral.remy.game2shop;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class ClientBluetooth extends Thread {
    private final BluetoothSocket blueSocket;
    private final BluetoothDevice blueDevice;
    private OutputStream sendStream;
    private static final UUID MON_UUID = UUID.fromString("7be1fcb3-5776-42fb-91fd-2ee7b5bbb86d");

    BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();

    public ClientBluetooth(BluetoothDevice device) {
        // On utilise un objet temporaire car blueSocket et blueDevice sont "final"
        BluetoothSocket tmp = null;
        blueDevice = device;

        // On récupère un objet BluetoothSocket grâce à l'objet BluetoothDevice
        try {
            // MON_UUID est l'UUID (identifiant serveur) de l'application. Cette valeur est nécessaire côté serveur également !
            tmp = device.createRfcommSocketToServiceRecord(MON_UUID);
        } catch (IOException e) { }
        blueSocket = tmp;
    }

    public void run() {
        // On annule la découverte des périphériques (inutile puisqu'on est en train d'essayer de se connecter)
        blueAdapter.cancelDiscovery();
        Log.w("ListDeviceBluetooth", "Initialisation de la connexion");
        try {
            // On se connecte. Cet appel est bloquant jusqu'à la réussite ou la levée d'une erreur
            blueSocket.connect();
            Log.w("ListDeviceBluetooth", "Appairage effectué");
        } catch (IOException connectException) {
            // Impossible de se connecter, on ferme la socket et on tue le thread
            try {
                blueSocket.close();
            } catch (IOException closeException) { }
            return;
        }

        // Utilisez la connexion (dans un thread séparé) pour faire ce que vous voulez
        manageConnectedSocket(blueSocket);
    }

    private void manageConnectedSocket(BluetoothSocket blueSocket) {
        System.out.println(blueSocket.isConnected());
    }

    /*
    public void sendData(String data) {
        try {
            // On écrit les données dans le buffer d'envoi
            sendStream.write(data.getBytes());

            // On s'assure qu'elles soient bien envoyées
            sendStream.flush();
            System.out.println("Données envoyées");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

    // Annule toute connexion en cours et tue le thread
    public void cancel() {
        try {
            blueSocket.close();
        } catch (IOException e) { }
    }
}
