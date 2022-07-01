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
public class SongTrackResponse {
    @JsonUnwrapped
    @JsonProperty("recordings")
    public List<Song> recordings = new ArrayList<Song>();

    @Override
    public String toString()
    {
        return recordings.toString();
    }
}
