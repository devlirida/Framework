/** ******************************************************************************** */
/*CRIADO DE FORMA AUTOMATICA PELO GERADOR DE CÓDIGO DA CROSSYSTEM*/
 /*Data:  2020-10-31 */
/** ******************************************************************************** */
package DAO;

import Bean.Cliente;
import Conexao.Conexao;
import Interface.IDAO;
import static Util.LiridaUtil.gerarRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteDAO implements IDAO {

    @Override
    public String inserir(Object pObject) {
        String retorno = "";
        Cliente cliente = (Cliente) pObject;
        try {
            Conexao conexao = new Conexao();
            Connection conn = conexao.getConnection();
            String sql = "insert into CLIENTE"
                    + " (seq_usuario,codigo,data_cadastro,data_ult_alteracao,situacao,situacao_descricao,tipo_pessoa,nome,documento,apelido,telefone,whatsapp,email,site,email_usuario,vendedor,observacao,chave,tag_01,tag_02,tag_03,tag_04,tag_05,tag_06,tag_07,tag_08,tag_09,tag_10,inscricao_municipal,inscricao_estadual,codigo_processo,codigo_processo_item,linha1,linha2)"
                    + " values "
                    + " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            try {
                ps.setLong(1, cliente.getSeqUsuario());
            } catch (NullPointerException e) {
                ps.setObject(1, null);
            }
            ps.setString(2, cliente.getCodigo());
            try {
                ps.setDate(3, new java.sql.Date(cliente.getDataCadastro().getTime()));
            } catch (NullPointerException e) {
                ps.setDate(3, null);
            }
            try {
                ps.setDate(4, new java.sql.Date(cliente.getDataUltAlteracao().getTime()));
            } catch (NullPointerException e) {
                ps.setDate(4, null);
            }
            ps.setString(5, cliente.getSituacao());
            ps.setString(6, cliente.getSituacaoDescricao());
            ps.setString(7, cliente.getTipoPessoa());
            ps.setString(8, cliente.getNome());
            ps.setString(9, cliente.getDocumento());
            ps.setString(10, cliente.getApelido());
            ps.setString(11, cliente.getTelefone());
            ps.setString(12, cliente.getWhatsapp());
            ps.setString(13, cliente.getEmail());
            ps.setString(14, cliente.getSite());
            ps.setString(15, cliente.getEmailUsuario());
            ps.setString(16, cliente.getVendedor());
            ps.setString(17, cliente.getObservacao());
            ps.setString(18, cliente.getChave());
            ps.setString(19, cliente.getTag01());
            ps.setString(20, cliente.getTag02());
            ps.setString(21, cliente.getTag03());
            ps.setString(22, cliente.getTag04());
            ps.setString(23, cliente.getTag05());
            ps.setString(24, cliente.getTag06());
            ps.setString(25, cliente.getTag07());
            ps.setString(26, cliente.getTag08());
            ps.setString(27, cliente.getTag09());
            ps.setString(28, cliente.getTag10());
            ps.setString(29, cliente.getInscricaoMunicipal());
            ps.setString(30, cliente.getInscricaoEstadual());
            ps.setString(31, cliente.getCodigoProcesso());
            ps.setString(32, cliente.getCodigoProcessoItem());

// Linha 1 - Apresentação no card do APP e WEB
            ps.setString(33, cliente.getCodigo());

// Linha 2 - Apresentação no card do APP e WEB
            ps.setString(34, "Situação: " + cliente.getSituacao());

            ps.execute();

            ResultSet rsID = ps.getGeneratedKeys();
            if (rsID.next()) {
                Long seq = rsID.getLong("seq_cliente");
                cliente.setSeqCliente(seq);
            }

            ps.close();

            retorno = "Informações armazenadas com sucesso.";
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            retorno = ex.getMessage();
        }

        return retorno;
    }

    @Override
    public Cliente novo(Object pObject) {
        Cliente cliente = (Cliente) pObject;
        cliente.setSituacao("0");
        cliente.setCodigo(gerarRandom());
        return cliente;
    }

    @Override
    public String alterar(Object pObject) {
        String retorno;
        Cliente cliente = (Cliente) pObject;
        try {
            Conexao conexao = new Conexao();
            Connection conn = conexao.getConnection();
            String sql = "update CLIENTE set seq_usuario = ?,codigo = ?,data_cadastro = ?,data_ult_alteracao = ?,situacao = ?,situacao_descricao = ?,tipo_pessoa = ?,nome = ?,documento = ?,apelido = ?,telefone = ?,whatsapp = ?,email = ?,site = ?,email_usuario = ?,vendedor = ?,observacao = ?,chave = ?,tag_01 = ?,tag_02 = ?,tag_03 = ?,tag_04 = ?,tag_05 = ?,tag_06 = ?,tag_07 = ?,tag_08 = ?,tag_09 = ?,tag_10 = ?,inscricao_municipal = ?,inscricao_estadual = ?,codigo_processo = ?,codigo_processo_item = ?,linha1 = ?,linha2 = ? where seq_cliente = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            try {
                ps.setLong(1, cliente.getSeqUsuario());
            } catch (NullPointerException e) {
                ps.setObject(1, null);
            }
            ps.setString(2, cliente.getCodigo());
            try {
                ps.setDate(3, new java.sql.Date(cliente.getDataCadastro().getTime()));
            } catch (NullPointerException e) {
                ps.setDate(3, null);
            }
            try {
                ps.setDate(4, new java.sql.Date(cliente.getDataUltAlteracao().getTime()));
            } catch (NullPointerException e) {
                ps.setDate(4, null);
            }
            ps.setString(5, cliente.getSituacao());
            ps.setString(6, cliente.getSituacaoDescricao());
            ps.setString(7, cliente.getTipoPessoa());
            ps.setString(8, cliente.getNome());
            ps.setString(9, cliente.getDocumento());
            ps.setString(10, cliente.getApelido());
            ps.setString(11, cliente.getTelefone());
            ps.setString(12, cliente.getWhatsapp());
            ps.setString(13, cliente.getEmail());
            ps.setString(14, cliente.getSite());
            ps.setString(15, cliente.getEmailUsuario());
            ps.setString(16, cliente.getVendedor());
            ps.setString(17, cliente.getObservacao());
            ps.setString(18, cliente.getChave());
            ps.setString(19, cliente.getTag01());
            ps.setString(20, cliente.getTag02());
            ps.setString(21, cliente.getTag03());
            ps.setString(22, cliente.getTag04());
            ps.setString(23, cliente.getTag05());
            ps.setString(24, cliente.getTag06());
            ps.setString(25, cliente.getTag07());
            ps.setString(26, cliente.getTag08());
            ps.setString(27, cliente.getTag09());
            ps.setString(28, cliente.getTag10());
            ps.setString(29, cliente.getInscricaoMunicipal());
            ps.setString(30, cliente.getInscricaoEstadual());
            ps.setString(31, cliente.getCodigoProcesso());
            ps.setString(32, cliente.getCodigoProcessoItem());

// Linha 1 - Apresentação no card do APP e WEB
            ps.setString(33, cliente.getCodigo());

// Linha 2 - Apresentação no card do APP e WEB
            ps.setString(34, "Situação: " + cliente.getSituacao());
            ps.setLong(35, cliente.getSeqCliente());
            ps.execute();
            ps.close();
            retorno = "Informações armazenadas com sucesso.";
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            retorno = ex.getMessage();
        }

        return retorno;
    }

    @Override
    public List<Object> listar(String pData) {
        try {
            Conexao conexao = new Conexao();
            Connection conn = conexao.getConnection();
            String sql = "select * From (SELECT concat(codigo) campo_pesquisa, CLIENTE.* FROM CLIENTE) CLIENTE where campo_pesquisa like '%" + pData + "%' or  campo_pesquisa ='" + pData+"'";
            System.out.println(sql);

            List<Object> listaCliente = new ArrayList<Object>();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setSeqCliente(rs.getLong("seq_cliente"));
                cliente.setSeqUsuario(rs.getLong("seq_usuario"));
                cliente.setCodigo(rs.getString("codigo"));
                cliente.setDataCadastro(rs.getDate("data_cadastro"));
                cliente.setDataUltAlteracao(rs.getDate("data_ult_alteracao"));
                cliente.setSituacao(rs.getString("situacao"));
                cliente.setSituacaoDescricao(rs.getString("situacao_descricao"));
                cliente.setTipoPessoa(rs.getString("tipo_pessoa"));
                cliente.setNome(rs.getString("nome"));
                cliente.setDocumento(rs.getString("documento"));
                cliente.setApelido(rs.getString("apelido"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setWhatsapp(rs.getString("whatsapp"));
                cliente.setEmail(rs.getString("email"));
                cliente.setSite(rs.getString("site"));
                cliente.setEmailUsuario(rs.getString("email_usuario"));
                cliente.setVendedor(rs.getString("vendedor"));
                cliente.setObservacao(rs.getString("observacao"));
                cliente.setChave(rs.getString("chave"));
                cliente.setTag01(rs.getString("tag_01"));
                cliente.setTag02(rs.getString("tag_02"));
                cliente.setTag03(rs.getString("tag_03"));
                cliente.setTag04(rs.getString("tag_04"));
                cliente.setTag05(rs.getString("tag_05"));
                cliente.setTag06(rs.getString("tag_06"));
                cliente.setTag07(rs.getString("tag_07"));
                cliente.setTag08(rs.getString("tag_08"));
                cliente.setTag09(rs.getString("tag_09"));
                cliente.setTag10(rs.getString("tag_10"));
                cliente.setInscricaoMunicipal(rs.getString("inscricao_municipal"));
                cliente.setInscricaoEstadual(rs.getString("inscricao_estadual"));
                cliente.setCodigoProcesso(rs.getString("codigo_processo"));
                cliente.setCodigoProcessoItem(rs.getString("codigo_processo_item"));
                cliente.setLinha1(rs.getString("linha1"));
                cliente.setLinha2(rs.getString("linha2"));
                listaCliente.add(cliente);
            }

            ps.execute();
            ps.close();

            return listaCliente;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());                     // Print da mensagem de tratamento de erro.
            return null;
        }
    }

    @Override
    public Object buscar(String pData) {
        return listar(pData).get(0);
    }

    @Override
    public String deletar(String pCodigo) {
        String retorno = "";
        try {
            Conexao conexao = new Conexao();
            Connection conn = conexao.getConnection();
            String sql = "DELETE FROM CLIENTE WHERE codigo =  ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, pCodigo);

            ps.execute();
            ps.close();

            ps.close();

            retorno = "Informações deletada com sucesso.";
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            retorno = ex.getMessage();
        }

        return retorno;
    }

}
