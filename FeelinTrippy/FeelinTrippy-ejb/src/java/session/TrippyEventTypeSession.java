package session;

import entity.Customer;
import entity.TrippyEventType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author dk349
 */
@Stateless
public class TrippyEventTypeSession implements TrippyEventTypeSessionLocal {
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public void createTrippyType(TrippyEventType type) {
        if (type != null) {
            try {
                em.persist(type);
            } catch (EntityExistsException e) {
                System.out.println("Trippy type exists.");
            }
        }
    }

    @Override
    public void updateTrippyType(TrippyEventType type) {
        TrippyEventType existingTrippyType = em.find(TrippyEventType.class, type.getTypeID());
        if (existingTrippyType != null) {
            existingTrippyType.setTypeName(type.getTypeName());
            existingTrippyType.setSoftDelete(type.getSoftDelete());
        }
        else {
            throw new NoResultException("Trippy Event Type not found.");
        }
    }

    @Override
    public void removeTrippyType(Long trippyTypeID) {
        TrippyEventType toBeRemoved = em.find(TrippyEventType.class, trippyTypeID);
        if (toBeRemoved != null) {
            em.remove(toBeRemoved);
        }
        else {
            throw new NoResultException("Trippy Event Type not found");
        }
    }
        
    @Override
    public List<TrippyEventType> getAllTripType() {
        Query q;
        q = em.createQuery("SELECT t FROM TrippyEventType t");
        return q.getResultList();
    }

    @Override
    public TrippyEventType searchTrippyEventType(String name) {
        Query q;
        if (name != null) {
            q = em.createQuery("SELECT te FROM TrippyEventType te WHERE "
                    + "LOWER(te.typeName) = :name");
            q.setParameter("name", name.toLowerCase());

            if (!q.getResultList().isEmpty()) {
                return (TrippyEventType) q.getResultList().get(0);
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    @Override
    public TrippyEventType getTypeById(Long typeID) throws NoResultException {
        TrippyEventType type = em.find(TrippyEventType.class, typeID);

        if (type != null) {
            return type;
        } else {
            throw new NoResultException("Not found");
        }
    }
    @Override
    public void removeType(Long tId) {
        TrippyEventType type = em.find(TrippyEventType.class, tId);
        type.setSoftDelete(true);
        em.flush();
    }
    @Override
    public void recoverType(Long tId) {
        TrippyEventType type = em.find(TrippyEventType.class, tId);
        type.setSoftDelete(false);
        em.flush();
    }
}
