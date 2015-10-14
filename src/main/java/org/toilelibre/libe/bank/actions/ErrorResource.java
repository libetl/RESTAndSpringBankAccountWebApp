package org.toilelibre.libe.bank.actions;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.error.ErrorCode;
import org.toilelibre.libe.bank.model.account.error.ErrorCode.Kind;

@RestController
@ControllerAdvice
public class ErrorResource {
    private static Logger LOGGER = LoggerFactory.getLogger (ErrorResource.class);

    @RequestMapping (path = "notFound")
    @ExceptionHandler (NoHandlerFoundException.class)
    @ResponseStatus (code = HttpStatus.NOT_FOUND)
    public ObjectNode notFound () {
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        return factory.objectNode ().put ("name", "APINotFound").put ("description", "This API does not exist").put ("kind", Kind.BAD_INPUT.name ());
    }

    @RequestMapping (path = "badMethod")
    @ExceptionHandler (HttpRequestMethodNotSupportedException.class)
    @ResponseStatus (code = HttpStatus.METHOD_NOT_ALLOWED)
    public ObjectNode badMethod () {
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        return factory.objectNode ().put ("name", "BadMethodOfAPI").put ("description", "This API exists, but the request method does not exist.").put ("kind", Kind.BAD_INPUT.name ());
    }

    @RequestMapping (path = "badEntityFormat")
    @ExceptionHandler (HttpMediaTypeNotSupportedException.class)
    @ResponseStatus (code = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ObjectNode badEntityFormat () {
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        return factory.objectNode ().put ("name", "BadEntityFormat").put ("description", "This API does not understand this input format. It accepts JSON only.").put ("kind", Kind.BAD_INPUT.name ());
    }

    @RequestMapping (path = "internalServerError")
    @ExceptionHandler (RuntimeException.class)
    @ResponseStatus (code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ObjectNode internalServerError (final RuntimeException exception) {
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        final UUID errorUuid = UUID.randomUUID ();
        ErrorResource.LOGGER.error ("An internal server error has happened (errorUuid : " + errorUuid + ")", exception);
        return factory.objectNode ().put ("name", "InternalServerError").put ("description", "This API could not respond correctly. There was an API bug during the service").put ("kind", HttpStatus.INTERNAL_SERVER_ERROR.name ()).put ("errorUuid", errorUuid.toString ());
    }

    @RequestMapping (path = "error")
    @ExceptionHandler (BankAccountException.class)
    public ObjectNode handleApplicationException (final HttpServletRequest request, final HttpServletResponse response, final BankAccountException exception) throws JsonProcessingException {
        final ErrorCode code = exception.getCode ();
        ErrorResource.LOGGER.error (code.getKind ().name () + " while trying to use \"" + request.getRequestURI () + "\" with verb \"" + request.getMethod () + "\" : ", exception);
        response.setStatus (KindToHttpStatus.from (code.getKind ()));
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        return factory.objectNode ().put ("name", code.name ()).put ("description", code.getDescription ()).put ("kind", code.getKind ().name ());
    }
}
