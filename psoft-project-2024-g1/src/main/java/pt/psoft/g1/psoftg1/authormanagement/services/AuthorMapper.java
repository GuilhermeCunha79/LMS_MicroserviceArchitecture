package pt.psoft.g1.psoftg1.authormanagement.services;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.AuthorMongo;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;
import pt.psoft.g1.psoftg1.shared.model.Photo;

import java.nio.file.Path;

@Mapper(componentModel = "spring")
public abstract class AuthorMapper extends MapperInterface {

    @Mapping(target = "photo", source = "photoURI")
    public abstract Author create(CreateAuthorRequest request);

    public abstract void update(UpdateAuthorRequest request, @MappingTarget Author author);

    public abstract Author toAuthor(AuthorMongo authorMongo);

    @Mapping(target = "version", ignore = true)
    public abstract AuthorMongo toAuthorMongo(Author author);

}

