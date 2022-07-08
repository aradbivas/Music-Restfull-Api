package restapi.webapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeVideo {

        @JsonUnwrapped @JsonProperty("audio")
        public List<SongAudio> songAudio = new ArrayList<SongAudio>();



        @Override
        public String toString()
        {
            return songAudio.toString();
        }

}
