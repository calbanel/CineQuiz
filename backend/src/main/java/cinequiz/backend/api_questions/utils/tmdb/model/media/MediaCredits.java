package cinequiz.backend.api_questions.utils.tmdb.model.media;

import java.util.ArrayList;

import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonCast;
import cinequiz.backend.api_questions.utils.tmdb.model.people.PersonCrew;

public class MediaCredits {
    public int id;
    public ArrayList<PersonCast> cast;
    public ArrayList<PersonCrew> crew;
}
