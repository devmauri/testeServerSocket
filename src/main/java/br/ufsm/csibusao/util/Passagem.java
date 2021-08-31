package br.ufsm.csibusao.util;

import java.util.Date;

/**
 *
 * @author mauri
 */
public class Passagem {
     public String ip="";
        public String passagueiro="-";
        public Assento assento;
        public Date horaCompra = new Date();
        public Passagem(int id){
            assento = new Assento(id);
            horaCompra.setTime(new Date().getTime());
        }
        
    public class Assento {
        public int id;
        public LUGAR status=LUGAR.VAGO;
        public Assento(int i) {this.id=i;}        
    }
    
    public enum LUGAR {VAGO , RESERVADO};   
    
}
