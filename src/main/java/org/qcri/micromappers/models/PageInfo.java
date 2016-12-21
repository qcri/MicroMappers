package org.qcri.micromappers.models;

import org.qcri.micromappers.entity.GlobalEventDefinition;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jlucas on 12/21/16.
 */
public class PageInfo<T> implements Serializable {
    private static final long serialVersionUID = 2254473661569318782L;

    private List<T> list;

    private int total = 0;

    private int pageSize = 10;

    @SuppressWarnings("unused")
    private int pageStart = 0;

    private int pages = 1;

    private int pageNumber = 1;

    private boolean isFirstPage = false;

    private boolean isLastPage = false;

    private boolean hasPreviousPage = false;

    private boolean hasNextPage = false;

    private int navigatePages = 10;

    private int[] navigatePageNumbers;

    public PageInfo(Page<T> pages){
        init(pages);
    }

    private void init(Page<T> pages) {

        this.total = (int)pages.getTotalElements();
        this.pages = pages.getTotalPages();
        this.pageNumber = pages.getNumber() + 1;

        this.isFirstPage = pages.isFirst();
        this.isLastPage = pages.isLast();
        this.hasPreviousPage = pages.hasPrevious();
        this.hasNextPage = pages.hasNext();

        this.calcNavigatePageNumbers();

    }

    private void calcNavigatePageNumbers() {
        if (this.pages <= navigatePages) {
            navigatePageNumbers = new int[this.pages];
            for (int i = 0; i < this.pages; i++) {
                navigatePageNumbers[i] = i + 1;
            }
        } else {
            navigatePageNumbers = new int[navigatePages];
            int startNum = pageNumber - navigatePages / 2;
            int endNum = pageNumber + navigatePages / 2;

            if (startNum < 1) {
                startNum = 1;
                for (int i = 0; i < navigatePages; i++) {
                    navigatePageNumbers[i] = startNum++;
                }
            } else if (endNum > this.pages) {
                endNum = this.pages;
                for (int i = navigatePages - 1; i >= 0; i--) {
                    navigatePageNumbers[i] = endNum--;
                }
            } else {
                for (int i = 0; i < navigatePages; i++) {
                    navigatePageNumbers[i] = startNum++;
                }
            }
        }
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public int getTotal() {
        return total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPages() {
        return pages;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public boolean hasPreviousPage() {
        return hasPreviousPage;
    }

    public boolean hasNextPage() {
        return hasNextPage;
    }

    public int getPageStart() {
        return (pageNumber - 1) * pageSize;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[").append("total=").append(total).append(",pages=")
                .append(pages).append(",pageNumber=").append(pageNumber)
                .append(",pageSize=").append(pageSize).append(",isFirstPage=")
                .append(isFirstPage).append(",isLastPage=").append(isLastPage)
                .append(",hasPreviousPage=").append(hasPreviousPage)
                .append(",hasNextPage=").append(hasNextPage)
                .append(",navigatePageNumbers=");
        int len = navigatePageNumbers.length;
        if (len > 0)
            sb.append(navigatePageNumbers[0]);
        for (int i = 1; i < len; i++) {
            sb.append(" " + navigatePageNumbers[i]);
        }
        sb.append(",list.size=" + list.size());
        sb.append("]");
        return sb.toString();
    }
}
