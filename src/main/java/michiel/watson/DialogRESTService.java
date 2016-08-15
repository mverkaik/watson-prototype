package michiel.watson;

import com.ibm.watson.developer_cloud.dialog.v1.DialogService;
import com.ibm.watson.developer_cloud.dialog.v1.model.Conversation;
import com.ibm.watson.developer_cloud.dialog.v1.model.Dialog;
import com.ibm.watson.developer_cloud.dialog.v1.model.DialogContent;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.ibm.watson.developer_cloud.util.MediaType.APPLICATION_FORM_URLENCODED;
import static com.ibm.watson.developer_cloud.util.MediaType.APPLICATION_JSON;

/**
 * Service to interact with IBM Watson Dialog service
 */
@Path("/dialogs")
public class DialogRESTService {

    private static final String USER_NAME = "9307be6e-5b07-4dfb-985b-dea20914f28a";

    private static final String PASSWORD = "gDbqzMtQaKil";

    private DialogService dialogService = new DialogService();

    private ObjectMapper jsonObjMapper = new ObjectMapper();

    /**
     * Default constructor: initialize dialog service
     */
    public DialogRESTService() {

        dialogService.setUsernameAndPassword("9307be6e-5b07-4dfb-985b-dea20914f28a", "gDbqzMtQaKil");
    }

    /**
     * Get all dialogs
     *
     * @return All dialogs
     */
    @GET
    @Path("/all")
    @Produces(APPLICATION_JSON)
    public String getDialogs() {

        List<Dialog> dialogs = dialogService.getDialogs().execute();
        return dialogs.toString();
    }


    /**
     * Get dialog content
     *
     * @param dialogID The dialog identifier
     * @return The dialog content (definition)
     */
    @GET
    @Path("/id/{dialogID}")
    @Produces(APPLICATION_JSON)
    public String getDialogContent(@PathParam("dialogID") String dialogID) {

        List<DialogContent> content = dialogService.getContent(dialogID).execute();
        return content.toString();
    }


    /**
     * Get dialog profile
     *
     * @param dialogID The dialog identifier
     * @param clientID The client identifier
     * @return A dialog profile
     */
    @GET
    @Path("/id/{dialogID}/profile/{clientID}")
    @Produces(APPLICATION_JSON)
    public String getDialogProfile(@PathParam("dialogID") String dialogID, @PathParam("clientID") Integer clientID)
            throws IOException {

        Map<String, String> profile = dialogService.getProfile(dialogID, clientID, null).execute();
        return jsonObjMapper.writeValueAsString(profile);
    }


    /**
     * Create a conversation
     *
     * @param dialogID The dialog identifier
     * @return
     */
    @GET
    @Path("/id/{dialogID}/conversation/create")
    @Produces(APPLICATION_JSON)
    public String createConversation(@PathParam("dialogID") String dialogID) {

        Conversation c = dialogService.createConversation(dialogID).execute();
        return c.toString();
    }


    /**
     * Converse with Watson
     *
     * @param dialogID
     * @param conversationID
     * @param clientID
     * @param input
     * @return
     */
    @POST
    @Path("/id/{dialogID}/conversation")
    @Consumes(APPLICATION_FORM_URLENCODED)
    @Produces(APPLICATION_JSON)
    public String converse(@PathParam("dialogID") String dialogID,
                           @FormParam("conversationID") Integer conversationID,
                           @FormParam("clientID") Integer clientID,
                           @FormParam("input") String input) {

        Conversation c = new Conversation();
        c.setDialogId(dialogID);
        c.setId(conversationID);
        c.setClientId(clientID);
        c.setInput(input);

        Conversation c1 = dialogService.converse(c, input).execute();

        return c1.toString();
    }


}
