import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.SearchResponse;
import java.lang.Thread;

public class VKParser {
    TransportClient transportClient = new HttpTransportClient();
    VkApiClient vk = new VkApiClient(transportClient);

    public String getSex(String studentFullName) {
        var token = "Some token";
        var actor = new UserActor(1, token);

        var request = FindInGroup(actor, studentFullName, 215509427); // basingProgramming
        if (request.getItems().size() == 0){
            request = FindInGroup(actor, studentFullName, 49378); // новости радиофака
            if (request.getItems().size() == 0)
                return "0";
        }
        return request.getItems().get(0).getSex().getValue();
    }

    private SearchResponse FindInGroup(UserActor actor, String fullName, int idGroup) {
        SearchResponse request = null;
        try {
            request = vk
                    .users()
                    .search(actor)
                    .groupId(idGroup)
                    .q(fullName)
                    .fields(Fields.SEX)
                    .count(1)
                    .execute();
            Thread.sleep(260);
        } catch (Exception ignore) {}
        return request;
    }
}

