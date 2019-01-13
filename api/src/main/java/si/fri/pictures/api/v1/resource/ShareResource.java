package si.fri.pictures.api.v1.resource;

import services.beans.ShareBean;
import si.fri.pictures.models.entities.Share;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@RequestScoped
@Path("/share")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShareResource {

    @Inject
    private ShareBean shareBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getShare() {
        List<Share> shares = shareBean.getShare();
        if (shares == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(shares).build();
    }

    @POST
    public Response createShare(Share share) {


        share = shareBean.createShare(share);

        if (share.getId() != null) {
            return Response.status(Response.Status.CREATED).entity(share).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(share).build();
        }
    }

    @DELETE
    public Response deleteShare(@QueryParam("idP") Integer idProfila,
                                 @QueryParam("idS") Integer idShareProfila) {


        boolean d = shareBean.deleteShare(idProfila, idShareProfila);

        if (d) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getShareById(@PathParam("id") Integer id) {
        Share share = shareBean.getShareById(id);
        if (share == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(share).build();
    }


    @GET
    @Path("profil/{idProfila}")
    public Response getShareByIdProfila(@PathParam("idProfila") Integer idProfila) {
        List<Share> list = shareBean.getShareByIdProfila(idProfila);
        if (list == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(list).build();
    }

    @GET
    @Path("allowed/{idSProfila}")
    public Response getShareByIdSProfila(@PathParam("idSProfila") Integer idSProfila) {
        List<Share> list = shareBean.getShareByIdSProfila(idSProfila);
        if (list == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(list).build();
    }

}
