package ru.itmo.common.managers;

import lombok.Data;
import lombok.Getter;
import ru.itmo.common.utility.Element;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public abstract class CollectionManager<T extends Element> {
    @Getter
    private final LocalDateTime createdAt = LocalDateTime.now();
    private CopyOnWriteArrayList<T> collection;
    @Getter
    private long nextId = 0;

    public CollectionManager() {
        this.collection = new CopyOnWriteArrayList<>();
    }

    public List<T> getCollection() {
        return collection;
    }

    public void setCollection(CopyOnWriteArrayList<T> collection) {
        this.collection = collection;
        updateNextId();
    }

    protected void updateNextId() {
        this.nextId = collection.stream().mapToLong(this::getElementId).max().orElse(0) + 1;
    }

    public boolean add(T element) throws Exception {
        long id = saveToDatabase(element);
        if (id > 0) {
            element.setId(id);
            return collection.add(element);
        } else {
            return false;
        }
    }

    public boolean remove(long id, String username) {
        T element = get(id);
        try {
            if (element != null && checkOwnerShip(id, username) && removeFromDatabase(id)) {
                return collection.remove(element);
            } else {
                throw new Exception("Error! Element with ID " + id + " not found");
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean remove(T element, String username) {
        if (element == null) return false;
        var id = element.getId();
        try {
            if (checkOwnerShip(id, username) && removeFromDatabase(id)) {
                return collection.remove(element);
            } else {
                throw new Exception("Error! Element with ID " + id + " not found or you don't have access :(");
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean update(long id, T newElement, String username) {
        T existingElement = get(id);
        try {
            if (existingElement != null && checkOwnerShip(id, username) && updateInDatabase(newElement)) {
                return updateElement(existingElement, newElement);
            } else {
                throw new Exception("Error! Element with ID " + id + " not found");
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean clear(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Error! You are not logged in");
        }

        // Создаем список для элементов, которые нужно удалить
        List<T> elementsToRemove = new ArrayList<>();

        // Проходим по коллекции и добавляем элементы, которые принадлежат пользователю
        for (T element : collection) {
            if (checkOwnerShip(getElementId(element), username)) {
                elementsToRemove.add(element);
            }
        }

        // Удаляем элементы и из коллекции, и из базы данных
        for (T element : elementsToRemove) {
            try {
                if (!removeFromDatabase(getElementId(element))) {
                    return false;
                }
                collection.remove(element);
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }


    public T get(long id) {
        return collection.stream().filter(element -> getElementId(element) == id).findFirst().orElse(null);
    }

    public String description() {
        return String.format("Type: %s\nInitialization date: %s\nNumber of elements: %d", collection.getClass().getName(), createdAt, collection.size());
    }

    public T min() {
        return collection.stream().min(T::compareTo).orElse(null);
    }

    // Вспомогательные методы для работы с ID элементов и обновления элементов
    protected abstract long getElementId(T element);

    protected abstract boolean updateElement(T existingElement, T newElement);

    public int collectionSize() {
        return collection.size();
    }

    // Абстрактные методы для взаимодействия с базой данных
    protected abstract long saveToDatabase(T element) throws Exception;

    protected abstract boolean removeFromDatabase(long id) throws Exception;

    protected abstract boolean updateInDatabase(T newElement) throws Exception;

    protected abstract boolean checkOwnerShip(long id, String login);
}
