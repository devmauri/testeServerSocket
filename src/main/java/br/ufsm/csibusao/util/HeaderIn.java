
package br.ufsm.csibusao.util;
/**
 *
 * @author mauri
 */
public class HeaderIn {
    private ENUMERADORES.COMMAND command;
    private ENUMERADORES.PROTOCOL protocol;
    private String resource;
    
    public HeaderIn(String req){
        FilterHeader(req);
    }
    
    private void FilterHeader(String req){
        String[] lines = req.split("\n");
        
        this.parseHeader(lines[0]);
    }
    
    public void parseHeader(String line0){
        String[] head = line0.split(" ");
        
        this.command = ENUMERADORES.COMMAND.valueOf(head[0]);        
        this.resource = head[1];
        //this.protocol = PROTOCOL.valueOf(head[2]);
        this.protocol = ENUMERADORES.PROTOCOL.DEFAULT;
    }

    public ENUMERADORES.COMMAND getCommand() {
        return command;
    }

    public ENUMERADORES.PROTOCOL getProtocol() {
        return protocol;
    }

    public String getResource() {
        return resource;
    }
    
    
}
