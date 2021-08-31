package br.ufsm.csibusao.util;

/**
 *
 * @author mauri
 */
public class ENUMERADORES {

    public enum COMMAND {
        GET, POST, PUT, PUSH;
    }

    public enum PROTOCOL {
        DEFAULT("HTTP/1.1");
        private String value;

        PROTOCOL(String s) {
            this.value = s;
        }

        public String getValue() {
            return value;
        }
    }

    public enum RESPONSE {
        OK(200, "OK"), NOTFOUND(404, "NOT FOUN");
        private int cod;
        private String status;

        RESPONSE(int i, String s) {
            this.cod = i;
            this.status = s;
        }

        public int getCod() {
            return cod;
        }

        public String getStatus() {
            return status;
        }

    }

}
