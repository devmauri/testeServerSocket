package br.ufsm.csibusao.util;

import br.ufsm.csibusao.busao.ServerBusHttp;

/**
 *
 * @author mauri
 */
public class Pagina {
    public static String MontaPaginaInicial(Busao bb, String resposta){
        String result = "<html><head>\n" +
"        <!-- Required meta tags -->\n" +
"        <meta charset=\"utf-8\">\n" +
"        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
"\n" +
"        <!-- Bootstrap CSS -->\n" +
"        <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\n" +
"\n" +
"        <title>BB-Lugares</title>\n" +
"    </head><body><script type=\"text/javascript\">\n" +
"            function mostraReq() {\n" +
"                var resp = document.getElementById(\"idresposta\");\n" +
"                var msg = document.createElement(\"div\");\n" +
"                if (resp.value == \"ok\") {\n" +
"                    msg.innerHTML = '<div class=\"alert alert-success\" role=\"alert\">' +\n" +
"                            '<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">' +\n" +
"                            '<span aria-hidden=\"true\">&times;</span> </button>' +\n" +
"                            '<h4 class=\"alert-heading\">Muito Bem</h4>' +\n" +
"                            '<hr><p class=\"mb-0\">Seu Lugar foi reservado.</p></div>';\n" +
"                    document.body.appendChild(msg);\n" +
"                } else if (resp.value == \"erro\") {\n" +
"                    msg.innerHTML = '<div class=\"alert alert-warning alert-dismissible fade show\" role=\"alert\"> ' +\n" +
"                            '<strong>Erro!</strong> Seu lugar não pode ser reservado.' +\n" +
"                            '<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">' +\n" +
"                            '<span aria-hidden=\"true\">&times;</span>' +\n" +
"                            '</button> <div>';\n" +
"                    document.body.appendChild(msg);\n" +
"                } else {\n" +
"                    alert(resp.value);\n" +
"                }\n" +
"\n" +
"            }\n" +
"        </script><nav class=\"navbar navbar-expand-lg navbar-light bg-light\">\n" +
"            <div class=\"container-fluid\">\n" +
"                <a class=\"navbar-brand\" href=\"./lugares.html\">BB - Busão Bala</a>               \n" +
"                <div class=\"collapse navbar-collapse\" id=\"navbarSupportedContent\">\n" +
"                    <ul class=\"navbar-nav me-auto mb-2 mb-lg-0\">\n" +
"                        <li class=\"nav-item\">\n" +
"                            <a class=\"nav-link active\" aria-current=\"page\" href=\"./lugares.html\"> Lugares </a>\n" +
"                        </li>\n" +
"                        <li class=\"nav-item\">\n" +
"                            <a class=\"nav-link\" aria-current=\"page\" href=\"./reservar.html\"> Reserva </a>\n" +
"                        </li>\n" +
"                    </ul>\n" +
"                </div>\n" +
"            </div>\n" +
"        </nav>";
//interativo       
        result+= "<input type=\"hidden\" id = \"idresposta\" name=\"resposta\" value=\""+resposta+"\">\n" +
"        <script>mostraReq();</script>";
//tabela        
        result += "<table class=\"table table-striped table-bordered\">\n"
                + "            <thead class=\"table-dark\">\n"
                + "                <tr>\n"
                + "                    <th scope=\"col\">Lugar</th>\n"
                + "                    <th scope=\"col\">Status</th>\n"
                + "                    <th scope=\"col\">Responsável</th>\n"
                + "                    <th scope=\"col\">Hora da Reserva</th>\n"
                + "                </tr>\n"
                + "            </thead>\n"
                + "            <tbody>";
        for (Passagem lugar : bb.lugares) {
            result += "<tr>"
                    + "<td scope=\"row\">Poltrona " + lugar.assento.id + "</td>\n"
                    + "                    <td>" + lugar.assento.status + "</td>\n"
                    + "                    <td>" + lugar.passagueiro + "</td>\n";
            if (lugar.assento.status == Passagem.LUGAR.RESERVADO) {
                result += "<td>" + lugar.horaCompra + "</td>";
            } else {
                result += "<td>\n"
                        + "                        <form method=\"get\" action=\"./reservar.html?\">\n"
                        + "                            <button type=\"submit\" class=\"btn btn-success\" value="+lugar.assento.id+" name=\"reservar\">Reservar</button>\n"
                        + "                        </form>\n"
                        + "                    </td>";
            }
            result += "</tr>";
        }
        result += "</tbody></table>"+
                "<!-- Optional JavaScript -->\n" +
"        <!-- jQuery first, then Popper.js, then Bootstrap JS -->\n" +
"        <script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" integrity=\"sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN\" crossorigin=\"anonymous\"></script>\n" +
"        <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js\" integrity=\"sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q\" crossorigin=\"anonymous\"></script>\n" +
"        <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\" integrity=\"sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl\" crossorigin=\"anonymous\"></script>\n";
        
        result+="</body></html>";
        
        return result;
    }
    
    
    public static String MontaPaginaDois(String valor){
        String result ="<!doctype html>\n" +
"<html lang=\"pt-br\">\n" +
"    <head>\n" +
"        <!-- Required meta tags -->\n" +
"        <meta charset=\"utf-8\">\n" +
"        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
"\n" +
"        <!-- Bootstrap CSS -->\n" +
"        <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\n" +
"\n" +
"        <title>BB-Lugares</title>\n" +
"    </head>\n" +
"    <body>\n" +
"        <script type=\"text/javascript\">\n" +
"            function mostraReq() {\n" +
"                var resp = document.getElementById(\"idresposta\");\n" +
"                if (parseInt (resp.value) > 0 && !isNaN(resp.value)) {\n" +
"                    document.getElementById(\"assento\").value = parseInt (resp.value);\n" +
"                } else {\n" +
"                    alert(resp.value);\n" +
"                }\n" +
"\n" +
"            }\n" +
"        </script>\n" +
"\n" +
"        <nav class=\"navbar navbar-expand-lg navbar-light bg-light\">\n" +
"            <div class=\"container-fluid\">\n" +
"                <a class=\"navbar-brand\" href=\"./lugares.html\">BB - Busão Bala</a>               \n" +
"                <div class=\"collapse navbar-collapse\" id=\"navbarSupportedContent\">\n" +
"                    <ul class=\"navbar-nav me-auto mb-2 mb-lg-0\">\n" +
"                        <li class=\"nav-item\">\n" +
"                            <a class=\"nav-link\" aria-current=\"page\" href=\"./lugares.html\"> Lugares </a>\n" +
"                        </li>\n" +
"                        <li class=\"nav-item\">\n" +
"                            <a class=\"nav-link active\" aria-current=\"page\" href=\"./reservar.html\"> Reserva </a>\n" +
"                        </li>\n" +
"                    </ul>\n" +
"                </div>\n" +
"            </div>\n" +
"        </nav>\n";
//iterativo        
        result +="<input type=\"hidden\" id = \"idresposta\" name=\"resposta\" value=\"" + valor + "\"\\>";
        
        result +="<form method=\"get\" action=\"./lugares.html\">\n" +
"            <div class=\"d-flex justify-content-center form-row \">\n" +
"                <div class=\"form-group col-md-1\">\n" +
"                    <label for=\"assento\">Poltrona:</label>\n" +
"                    <input type=\"number\" class=\"form-control\" id=\"assento\" min=\"1\"\n" +
"                           value=\"1\" name=\"assento\" placeholder=\"Numero da Poltrona\">\n" +
"                </div>\n" +
"                <div class=\"form-group col-md-6\">\n" +
"                    <label for=\"nomeUsuario\">Nome:</label>\n" +
"                    <input type=\"text\" class=\"form-control\" id=\"nomeUsuario\"\n" +
"                           value=\"\" name=\"nome\" placeholder=\"Digite seu nome aqui.\">\n" +
"                </div>\n" +
"            </div>\n" +
"            <div class=\"d-flex justify-content-center form-row\">               \n" +
"                <a href=\"./lugares.html\"><button type=\"button\" class=\"btn btn-primary\" >Voltar</button></a>\n" +
"                <button type=\"submit\" class=\"btn btn-success\" value=\"reservar\" name=\"acao\">Reservar</button>              \n" +
"            </div>\n" +
"\n" +
"        </form>\n" +
"        \n" +
"        <script>mostraReq();</script>\n" +
"\n" +
"        <!-- Optional JavaScript -->\n" +
"        <!-- jQuery first, then Popper.js, then Bootstrap JS -->\n" +
"        <script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" integrity=\"sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN\" crossorigin=\"anonymous\"></script>\n" +
"        <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js\" integrity=\"sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q\" crossorigin=\"anonymous\"></script>\n" +
"        <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\" integrity=\"sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl\" crossorigin=\"anonymous\"></script>\n" +
"    </body>\n" +
"</html>";
        
         return result;
    }
}
