package pt.psoft.g1.psoftg1.shared.api;

import org.mapstruct.Named;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class MapperInterface {

    public <T> String map(final T value) {
        if (value == null)
            return null;
        return value.toString();}

    public <T extends Number> Number map(final T value) {
        if(value instanceof Double)
            return value.doubleValue();
        if(value instanceof Integer)
            return value.intValue();
        if(value instanceof Long)
            return value.longValue();
        else throw new NumberFormatException("Invalid number format");
    }

    public <T> T mapOpt(final Optional<T> i) {return i.orElse(null);}

    @Named(value = "lendingLink")
    protected Map<String, String> mapLendingLink(Lending lending){
        Map<String, String> lendingLink = new HashMap<>();
        String lendingUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/lendings/")
                .path(lending.getLendingNumber())
                .toUriString();
        lendingLink.put("href", lendingUri);
        return lendingLink;
    }


    @Named(value = "readerLink")
    protected Map<String, String> mapReaderLink(Long readerDetails){
        Map<String, String> readerLink = new HashMap<>();
        String readerUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/readers/")
                .path(String.valueOf(readerDetails))
                .toUriString();
        readerLink.put("href", readerUri);
        return readerLink;
    }
}
