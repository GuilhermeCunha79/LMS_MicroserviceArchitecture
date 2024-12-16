package pt.psoft.g1.psoftg1.lendingmanagement.model;

public interface LendingStatus {
    int LENDING_VALIDATED_BOOKS = 1;
    int LENDING_VALIDATED_READERS = 2;
    int LENDING_INVALIDATED = 3;
    int LENDING_WAITING_VALIDATION = 0;
}