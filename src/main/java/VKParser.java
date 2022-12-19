import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.SearchResponse;
import java.lang.Thread;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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

    public ArrayList<String> getGenderAndHomeTown(String studentFullName) {

        var result = new ArrayList<String>();

        var request = FindInGroup(studentFullName, 215509427); // basicprogrammingrtf2022
        if (request.getItems().size() == 0) {
            request = FindInGroup(studentFullName, 22941070); // ural.federal.university
            if (request.getItems().size() == 0)
                return null;
        }
        if (request.getItems().get(0).getSex() != null)
            result.add(request.getItems().get(0).getSex().getValue());
        if (request.getItems().get(0).getCity() != null)
            result.add(request.getItems().get(0).getCity().getTitle());
        return result;
    }

    private SearchResponse FindInGroup(String fullName, int idGroup) {
        SearchResponse request = null;
        try {
            request = vk
                    .users()
                    .search(actor)
                    .count(1)
                    .groupId(idGroup)
                    .q(fullName)
                    .fields(Fields.SEX, Fields.CITY)
                    .execute();
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return request;
    }
}

