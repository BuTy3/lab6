package ru.itmo.common.managers;

import lombok.Data;
import lombok.Getter;
import ru.itmo.common.utility.Element;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public abstract class CollectionManager<T extends Element> {
    @Getter
    private final LocalDateTime createdAt = LocalDateTime.now();
    private ArrayList<T> collection;
    @Getter
    private long nextId = 0;

    public CollectionManager() {
        this.collection = new ArrayList<>();
    }

    public List<T> getCollection() {
        return collection;
    }

    public void setCollection(ArrayList<T> collection) {
        this.collection = collection;
        updateNextId();
    }

    protected void updateNextId() {
        this.nextId = collection.stream()
                .mapToLong(this::getElementId)
                .max()
                .orElse(0) + 1;
    }

    public boolean add(T element) throws Exception {
        element.setId(nextId++);
//        if (saveToDatabase(element)) {}
        return collection.add(element);
    }

    //    public boolean remove(long id, String login)
    public boolean remove(long id) {
        T element = get(id);
        //if (element != null && checkOwnerShip(id, login) && removeFromDatabase(id)) {}
        return collection.remove(element);
        //else {throw new Exception("Error! Element with ID " + id + " not found");}
    }

    public boolean remove(T element) {
        //if (element != null && checkOwnerShip(id, login) && removeFromDatabase(id)) {}
        return collection.remove(element);
        //else {throw new Exception("Error! Element with ID " + id + " not found");}
    }

    //    public boolean update(long id, String login, T newElement) {
    public boolean update(long id, T newElement) {
        T existingElement = get(id);
        //if (existingElement != null && checkOwnerShip(id, login) && updateInDatabase(id, newElement)) {}
        return updateElement(existingElement, newElement);
        //else {throw new Exception("Error! Element with ID " + id + " not found");}
    }
//    public boolean removeById(long id, String login) {

    public boolean removeById(long id) {
        T element = get(id);
//        if (element != null && checkOwnerShip(id, login) && removeFromDatabase(id)) {}
        return collection.remove(element);
//        else {throw new Exception("Error! Element with ID " + id + " not found");}
    }

    //    public boolean clear(String login)
    public boolean clear() {
//        if (login == null) {throw new Exception("Error! You are not logged in");}
//        if (clearDatabase()) {}
        collection.clear();
        return true;
    }

    public T get(long id) {
        return collection.stream()
                .filter(element -> getElementId(element) == id)
                .findFirst()
                .orElse(null);
    }

    public String description() {
        return String.format("Type: %s\nInitialization date: %s\nNumber of elements: %d",
                collection.getClass().getName(), createdAt, collection.size());
    }

    public T min() {
        return collection.stream()
                .min(T::compareTo)
                .orElse(null);
    }

    // Вспомогательные методы для работы с ID элементов и обновления элементов
    protected abstract long getElementId(T element);

    protected abstract boolean updateElement(T existingElement, T newElement);

    public int collectionSize() {
        return collection.size();
    }

    // Абстрактные методы для взаимодействия с базой данных
    //protected abstract boolean saveToDatabase(T element) throws Exception;

    //protected abstract boolean removeFromDatabase(long id) throws Exception;

    //protected abstract boolean updateInDatabase(long id, T newElement) throws Exception;

    //protected abstract boolean clearDatabase() throws Exception;

    //protected abstract boolean checkOwnerShip(long id, String login);
}
