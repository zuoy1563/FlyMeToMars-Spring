package flymetomars.core.mining;

import com.google.common.collect.*;

import flymetomars.data.ExpertiseDAO;
import flymetomars.data.InvitationDAO;
import flymetomars.data.MissionDAO;
import flymetomars.data.PersonDAO;
import flymetomars.model.*;
import flymetomars.data.*;

import java.sql.SQLException;
import java.util.*;

/**
 * @author Yuan-Fang Li
 * @version $Id: $
 */
public class EntityMiner {
    private PersonDAO personDAO;
    private MissionDAO missionDAO;
    private ExpertiseDAO expertiseDAO;
    private InvitationDAO invitationDAO;

    public EntityMiner(PersonDAO personDAO, MissionDAO missionDAO, ExpertiseDAO expertiseDAO, InvitationDAO invitationDAO) {
        this.personDAO = personDAO;
        this.missionDAO = missionDAO;
        this.expertiseDAO = expertiseDAO;
        this.invitationDAO = invitationDAO;
    }

    /**
     * Return the most popular person by the count of invitations received.
     *
     * @return The most popular person.
     */
    public Person getMostPopularPerson() throws SQLException {
        List<Person> persons = personDAO.getAll();
        int maxInvites = 0;
        Person result = null;
        for (Person p : persons) {
            int noInvites = p.getInvitationsReceived().size();
            if (maxInvites <= noInvites) {
                maxInvites = noInvites;
                result = p;
            } 
        }
        return result;
    }

    /**
     * Get a list of persons of the given size with the most invitations received.
     *
     * @param size the size of the set to be returned.
     * @return the set of the most popular person by invitations received.
     */

    public List<Person> getCelebrarity(int size) throws SQLException{
        /* please uncomment if need to implement
        // check if size is greater than 0
        if (size > 0) {
            List<Person> celebrarities = new ArrayList<>();
            List<Person> persons = personDAO.getAll();

            // check if persons size is greater than 0
            if (persons.size() > 0) {

                // sort persons by the number of their invitation received
                Collections.sort(persons, new Comparator<Person>() {
                    @Override
                    public int compare(Person o1, Person o2) {
                        return o2.getInvitationsReceived().size() - o1.getInvitationsReceived().size();
                    }
                });

                // put top-k persons into a list called celebrarities
                for (Person person:persons) {
                    System.out.println(person.getEmail() + ", " + person.getInvitationsReceived().size());
                }

                if (persons.size() > size) {
                    for (int i = 0; i < size; i++)
                        celebrarities.add(persons.get(i));
                }
                else
                    celebrarities.addAll(persons);
                return celebrarities;
            }
            else
                throw new IllegalArgumentException("Sorry, no person is in database!");
        }
        else
            throw new IllegalArgumentException("Size should be greater than 0!");
        */


        return null;  // if need to implement, please delete this sentence
    }

    /**
     * Get the list of the given size of persons with the most missions registered.
     *
     * @param person the person of the buddies to be returned.
     * @param size the size of the set to be returned.
     * @return the set of the busiest persons.
     */
    public List<Person> getBuddies(Person person, int size) throws SQLException{
        /*
        Get all the friends of a person, and return the friends who have registered the largest number of missions
         */

        /* please uncomment if need to implement
        if (size > 0) {
            List<Person> personList = new ArrayList<>();
            List<Person> buddies = new ArrayList<>();
            buddies.addAll(person.getFriends());

            // check if buddies size is greater than 0
            if (buddies.size() > 0) {

                // sort buddies by the number of their missions registered
                Collections.sort(buddies, new Comparator<Person>() {
                    @Override
                    public int compare(Person o1, Person o2) {
                        return o2.getMissionRegistered().size() - o1.getMissionRegistered().size();
                    }
                });

                // put top-k busiest persons
                if (buddies.size() > size) {
                    for (int i = 0; i < size; i++)
                        personList.add(buddies.get(i));
                }
                else
                    personList.addAll(buddies);
                return personList;
            }
            else
                throw new IllegalArgumentException("Sorry, this person has no buddy!");
        }
        else
            throw new IllegalArgumentException("Size should be greater than 0!");
        */

        return null;    // please delete this sentence if need to implement
    }

    /**
     * Given a person, return the largest group of persons (including this person)
     * such that each pair of persons are connected by some mission (i.e., they all
     * know each other through these missions).
     *
     * @param person the person of the social circle.
     * @return the social circle of the given person.
     */
    public Set<Person> getSocialCircle(Person person) throws SQLException{
        /*
        If there are a number of people, and each pair of persons are connected by some missions,
        then all these people must have attended a same mission. In addition, there may be other persons
        that participate the same missions separately. For example, m1{p1,p2,p3}, m2{p2,p3,p4}, m3{p1,p4} should
        return {p1,p2,p3,p4}.
         */

        /* please uncomment if need to implement
        List<Mission> missions = new ArrayList<>();
        missions.addAll(person.getMissionRegistered());
        List<Person> people = personDAO.getAll();

        if (!missions.isEmpty()) {
            Set<Person> socialCircle = new HashSet<>();

            for (int i = 0; i < missions.size(); i++) {
                // get all participant of current mission, and add all of them as temporary social circle
                Set<Person> tmpSocialCircle = new HashSet<>();
                tmpSocialCircle.addAll(missions.get(i).getParticipantSet());

                for (Person pp : people) {
                    // status == true means that persons have common missions
                    boolean status = true;

                    if (!tmpSocialCircle.contains(pp)) {
                        for (Person p : tmpSocialCircle) {
                            int count = 0;
                            Set<Mission> pMissions = p.getMissionRegistered();

                            // each pp should contain some common missions from p
                            for (Mission mission : pMissions) {
                                if (pp.getMissionRegistered().contains(mission))
                                    count++;
                            }

                            // if count = 0, there is definitely no common mission
                            if (count == 0)
                                status = false;
                        }
                        if (status)
                            tmpSocialCircle.add(pp);
                    }
                }
                // refresh set
                if (socialCircle.size() < tmpSocialCircle.size()) {
                    socialCircle.removeAll(socialCircle);
                    socialCircle.addAll(tmpSocialCircle);
                }
            }
            return socialCircle;
        }
        else
            throw new IllegalArgumentException("This person has no mission!");
        */

        return null;  // please delete this sentence if need to implement
    }

    /**
     * Given a person, return the top-k upcoming missions that his/her friends
     * have been invited to but he/she hasn’t been (ranked by the number of
     * his/her friends who have been invited).
     *
     * @param person the person of the sour grape.
     * @param size the max number of upcoming missions that the person is not invited to.
     * @return the ranked list of the person's friends who are invited to
     * a mission that the person is not invited to.
     */
    public List<Mission> getSourGrapes(Person person, int size) throws SQLException {

        /* please uncomment if need to implement
        if (size > 0) {
            Map<Mission,Integer> missionIntegerHashMap = new HashMap<>();
            List<Mission> missions = missionDAO.getAll();
            Set<Person> friends = person.getFriends();

            if (!missions.isEmpty()) {
                for (Mission mission : missions) {

                    // count how many friends are invited by a mission
                    int count = 0;
                    boolean status = false;
                    Set<Invitation> invitations = mission.getInvitationSet();
                    for (Invitation invitation : invitations) {
                        for (Person p : friends) {
                            if (invitation.getRecipient().equals(p))
                                count++;

                            // skip the mission if the person is invited by this mission
                            else if (invitation.getRecipient().equals(person)){
                                status = true;
                                break;
                            }
                        }
                        if (status)
                            break;
                    }
                    if (status)
                        continue;
                    else {
                        // if there are more than 0 person that are invited by this mission, then put the mission
                        // and count in map
                        if (count != 0)
                            missionIntegerHashMap.put(mission, count);
                    }
                }

                // sort the map according to value
                List<Map.Entry<Mission, Integer>> entryList = new ArrayList<>(missionIntegerHashMap.entrySet());
                Collections.sort(entryList, new Comparator<Map.Entry<Mission, Integer>>() {
                    @Override
                    public int compare(Map.Entry<Mission, Integer> o1, Map.Entry<Mission, Integer> o2) {
                        return o2.getValue() - o1.getValue();
                    }
                });

                Iterator<Map.Entry<Mission, Integer>> it = entryList.iterator();
                Map.Entry<Mission, Integer> tmpEntry = null;
                Map<Mission, Integer> sortedMissions = new LinkedHashMap<>();  // place sequentially to LinkedHashMap
                while (it.hasNext()) {
                    tmpEntry = it.next();
                    sortedMissions.put(tmpEntry.getKey(), tmpEntry.getValue());
                }

                // put missions sequentially according to count in the list
                List<Mission> missionsDescByScore = new ArrayList<>();
                for (Map.Entry<Mission, Integer> missionIntegerEntry : sortedMissions.entrySet())
                    missionsDescByScore.add(missionIntegerEntry.getKey());

                // deal with list size
                if (missionsDescByScore.size() > size)
                    missionsDescByScore = missionsDescByScore.subList(0, size - 1);

                return missionsDescByScore;
            }
            else
                throw new IllegalArgumentException("There is no mission in database!");
        }
        else
            throw new IllegalArgumentException("Size should be greater than 0!");
        */
        return null;   // please delete this sentence if need to implement
    }

    /**
     * From assignment2 code base
     */


    /**
     * Return the top-k most expensive missions. The cost of a mission is the sum
     * of the costs of its required equipments.
     *
     * @return
     */
    public List<Mission> getExorbitance(int k) throws SQLException {
        // TODO: implement and test this!
        List<Mission> mission = missionDAO.getAll();
        ListMultimap<Integer, Mission> missionMap = ArrayListMultimap.create();
        if (null == mission) {
            throw new SQLException(" No Mission found to get exorbitance. ");
        }
        for (Mission m : mission) {
            Multiset<Equipment> equipments = m.getEquipmentsRequired();
            Iterator<Equipment> ie = equipments.iterator();
            int sum = 0;
            while (ie.hasNext()) {
                sum += (ie.next()).getAttributes().get(Equipment.Attribute.cost);
            }
            missionMap.put(sum, m);
        }

        List<Integer> keys = Lists.newArrayList(missionMap.keys());
        List<Integer> sortedKeys = Ordering.natural().greatestOf(keys, keys.size());
        List<Mission> results = Lists.newArrayList();

        for (Integer key : sortedKeys) {
            List<Mission> topExorbitance = missionMap.get(key);
            if (results.size() + topExorbitance.size() <= k) {
                results.addAll(topExorbitance);
            } else {
                results.addAll(topExorbitance.subList(0, (k - results.size())));
            }
        }
        return results;
    }

    /**
     * Return the top-k persons with the most demanded expertises (required by missions).
     */
    public List<Person> getHotshots(int k) throws SQLException {
        // TODO: implement and test this!
        List<Mission> mission = missionDAO.getAll();
        List<Person> people = personDAO.getAll();
        ListMultimap<Integer, Person> personMap = ArrayListMultimap.create();
        List<String> expertises = new ArrayList<>();
        if (null == mission) {
            throw new SQLException(" No Mission found to check hotshots. ");
        }
        if (null == people) {
            throw new SQLException(" No Person found to check hotshots. ");
        }

        if ((personDAO.getAll().size() == 1) || (personDAO.getAll().size() <= k)) {
            return people;
        } else {
            // collect all expertise description required for all missions; Expertise with highest occurrence in all mission is most demanded expertise.
            for (Mission m : mission) {
                Set<Expertise> expertiseRequired = m.getExpertiseRequired();
                Iterator<Expertise> ie = expertiseRequired.iterator();
                if (ie.hasNext()) {
                    String desc = ie.next().getDescription();
                    expertises.add(desc);
                }
            }
            Map<Integer, String> mostDemandedExpertise = new HashMap<>();

            for (String e : expertises) {
                Integer frequency = Collections.frequency(expertises, e);
                mostDemandedExpertise.put(frequency, e);
            }
            List<Integer> keys = Lists.newArrayList(mostDemandedExpertise.keySet());
            List<Integer> sortedValues = Ordering.natural().greatestOf(keys, keys.size());
            //get top 1 expertise
            Integer topCounts = sortedValues.get(0);
            String hotExpertise = mostDemandedExpertise.get(topCounts);
            //each person with top 1 expertise is a hotshot.
            int c = 0;
            for (Person p : people) {
                Collection<Expertise> tempExpertise = p.getExpertise();
                Set<String> expertiseDetails = new HashSet<>();
                for (Expertise e : tempExpertise) {
                    expertiseDetails.add(e.getDescription());
                }
                if (expertiseDetails.contains(hotExpertise)) {
                    personMap.put(c, p);
                    c++;
                }
            }
            List<Integer> keyList = Lists.newArrayList(personMap.keys());
            List<Integer> sortedKeys = Ordering.natural().leastOf(keyList, keys.size());
            List<Person> results = Lists.newArrayList();

            for (Integer key : sortedKeys) {
                List<Person> topExperts = personMap.get(key);
                if (results.size() + topExperts.size() <= k) {
                    results.addAll(topExperts);
                } else {
                    results.addAll(topExperts.subList(0, (k - results.size())));
                }
            }
            return results;
        }
    }
}
