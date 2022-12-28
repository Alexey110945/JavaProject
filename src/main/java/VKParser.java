import com.vk.api.sdk.client.Lang;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.SearchResponse;
import java.lang.Thread;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class VKParser {
    private final TransportClient transportClient = new HttpTransportClient();
    private final VkApiClient vk = new VkApiClient(transportClient);
    private final UserActor actor;

    public VKParser() {
        List<String> reader = null;
        try {
            reader = Files.readAllLines(Path.of("resources/TokenAndIdApp"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assert reader != null;
        actor = new UserActor(Integer.parseInt(reader.get(1)), reader.get(0));
       }

    public SearchResponse getGenderAndCity(String studentFullName) {
        var request = FindInGroup(studentFullName);
        if (request.getItems().size() == 0)
            return null;
        return request;
    }

    private SearchResponse FindInGroup(String fullName) {
        SearchResponse request = null;
        try {
            request = vk
                    .users()
                    .search(actor)
                    .fields(Fields.SEX, Fields.CITY)
                    .lang(Lang.RU)
                    .q(fullName)
                    .count(1)
                    .execute();
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return request;
    }
}

