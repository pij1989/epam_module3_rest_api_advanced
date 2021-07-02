package com.epam.esm.model.entity;

import java.util.List;

public class Page<T extends Entity> {
    private List<T> list;
    private int totalPages;
    private long totalElements;
    private int number;
    private int size;

    public Page() {
    }

    public Page(List<T> list, int totalPages, long totalElements, int number, int size) {
        this.list = list;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.number = number;
        this.size = size;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean hasPrevious(){
        return number > 1;
    }

    public boolean hasNext() {
        return number + 1 <= totalPages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page<?> page = (Page<?>) o;

        if (totalPages != page.totalPages) return false;
        if (totalElements != page.totalElements) return false;
        if (number != page.number) return false;
        if (size != page.size) return false;
        return list != null ? list.equals(page.list) : page.list == null;
    }

    @Override
    public int hashCode() {
        int result = list != null ? list.hashCode() : 0;
        result = 31 * result + totalPages;
        result = 31 * result + (int) (totalElements ^ (totalElements >>> 32));
        result = 31 * result + number;
        result = 31 * result + size;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Page{");
        sb.append("list=").append(list);
        sb.append(", totalPages=").append(totalPages);
        sb.append(", totalElements=").append(totalElements);
        sb.append(", number=").append(number);
        sb.append(", size=").append(size);
        sb.append('}');
        return sb.toString();
    }
}
