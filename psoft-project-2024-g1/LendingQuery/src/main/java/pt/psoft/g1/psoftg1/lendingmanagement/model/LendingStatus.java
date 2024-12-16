package pt.psoft.g1.psoftg1.lendingmanagement.model;

public interface LendingStatus {
    static final int LENDING_VALIDATED = 1;
    static final int LENDING_INVALIDATED = 2;
    static final int LENDING_WAITING_VALIDATION = 0;
}