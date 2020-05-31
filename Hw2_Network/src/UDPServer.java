import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;

public class UDPServer {


    public static void main(String[] args) throws SocketException, IOException {

        DatagramSocket serverSocket = new DatagramSocket(1234);
        ArrayList<String> words = new ArrayList<String>();
        byte[] receivebuffer = new byte[1024];
        byte[] sendbuffer  = new byte[1024];
        int ifController; //checks for mistakes

        System.out.print("You are Player Two. Player One starts the game. You have 10 seconds per word.\n");

        while(true) {
            DatagramPacket receivePacket = new DatagramPacket(receivebuffer, receivebuffer.length);
            try {
                serverSocket.setSoTimeout(10000);   //If the word is not received within 10 seconds, the system will shut down.
                serverSocket.receive(receivePacket);
            }
            catch (SocketTimeoutException e){
                System.out.println("Time is up. You won the game.");
                System.exit(0);
            }
            InetAddress IP = receivePacket.getAddress();
            int portno = receivePacket.getPort();
            byte[] clientData = new byte[receivePacket.getLength()];
            System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), clientData, 0, receivePacket.getLength());
            //String clientData = new String( receivePacket.getData(),0,receivePacket.getLength(),"UTF-8");
            //String clientData = new String(receivePacket.getData()); //word is received
            String clientMessage = new String(clientData);
            System.out.print("Player One: "+ clientMessage);
            words.add(clientMessage);
            System.out.print("\nPlayer Two: ");
            BufferedReader serverRead = new BufferedReader(new InputStreamReader (System.in) );
            String serverData = serverRead.readLine();
            while(true){
                ifController = 0;
                for (int i = 0; i < words.size(); i++) {    //Checks whether the word has been written before
                    if (serverData.equals(words.get(i))) {
                        System.out.println("This word was written. Write another word.");
                        ifController++;
                        break;
                    }
                }
                if(ifController==1) {          //If there is a mistake in the word, another word is written
                    serverData = serverRead.readLine();
                    continue;
                }
                else
                    break;
            }
            //Checks whether the first two letters of the typed word is same as the last two letters of the previous word
            while(true) {
                ifController=0;
                if (clientMessage.substring(clientMessage.length() - 2).equalsIgnoreCase(serverData.substring(0, 2))) {
                    break;
                }
                else {
                    System.out.println("The first two letters of this word are not the same as the last two letters of the previous word.");
                    System.out.println("Write another word.");
                    ifController++;
                }
                if(ifController==1){        //If there is a mistake in the word, another word is written
                    serverData = serverRead.readLine();
                    continue;
                }
            }
            while(true) {   //Second control of word mistake
                ifController = 0;                           //Checks for word mistakes
                for (int i = 0; i < words.size(); i++) {    //Checks whether the word has been written before
                    if (serverData.equals(words.get(i))) {
                        System.out.println("This word was written. Write another word.");
                        ifController++;
                        break;
                    }
                }
                if(ifController==1) {          //If there is a mistake in the word, another word is written
                    serverData = serverRead.readLine();
                    continue;
                }
                else
                    break;
            }
            while(true) {   //Second control of typed or not
                ifController=0;
                if (clientMessage.substring(clientMessage.length() - 2).equalsIgnoreCase(serverData.substring(0, 2))) {
                    break;
                }
                else {
                    System.out.println("The first two letters of this word are not the same as the last two letters of the previous word.");
                    System.out.println("Write another word.");
                    ifController++;
                }
                if(ifController==1){        //If there is a mistake in the word, another word is written
                    serverData = serverRead.readLine();
                    continue;
                }
            }
            sendbuffer = serverData.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendbuffer, sendbuffer.length, IP,portno);
            serverSocket.send(sendPacket);
        }
    }
}