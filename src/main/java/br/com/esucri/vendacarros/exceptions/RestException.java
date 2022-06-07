package br.com.esucri.vendacarros.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestException extends WebApplicationException {

    public RestException(ErroMessage erro, Response.Status status) {
        super(Response.status(status).entity(erro).type(MediaType.APPLICATION_JSON).build());
    }


}
