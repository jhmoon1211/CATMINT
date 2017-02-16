package kr.ac.kpu.catmint_1;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by leesin on 2017-02-06.
 */

public class ServerTask extends AsyncTask<Void, Void, Void> {
    String dstAddress;
    int dstPort;
    String response = "";
    String myMessage = "";

    ServerTask(String addr, int port, String message){
        dstAddress = addr;
        dstPort = port;
        myMessage = message;
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        Socket socket = null;
        myMessage = myMessage.toString();
        try{
            //송신
            socket = new Socket(dstAddress, dstPort);
            OutputStream out = socket.getOutputStream();
            out.write(myMessage.getBytes());

            //수신
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];
            int bytesRead;
            InputStream inputStream = socket.getInputStream();

            while((bytesRead = inputStream.read(buffer)) != -1){
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                response += byteArrayOutputStream.toString("UTF-8");
            }
            response = "서버의 응답 : "+ response;
        }catch (UnknownHostException e){
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        }catch(IOException e){
            e.printStackTrace();
            response = "IOException:" + e.toString();
        }finally{
            if(socket != null){
                try{
                    socket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        //recieveText.setText(response);
        super.onPostExecute(aVoid);
    }
}
