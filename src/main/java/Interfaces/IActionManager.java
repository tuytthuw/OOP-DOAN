package Interfaces;

public interface IActionManager<T> {
    public void displayList();
    public void add(T item);
    public void update(String id);
    public boolean remove(String id);
    public T findById(String id);
    public void generateStatistics();
}
