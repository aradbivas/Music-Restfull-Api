package restapi.webapp;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
@Service
public class SongService {

    private RestTemplate template;

    public SongService(RestTemplateBuilder template) {
        this.template = template.build();
    }

    @Async
    public CompletableFuture<SongTrackResponse> songDetails(String songName, String artistName){

        String urlTemplate ="https://musicbrainz.org/ws/2/recording/?query=recording:"+songName+"%20and%20artist:" +artistName + "&fmt=json&inc=&limit=1";
        SongTrackResponse aUser = this.template.getForObject(urlTemplate,SongTrackResponse.class);
        return CompletableFuture.completedFuture(aUser);
    }
    @Async
    public CompletableFuture<First> songAudio(String songName, String artistName) {
        String name=artistName+" "+songName;
        try {
            name = URLEncoder.encode(name,"utf-8");
            name = name.replace(" " , "+");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String urlTemplate =String.format("https://spotify-scraper.p.rapidapi.com/v1/track/download?track=%s",name);

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-RapidAPI-Key", "107c1fc9bbmsh66a0fd84e07fef2p10637fjsn36cb29517a4d");
            headers.set("X-RapidAPI-Host", "spotify-scraper.p.rapidapi.com");

            HttpEntity<Void> requestEntity = new HttpEntity<>(null,headers);
            ResponseEntity<First> response = this.template.exchange(
                    urlTemplate, HttpMethod.GET, requestEntity, First.class);
            return CompletableFuture.completedFuture(response.getBody());


    }

    }
