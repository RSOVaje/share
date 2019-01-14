package services.beans;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import services.configuration.AppProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import si.fri.pictures.models.dtos.Picture;
import si.fri.pictures.models.entities.Share;

@ApplicationScoped
public class ShareBean {

    private Logger log = Logger.getLogger(ShareBean.class.getName());

    @Inject
    private EntityManager em;

    @Inject
    private AppProperties appProperties;

    @Inject
    private ShareBean shareBean;

    private Client httpClient;

    @Inject
    @DiscoverService("picture")
    private Optional<String> pictureUrl;

    public List<Share> getShare() {

        TypedQuery<Share> query = em.createNamedQuery("Share.getAll", Share.class);

        return query.getResultList();

    }

    public Share createShare(Share share) {

        try {
            beginTx();
            em.persist(share);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return share;
    }

    public Share getShareById(Integer id) {

        TypedQuery<Share> query = em.createNamedQuery("Share.getById", Share.class).setParameter("id", id);
        Share share = query.getSingleResult();

        Picture picture = shareBean.getPicture(share.getIdPicture());
        share.setPictures(picture);

        return share;

    }

    public List<Share> getShareByIdProfila(Integer idProfila) {

        TypedQuery<Share> query = em.createNamedQuery("Share.getByIdProfile", Share.class).setParameter("idProfila", idProfila);

        List<Share> share = query.getResultList();
        Iterator it = share.iterator();

        List<Share> shareL = new ArrayList<>();
        if(it.hasNext() == true) {
            for (int i = 0; i < share.size(); i++) {
                Share s = share.get(i);
                Picture picture = getPicture(s.getIdPicture());
                s.setPictures(picture);
                shareL.add(s);
            }
            return shareL;
        }

        return null;

    }

    public Picture getPicture(Integer id) {
        if(appProperties.isExternalServicesEnabled() && pictureUrl.isPresent()) {
            log.info(pictureUrl.get() + "/v1/picture/" + id);
            try {
                return httpClient
                        .target(pictureUrl.get() + "/v1/picture/" + id)
                        .request().get(new GenericType<Picture>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.severe(e.getMessage());
                throw new InternalServerErrorException(e);
            }
        }
        return null;
    }

    public Boolean deleteShare(Integer idProfila, Integer idSProfila) {

        TypedQuery<Share> query = em.createNamedQuery("Share.getShare", Share.class).setParameter("idProfila", idProfila).setParameter("idSProfila", idSProfila);
        Share share = query.getSingleResult();

        if(share != null) {
            try {
                beginTx();
                em.remove(share);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else {
            return false;
        }

        return true;

    }

    public List<Share> getShareByIdSProfila(Integer idSProfila) {

        TypedQuery<Share> query = em.createNamedQuery("Share.getByIdSProfile", Share.class).setParameter("idSProfila", idSProfila);

        List<Share> share = query.getResultList();
        Iterator it = share.iterator();

        List<Share> shareL = new ArrayList<>();
        if(it.hasNext() == true) {
            for (int i = 0; i < share.size(); i++) {
                Share s = share.get(i);
                Picture picture = getPicture(s.getIdPicture());
                s.setPictures(picture);
                shareL.add(s);
            }
            return shareL;
        }

        return null;

    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }


}
