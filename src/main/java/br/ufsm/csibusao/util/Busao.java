package br.ufsm.csibusao.util;

import br.ufsm.csibusao.busao.ServerBusHttp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mauri
 */
public class Busao {
     private int capacidade=30;
       public List<Passagem> lugares = new ArrayList<Passagem>();
       public Busao(){
           EsvaziaBusao();
       }
       public Busao(int capacidade){
           this.capacidade=capacidade;
           EsvaziaBusao();
       }
       private void EsvaziaBusao(){
           lugares.clear();
           for (int i = 0; i < capacidade; i++) {
               lugares.add (new Passagem(i+1));
           }
       }
}
