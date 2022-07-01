package restapi.webapp;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
@Service
public class SongService {

    private RestTemplate template;

    public SongService(RestTemplateBuilder template) {
        this.template = template.build();
    }

    @Async
    public CompletableFuture<SongTrackResponse> userDetails(String songName,String artistName){

    String urlTemplate ="https://musicbrainz.org/ws/2/recording/?query=recording:"+songName+"%20and%20artist:" +artistName + "&fmt=json&inc=&limit=1";
        SongTrackResponse aUser = this.template.getForObject(urlTemplate,SongTrackResponse.class);
        return CompletableFuture.completedFuture(aUser);
    }
}
