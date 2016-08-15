import com.ibm.watson.developer_cloud.dialog.v1.DialogService;
import com.ibm.watson.developer_cloud.dialog.v1.model.Dialog;
import org.junit.Test;

import java.util.List;

/**
 * Space: dev
 * Service name: Dialog-service
 * Credential name: Dialog-credentials
 *
 * url: https://gateway.watsonplatform.net/dialog/api
 * username: 9307be6e-5b07-4dfb-985b-dea20914f28a
 * password: gDbqzMtQaKil
 */
public class DialogTest {

    @Test
    public void dialogTest() {

        DialogService service = new DialogService();
        service.setUsernameAndPassword("9307be6e-5b07-4dfb-985b-dea20914f28a", "gDbqzMtQaKil");

        List<Dialog> dialogs = service.getDialogs().execute();
        System.out.println(dialogs);
    }

}
