package pt.psoft.g1.psoftg1.shared.api;

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
}
