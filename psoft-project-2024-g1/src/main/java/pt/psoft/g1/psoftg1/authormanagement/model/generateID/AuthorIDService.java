package pt.psoft.g1.psoftg1.authormanagement.model.generateID;

import pt.psoft.g1.psoftg1.authormanagement.api.AuthorView;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;

public interface AuthorIDService {

    String generateAuthorID();
}