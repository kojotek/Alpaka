package pl.edu.uam.restapi.storage.resources;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.uam.restapi.storage.database.UserDatabase;
import pl.edu.uam.restapi.storage.model.ErrorMessage;
import pl.edu.uam.restapi.storage.model.Survey;
import org.springframework.web.bind.annotation.CrossOrigin;
import pl.edu.uam.restapi.dokumentacjaibledy.exceptions.UserException;
import javax.validation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.*;

@RestController
public abstract class AbstractSurveyResource {

    protected abstract UserDatabase getDatabase();
    @CrossOrigin(origins = "*")
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ApiOperation(value = "Get surveys collection", notes = "Get surveys collection", response = Survey.class, responseContainer = "LIST")
    public Collection<Survey> list()

    {
        return getDatabase().getSurveys();
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value="/{surveyId}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ApiOperation(value = "Get survey by id", notes = "[note]Get user by id", response = Survey.class)
    public Survey getUser(@ PathVariable("surveyId") @Valid String surveyId) throws Exception {
        Survey survey = getDatabase().getSurvey(surveyId);

        if (survey == null) {
            throw new UserException("User not found", "Użytkownik nie został znaleziony", "http://docu.pl/errors/user-not-found");
            //return new ErrorMessage("Error 404", "Requested ID did not match that of any survey in the database. Make sure it is a proper ObjectId");
        }

        return survey;
    }
    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create survey", notes = "Create survey", response = Survey.class)
    public ResponseEntity createSurvey(@RequestBody @Valid Survey survey, HttpServletRequest request) {
        Survey dbSurvey = new Survey(
                "115",
                survey.getTitle(),
                survey.getQuestions()
        );

        Survey createdSurvey = getDatabase().createSurvey(dbSurvey);

        return ResponseEntity.created(URI.create(request.getPathInfo() + "/" + createdSurvey.getId())).body(createdSurvey);
    }

    }
//.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Credentials", "true")