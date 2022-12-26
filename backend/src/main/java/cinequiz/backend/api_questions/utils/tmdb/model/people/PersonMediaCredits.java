package cinequiz.backend.api_questions.utils.tmdb.model.people;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonMediaCredits extends PersonInfos {

    @JsonProperty("original_name")
    private String originalName;

    @JsonProperty("credit_id")
    private String creditId;

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

}
