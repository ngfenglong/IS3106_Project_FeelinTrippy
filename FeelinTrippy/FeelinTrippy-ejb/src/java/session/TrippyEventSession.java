package session;

import entity.TrippyEventItem;
import entity.TrippyEventType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class TrippyEventSession implements TrippyEventSessionLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void createTrippyEvent(TrippyEventItem trippyEventItem) {
        if (trippyEventItem != null) {
            try {
                em.persist(trippyEventItem);
            } catch (EntityExistsException e) {
                System.out.println("Trippy event exists.");
            }
        }
    }

    @Override
    public void updateTrippyEvent(TrippyEventItem trippyEventItem) {

        TrippyEventItem existingTrippyEvent = em.find(TrippyEventItem.class, trippyEventItem.getEventID());

        if (existingTrippyEvent != null) {
            System.out.println("Found a match!");
            existingTrippyEvent.setEventName(trippyEventItem.getEventName());
            existingTrippyEvent.setEventDescription(trippyEventItem.getEventDescription());
            existingTrippyEvent.setEventType(trippyEventItem.getEventType());
            existingTrippyEvent.setPoint(trippyEventItem.getPoint());
            existingTrippyEvent.setPrice(trippyEventItem.getPrice());
            if (trippyEventItem.getSoftDelete() != null) {
                existingTrippyEvent.setSoftDelete(trippyEventItem.getSoftDelete());
            }       
            existingTrippyEvent.setStartDate(trippyEventItem.getStartDate());
            existingTrippyEvent.setEndDate(trippyEventItem.getEndDate());
            existingTrippyEvent.setAddress(trippyEventItem.getAddress());
            existingTrippyEvent.setEventTypeString(trippyEventItem.getEventTypeString());
            existingTrippyEvent.setEventImage(trippyEventItem.getEventImage());
        } else {
            throw new NoResultException("Trippy Event not found.");
        }
    }

    @Override
    public void removeTrippyEvent(TrippyEventItem trippyEventItem) {
        TrippyEventItem toBeRemoved = em.find(TrippyEventItem.class, trippyEventItem.getEventID());
        if (toBeRemoved != null) {
            em.remove(toBeRemoved);
        } else {
            throw new NoResultException("Trippy Event not found");
        }
    }

    @Override
    public TrippyEventItem retrieveEventByEventId(Long eventID) {
        TrippyEventItem result = em.find(TrippyEventItem.class, eventID);
        return result;
    }

    @Override
    public List<TrippyEventItem> retrieveAllEvents() {
        Query q;
        q = em.createQuery("SELECT t FROM TrippyEventItem t");
        return q.getResultList();
    }

    @Override
    public List<TrippyEventItem> searchEventListByConditions(TrippyEventType type, Double price) {
        Query q = em.createQuery("SELECT t FROM TrippyEventItem t WHERE "
                + "t.price <= :price");
        q.setParameter("price", price);

        List<TrippyEventItem> checkList = q.getResultList();
        List<TrippyEventItem> returnList = new ArrayList<TrippyEventItem>();

        for (TrippyEventItem tItem : checkList) {
            
                if (tItem.getEventTypeString().equals(type.getTypeName())) {
                    returnList.add(tItem);
                
            }
        }
        return returnList;
    }

    @Override
    public TrippyEventItem randomEvent(TrippyEventType type, Double price) {
        List<TrippyEventItem> listToRand = searchEventListByConditions(type, price);
        int listSize = listToRand.size();

        return listToRand.get(new Random().nextInt(listSize));
    }

    @Override
    public List<TrippyEventItem> searchEventListByPrice(Double price) {
        Query q = em.createQuery("SELECT t FROM TrippyEventItem t WHERE "
                + "t.price <= :price");
        q.setParameter("price", price);

        return q.getResultList();
    }

    @Override
    public TrippyEventItem retrieveEventByEventName(String eventName) {
        
        TrippyEventItem returnItem;
        Query q = em.createQuery("SELECT t FROM TrippyEventItem t WHERE "
                + "LOWER(t.eventName) = :eventName");
        q.setParameter("eventName", eventName.toLowerCase());

        returnItem =  (TrippyEventItem)q.getResultList().get(0);
        
        if(returnItem == null){
            return retrieveAllEvents().get(0);
        }
        else{
            return returnItem;
        }
    }

}
