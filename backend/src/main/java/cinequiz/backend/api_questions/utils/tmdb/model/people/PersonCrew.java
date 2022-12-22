package cinequiz.backend.api_questions.utils.tmdb.model.people;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonCrew extends PersonMovieCredits {

    @JsonProperty("department")
    private String department;

    @JsonProperty("job")
    private String job;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

}