package restapi.webapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

@Value
@JsonPropertyOrder({"name"})
public class playlistDTO {
    @JsonIgnore
    private final Playlist playlist;

    public String getName() {
        return this.playlist.getName();
    }

}
