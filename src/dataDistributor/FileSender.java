package dataDistributor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Tanguy
 */
public class FileSender {

    CNConnection connexion;

    public FileSender(String address, int port) throws IOException {
        connexion = new CNConnection(address, port);
    }

    public void close() {
        connexion.close();
    }

    public void sendFile(File f, int partSize) throws FileNotFoundException, IOException {

        long start = System.currentTimeMillis();
        long fileSize = 0;
        BufferedReader bufr = null;
        try{
        	bufr = new BufferedReader(new FileReader(f));
            char[] cbuf = new char[partSize];
            int nbPart = 0;
            String buffer1 = new String();
            String buffer2 = new String();
            int rv = bufr.read(cbuf, 0, partSize);
            while (rv != -1) {
                fileSize += rv;
                int id_n = rv - 1;
                while (id_n >= 0) {
                    if (cbuf[id_n] == '\n') {
                        break;
                    }
                    id_n--;
                }
                if (id_n >= 0) {
                    buffer1 = buffer2 + String.valueOf(cbuf, 0, id_n + 1);
                    buffer2 = String.valueOf(cbuf, id_n + 1, rv - id_n - 1);
                } else {
                    buffer1 = buffer2 + String.valueOf(cbuf, 0, rv);
                    buffer2 = "";
                }

                connexion.sendData(f.getName(), buffer1);
                nbPart++;
                cbuf = new char[partSize];
                rv = bufr.read(cbuf, 0, partSize);

            }
            System.out.println("'" + f.getName() + "' (" + Data_size_formatter(fileSize) + ") sent in " 
            		+ nbPart + " Parts :: total time :" + time_formatter(System.currentTimeMillis() - start));
        } finally {
        	if(bufr != null){
        		bufr.close();
        	}
        }
    }

    public void sendFile(String filePath, int partSize) throws FileNotFoundException, IOException {
        File f = new File(filePath);
        sendFile(f, partSize);
    }

    public static String Data_size_formatter(double size) {
        DecimalFormat df = new DecimalFormat("0");
        if (Math.floor(size / 1024) == 0) {
            return df.format(size) + " o";
        } else if (Math.floor(size / (1024 * 1024)) == 0) {
            return df.format(size / 1024) + " Ko";
        } else if (Math.floor(size / (1024 * 1024 * 1024)) == 0) {
            return df.format(size / (1024 * 1024)) + " Mo";
        } else {
            return df.format(size / (1024 * 1024 * 1024)) + " Go";
        }
    }

    public static String time_formatter(long timeMillis) {
        DecimalFormat df = new DecimalFormat("00");
        if (timeMillis / 1000 == 0) {
            return timeMillis + " ms";
        } else {
            int totalSeconds = (int) (timeMillis / 1000);
            int seconds = 0;
            int minuts = 0;
            int hours = 0;

            hours = totalSeconds / 3600;
            minuts = (totalSeconds % 3600) / 60;
            seconds = totalSeconds % 60;

            return (hours > 0 ? df.format(hours) + " h " + df.format(minuts) + " m " + df.format(seconds) + " s" : (minuts > 0 ? df.format(minuts) + "m " + df.format(seconds) + " s" : df.format(seconds) + " s"));
        }
    }

}
