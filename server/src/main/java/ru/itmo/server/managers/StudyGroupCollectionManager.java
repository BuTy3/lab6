package ru.itmo.server.managers;

import ru.itmo.common.entities.StudyGroup;
import ru.itmo.common.managers.CollectionManager;
import ru.itmo.server.dao.StudyGroupDAO;
import ru.itmo.server.dao.UserDAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public class StudyGroupCollectionManager extends CollectionManager<StudyGroup> {

    private static final StudyGroupDAO studyGroupDAO = new StudyGroupDAO();

    private static final UserDAO userDAO = new UserDAO();

    @Override
    protected long getElementId(StudyGroup element) {
        return element.getId();
    }

    @Override
    protected boolean updateElement(StudyGroup existingElement, StudyGroup newElement) {
        if (existingElement != null && newElement != null) {
            existingElement.update(newElement);
            return true;
        }
        return false;
    }

    @Override
    protected long saveToDatabase(StudyGroup element) throws Exception {
        return studyGroupDAO.insertStudyGroup(element);
    }

    @Override
    protected boolean removeFromDatabase(long id) throws Exception {
        return studyGroupDAO.removeStudyGroupById(id);
    }

    @Override
    protected boolean updateInDatabase(StudyGroup newElement) throws Exception {
        return studyGroupDAO.updateStudyGroup(newElement);
    }

    @Override
    protected boolean checkOwnerShip(long id, String login) {
        return get(id).getUsername().equals(login);
    }

    public void loadCollection(StudyGroupDAO studyGroupDAO) {
        Collection<StudyGroup> loadedCollection = studyGroupDAO.getAllStudyGroups();
        CopyOnWriteArrayList<StudyGroup> collection = new CopyOnWriteArrayList<>(loadedCollection);
        setCollection(collection);

        long maxId = collection.stream()
                .mapToLong(StudyGroup::getId)
                .max()
                .orElse(0);
        updateNextId();
    }
}
