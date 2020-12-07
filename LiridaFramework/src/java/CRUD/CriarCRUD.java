package CRUD;

import Conexao.Conexao;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CriarCRUD {

    public static void main(String[] args) throws IOException {

        String tabela = "cliente_contato".toUpperCase();
        CriarCRUD crud = new CriarCRUD();
        //crud.criarBean(tabela);
        //crud.criarDAO(tabela);
        crud.criarTeste(tabela);
        //crud.criarDAOAPI(tabela);
        //crud.criarService(tabela);
        //crud.criarWebService(tabela);
        //crud.criarController(tabela);

    }

    public void criarDAO(String pTabela) throws IOException {
        CriarCRUD crud = new CriarCRUD();

        List<Campo> listaCampo = crud.listarCampos(pTabela);
        Campo campo = listaCampo.get(0);
        List<String> codigo = new ArrayList<>();

        /*Campos Insert*/
        String CamposInsert = "";
        String ConteudoInsert = "";
        for (int i = 1; i < listaCampo.size(); i++) {
            CamposInsert = CamposInsert + listaCampo.get(i).getNome();
            ConteudoInsert = ConteudoInsert + "?";
            if (listaCampo.size() != i + 1) {
                CamposInsert = CamposInsert + ",";
                ConteudoInsert = ConteudoInsert + ",";
            }
        }

        /*Campos Update*/
        String CamposUpdate = "";
        for (int i = 1; i < listaCampo.size(); i++) {
            CamposUpdate = CamposUpdate + listaCampo.get(i).getNome() + " = ?";
            if (listaCampo.size() != i + 1) {
                CamposUpdate = CamposUpdate + ",";
            }
        }
        /* MÉTODO INSERIR*/
        //codigo.add("public " + campo.getClasse() + " inserir(" + campo.getClasse() + " " + campo.getInstancia() + " ) {");
        codigo.add("@Override");
        codigo.add("public HashMap<String, String> inserir(Object pObject) {");
        codigo.add("HashMap<String, String> retorno;");
        codigo.add(campo.getClasse() + " " + campo.getInstancia() + " = (" + campo.getClasse() + ") pObject;");
        codigo.add("try {");

        //codigo.add("Long seq = Sequence.buscarNumeroSequence(\"" + pTabela + "_SEQ_" + pTabela + "_SEQ\");");
        //codigo.add(campo.getInstancia() + ".set" + campo.atributoGetSet + "(seq);");
        codigo.add("Conexao conexao = new Conexao();");
        codigo.add("Connection conn = conexao.getConnection();");
        codigo.add("String sql = " + "\"insert into " + pTabela + "\" +");

        codigo.add("\" (" + CamposInsert + ")" + "\"");
        codigo.add("+ " + "\" values \"" + "+");
        codigo.add("\" (" + ConteudoInsert + ")" + "\"" + ";");
        codigo.add("PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);");
        codigo.add("");

        for (int i = 1; i < listaCampo.size(); i++) {
            if (listaCampo.get(i).getTipo().equals("Date")) {
                codigo.add("try{");
                codigo.add("ps.set" + listaCampo.get(i).getTipo() + "(" + String.valueOf(i) + ",new java.sql.Date(" + listaCampo.get(i).getInstancia() + ".get" + listaCampo.get(i).getAtributoGetSet() + "().getTime()));");
                codigo.add("} catch (NullPointerException e) {");
                codigo.add("ps.setDate(" + String.valueOf(i) + ", null);");
                codigo.add("}");
            } else if (listaCampo.get(i).getTipo().equals("Long")) {
                codigo.add("try{");
                codigo.add("ps.set" + listaCampo.get(i).getTipo() + "(" + String.valueOf(i) + "," + listaCampo.get(i).getInstancia() + ".get" + listaCampo.get(i).getAtributoGetSet() + "());");
                codigo.add("} catch (NullPointerException e) {");
                codigo.add("ps.setObject(" + String.valueOf(i) + ", null);");
                codigo.add("}");
            } else {
                if (listaCampo.get(i).getNome().toUpperCase().equals("LINHA1")) {
                    codigo.add("");
                    codigo.add("// Linha 1 - Apresentação no card do APP e WEB");
                    codigo.add("ps.set" + listaCampo.get(i).getTipo() + "(" + String.valueOf(i) + "," + listaCampo.get(i).getInstancia() + ".getCodigo());");
                } else if (listaCampo.get(i).getNome().toUpperCase().equals("LINHA2")) {
                    codigo.add("");
                    codigo.add("// Linha 2 - Apresentação no card do APP e WEB");
                    codigo.add("ps.set" + listaCampo.get(i).getTipo() + "(" + String.valueOf(i) + ", \"Situação: \"+" + listaCampo.get(i).getInstancia() + ".getSituacao());");
                } else {
                    codigo.add("ps.set" + listaCampo.get(i).getTipo() + "(" + String.valueOf(i) + "," + listaCampo.get(i).getInstancia() + ".get" + listaCampo.get(i).getAtributoGetSet() + "());");
                }
            }

        }

        codigo.add("");
        codigo.add("ps.execute();");
        codigo.add("");
        codigo.add("ResultSet rsID = ps.getGeneratedKeys();");
        codigo.add("if (rsID.next()) {");
        codigo.add("Long seq = rsID.getLong (\"" + campo.getNome() + "\");");
        codigo.add(campo.getInstancia() + ".setSeq" + campo.getClasse() + "(seq);");
        codigo.add("}");

        codigo.add("");
        codigo.add("ps.close();");
        codigo.add("");
        codigo.add("retorno = Util.preencherRetorno(\"true\", \"Informações armazenadas com sucesso.\");");
        codigo.add("} catch (SQLException ex) {");
        codigo.add("Logger.getLogger(" + campo.classe + "DAO.class.getName()).log(Level.SEVERE, null, ex);");
        codigo.add("retorno = Util.preencherRetorno(\"false\", ex.getMessage());");
        codigo.add("}");
        codigo.add("");
        codigo.add("return retorno;");
        codigo.add("}");
        codigo.add("");

        /* MÉTODO NOVO */
        codigo.add("@Override");
        codigo.add("public " + campo.getClasse() + " novo(Object pObject) {");
        codigo.add(campo.getClasse() + " " + campo.getInstancia() + " = (" + campo.classe + ") pObject;");
        codigo.add(campo.getInstancia() + ".setSituacao(\"0\");");
        codigo.add(campo.getInstancia() + ".setCodigo(Util.gerarRandom());");
        codigo.add("return " + campo.getInstancia() + ";");
        codigo.add("}");

        /* MÉTODO ALTERAR */
        codigo.add("@Override");
        codigo.add("public HashMap<String, String> alterar(Object pObject) {");
        codigo.add("HashMap<String, String> retorno;");
        codigo.add(campo.getClasse() + " " + campo.getInstancia() + " = (" + campo.getClasse() + ") pObject;");
        codigo.add("        try {");
        codigo.add("        Conexao conexao = new Conexao();");
        codigo.add("        Connection conn = conexao.getConnection();");
        codigo.add("        String sql = " + "\"update " + pTabela + " set " + CamposUpdate + " where " + campo.nome + " = ?" + "\";");
        codigo.add("        ");
        codigo.add("        PreparedStatement ps = conn.prepareStatement(sql);");
        codigo.add("        ");

        for (int i = 1;
                i < listaCampo.size();
                i++) {

            if (listaCampo.get(i).getTipo().equals("Date")) {
                codigo.add("try{");
                codigo.add("ps.set" + listaCampo.get(i).getTipo() + "(" + String.valueOf(i) + ",new java.sql.Date(" + listaCampo.get(i).getInstancia() + ".get" + listaCampo.get(i).getAtributoGetSet() + "().getTime()));");
                codigo.add("} catch (NullPointerException e) {");
                codigo.add("ps.setDate(" + String.valueOf(i) + ", null);");
                codigo.add("}");

            } else if (listaCampo.get(i).getTipo().equals("Long")) {
                codigo.add("try{");
                codigo.add("ps.set" + listaCampo.get(i).getTipo() + "(" + String.valueOf(i) + "," + listaCampo.get(i).getInstancia() + ".get" + listaCampo.get(i).getAtributoGetSet() + "());");
                codigo.add("} catch (NullPointerException e) {");
                codigo.add("ps.setObject(" + String.valueOf(i) + ", null);");
                codigo.add("}");
            } else {

                if (listaCampo.get(i).getNome().toUpperCase().equals("LINHA1")) {
                    codigo.add("");
                    codigo.add("// Linha 1 - Apresentação no card do APP e WEB");
                    codigo.add("ps.set" + listaCampo.get(i).getTipo() + "(" + String.valueOf(i) + "," + listaCampo.get(i).getInstancia() + ".getCodigo());");
                } else if (listaCampo.get(i).getNome().toUpperCase().equals("LINHA2")) {
                    codigo.add("");
                    codigo.add("// Linha 2 - Apresentação no card do APP e WEB");
                    codigo.add("ps.set" + listaCampo.get(i).getTipo() + "(" + String.valueOf(i) + ", \"Situação: \"+" + listaCampo.get(i).getInstancia() + ".getSituacao());");
                } else {
                    codigo.add("ps.set" + listaCampo.get(i).getTipo() + "(" + String.valueOf(i) + "," + listaCampo.get(i).getInstancia() + ".get" + listaCampo.get(i).getAtributoGetSet() + "());");
                }

            }
        }

        codigo.add(
                "ps.set" + campo.getTipo() + "(" + String.valueOf(listaCampo.size()) + "," + campo.getInstancia() + ".get" + campo.getAtributoGetSet() + "());");

        codigo.add(
                "        ps.execute();");
        codigo.add(
                "        ps.close();");

        codigo.add("retorno = Util.preencherRetorno(\"true\", \"Informações armazenadas com sucesso.\");");
        codigo.add("} catch (SQLException ex) {");
        codigo.add("Logger.getLogger(" + campo.classe + "DAO.class.getName()).log(Level.SEVERE, null, ex);");
        codigo.add("retorno = Util.preencherRetorno(\"false\", ex.getMessage());");
        codigo.add("}");
        codigo.add("");
        codigo.add("return retorno;");
        codigo.add("}");
        codigo.add("");

        /* MÉTODO SELECT */
        codigo.add("@Override");
        codigo.add("public List<Object> listar(ClausulaWhere sClausula) {");
        codigo.add(
                "try {");
        codigo.add(
                "Conexao conexao = new Conexao();");
        codigo.add(
                "Connection conn = conexao.getConnection();");
        codigo.add("String sql = \"select * From (SELECT concat(codigo) campo_pesquisa, " + pTabela + ".* FROM " + pTabela + ") " + pTabela + "\" + sClausula.montarsClausula();");

        codigo.add(
                "System.out.println(sql);");
        codigo.add(
                "");
        codigo.add(
                "List<Object> lista" + campo.getClasse() + " = new ArrayList<Object>();");
        codigo.add(
                "PreparedStatement ps = conn.prepareStatement(sql);");
        codigo.add(
                "ResultSet rs = ps.executeQuery();");
        codigo.add(
                "");
        codigo.add(
                "while (rs.next()) {");
        codigo.add(campo.getClasse() + " " + campo.getInstancia() + " = new " + campo.getClasse() + "();");

        for (int i = 0;
                i < listaCampo.size();
                i++) {
            codigo.add(campo.getInstancia() + ".set" + listaCampo.get(i).getAtributoGetSet() + "(rs.get" + listaCampo.get(i).getTipo() + "(" + "\"" + listaCampo.get(i).getNome() + "\"" + "));");
        }

        codigo.add(
                "lista" + campo.getClasse() + ".add(" + campo.getInstancia() + ");");

        codigo.add(
                "}");
        codigo.add(
                "");
        codigo.add(
                "            ps.execute();");
        codigo.add(
                "            ps.close();");
        codigo.add(
                "");
        codigo.add(
                "            return lista" + campo.getClasse() + ";");
        codigo.add(
                "} catch (SQLException ex) {");
        codigo.add(
                "Logger.getLogger(" + campo.classe + "DAO.class.getName()).log(Level.SEVERE, null, ex);");
        codigo.add(
                "System.out.println(ex.getMessage());                     // Print da mensagem de tratamento de erro.");
        codigo.add(
                "return null;");
        codigo.add(
                "}");
        codigo.add(
                "}");

        /* MÉTODO BUSCAR */
        codigo.add("@Override");
        codigo.add("public Object buscar(ClausulaWhere sClausula) {");
        codigo.add("return listar(sClausula).get(0);");
        codigo.add("}");
        codigo.add("");

        /* MÉTODO DELETAR */
        codigo.add("@Override");
        codigo.add("public HashMap<String, String> deletar(Object pObject) {");
        codigo.add("HashMap<String, String> retorno;");
        codigo.add(campo.getClasse() + " " + campo.getInstancia() + " = (" + campo.getClasse() + ") pObject;");
        codigo.add("try {");
        codigo.add("Conexao conexao = new Conexao();");
        codigo.add("Connection conn = conexao.getConnection();");
        codigo.add("String sql = " + "\"" + "DELETE FROM " + pTabela + " WHERE " + campo.getNome() + " =  ? " + "\";");
        codigo.add("");
        codigo.add("PreparedStatement ps = conn.prepareStatement(sql);");
        codigo.add("");
        codigo.add("ps.setLong(1," + campo.getInstancia() + ".get" + campo.getAtributoGetSet() + "());");
        codigo.add("");
        codigo.add("            ps.execute();");
        codigo.add("            ps.close();");
        codigo.add("");
        codigo.add("");
        codigo.add("ps.close();");
        codigo.add("");
        codigo.add("retorno = Util.preencherRetorno(\"true\", \"Informações armazenadas com sucesso.\");");
        codigo.add("} catch (SQLException ex) {");
        codigo.add("Logger.getLogger(" + campo.classe + "DAO.class.getName()).log(Level.SEVERE, null, ex);");
        codigo.add("retorno = Util.preencherRetorno(\"false\", ex.getMessage());");
        codigo.add("}");
        codigo.add("");
        codigo.add("return retorno;");
        codigo.add("}");
        codigo.add("");

        String caminho = System.getProperty("user.dir") + "/src/java/" + campo.classe;

        System.out.println(caminho);

        System.out.println(
                "******** DAO*****************");

        File diretorio = new File(caminho);

        diretorio.mkdir();

        FileWriter fw = new FileWriter(caminho + "/" + campo.classe + "DAO.java");
        BufferedWriter bw = new BufferedWriter(fw);

        bw.newLine();

        bw.write(
                "/***********************************************************************************/");
        bw.newLine();

        bw.write(
                "/*CRIADO DE FORMA AUTOMATICA PELO GERADOR DE CÓDIGO DA CROSSYSTEM*/");
        bw.newLine();

        bw.write(
                "/*Data:  " + String.valueOf(new Date(System.currentTimeMillis())) + " */");
        bw.newLine();

        bw.write(
                "/***********************************************************************************/");
        bw.newLine();

        bw.write(
                "package " + campo.getClasse() + ";");

        bw.newLine();

        bw.write(
                "public class " + campo.classe + "DAO implements IDAO {");
        bw.newLine();

        bw.newLine();

        System.out.println(
                "");
        for (int i = 0;
                i < codigo.size();
                i++) {
            bw.write(codigo.get(i));
            bw.newLine();;
            //System.out.println(codigo.get(i));

        }

        bw.write(
                "}");

        bw.close();

        fw.close();

        System.out.println(
                "");
        System.out.println(
                "*******************************");

    }

    public void criarTeste(String pTabela) throws IOException {
        CriarCRUD crud = new CriarCRUD();

        List<Campo> listaCampo = crud.listarCampos(pTabela);
        Campo campo = listaCampo.get(0);
        List<String> codigo = new ArrayList<>();

        /*Campos Insert*/
        String CamposInsert = "";
        String ConteudoInsert = "";
        for (int i = 1; i < listaCampo.size(); i++) {
            CamposInsert = CamposInsert + listaCampo.get(i).getNome();
            ConteudoInsert = ConteudoInsert + "?";
            if (listaCampo.size() != i + 1) {
                CamposInsert = CamposInsert + ",";
                ConteudoInsert = ConteudoInsert + ",";
            }
        }

        /*Campos Update*/
        String CamposUpdate = "";
        for (int i = 1; i < listaCampo.size(); i++) {
            CamposUpdate = CamposUpdate + listaCampo.get(i).getNome() + " = ?";
            if (listaCampo.size() != i + 1) {
                CamposUpdate = CamposUpdate + ",";
            }
        }

        /* MÉTODO MAIN*/
        //codigo.add("public " + campo.getClasse() + " inserir(" + campo.getClasse() + " " + campo.getInstancia() + " ) {");
        codigo.add("public static void main(String[] args) throws IOException {");
        codigo.add(campo.getClasse() + " " + campo.getInstancia() + " = new " + campo.getClasse() + "();");
        codigo.add(campo.getClasse() + "Service " + campo.getInstancia() + "Service = new " + campo.getClasse() + "Service(1L);");

        for (int i = 1; i < listaCampo.size(); i++) {
            if (listaCampo.get(i).getTipo().equals("Date")) {
                codigo.add(listaCampo.get(i).getInstancia() + ".set" + listaCampo.get(i).getAtributoGetSet() + "(new Date()" + "" + ");");
            } else if (listaCampo.get(i).getTipo().equals("Long")) {
                codigo.add(listaCampo.get(i).getInstancia() + ".set" + listaCampo.get(i).getAtributoGetSet() + "(" + String.valueOf(i) + "L);");
            } else if (listaCampo.get(i).getTipo().equals("BigDecimal")) {
                codigo.add(listaCampo.get(i).getInstancia() + ".set" + listaCampo.get(i).getAtributoGetSet() + "( BigDecimal.valueOf(" + String.valueOf(i) + ") );");
            } else {
                codigo.add(listaCampo.get(i).getInstancia() + ".set" + listaCampo.get(i).getAtributoGetSet() + "(\"" + listaCampo.get(i).getNome() + "\");");
            }

        }
        codigo.add(campo.getInstancia() + ".setSeq" + campo.getClasse() + "(0L);");

        codigo.add(campo.getInstancia() + "Service.salvar(" + campo.getInstancia() + ");");

        codigo.add("Gson gson = new GsonBuilder().setDateFormat(\"dd-MM-yyyy'T'HH:mm:ss\").create();");
        codigo.add("String json = gson.toJson(" + campo.getInstancia() + ");");
        codigo.add("System.out.println(json);");

        codigo.add("}");

        String caminho = System.getProperty("user.dir") + "/src/java/" + campo.classe;

        System.out.println(caminho);

        System.out.println(
                "******** DAO*****************");

        File diretorio = new File(caminho);

        diretorio.mkdir();

        FileWriter fw = new FileWriter(caminho + "/" + campo.classe + "Teste.java");
        BufferedWriter bw = new BufferedWriter(fw);

        bw.newLine();

        bw.write(
                "/***********************************************************************************/");
        bw.newLine();

        bw.write(
                "/*CRIADO DE FORMA AUTOMATICA PELO GERADOR DE CÓDIGO DA CROSSYSTEM*/");
        bw.newLine();

        bw.write(
                "/*Data:  " + String.valueOf(new Date(System.currentTimeMillis())) + " */");
        bw.newLine();

        bw.write(
                "/***********************************************************************************/");
        bw.newLine();

        bw.write(
                "package " + campo.getClasse() + ";");

        bw.newLine();

        bw.write(
                "public class " + campo.classe + "Teste{");
        bw.newLine();

        bw.newLine();

        System.out.println(
                "");
        for (int i = 0;
                i < codigo.size();
                i++) {
            bw.write(codigo.get(i));
            bw.newLine();;
            //System.out.println(codigo.get(i));

        }

        bw.write(
                "}");

        bw.close();

        fw.close();

        System.out.println(
                "");
        System.out.println(
                "*******************************");

    }

    public void criarDAOAPI(String pTabela) throws IOException {
        CriarCRUD crud = new CriarCRUD();

        List<Campo> listaCampo = crud.listarCampos(pTabela);
        Campo campo = listaCampo.get(0);
        List<String> codigo = new ArrayList<>();

        /*Campos Insert*/
        String CamposInsert = "";
        String ConteudoInsert = "";
        for (int i = 1; i < listaCampo.size(); i++) {
            CamposInsert = CamposInsert + listaCampo.get(i).getNome();
            ConteudoInsert = ConteudoInsert + "?";
            if (listaCampo.size() != i + 1) {
                CamposInsert = CamposInsert + ",";
                ConteudoInsert = ConteudoInsert + ",";
            }
        }

        /*Campos Update*/
        String CamposUpdate = "";
        for (int i = 1; i < listaCampo.size(); i++) {
            CamposUpdate = CamposUpdate + listaCampo.get(i).getNome() + " = ?";
            if (listaCampo.size() != i + 1) {
                CamposUpdate = CamposUpdate + ",";
            }
        }
        /* MÉTODO INSERIR*/
        codigo.add("@Override ");
        codigo.add("public HashMap<String, String> inserir(Object pObject) {");
        codigo.add("HashMap<String, String> retorno;");
        codigo.add("try {");
        codigo.add(campo.getClasse() + " " + campo.getInstancia() + " = ((" + campo.getClasse() + ") pObject);");
        codigo.add("String urlBase = Util.buscarURLBaseAPI(" + campo.getInstancia() + ".getSeqUsuario());");
        codigo.add("");
        codigo.add("            String api = urlBase + \"" + pTabela.toLowerCase() + "/inserir\";");
        codigo.add("");
        codigo.add("            Gson gson = new GsonBuilder().setDateFormat(\"dd-MM-yyyy'T'HH:mm:ss\").create();");
        codigo.add("            String json = gson.toJson(" + campo.getInstancia() + ");");
        codigo.add("");
        codigo.add("Map<String, String> header = new HashMap<String, String>();");
        codigo.add("header.put(\"seq_modulo\", String.valueOf(modulo));");
        codigo.add("header.put(\"email_usuario\", " + campo.getInstancia() + ".getEmailUsuario());");
        codigo.add("");
        codigo.add("            HttpResponse response = Util.sendPost(api, json, header);");
        codigo.add("");
        codigo.add("            json = EntityUtils.toString(response.getEntity());");
        codigo.add("            org.apache.http.Header[] cabecalho = response.getAllHeaders();");
        codigo.add("");
        codigo.add("            String msg =  \"Salvamento com sucesso.\"  ;");
        codigo.add("            String result = \"false\";");
        codigo.add("            for (int i = 0; i < cabecalho.length; i++) {");
        codigo.add("");
        codigo.add("                if (cabecalho[i].getName().toLowerCase().equals(\"msg\")) {");
        codigo.add("                    msg = cabecalho[i].getValue();");
        codigo.add("                } else if (cabecalho[i].getName().toLowerCase().equals(\"result\")) {");
        codigo.add("                    result = cabecalho[i].getValue();");
        codigo.add("                }");
        codigo.add("");
        codigo.add("            }");
        codigo.add("");
        codigo.add("            retorno = Util.preencherRetorno(result, msg);");
        codigo.add("} catch (IOException ex) {");
        codigo.add("Logger.getLogger(" + campo.getClasse() + "DAOAPI.class.getName()).log(Level.SEVERE, null, ex);");
        codigo.add("retorno = Util.preencherRetorno(\"false\", ex.getMessage());");
        codigo.add("} catch (ParseException ex) {");
        codigo.add("Logger.getLogger(" + campo.getClasse() + "DAOAPI.class.getName()).log(Level.SEVERE, null, ex);");
        codigo.add("retorno = Util.preencherRetorno(\"false\", ex.getMessage());");
        codigo.add("}");
        codigo.add("return retorno;");
        codigo.add("");
        codigo.add("}");
        codigo.add("");
        codigo.add("@Override");
        codigo.add("public " + campo.getClasse() + " novo(Object pObject) {");
        codigo.add(campo.getClasse() + " " + campo.getInstancia() + "= ((" + campo.getClasse() + ") pObject);");
        codigo.add("try {");
        codigo.add("String urlBase = Util.buscarURLBaseAPI(" + campo.getInstancia() + ".getSeqUsuario());");
        codigo.add("String api = urlBase + \"" + pTabela.toLowerCase()+ "/novo\";");
        codigo.add("");
        codigo.add("            Gson gson = new GsonBuilder().setDateFormat(\"dd-MM-yyyy'T'HH:mm:ss\").create();");
        codigo.add("            String json = gson.toJson(" + campo.getInstancia() + ");");
        codigo.add("");
        codigo.add("Map<String, String> header = new HashMap<String, String>();");
        codigo.add("header.put(\"seq_modulo\", String.valueOf(modulo));");
        codigo.add("header.put(\"email_usuario\", " + campo.getInstancia() + ".getEmailUsuario());");
        codigo.add("");
        codigo.add("            HttpResponse response = Util.sendPost(api, json, header);");
        codigo.add("");
        codigo.add("            json = EntityUtils.toString(response.getEntity());");
        codigo.add("");
        codigo.add(campo.getInstancia() + " = gson.fromJson(json, " + campo.getClasse() + ".class);");
        codigo.add("");
        codigo.add("} catch (IOException | ParseException ex) {");
        codigo.add("Logger.getLogger(" + campo.getClasse() + "DAOAPI.class.getName()).log(Level.SEVERE, null, ex);");
        codigo.add("}");
        codigo.add("");
        codigo.add("        return " + campo.getInstancia() + ";");
        codigo.add("}");
        codigo.add("");
        codigo.add("@Override");
        codigo.add("public HashMap<String, String> alterar(Object pObject) {");
        codigo.add("HashMap<String, String> retorno;");
        codigo.add("try {");
        codigo.add(campo.getClasse() + " " + campo.getInstancia() + "= ((" + campo.getClasse() + " ) pObject);");
        codigo.add("String urlBase = Util.buscarURLBaseAPI(" + campo.getInstancia() + ".getSeqUsuario());");
        codigo.add("String api = urlBase + \"" + pTabela.toLowerCase() + "/alterar\";");
        codigo.add("");
        codigo.add("            Gson gson = new GsonBuilder().setDateFormat(\"dd-MM-yyyy'T'HH:mm:ss\").create();");
        codigo.add("            String json = gson.toJson(" + campo.getInstancia() + ");");
        codigo.add("");
        codigo.add("Map<String, String> header = new HashMap<String, String>();");
        codigo.add("header.put(\"seq_modulo\", String.valueOf(modulo));");
        codigo.add("header.put(\"email_usuario\", " + campo.getInstancia() + ".getEmailUsuario());");
        codigo.add("");
        codigo.add("            HttpResponse response = Util.sendPost(api, json, header);");
        codigo.add("");
        codigo.add("            json = EntityUtils.toString(response.getEntity());");
        codigo.add("            org.apache.http.Header[] cabecalho = response.getAllHeaders();");
        codigo.add("");
        codigo.add("            String msg = \"Informações armazenadas com sucesso\";");
        codigo.add("            String result = \"false\";");
        codigo.add("            for (int i = 0; i < cabecalho.length; i++) {");
        codigo.add("");
        codigo.add("                if (cabecalho[i].getName().toLowerCase().equals(\"msg\")) {");
        codigo.add("                    msg = cabecalho[i].getValue();");
        codigo.add("                } else if (cabecalho[i].getName().toLowerCase().equals(\"result\")) {");
        codigo.add("                    result = cabecalho[i].getValue();");
        codigo.add("                }");
        codigo.add("            }");
        codigo.add("");
        codigo.add("            retorno = Util.preencherRetorno(result, msg);");
        codigo.add("} catch (IOException ex) {");
        codigo.add("Logger.getLogger(" + campo.getClasse() + "DAOAPI.class.getName()).log(Level.SEVERE, null, ex);");
        codigo.add("retorno = Util.preencherRetorno(\"false\", ex.getMessage());");
        codigo.add("} catch (ParseException ex) {");
        codigo.add("retorno = Util.preencherRetorno(\"false\", ex.getMessage());");
        codigo.add("}");
        codigo.add("return retorno;");
        codigo.add("}");
        codigo.add("");
        codigo.add("@Override");
        codigo.add("public List<Object> listar(ClausulaWhere sClausula) {");
        codigo.add("");
        codigo.add("String emailUsuario = Util.buscarConteudoSClausula(sClausula, \"email_usuario\");");
        codigo.add("String texto = Util.buscarConteudoSClausula(sClausula, \"campo_pesquisa\");");
        codigo.add("String seqUsuario = Util.buscarConteudoSClausula(sClausula, \"seq_usuario\");");
        codigo.add("");
        codigo.add("String urlBase = Util.buscarURLBaseAPI(Long.valueOf(seqUsuario));");
        codigo.add("String api = urlBase + \"" + pTabela.toLowerCase() + "/listar\";");
        codigo.add("");
        codigo.add("List<Object> retorno = new ArrayList<Object>();");
        codigo.add("");
        codigo.add("Gson gson = new GsonBuilder().setDateFormat(\"dd-MM-yyyy'T'HH:mm:ss\").create();");
        codigo.add("");
        codigo.add("Util util = new Util();");
        codigo.add("HashMap<String, String> cabecalho = new HashMap<String, String>();");
        codigo.add("cabecalho.put(\"email_usuario\", emailUsuario);");
        codigo.add("cabecalho.put(\"texto_pesquisa\", texto);");
        codigo.add("String jsonString = sendGet(api, null, cabecalho);");
        codigo.add("");
        codigo.add("java.lang.reflect.Type tipo = new TypeToken<List<" + campo.getClasse() + ">>() {");
        codigo.add("}.getType();");
        codigo.add("");
        codigo.add("retorno = gson.fromJson(jsonString, tipo);");
        codigo.add("");
        codigo.add("return retorno;");
        codigo.add("}");
        codigo.add("");
        codigo.add("@Override");
        codigo.add("public Object buscar(ClausulaWhere sClausula) {");
        codigo.add("");
        codigo.add("String emailUsuario = Util.buscarConteudoSClausula(sClausula, \"email_usuario\");");
        codigo.add("String codigo = Util.buscarConteudoSClausula(sClausula, \"codigo\");");
        codigo.add("String seqUsuario = Util.buscarConteudoSClausula(sClausula, \"seq_usuario\");");
        codigo.add("");
        codigo.add("String urlBase = Util.buscarURLBaseAPI(Long.valueOf(seqUsuario));");
        codigo.add("String api = urlBase + \"" + pTabela.toLowerCase() + "/buscar\";");
        codigo.add("");
        codigo.add("");
        codigo.add("Gson gson = new GsonBuilder().setDateFormat(\"dd-MM-yyyy'T'HH:mm:ss\").create();");
        codigo.add("");
        codigo.add("Util util = new Util();");
        codigo.add("HashMap<String, String> cabecalho = new HashMap<String, String>();");
        codigo.add("cabecalho.put(\"email_usuario\", emailUsuario);");
        codigo.add("cabecalho.put(\"codigo\", codigo);");
        codigo.add("String jsonString = sendGet(api, null, cabecalho);");
        codigo.add("");
        codigo.add(campo.getClasse() + " " + campo.getInstancia() + " = gson.fromJson(jsonString, " + campo.getClasse() + ".class);");
        codigo.add(campo.getInstancia() + ".setSeq" + campo.getClasse() + "(1L);");
        codigo.add("return " + campo.getInstancia() + ";");
        codigo.add("}");
        codigo.add("");
        codigo.add("@Override");
        codigo.add("public HashMap<String, String> deletar(Object pObject) {");
        codigo.add("HashMap<String, String> retorno;");
        codigo.add("try {");
        codigo.add(campo.getClasse() + " " + campo.getInstancia() + " = ((" + campo.getClasse() + ") pObject);");
        codigo.add("String urlBase = Util.buscarURLBaseAPI(" + campo.getInstancia() + ".getSeqUsuario());");
        codigo.add("String api = urlBase + \" " + pTabela.toLowerCase() + "/deletar\";");
        codigo.add("");
        codigo.add("            Gson gson = new GsonBuilder().setDateFormat(\"dd-MM-yyyy'T'HH:mm:ss\").create();");
        codigo.add("            String json = gson.toJson(" + campo.getInstancia() + ");");
        codigo.add("");
        codigo.add("Map<String, String> header = new HashMap<String, String>();");
        codigo.add("header.put(\"seq_modulo\", String.valueOf(modulo));");
        codigo.add("header.put(\"email_usuario\", " + campo.getInstancia() + ".getEmailUsuario());");
        codigo.add("");
        codigo.add("            HttpResponse response = Util.sendPost(api, json, header);");
        codigo.add("");
        codigo.add("            json = EntityUtils.toString(response.getEntity());");
        codigo.add("            org.apache.http.Header[] cabecalho = response.getAllHeaders();");
        codigo.add("");
        codigo.add("            String msg = \"Deletado com sucesso.\";");
        codigo.add("            String result = \"false\";");
        codigo.add("            for (int i = 0; i < cabecalho.length; i++) {");
        codigo.add("");
        codigo.add("                if (cabecalho[i].getName().toLowerCase().equals(\"msg\")) {");
        codigo.add("                    msg = cabecalho[i].getValue();");
        codigo.add("                } else if (cabecalho[i].getName().toLowerCase().equals(\"result\")) {");
        codigo.add("                    result = cabecalho[i].getValue();");
        codigo.add("                }");
        codigo.add("");
        codigo.add("            }");
        codigo.add("");
        codigo.add("            retorno = Util.preencherRetorno(result, msg);");
        codigo.add("} catch (IOException ex) {");
        codigo.add("Logger.getLogger(" + campo.getClasse() + "DAOAPI.class.getName()).log(Level.SEVERE, null, ex);");
        codigo.add("retorno = Util.preencherRetorno(\"false\", ex.getMessage());");
        codigo.add("} catch (ParseException ex) {");
        codigo.add("retorno = Util.preencherRetorno(\"false\", ex.getMessage());");
        codigo.add("}");
        codigo.add("return retorno;");
        codigo.add("}        ");
        codigo.add("        ");
        codigo.add("        ");
        codigo.add("        ");

        String caminho = System.getProperty("user.dir") + "/src/java/" + campo.classe;

        System.out.println(caminho);

        System.out.println(
                "******** DAO API*****************");

        File diretorio = new File(caminho);

        diretorio.mkdir();

        FileWriter fw = new FileWriter(caminho + "/" + campo.classe + "DAOAPI.java");
        BufferedWriter bw = new BufferedWriter(fw);

        bw.newLine();

        bw.write(
                "/***********************************************************************************/");
        bw.newLine();

        bw.write(
                "/*CRIADO DE FORMA AUTOMATICA PELO GERADOR DE CÓDIGO DA CROSSYSTEM*/");
        bw.newLine();

        bw.write(
                "/*Data:  " + String.valueOf(new Date(System.currentTimeMillis())) + " */");
        bw.newLine();

        bw.write(
                "/***********************************************************************************/");
        bw.newLine();

        bw.write(
                "package " + campo.getClasse() + ";");

        bw.newLine();

        bw.write("public class " + campo.classe + "DAOAPI implements IDAO {");
        bw.newLine();

        bw.write("private final Long modulo = LiridaModulo. ;");

        bw.newLine();

        System.out.println(
                "");
        for (int i = 0;
                i < codigo.size();
                i++) {
            bw.write(codigo.get(i));
            bw.newLine();;
            //System.out.println(codigo.get(i));

        }

        bw.write(
                "}");

        bw.close();

        fw.close();

        System.out.println(
                "");
        System.out.println(
                "*******************************");

    }

    public void criarBean(String pTabela) throws IOException {

        CriarCRUD crud = new CriarCRUD();
        List<Campo> listaCampo = new ArrayList<Campo>();
        listaCampo = crud.listarCampos(pTabela);
        Campo campo = listaCampo.get(0);
        List<String> codigo = new ArrayList<>();

        for (int i = 0; i < listaCampo.size(); i++) {
            codigo.add("private " + listaCampo.get(i).getTipo() + " " + listaCampo.get(i).getAtributo() + ";");
        }
        codigo.add("");
        codigo.add("// Criar GETs e SETs");

        System.out.println("******** BEAN *****************");
        System.out.println("");

        String caminho = System.getProperty("user.dir") + "/src/java/" + campo.classe; // windows
        System.out.println(caminho);

        File diretorio = new File(caminho);
        diretorio.mkdir();

        FileWriter fw = new FileWriter(caminho + "/" + campo.classe + ".java");
        BufferedWriter bw = new BufferedWriter(fw);
        bw.newLine();
        bw.write("/***********************************************************************************/");
        bw.newLine();
        bw.write("/*CRIADO DE FORMA AUTOMATICA PELO GERADOR DE CÓDIGO DA CROSSYSTEM*/");
        bw.newLine();
        bw.write("/*Data:  " + String.valueOf(new Date(System.currentTimeMillis())) + " */");
        bw.newLine();
        bw.write("/***********************************************************************************/");
        bw.newLine();
        bw.write("package " + campo.getClasse() + ";");

        bw.newLine();
        bw.write("public class " + campo.classe + " {");
        bw.newLine();
        bw.newLine();
        for (int i = 0; i < codigo.size(); i++) {
            bw.write(codigo.get(i));
            bw.newLine();
        }

        bw.write("}");

        bw.close();
        fw.close();
        System.out.println("");
        System.out.println("*******************************");

    }

    public void criarService(String pTabela) throws IOException {
        CriarCRUD crud = new CriarCRUD();

        List<Campo> listaCampo = crud.listarCampos(pTabela);
        Campo campo = listaCampo.get(0);
        List<String> codigo = new ArrayList<>();

        codigo.add(" private Long seqModulo = LiridaModulo.L; ");
        codigo.add("private IDAO dao;");
        codigo.add("private Usuario usuario;");
        codigo.add("");
        codigo.add("public " + campo.getClasse() + "Service(Long pSeqUsuario) {");
        codigo.add("UsuarioService usuarioService = new UsuarioService();");
        codigo.add("usuario = usuarioService.buscarPorSeqUsuario(pSeqUsuario);");
        codigo.add("if (usuario.getErp().equals(\"LIRIDA\")) {");
        codigo.add("dao = new " + campo.getClasse() + "DAO();");
        codigo.add("System.out.println(\"LIRIDA\");");
        codigo.add("        } else {");
        codigo.add("dao = new " + campo.getClasse() + "DAOAPI();");
        codigo.add("System.out.println(\"API\");");
        codigo.add("        }");
        codigo.add("    }");
        codigo.add("");
        codigo.add("    public HashMap<String, String> salvar(" + campo.getClasse() + " " + campo.getInstancia() + ") {");
        codigo.add(campo.getInstancia() + ".setSeqUsuario(usuario.getSeqUsuario());");
        codigo.add("");
        codigo.add("        if (!Util.stringVazia(" + campo.getInstancia() + ".getSituacao())) {");
        codigo.add("            if (" + campo.getInstancia() + ".getSituacao().equals(\"0\")) {");
        codigo.add("                " + campo.getInstancia() + ".setSituacao(\"Normal\");");
        codigo.add("            } else if (" + campo.getInstancia() + ".getSituacao().equals(\"1\")) {");
        codigo.add("                " + campo.getInstancia() + ".setSituacao(\"Crítico\");");
        codigo.add("            } else if (" + campo.getInstancia() + ".getSituacao().equals(\"2\")) {");
        codigo.add("                " + campo.getInstancia() + ".setSituacao(\"Urgente\");");
        codigo.add("            } else if (" + campo.getInstancia() + ".getSituacao().equals(\"3\")) {");
        codigo.add("                " + campo.getInstancia() + ".setSituacao(\"Moderado\");");
        codigo.add("            }");
        codigo.add("}");
        codigo.add("");
        codigo.add("if (" + campo.getInstancia() + ".getSeq" + campo.getClasse() + "() == 0) {");
        codigo.add("" + campo.getInstancia() + ".setDataCadastro(new Date());");
        codigo.add("return dao.inserir(" + campo.getInstancia() + ");");
        codigo.add("} else {");
        codigo.add("return dao.alterar(" + campo.getInstancia() + ");");
        codigo.add("}");
        codigo.add("}");
        codigo.add("");
        codigo.add("public Object novo(" + campo.getClasse() + " " + campo.getInstancia() + ") {");

        codigo.add(campo.getInstancia() + " = (" + campo.getClasse() + ") dao.novo(" + campo.getInstancia() + ");");
        codigo.add(campo.getInstancia() + ".setSeq" + campo.getClasse() + "(0L);");
        codigo.add(campo.getInstancia() + ".setSeqUsuario(usuario.getSeqUsuario());");
        codigo.add(campo.getInstancia() + ".setDataCadastro(new Date());");
        codigo.add("return " + campo.getInstancia() + ";");
        codigo.add("}");

        codigo.add("");
        codigo.add("public List<" + campo.getClasse() + "> listar(String pEmailUsuario, String pTexto, String pCodigoSituacao) {");
        codigo.add("");
        codigo.add("ClausulaWhere condicao = new ClausulaWhere();");
        codigo.add("condicao.AdicionarCondicao(OperacaoCondicaoWhere.vazio, \"seq_usuario\", GeneroCondicaoWhere.igual, String.valueOf(usuario.getSeqUsuario()), TipoCondicaoWhere.Numero);");
        codigo.add("condicao.AdicionarCondicao(OperacaoCondicaoWhere.and, \"email_usuario\", GeneroCondicaoWhere.igual, pEmailUsuario, TipoCondicaoWhere.Texto);");
        codigo.add("condicao.AdicionarCondicao(OperacaoCondicaoWhere.and, \"campo_pesquisa\", GeneroCondicaoWhere.contem, pTexto, TipoCondicaoWhere.Texto);");
        codigo.add("");
        codigo.add("if (!Util.stringVazia(pCodigoSituacao)) {");
        codigo.add("if (pCodigoSituacao.equals(\"1\")) {");
        codigo.add("condicao.AdicionarCondicao(OperacaoCondicaoWhere.and, \"situacao\", GeneroCondicaoWhere.igual, \"Crítico\", TipoCondicaoWhere.Texto);");
        codigo.add("} else if (pCodigoSituacao.equals(\"2\")) {");
        codigo.add("condicao.AdicionarCondicao(OperacaoCondicaoWhere.and, \"situacao\", GeneroCondicaoWhere.igual, \"Urgente\", TipoCondicaoWhere.Texto);");
        codigo.add("} else if (pCodigoSituacao.equals(\"3\")) {");
        codigo.add("condicao.AdicionarCondicao(OperacaoCondicaoWhere.and, \"situacao\", GeneroCondicaoWhere.igual, \"Moderado\", TipoCondicaoWhere.Texto);");
        codigo.add("}");
        codigo.add("}");
        codigo.add("");
        codigo.add("List<" + campo.getClasse() + "> lista" + campo.getClasse() + " = (List<" + campo.getClasse() + ">) (List<?>) dao.listar(condicao);");
        codigo.add("");
        codigo.add("// Atualizar a data de ultimo acesso");
        codigo.add("Util.atualizarDataUltimoAcesso(pEmailUsuario, usuario.getSeqUsuario(), seqModulo);");
        codigo.add("");
        codigo.add("return lista" + campo.getClasse() + ";");
        codigo.add("");
        codigo.add("}");
        codigo.add("");
        codigo.add("public " + campo.getClasse() + " buscar(String pEmailUsuario, String pCodigo) {");
        codigo.add("");
        codigo.add("ClausulaWhere condicao = new ClausulaWhere();");
        codigo.add("condicao.AdicionarCondicao(OperacaoCondicaoWhere.vazio, \"seq_usuario\", GeneroCondicaoWhere.igual, String.valueOf(usuario.getSeqUsuario()), TipoCondicaoWhere.Numero);");
        codigo.add("condicao.AdicionarCondicao(OperacaoCondicaoWhere.and, \"email_usuario\", GeneroCondicaoWhere.igual, pEmailUsuario, TipoCondicaoWhere.Texto);");
        codigo.add("condicao.AdicionarCondicao(OperacaoCondicaoWhere.and, \"codigo\", GeneroCondicaoWhere.contem, pCodigo, TipoCondicaoWhere.Texto);");
        codigo.add("" + campo.getClasse() + " retorno = (" + campo.getClasse() + ") dao.buscar(condicao);");
        codigo.add("if (!Util.stringVazia(retorno.getSituacao())) {");
        codigo.add("if (retorno.getSituacao().equals(\"Normal\")) {");
        codigo.add("retorno.setSituacao(\"0\");");
        codigo.add("} else if (retorno.getSituacao().equals(\"Crítico\")) {");
        codigo.add("retorno.setSituacao(\"1\");");
        codigo.add("} else if (retorno.getSituacao().equals(\"Urgente\")) {");
        codigo.add("retorno.setSituacao(\"2\");");
        codigo.add("} else if (retorno.getSituacao().equals(\"Moderado\")) {");
        codigo.add("retorno.setSituacao(\"3\");");
        codigo.add("}");
        codigo.add("}");
        codigo.add("");
        codigo.add("        return retorno;");
        codigo.add("");
        codigo.add("}");
        codigo.add("");
        codigo.add("public List<" + campo.getClasse() + "> listar(String pTexto) {");
        codigo.add("ClausulaWhere condicao = new ClausulaWhere();");
        codigo.add("condicao.AdicionarCondicao(OperacaoCondicaoWhere.vazio, \"email_usuario\", GeneroCondicaoWhere.igual, usuario.getEmail(), TipoCondicaoWhere.Texto);");
        codigo.add("condicao.AdicionarCondicao(OperacaoCondicaoWhere.and, \"seq_usuario\", GeneroCondicaoWhere.igual, String.valueOf(usuario.getSeqUsuario()), TipoCondicaoWhere.Texto);");
        codigo.add("condicao.AdicionarCondicao(OperacaoCondicaoWhere.and, \"nome\", GeneroCondicaoWhere.contem, pTexto, TipoCondicaoWhere.Texto);");
        codigo.add("");
        codigo.add("List<" + campo.getClasse() + "> lista" + campo.getClasse() + " = (List<" + campo.getClasse() + ">) (List<?>) dao.listar(condicao);");
        codigo.add("");
        codigo.add("return lista" + campo.getClasse() + ";");
        codigo.add("}");
        codigo.add("");
        codigo.add("public HashMap<String, String> deletar(" + campo.getClasse() + " " + campo.getInstancia() + ") {");
        codigo.add("" + campo.getInstancia() + ".setSeqUsuario(usuario.getSeqUsuario());");
        codigo.add("");
        codigo.add("return dao.deletar(" + campo.getInstancia() + ");");
        codigo.add("}");
        codigo.add("");
        System.out.println("******** SERVICE *****************");
        System.out.println("");

        String caminho = System.getProperty("user.dir") + "/src/java/" + campo.classe;
        System.out.println(caminho);

        File diretorio = new File(caminho);
        diretorio.mkdir();

        FileWriter fw = new FileWriter(caminho + "/" + campo.classe + "Service.java");
        BufferedWriter bw = new BufferedWriter(fw);
        bw.newLine();
        bw.write("/***********************************************************************************/");
        bw.newLine();
        bw.write("/*CRIADO DE FORMA AUTOMATICA PELO GERADOR DE CÓDIGO DA CROSSYSTEM*/");
        bw.newLine();
        bw.write("/*Data:  " + String.valueOf(new Date(System.currentTimeMillis())) + " */");
        bw.newLine();
        bw.write("/***********************************************************************************/");
        bw.newLine();
        bw.write("package " + campo.getClasse() + ";");

        bw.newLine();
        bw.write("public class " + campo.classe + "Service {");
        bw.newLine();
        bw.newLine();
        for (int i = 0; i < codigo.size(); i++) {
            bw.write(codigo.get(i));
            bw.newLine();
        }

        bw.write("}");

        bw.close();
        fw.close();
        System.out.println("");
        System.out.println("*******************************");

    }

    public void criarController(String pTabela) throws IOException {
        CriarCRUD crud = new CriarCRUD();

        List<Campo> listaCampo = crud.listarCampos(pTabela);
        Campo campo = listaCampo.get(0);
        List<String> codigo = new ArrayList<>();

        codigo.add("@ManagedProperty(value = " + "\"" + "#{loginController}" + "\"" + ") ");
        codigo.add("protected LoginController loginController; ");
        codigo.add(campo.getClasse() + "Service " + campo.getInstancia() + "Service = new " + campo.getClasse() + "Service(); ");
        codigo.add(campo.getClasse() + " " + campo.getInstancia() + " = new " + campo.getClasse() + "();");
        codigo.add("List<" + campo.getClasse() + " > lista" + campo.getClasse() + " = new ArrayList<" + campo.getClasse() + ">(); ");
        codigo.add("String pesquisa =  " + "\"" + "\"" + ";");
        codigo.add("Integer tela = 0; ");
        codigo.add(" ");
        codigo.add("    public void iniciar(){");
        codigo.add("    }");
        codigo.add(" ");
        codigo.add("public void salvar(int pTela) { ");
        codigo.add(campo.instancia + ".setSeqUsuario(loginController.getUsuario().getSeqUsuario());");
        codigo.add(campo.instancia + " = " + campo.instancia + "Service.salvar(" + campo.getInstancia() + ");");

        codigo.add(" ");
        codigo.add(" ");
        codigo.add("FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " + "\"Registro armazenado com sucesso." + "\"," + "\"" + "\"" + ")); ");
        codigo.add("tela = pTela; ");
        codigo.add("} ");
        codigo.add(" ");
        codigo.add("public void novo() { ");
        codigo.add(campo.getInstancia() + " = new " + campo.getClasse() + "(); ");
        codigo.add("tela = 1; ");
        codigo.add("} ");
        codigo.add(" ");
        codigo.add("public void listar() { ");
        codigo.add("lista" + campo.getClasse() + " = " + campo.getInstancia() + "Service.listar(loginController.getCliente().getSeqCliente(), pesquisa,Situacao.TODOS); ");

        codigo.add("} ");
        codigo.add(" ");
        codigo.add("public void deletar() { ");
        codigo.add("if (" + campo.getInstancia() + "Service.deletar(" + campo.getInstancia() + " )) { ");
        codigo.add("listar(); ");
        codigo.add(campo.getInstancia() + " = new " + campo.getClasse() + "(); ");
        codigo.add("FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " + "\"Registro eliminado com sucesso." + "\"," + "\"" + "\"" + ")); ");
        codigo.add("tela = 0; ");
        codigo.add("listar(); ");
        codigo.add("} else { ");

        codigo.add("FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, " + "\"Falha ao excluir registro." + "\"," + "\"" + "\"" + ")); ");

        codigo.add("} ");
        codigo.add("} ");
        codigo.add(" ");
        codigo.add("public void fecharTela() throws IOException { ");
        codigo.add("loginController.mudarPagina(" + "\"" + "/principal/ principal.jsf" + "\"" + "); ");
        codigo.add("} ");
        codigo.add(" ");
        codigo.add("public void selecionar(" + campo.classe + " p" + campo.getClasse() + ") { ");
        codigo.add(campo.getInstancia() + " = p" + campo.getClasse() + "; ");
        codigo.add("tela = 1; ");
        codigo.add("} ");
        codigo.add(" ");
        codigo.add("public void mudarTela(Integer pTela) { ");
        codigo.add("tela = pTela; ");
        codigo.add("}         ");

        System.out.println("******** CONTROLLER *****************");
        System.out.println("");

        String caminho = System.getProperty("user.dir") + "//src///java//WebController//";//linux
        System.out.println(caminho);

        File diretorio = new File(caminho);
        diretorio.mkdir();

        FileWriter fw = new FileWriter(caminho + "/" + campo.classe + "Controller.java");
        BufferedWriter bw = new BufferedWriter(fw);
        bw.newLine();
        bw.write("/***********************************************************************************/");
        bw.newLine();
        bw.write("/*CRIADO DE FORMA AUTOMATICA PELO GERADOR DE CÓDIGO DA CROSSYSTEM*/");
        bw.newLine();
        bw.write("/*Data:  " + String.valueOf(new Date(System.currentTimeMillis())) + " */");
        bw.newLine();
        bw.write("/***********************************************************************************/");
        bw.newLine();
        bw.write("package " + campo.getClasse() + ";");
        bw.newLine();
        bw.write("@ManagedBean(name = \"" + campo.instancia + "Controller" + "\"" + ") ");
        bw.write("@ViewScoped ");
        bw.write("public class " + campo.classe + "Controller { ");
        bw.newLine();
        bw.newLine();
        for (int i = 0; i < codigo.size(); i++) {
            bw.write(codigo.get(i));
            bw.newLine();
        }

        bw.newLine();
        bw.newLine();
        bw.write("/* GET´s SET´s*/");
        bw.newLine();
        bw.newLine();
        bw.write("}");

        bw.close();
        fw.close();
        System.out.println("");
        System.out.println("*******************************");

    }

    public void criarWebService(String pTabela) throws IOException {
        CriarCRUD crud = new CriarCRUD();

        List<Campo> listaCampo = crud.listarCampos(pTabela);
        Campo campo = listaCampo.get(0);
        List<String> codigo = new ArrayList<>();

        codigo.add(" @Context ");
        codigo.add("private UriInfo context; ");
        codigo.add("     ");
        codigo.add("@GET ");
        codigo.add("@Produces(MediaType.APPLICATION_JSON) ");
        codigo.add("@Path(\"/listar\") ");
        codigo.add("public Response listar( ");
        codigo.add("@HeaderParam(\"seq_usuario_selecionado\") String pSeqUsuarioSelecionado, ");
        codigo.add("@HeaderParam(\"email_usuario_logado\") String pEmailUsuarioLogado, ");
        codigo.add("@HeaderParam(\"seq_modulo\") String pSeqModulo, ");
        codigo.add("@HeaderParam(\"texto\") String pTexto, ");
        codigo.add("@HeaderParam(\"codigo_situacao\") String pCodigoSituacao ");
        codigo.add(") { ");
        codigo.add("         ");
        codigo.add("" + campo.getClasse() + "Service " + campo.getInstancia() + "Service = new " + campo.getClasse() + "Service(Long.valueOf(pSeqUsuarioSelecionado)); ");
        codigo.add("List<" + campo.getClasse() + "> retorno = " + campo.getInstancia() + "Service.listar(pEmailUsuarioLogado, pTexto, pCodigoSituacao);");
        codigo.add(" ");
        codigo.add("Gson gson = new GsonBuilder().setDateFormat(\"dd-MM-yyyy'T'HH:mm:ss\").create(); ");
        codigo.add("String json = gson.toJson(retorno); ");
        codigo.add("         ");
        codigo.add("return Response.status(200).entity(json).header(\"result\", \"true\").header(\"Access-Control-Allow-Origin\", \"*\").build(); ");
        codigo.add("} ");
        codigo.add("     ");
        codigo.add("@POST ");
        codigo.add("@Produces(MediaType.APPLICATION_JSON) ");
        codigo.add("@Path(\"/salvar\") ");
        codigo.add("public Response salvar( ");
        codigo.add("@HeaderParam(\"seq_usuario_selecionado\") String pSeqUsuarioSelecionado, ");
        codigo.add("@HeaderParam(\"email_usuario_logado\") String pEmailUsuarioLogado, ");
        codigo.add("@HeaderParam(\"seq_modulo\") String pSeqModulo, ");
        codigo.add("String pJson ");
        codigo.add(") { ");
        codigo.add("         ");
        codigo.add("        ");
        codigo.add("Gson gson = new GsonBuilder().setDateFormat(\"dd-MM-yyyy'T'HH:mm:ss\").create(); ");
        codigo.add("" + campo.getClasse() + " " + campo.getInstancia() + " = gson.fromJson(pJson, " + campo.getClasse() + ".class); ");
        codigo.add("         ");
        codigo.add("" + campo.getInstancia() + ".setSeqUsuario(Long.valueOf(pSeqUsuarioSelecionado)); ");
        codigo.add("" + campo.getInstancia() + ".setEmailUsuario(pEmailUsuarioLogado); ");
        codigo.add("         ");
        codigo.add("" + campo.getClasse() + "Service " + campo.getInstancia() + "Service = new " + campo.getClasse() + "Service(" + campo.getInstancia() + ".getSeqUsuario()); ");
        codigo.add("HashMap<String, String> retorno = " + campo.getInstancia() + "Service.salvar(" + campo.getInstancia() + "); ");
        codigo.add("         ");
        codigo.add("return Response.status(200).header(\"result\", retorno.get(\"result\")).header(\"msg\", retorno.get(\"msg\")).header(\"Access-Control-Allow-Origin\", \"*\").build(); ");
        codigo.add("} ");
        codigo.add("     ");
        codigo.add("@GET ");
        codigo.add("@Produces(MediaType.APPLICATION_JSON) ");
        codigo.add("@Path(\"/novo\") ");
        codigo.add("public Response novo( ");
        codigo.add("@HeaderParam(\"seq_usuario_selecionado\") String pSeqUsuarioSelecionado, ");
        codigo.add("@HeaderParam(\"email_usuario_logado\") String pEmailUsuarioLogado, ");
        codigo.add("@HeaderParam(\"seq_modulo\") String pSeqModulo ");
        codigo.add(") { ");
        codigo.add("         ");
        codigo.add("" + campo.getClasse() + " " + campo.getInstancia() + " = new " + campo.getClasse() + "(); ");
        codigo.add("" + campo.getInstancia() + ".setSeqUsuario(Long.valueOf(pSeqUsuarioSelecionado)); ");
        codigo.add("" + campo.getInstancia() + ".setEmailUsuario(pEmailUsuarioLogado);");
        codigo.add("         ");
        codigo.add("Gson gson = new GsonBuilder().setDateFormat(\"dd-MM-yyyy'T'HH:mm:ss\").create(); ");
        codigo.add("         ");
        codigo.add("" + campo.getClasse() + "Service " + campo.getInstancia() + "Service = new " + campo.getClasse() + "Service(" + campo.getInstancia() + ".getSeqUsuario()); ");
        codigo.add("         ");
        codigo.add("" + campo.getInstancia() + " = (" + campo.getClasse() + ") " + campo.getInstancia() + "Service.novo(" + campo.getInstancia() + "); ");
        codigo.add("         ");
        codigo.add("         ");
        codigo.add("" + campo.getInstancia() + ".setSeqUsuario(Long.valueOf(pSeqUsuarioSelecionado)); ");
        codigo.add("" + campo.getInstancia() + ".setEmailUsuario(pEmailUsuarioLogado); ");
        codigo.add("return Response.status(200).entity(gson.toJson(" + campo.getInstancia() + ")).header(\"result\", \"true\").header(\"Access-Control-Allow-Origin\", \"*\").build(); ");
        codigo.add("} ");
        codigo.add("     ");
        codigo.add("@GET ");
        codigo.add("@Produces(MediaType.APPLICATION_JSON) ");
        codigo.add("@Path(\"/buscar\") ");
        codigo.add("public Response buscar( ");
        codigo.add("@HeaderParam(\"seq_usuario_selecionado\") String pSeqUsuarioSelecionado, ");
        codigo.add("@HeaderParam(\"email_usuario_logado\") String pEmailUsuarioLogado, ");
        codigo.add("@HeaderParam(\"seq_modulo\") String pSeqModulo, ");
        codigo.add("@HeaderParam(\"codigo\") String pCodigo ");
        codigo.add(") { ");
        codigo.add("         ");
        codigo.add("         ");
        codigo.add("" + campo.getClasse() + "Service " + campo.getInstancia() + "Service = new " + campo.getClasse() + "Service(Long.valueOf(pSeqUsuarioSelecionado)); ");
        codigo.add("" + campo.getClasse() + " " + campo.getInstancia() + " = " + campo.getInstancia() + "Service.buscar(pEmailUsuarioLogado, pCodigo); ");
        codigo.add("Gson gson = new GsonBuilder().setDateFormat(\"dd-MM-yyyy'T'HH:mm:ss\").create(); ");
        codigo.add("         ");
        codigo.add("" + campo.getInstancia() + ".setSeqUsuario(Long.valueOf(pSeqUsuarioSelecionado)); ");
        codigo.add("" + campo.getInstancia() + ".setEmailUsuario(pEmailUsuarioLogado); ");
        codigo.add("         ");
        codigo.add("String json = gson.toJson(" + campo.getInstancia() + "); ");
        codigo.add("         ");
        codigo.add("return Response.status(200).entity(json).header(\"result\", \"true\").header(\"Access-Control-Allow-Origin\", \"*\").build(); ");
        codigo.add("} ");
        codigo.add("     ");
        codigo.add("@POST ");
        codigo.add("@Produces(MediaType.APPLICATION_JSON) ");
        codigo.add("@Path(\"/deletar\") ");
        codigo.add("public Response deletar( ");
        codigo.add("@HeaderParam(\"seq_usuario_selecionado\") String pSeqUsuarioSelecionado, ");
        codigo.add("@HeaderParam(\"email_usuario_logado\") String pEmailUsuarioLogado, ");
        codigo.add("@HeaderParam(\"seq_modulo\") String pSeqModulo,");
        codigo.add("String pJson ");
        codigo.add(") { ");
        codigo.add("         ");
        codigo.add("Gson gson = new GsonBuilder().setDateFormat(\"dd-MM-yyyy'T'HH:mm:ss\").create(); ");
        codigo.add("" + campo.getClasse() + " " + campo.getInstancia() + " = gson.fromJson(pJson, " + campo.getClasse() + ".class); ");
        codigo.add("" + campo.getInstancia() + ".setSeqUsuario(Long.valueOf(pSeqUsuarioSelecionado)); ");
        codigo.add("" + campo.getInstancia() + ".setEmailUsuario(pEmailUsuarioLogado); ");
        codigo.add("         ");
        codigo.add("" + campo.getClasse() + "Service " + campo.getInstancia() + "Service = new " + campo.getClasse() + "Service(" + campo.getInstancia() + ".getSeqUsuario()); ");
        codigo.add("HashMap<String, String> retorno = " + campo.getInstancia() + "Service.deletar(" + campo.getInstancia() + "); ");
        codigo.add("         ");
        codigo.add("return Response.status(200).header(\"result\", retorno.get(\"result\")).header(\"msg\", retorno.get(\"msg\")).header(\"Access-Control-Allow-Origin\", \"*\").build(); ");
        codigo.add("} ");

        System.out.println("******** webservice *****************");
        System.out.println("");

        String caminho = System.getProperty("user.dir") + "/src/java/WebService";
        System.out.println(caminho);

        File diretorio = new File(caminho);
        diretorio.mkdir();

        FileWriter fw = new FileWriter(caminho + "/" + campo.classe + "WS.java");
        BufferedWriter bw = new BufferedWriter(fw);

        bw.newLine();
        bw.write("/***********************************************************************************/");
        bw.newLine();
        bw.write("/*CRIADO DE FORMA AUTOMATICA PELO GERADOR DE CÓDIGO DA CROSSYSTEM*/");
        bw.newLine();
        bw.write("/*Data:  " + String.valueOf(new Date(System.currentTimeMillis())) + " */");
        bw.newLine();
        bw.write("/***********************************************************************************/");
        bw.newLine();
        bw.write("package WebService;");
        bw.newLine();
        bw.write("@Path(\"/" + campo.getNome().replace("seq_", "") + "\")");
        bw.write("public class " + campo.getClasse() + "WS {");

        bw.newLine();
        bw.newLine();
        for (int i = 0; i < codigo.size(); i++) {
            bw.write(codigo.get(i));
            bw.newLine();
        }

        bw.newLine();
        bw.newLine();
        bw.write("/* GET´s SET´s*/");
        bw.newLine();
        bw.newLine();
        bw.write("}");

        bw.close();
        fw.close();
        System.out.println("");
        System.out.println("*******************************");

    }

    public List<Campo> listarCampos(String pTabela) {
        try {
            Conexao conexao = new Conexao();
            Connection conn = conexao.getConnection();
            String sql = ""
                    + "SELECT case COLUMN_NAME when '" + pTabela + "' then '0' else '1' end ordem, COLUMN_NAME nome,lower(substr(replace(initCap(COLUMN_NAME),'_',''),1,1))||substr(replace(initCap(COLUMN_NAME),'_',''),2,length(replace(initCap(COLUMN_NAME),'_',''))) atributo,replace(initCap(COLUMN_NAME),'_','') atributoGetSet,lower(substr(replace(initCap(TABLE_NAME),'_',''),1,1))||substr(replace(initCap(TABLE_NAME),'_',''),2,length(replace(initCap(TABLE_NAME),'_',''))) instancia,Replace(initCap(TABLE_NAME),'_','') classe,       DATA_TYPE tipo, \n"
                    + "       character_maximum_length tamanho, \n"
                    + "       numeric_precision precisao \n"
                    + "  FROM information_schema.columns \n"
                    + "WHERE UPPER(TABLE_NAME)= '" + pTabela + "'order by 1 ";

            System.out.println(sql);

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<Campo> listaCampo = new ArrayList<Campo>();

            while (rs.next()) {
                Campo campo = new Campo();
                campo.setNome(rs.getString("nome"));
                campo.setAtributo(rs.getString("atributo"));
                campo.setInstancia(rs.getString("instancia"));
                campo.setClasse(rs.getString("CLASSE"));
                campo.setAtributoGetSet(rs.getString("atributoGetSet"));

                if ((rs.getString("TIPO").equals("character varying")) || (rs.getString("TIPO").equals("text"))) {
                    campo.setTipo("String");
                } else if ((rs.getString("NOME").toUpperCase().contains("LATITUDE")) || (rs.getString("NOME").toUpperCase().contains("LONGITUDE"))) {
                    campo.setTipo("Double");
                } else if (rs.getString("TIPO").equals("integer")) {
                    campo.setTipo("Long");
                } else if (rs.getString("TIPO").equals("date")) {
                    campo.setTipo("Date");
                } else if (rs.getString("TIPO").equals("numeric")) {
                    if (rs.getString("PRECISAO") == null) {
                        campo.setTipo("Long");
                    } else {
                        campo.setTipo("BigDecimal");
                    }
                }

                listaCampo.add(campo);
            }
            return listaCampo;

        } catch (SQLException ex) {
            Logger.getLogger(CriarCRUD.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());                     // Print da mensagem de tratamento de erro.

            return null;
        }
    }

}
