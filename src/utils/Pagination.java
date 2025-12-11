package utils;

import java.util.List;

public class Pagination<T> {
    private List<T> items;
    private int currentPage;
    private int pageSize;
    private int totalItems;
    private int totalPages;

    public Pagination(List<T> items, int currentPage, int pageSize, int totalItems) {
        this.items = items;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = (int) Math.ceil((double) totalItems / pageSize);
    }

    public List<T> getItems() {
        return items;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean hasNext() {
        return currentPage < totalPages;
    }

    public boolean hasPrevious() {
        return currentPage > 1;
    }

    public int getNextPage() {
        return hasNext() ? currentPage + 1 : currentPage;
    }

    public int getPreviousPage() {
        return hasPrevious() ? currentPage - 1 : currentPage;
    }
}

