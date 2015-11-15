package org.toilelibre.libe.bank.actions;

import org.springframework.http.HttpStatus;

import org.toilelibre.libe.bank.model.account.error.ErrorCode.Kind;

public class KindToHttpStatus {
    
    public static int from (final Kind kind) {
        
        switch (kind) {
        case BAD_INPUT :
            return HttpStatus.BAD_REQUEST.value ();
        case NOT_FOUND :
            return HttpStatus.NOT_FOUND.value ();
        case CONFLICT :
            return HttpStatus.CONFLICT.value ();
        case FORBIDDEN :
            return HttpStatus.FORBIDDEN.value ();
        default :
            return HttpStatus.INTERNAL_SERVER_ERROR.value ();
            
        }
    }
    
}
