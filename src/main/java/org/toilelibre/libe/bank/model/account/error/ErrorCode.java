package org.toilelibre.libe.bank.model.account.error;

public enum ErrorCode {

    AccountAlreadyExists (Kind.CONFLICT, "This account already exists"), IllegalIban (Kind.BAD_INPUT, "An illegal iban code was typed"), IllegalAddOperation (Kind.BAD_INPUT, "This amount cannot be added"), IllegalBalance (Kind.FORBIDDEN, "The resulting balance is not permitted"), IllegalOverdraft (
            Kind.FORBIDDEN, "This overdraft value cannot be set"), NoSuchAccount (Kind.NOT_FOUND, "No such account"), IllegalAccountHistoryOperation (Kind.BAD_INPUT, "This operation is not valid"), InvalidSwiftCode (Kind.BAD_INPUT, "This swift code is not correct"), InvalidDetails (Kind.BAD_INPUT,
                    "Illegal Details Typed");

    public enum Kind {
        NOT_FOUND, BAD_INPUT, CONFLICT, FORBIDDEN;
    }

    private String description;
    private Kind   kind;

    ErrorCode (final Kind kind1, final String description1) {
        this.kind = kind1;
        this.description = description1;
    }

    public String getDescription () {
        return this.description;
    }

    public Kind getKind () {
        return this.kind;
    }
}
