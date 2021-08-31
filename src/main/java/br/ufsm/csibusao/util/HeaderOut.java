package br.ufsm.csibusao.util;

/**
 *
 * @author mauri
 */
public class HeaderOut {
    private ENUMERADORES.PROTOCOL protocol;
    private ENUMERADORES.RESPONSE response;
    private String tipo;
    private String charset;
    
    public HeaderOut(ENUMERADORES.RESPONSE r){
        this.response = r;
        this.protocol = ENUMERADORES.PROTOCOL.DEFAULT;
        this.tipo = "Content-Type: text/html";
        this.charset = "charset-UTF-8";
    }
    
    @Override
    public String toString(){
        String res = protocol.getValue();
        res+= " "; // primeiro espaco sinaliza o fim do protocolo
        res+= response.getCod();
        res+= " "; // segundo espaco sinaliza o fim do codigo
        res+= response.getStatus();
        res+= "\n";// primeira quebra de linha o fim do cabeçalho
        res+= tipo;
        res+=";"; // ; pra indicar o final do tipo
        res+=" "; // espaço pra indicar o inicio do charset
        res+=charset;
        res+= "\n\n";// duas quebra de linhas indica inicio do html
        return res;
    }

    public ENUMERADORES.PROTOCOL getProtocol() {
        return protocol;
    }

    public ENUMERADORES.RESPONSE getResponse() {
        return response;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCharset() {
        return charset;
    }
    
    
}
