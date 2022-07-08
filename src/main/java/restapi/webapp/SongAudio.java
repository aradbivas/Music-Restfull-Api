package restapi.webapp;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SongAudio {
    @Id @GeneratedValue Long audioId;
    public String url;
    public String durationText;

    public SongAudio(String url, String durationText) {
        this.url = url;
        this.durationText = durationText;
    }

    public String getUrl() {
        return url;
    }

    public String getDurationText() {
        return durationText;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDurationText(String durationText) {
        this.durationText = durationText;
    }

    @Override
    public String toString(){
        return url;
    }
}
