package WebService;

import Bean.Cliente;
import DAO.ClienteDAO;
import Interface.IDAO;
import Util.Json;
import Util.LiridaUtil;
import static Util.LiridaUtil.lerJson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("exemplo")
public class ExemploWS {

    @Context
    private UriInfo context;

    public ExemploWS() {

    }

    @GET
    @Produces("application/json")
    @Path("/vai/")
    public Response listar(
    ) {
        System.out.println("Funcionou..");
        return Response.status(200).header("status", "ok bruno").header("Access-Control-Allow-Origin", "*").build();
    }

    @POST
    @Produces("application/json")
    @Path("/listar/")
    public Response listar(
            String pData
    ) {
        System.out.println("Json Listar: SERVIÇO: " + pData);

        String token = lerJson(pData, "token");
        String texto = lerJson(pData, "texto");
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy'T'HH:mm:ss").create();

        IDAO clienteDAO = new ClienteDAO();

        List<Cliente> obj = (List<Cliente>) (List<?>) clienteDAO.listar(texto);

        String retorno = LiridaUtil.retornarJson(obj, true, "Retorno OK", token);
        return Response.status(200).entity(retorno).header("Access-Control-Allow-Origin", "*").build();
    }

    @POST
    @Produces("application/json")
    @Path("/buscar/")
    public Response buscar(
            String pData
    ) {
        String token = lerJson(pData, "token");
        String codigo = lerJson(pData, "codigo");

        System.out.println("Json buscar: " + pData);
        ClienteDAO clienteDAO = new ClienteDAO();

        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy'T'HH:mm:ss").create();
        Object obj = clienteDAO.buscar(codigo);

        String retorno = LiridaUtil.retornarJson(obj, true, "Retorno OK", "token");
        return Response.status(200).entity(retorno).header("Access-Control-Allow-Origin", "*").build();
    }

    @POST
    @Produces("application/json")
    @Path("/novo")
    public Response novo(
            //@HeaderParam("email_usuario_logado") String pEmailUsuarioLogado,
            //@HeaderParam("seq_modulo") String pSeqModulo,
            String pData
    ) {

        String token = lerJson(pData, "token");

        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy'T'HH:mm:ss").create();
        Cliente liridaCliente = gson.fromJson(pData, Cliente.class);
        //liridaCliente.setEmailUsuario(pEmailUsuarioLogado);
        liridaCliente.setSituacao("0");
        ClienteDAO dao = new ClienteDAO();
        liridaCliente = dao.novo(liridaCliente);

        String retorno = LiridaUtil.retornarJson(liridaCliente, true, "Retorno OK", token);
        return Response.status(200).entity(retorno).header("Access-Control-Allow-Origin", "*").build();

    }

    @POST
    @Produces("application/json")
    @Path("/salvar")
    public Response inserir(
            //            @HeaderParam("email_usuario_logado") String pEmailUsuarioLogado,
            //            @HeaderParam("seq_modulo") String pSeqModulo,
            String pData) {
        System.out.println("Data: " + pData);

        String token = lerJson(pData, "token");
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy'T'HH:mm:ss").create();
        Json json = gson.fromJson(pData, Json.class);

        String vai = gson.toJson(json.getJson());
        System.out.println("Json VAI: " + vai);
        Cliente liridaCliente = gson.fromJson(vai, Cliente.class);

        System.out.println("Clietne: " + liridaCliente.getNome());
        String retorno;
        ClienteDAO dao = new ClienteDAO();
        if (liridaCliente.getSeqCliente() == 0) {
            retorno = dao.inserir(liridaCliente);
        } else {
            retorno = dao.alterar(liridaCliente);
        }

        retorno = LiridaUtil.retornarJson(liridaCliente, true, retorno, token);
        return Response.status(200).entity(retorno).header("Access-Control-Allow-Origin", "*").build();
    }

    @POST
    @Produces("application/json")
    @Path("/deletar")
    public Response deletar(
            String pData) {

        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy'T'HH:mm:ss").create();

        String token = lerJson(pData, "token");
        String codigo = lerJson(pData, "codigo");

        ClienteDAO dao = new ClienteDAO();
        dao.deletar(codigo);
        String retorno = LiridaUtil.retornarJson(null, true, "Retorno OK", "token");
        return Response.status(200).entity(retorno).header("Access-Control-Allow-Origin", "*").build();
    }
}
