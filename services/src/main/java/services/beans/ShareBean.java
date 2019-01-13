package services.beans;

import services.configuration.AppProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.client.Client;
import java.util.List;
import java.util.logging.Logger;
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

        return query.getSingleResult();

    }

    public List<Share> getShareByIdProfila(Integer idProfila) {

        TypedQuery<Share> query = em.createNamedQuery("Share.getByIdProfile", Share.class).setParameter("idProfila", idProfila);

        return query.getResultList();

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

        return query.getResultList();

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
