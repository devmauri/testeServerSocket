package br.ufsm.csibusao.busao;

import br.ufsm.csibusao.util.Busao;
import br.ufsm.csibusao.util.Print;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mauri
 */
public class ServerBusHttp {

    private List<ServerService> threads;
    private final int maxReqSimultanea = 80;

    private final int sizeBufferSkt = 1024;
    private final int port = 80;

    private Print print;
    private ServerSocket sskt;

    private Busao bb;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ServerBusHttp sb = new ServerBusHttp();
    }

    private ServerBusHttp() {
        print = new Print("Servidor Onibus", "ServerBusHttp");
        print.Inform("Inicio.");
        this.threads = new ArrayList<ServerService>();
        this.bb = new Busao();
        Loop();
    }

    private void Loop() {
        boolean sair = false;
        while (!sair) {
            try {//instancia serversocket na porta
                sskt = new ServerSocket(port);
                while (!sair) {
                    Socket skt = Escuta(sskt);
                    if (skt != null) {
                        NovaThreadSocket(skt); //cria nova thread para tratar req.
                    }
                }
                print.printLog();
                sskt.close();
            } catch (Exception e) {
                synchronized (print) {
                    print.InformError("Erro no Loop: " + e.toString());
                }
            }

        }
    }

    private Socket Escuta(ServerSocket sskt) {
        Socket skt = null;
        try {
            synchronized (print) {
                print.InformLugar("Aguarnando nova conexão na porta " + port, "Escuta->Loop");
            }
            skt = sskt.accept();
        } catch (Exception e) {
            synchronized (print) {
                print.InformError("Erro ao escutar: " + e.toString());
            }
        }
        return skt;
    }

    private void NovaThreadSocket(Socket skt) {
        //instancia nova thread. Se chegou ao limite aguarda.Uzar monitor
        while (threads.size() >= maxReqSimultanea);

        synchronized (threads) {
            threads.add(new ServerService(skt, sizeBufferSkt, threads, print, bb));
            synchronized (print) {
                print.InformLugar("Criada nova thread numero " + threads.size()
                        + " na lista", "NovaThreadSoket->Loop");
            }
        }
    }

}
