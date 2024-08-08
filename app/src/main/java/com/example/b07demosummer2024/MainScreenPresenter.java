package com.example.b07demosummer2024;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * presenter for the main screen
 */
public class MainScreenPresenter implements MainScreenPresenterInterface {
    MainScreenView view;
    MainScreenModel model;

    private static int pageNumber = 1;
    int maxPages;

    /**
     * constructor for the main screen presenter. passes itself as an interface to the model
     * @param view the view
     * @param model the model
     */
    public MainScreenPresenter(MainScreenView view, MainScreenModel model) {
        this.view = view;
        this.model = model;
        this.model.setPresenterInterface(this);

    }

    /**
     * update max pages and pagination when the view is updated
     * @param maxPages the max number of pages
     */
    @Override
    public void update(int maxPages) {
        setMaxPages(maxPages);
        updatePage();
    }

    /**
     * load items from the model
     */
    void loadItems() {
        if (model.areItemsLoaded()) {
            update((model.getItemList().size() + 9) / 10);
        } else {
            model.loadItemsFromDb();
        }
    }

    /**
     * handles clicking of the page next button. moves to next page of exists and scrolls to top
     */
    void handlePageUp() {
        if (pageNumber < maxPages) {
            pageNumber++;
            updatePage();
            view.scrollToTop();
        }
    }

    /**
     * handles clicking of the back page button. move to the previous page if exits and scrolls
     * to top
     */
    void handlePageDown() {
        if (pageNumber > 1) {
            pageNumber--;
            updatePage();
            view.scrollToTop();
        }
    }

    /**
     * paginates the item list. based on the page number, updates the recycler view of items to
     * only contain the 10 items corresponding to the current page
     */
    void paginateList() {
        List<Item> pagedList = new ArrayList<>();
        int start = (pageNumber - 1) * 10;
        int end = Math.min(start + 10, model.getItemList().size());

        for (int i = start; i < end; i++) {
            pagedList.add(model.getItemList().get(i));
        }

        view.updateRecyclerList(pagedList);
        view.scrollToTop();
    }

    /**
     * updates the page inside the view by paginating the items and showing the page number info
     * string on the view
     */
    public void updatePage() {
        String pageInfo = makePageString();
        view.updatePageInfo(pageInfo);
        paginateList();
    }

    /**
     * setter for max pages, ensures that pageNumber does not exceed max pages
     * @param maxPages the max number of pages
     */
    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages;
        if (maxPages < pageNumber && maxPages != 0) {
            Log.d("BUG", "Setting page number to " + maxPages);
            pageNumber = maxPages;
        }
    }

    /**
     * makes a page info string of the format "Page <pageNumber> of <maxPages>". "No Items" if
     * there are 0 items in the itemList
     * @return the formatted page string
     */
    String makePageString() {
        if (maxPages == 0 && pageNumber == 1)
            return "No Items";
        return "Page " + pageNumber + " of " + maxPages;
    }


}
