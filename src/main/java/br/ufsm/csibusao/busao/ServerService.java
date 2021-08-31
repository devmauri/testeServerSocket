package br.ufsm.csibusao.busao;

import br.ufsm.csibusao.util.Passagem;
import br.ufsm.csibusao.util.Busao;
import br.ufsm.csibusao.util.HeaderIn;
import br.ufsm.csibusao.util.HeaderOut;
import br.ufsm.csibusao.util.Pagina;
import br.ufsm.csibusao.util.Print;
import br.ufsm.csibusao.util.ENUMERADORES.RESPONSE;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mauri
 */
public class ServerService implements Runnable {

    private int sizeBufferSkt;

    private List<ServerService> threads;
    private Print print;
    private Socket skt;
    private Busao bb;

    public ServerService(Socket skt, int sizeBufferSkt, List<ServerService> threads, Print print, Busao bb) {

        this.skt = new Socket();
        this.skt = skt;
        this.sizeBufferSkt = sizeBufferSkt;
        this.threads = threads;
        this.print = print;
        this.bb = bb;
        Thread th = new Thread(this);
        th.start();
    }

    @Override
    public void run() {
        try {
            //pega dados de entrada
            InputStream in = skt.getInputStream();
            //Transforma dados brutos em uma string
            String req = RetornaDados(in);
            //verifica cabeçalho de entrada
            HeaderIn hi = new HeaderIn(req);
            //verifica se recurso é valido
            //pega array de bytes para serem enviados
            byte[] envio = VerificaRota(hi.getResource());
            //Pega canal de envio via outputstream
            OutputStream out = skt.getOutputStream();
            //envia resposta
            out.write(envio);
            //força o envio do buffer para a rede.
            if (out != null) {
                out.flush();
            }

        } catch (Exception e) {
            synchronized (print) {
                print.InformError("Erro na thread "+ Thread.currentThread().getName() +e.toString());
            }
        } finally {
            synchronized (threads){
                threads.remove(this);
                synchronized (print) {
                print.Inform("Removendo thread "+ Thread.currentThread().getName() +" da lista."); 
                print.GravarLog(Thread.currentThread().getName());   
                //obs - implementar novo sistema de gravação com trhear dentro de print.
                
             }
            }            
            
        }
    }

    private boolean ReservaAssento(int assentoId, String nome) {
        boolean reservou = false;
        Passagem poltrona = bb.lugares.get(assentoId - 1);
        synchronized (poltrona) {
            //trava somente a pontrona, não a lista inteira.
            //entrando na região critica
            synchronized (print) {
                    print.InformLugar("Na da regiao critica, poltrona "+ (assentoId - 1)+
                            " na rotina "+Thread.currentThread().getName(),"ReservaAssento->Run");
                }
            if (poltrona.assento.status == Passagem.LUGAR.VAGO) {
                //se vago, reserva guardandodata/hora de quem reservou .
                poltrona.assento.status = Passagem.LUGAR.RESERVADO;
                poltrona.horaCompra.setTime(new Date().getTime());
                poltrona.passagueiro = nome;
                reservou = true;
                poltrona.ip = skt.getInetAddress().getHostAddress();
            }
            synchronized (print) {
                    print.InformLugar("\n*****Reservado poltrona "+poltrona.assento.id+
                            "Pelo IP:"+poltrona.ip + " na Thread "+
                            Thread.currentThread().getName()+"\n", "ReservaAssento->Run");
                    print.InformLugar("Saindo da regiao critica, poltrona "+ (assentoId - 1)+
                            " na rotina "+Thread.currentThread().getName(),"ReservaAssento->Run");
                }
            //saindo da regiao critica
        }
        return reservou;
    }

    private boolean IdValido(String id) {
        int idInt = Integer.parseInt(id);
        return (idInt > 0 && idInt <= bb.lugares.size());
    }

    private String PegaParametosGet(String rota, StringBuilder acao, StringBuilder nome, StringBuilder assentoId) {
        String recurso = new String(rota);
        if (recurso == "/" || recurso == "/lugares.html"
                || recurso == "/reservar.html") {
            return recurso;
        }
        if (recurso.contains("/lugares.html?")) {
            int inicio = recurso.indexOf("assento=") + 8;
            int fim = recurso.indexOf("nome=");
            assentoId.append( recurso.substring(inicio, fim - 1));
            if (!IdValido(assentoId.toString())) {
                return "Erro no id do Assento";
            }

            inicio = fim + 5;
            fim = recurso.indexOf("acao=");
            nome.append(recurso.substring(inicio, fim - 1));
            if (nome.toString().isEmpty()) {
                return "Nome vazio";
            }
            acao.append(recurso.substring(fim + 5));
            if (!acao.toString().equalsIgnoreCase("reservar")) {
                return "Ação desconhecida";
            }
            return "reservar";
        } else if (recurso.contains("/reservar.html?")) {
            int inicio = recurso.indexOf("reservar=");
            assentoId.append((recurso.substring(inicio + 9)));
            //verifica se assento é valido para esse onibus.
            if (IdValido(assentoId.toString())) {
                int assentoIdint = Integer.parseInt(assentoId.toString());
                //verifica se assento já não esta ocupado por outra thread.
                if (bb.lugares.get(assentoIdint-1).assento.status == Passagem.LUGAR.RESERVADO) {
                    return "lugares-nok";
                } else {
                    return "/reservar.html";
                }
            }
        }
        return recurso;
    }

    private byte[] VerificaRota(String rota) throws IOException {
        byte[] envio = null;
        HeaderOut ho = null;
        StringBuilder acao = new StringBuilder();        
        StringBuilder  nome = new StringBuilder();
        StringBuilder assentoId = new StringBuilder();
        String resposta = "Para reservar clique no botão verde.";
        String enviar = PegaParametosGet(rota, acao, nome, assentoId);
        if(assentoId.toString().isEmpty()) assentoId.append("Informe um Lugar vago.");
        
        switch (enviar) {
            case "/"://pagina inicial
            case "/lugares.html":
                synchronized (print) {
                    print.InformLugar("Pagina Inicial na rotina "+ Thread.currentThread().getName(),"VerificaRota->Run");
                }
                ho = new HeaderOut(RESPONSE.OK);

                envio = (ho.toString()
                        + Pagina.MontaPaginaInicial(bb, resposta)).getBytes();

                break;
            case "/reservar.html"://pagina informar nome
                synchronized (print) {
                    print.InformLugar("Pagina dois na rotina "+ Thread.currentThread().getName(),"VerificaRota->Run");
                }
                ho = new HeaderOut(RESPONSE.OK);

                envio = (ho.toString()
                        + Pagina.MontaPaginaDois(assentoId.toString())).getBytes();
                break;
            case "reservar"://tenta reservar assento deesejado                
                if (ReservaAssento(Integer.parseInt(assentoId.toString()), nome.toString())) {
                    synchronized (print) {
                    print.InformLugar("Pagina Inicial-ok na rotina "+ Thread.currentThread().getName(),"VerificaRota->Run");
                }
                    ho = new HeaderOut(RESPONSE.OK);
                    resposta = "ok";
                    envio = (ho.toString()
                            + Pagina.MontaPaginaInicial(bb, resposta)).getBytes();
                    break;
                }
            case "lugares-nok"://lugar já reservado por outra thread
                synchronized (print) {
                    print.InformLugar("Pagina Inicial-Nok na rotina "+ Thread.currentThread().getName(),"VerificaRota->Run");
                }
                ho = new HeaderOut(RESPONSE.OK);
                resposta = "erro";
                envio = (ho.toString()
                        + Pagina.MontaPaginaInicial(bb, resposta)).getBytes();
                break;
            default://recurso não encontrado
                synchronized (print) {
                    print.InformLugar("Rota/Recurso não encontrado. "+ Thread.currentThread().getName(),"VerificaRota->Run");
                }
                ho = new HeaderOut(RESPONSE.NOTFOUND);
                envio = (ho.toString()
                        + "<html><body>RESOURCE NOT FOUND IN APLICATION.</body></html>").getBytes();
                break;
        }
        return envio;
    }

    private String RetornaDados(InputStream in) throws IOException {
        String res = "";
        //cria buffer para armazenamento
        byte[] bf = new byte[sizeBufferSkt];
        int len = in.read(bf);
        if (len > 0) {
            res = new String(bf, 0, len);
        }
        return res;
    }

}
