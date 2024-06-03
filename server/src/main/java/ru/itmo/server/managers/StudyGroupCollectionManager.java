package ru.itmo.server.managers;

import ru.itmo.common.entities.StudyGroup;
import ru.itmo.common.managers.CollectionManager;

import java.util.ArrayList;
import java.util.Collection;

public class StudyGroupCollectionManager extends CollectionManager<StudyGroup> {

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

    public void loadCollection(DumpManager<StudyGroup> dumpManager) {
        Collection<StudyGroup> loadedCollection = dumpManager.readCollection();
        ArrayList<StudyGroup> collection = new ArrayList<>(loadedCollection);
        setCollection(collection);

        long maxId = collection.stream()
                .mapToLong(StudyGroup::getId)
                .max()
                .orElse(0);
        updateNextId();
    }
    public void saveCollection(DumpManager<StudyGroup> dumpManager) {
        dumpManager.writeCollection(getCollection());
    }
}
