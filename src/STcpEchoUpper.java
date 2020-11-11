
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class STcpEchoUpper {
    public static void main(String[] args) throws IOException {
        while (true) {
            try {
                int PORTA = 2000;  // porta di ascolto
                ServerSocket S;

                S = new ServerSocket(PORTA);
                Socket ss = S.accept();
                //ss connesso con client

                BufferedReader br = new BufferedReader(new InputStreamReader(ss.getInputStream()));
                String riga = br.readLine();
                boolean b = false;
                String riconoscimento = "";
                while (!riga.equals("")) {
                    System.out.println(">" + riga);
                    try {
                        if (riga.startsWith("GET")) {
                            b = true;
                            riconoscimento = riga;
                        }
                    } catch (NullPointerException e) {
                        System.out.println(e.toString());
                    }
                    riga = br.readLine();
                }
                String estensione = "";
                riconoscimento = riconoscimento.split(" ")[1].substring(1); //prende la riga, divide per spazi
                //e toglie il "/" iniziale
                estensione = riconoscimento.split("\\.")[1]; //prende l'estensione del file

                byte[] file = new byte[0];
                try {
                    file = Files.readAllBytes(Paths.get(riconoscimento));
                } catch (FileNotFoundException e) {
                    System.out.println("ERROR 404");
                    file = ("<html>\n" +
                            "<head>\n" +
                            "<h1>\n" +
                            "ERROR 404: page not found\n" +
                            "</h1>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "per sapere i file presenti clicca qui:\n" +
                            "<br>" +
                            "http://127.0.0.1:2000/info.html\n" +
                            "</body>" +
                            "</html>").getBytes();
                }
                System.out.println("\n-------------------------------\n");

                OutputStream ops = ss.getOutputStream(); //invio immagini
                invioFileMultimediali(file, ops, estensione);
                br.close();
                S.close();
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
    }

    public static void invioFileMultimediali(byte[] file, OutputStream ops, String estensione) throws IOException {
        ops.write("HTTP/1.1 200 OK\n".getBytes());
        ops.write("Connection: keep-alive\n".getBytes());
        ops.write(("Content Type: "+estensione+"\n").getBytes());
        ops.write(("Content length: "+file.length+"\n").getBytes());
        ops.write("\n".getBytes());
        ops.write(file);
    }
}
