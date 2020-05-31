import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class UDPClient {


    public static void main(String[] args) throws SocketException, IOException {

        BufferedReader clientRead =new BufferedReader(new InputStreamReader(System.in));
        InetAddress IP = InetAddress.getByName("127.0.0.1");
        DatagramSocket clientSocket = new DatagramSocket();
        ArrayList<String> words = new ArrayList<String>(); //words
        byte[] sendbuffer = new byte[1024]; //byte of word sended
        byte[] receivebuffer = new byte[1024]; //byte of word received
        int ifController; //checks for mistakes

        System.out.print("You are Player One. You start the game. You have 10 seconds per word.");

        System.out.print("\nPlayer One: ");     //first word
        String clientData = clientRead.readLine();
        words.add(clientData);
        sendbuffer = clientData.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendbuffer, sendbuffer.length, IP, 1234);
        clientSocket.send(sendPacket);

        while(true) {
            DatagramPacket receivePacket = new DatagramPacket(receivebuffer, receivebuffer.length);
            try {
                clientSocket.setSoTimeout(10000);
                clientSocket.receive(receivePacket);
                continue;
            }
            catch (SocketTimeoutException e)    {
                System.out.println("Time is up.You won the game.") ;

                System.exit(0);



            }

            byte[] serverData = new byte[receivePacket.getLength()];
            System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), serverData, 0, receivePacket.getLength());
            //String serverData = new String( receivePacket.getData(),0,receivePacket.getLength(),"UTF-8");
            //serverData = new String(receivePacket.getData()); //word is received
            String serverMessage = new String(serverData);
            words.add(serverMessage);
            System.out.print("Player Two: " + serverMessage);
            System.out.print("\nPlayer One: ");
            clientData = clientRead.readLine();
            while(true){
                ifController = 0;
                for (int i = 0; i < words.size(); i++) {    //Checks whether the word has been written before
                    if (clientData.equals(words.get(i))) {
                        System.out.println("This word was written. Write another word.");
                        ifController++;
                        break;
                    }
                }
                if(ifController==1) {          //If there is a mistake in the word, another word is written
                    clientData = clientRead.readLine();
                    continue;
                }
                else
                    break;
            }
            //Checks whether the first two letters of the typed word is same as the last two letters of the previous word
            while(true) {
                ifController=0;
                if (serverMessage.substring(serverMessage.length() - 2).equalsIgnoreCase(clientData.substring(0, 2))) {
                    break;
                }
                else {
                    System.out.println("The first two letters of this word are not the same as the last two letters of the previous word.");
                    System.out.println("Write another word.");
                    ifController++;
                }
                if(ifController==1){        //If there is a mistake in the word, another word is written
                    clientData = clientRead.readLine();
                    continue;
                }
            }
            while(true) {   //Second control of word mistake
                ifController = 0;                           //Checks for word mistakes
                for (int i = 0; i < words.size(); i++) {    //Checks whether the word has been written before
                    if (clientData.equals(words.get(i))) {
                        System.out.println("This word was written. Write another word.");
                        ifController++;
                        break;
                    }
                }
                if(ifController==1) {          //If there is a mistake in the word, another word is written
                    clientData = clientRead.readLine();
                    continue;
                }
                else
                    break;
            }
            while(true) {   //Second control of typed or not
                ifController=0;
                if (serverMessage.substring(serverMessage.length() - 2).equalsIgnoreCase(clientData.substring(0, 2))) {
                    break;
                }
                else {
                    System.out.println("The first two letters of this word are not the same as the last two letters of the previous word.");
                    System.out.println("Write another word.");
                    ifController++;
                }
                if(ifController==1){        //If there is a mistake in the word, another word is written
                    clientData = clientRead.readLine();
                    continue;
                }
            }
            words.add(clientData);
            sendbuffer = clientData.getBytes();
            sendPacket = new DatagramPacket(sendbuffer, sendbuffer.length, IP, 1234);
            clientSocket.send(sendPacket);
        }
    }
}