import com.vk.api.sdk.client.Lang;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.base.Sex;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.SearchResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.lang.Thread;

public class VKParser {
    TransportClient transportClient = new HttpTransportClient();
    VkApiClient vk = new VkApiClient(transportClient);

    public VKParser(){
        SearchResponse request = null;

        var actor = new UserActor(1,"");
        try{
            request = vk
                    .users()
                    .search(actor)
                    .fields(Fields.SEX, Fields.BDATE, Fields.HOME_TOWN, Fields.CITY)
                    .lang(Lang.EN)
                    .q("Годзилла")
                    .count(1)
                    .execute();

        } catch (Exception ignore) {}

        if (request != null)
            System.out.println(request.getItems());
    }
}

